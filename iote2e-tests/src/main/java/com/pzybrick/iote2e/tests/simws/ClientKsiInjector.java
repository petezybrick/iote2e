package com.pzybrick.iote2e.tests.simws;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteSingleton;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;
import com.pzybrick.iote2e.ws.security.LoginVo;
import com.pzybrick.iote2e.ws.socket.ClientSocketAvro;

public class ClientKsiInjector {
	private static final Logger logger = LogManager.getLogger(ClientKsiInjector.class);
	private URI uri;
	private WebSocketContainer container;
	private MasterConfig masterConfig;
	private IgniteSingleton igniteSingleton;
	private ThreadIgniteSubscribe threadIgniteSubscribe;
	private ThreadPollResult threadPollResult;
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	protected Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	protected ThreadSparkRun threadSparkRun;
	protected IotClientSocketThread iotClientSocketThread;
	private static Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");
	private static Utf8 TEST_SOURCE_NAME = new Utf8("rpi_999");
	private static Utf8 TEST_SOURCE_TYPE = new Utf8("temp");
	private static Utf8 TEST_SENSOR_NAME = new Utf8("temp1");	// fan
    
	public static void main(String[] args) {
		// "ws://localhost:8090/iote2e/"
		try {
			ClientKsiInjector clientBasicInjector = new ClientKsiInjector();
			clientBasicInjector.process(args[0]);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
	}

	public void process(String url) throws Exception {
		try {
			masterConfig = MasterConfig.getInstance();
			startKsiThreads();
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
					iotClientSocketThread = new IotClientSocketThread().setLogin( TEST_SOURCE_LOGIN.toString() )
							.setUri(uri).setContainer(container);
					iotClientSocketThread.start();
					iotClientSocketThread.join();

			} finally {
				// Force lifecycle stop when done with container.
				// This is to free up threads and resources that the
				// JSR-356 container allocates. But unfortunately
				// the JSR-356 spec does not handle lifecycles (yet)
				if (container instanceof LifeCycle) {
					((LifeCycle) container).stop();
				}
				stopKsiThreads();
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	
	
	private void startKsiThreads() throws Exception {
		igniteSingleton = IgniteSingleton.getInstance(masterConfig);
		threadPollResult.start();
		threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(
			TestCommonHandler.testTempToFanFilterKey, igniteSingleton, queueIote2eResults, threadPollResult);
		// if Spark not running standalone then start
		if( masterConfig.getSparkMaster().startsWith("local")) {
	    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
	    	threadSparkRun = new ThreadSparkRun( iote2eRequestSparkConsumer);
	    	threadSparkRun.start();
	    	long expiredAt = System.currentTimeMillis() + (10*1000);
	    	while( expiredAt > System.currentTimeMillis() ) {
	    		if( threadSparkRun.isStarted() ) break;
	    		try {
	    			Thread.sleep(250);
	    		} catch( Exception e ) {}
	    	}
	    	if( !threadSparkRun.isStarted() ) throw new Exception("Timeout waiting for Spark to start");
		}

	}
	
	private void stopKsiThreads() {
		try {
			if( iotClientSocketThread != null ) {
				iotClientSocketThread.shutdown();
				iotClientSocketThread.join();
			}
			
			if( MasterConfig.getInstance().getSparkMaster().startsWith("local")) {
		    	iote2eRequestSparkConsumer.stop();
				threadSparkRun.join();
			}

			threadIgniteSubscribe.shutdown();
			threadIgniteSubscribe.join();
			IgniteSingleton.reset();
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}

	}
	
	private static class IotClientSocketThread extends Thread {
		private String login;
		private URI uri;
		private WebSocketContainer container;
		private boolean shutdown;
		private Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		private Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();

		public IotClientSocketThread() throws Exception {

		}

		@Override
		public void run() {
			Session session = null;
			try {
				LoginVo loginVo = new LoginVo().setLogin(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString()).setOptionalFilterSensorName(TEST_SENSOR_NAME.toString());
				ConcurrentLinkedQueue<byte[]> iote2eResultBytes = new ConcurrentLinkedQueue<byte[]>();
				ClientSocketAvro iotClientSocketAvro = new ClientSocketAvro(this,iote2eResultBytes);
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText( Iote2eUtils.getGsonInstance().toJson(loginVo));
				// TODO: send a single test message, then work on sim
				for (int i = 45; i < 56; i++) {
					Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
					pairs.put( TEST_SENSOR_NAME, new Utf8(String.valueOf(i)));
					Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(TEST_SOURCE_LOGIN)
							.setSourceName(TEST_SOURCE_NAME)
							.setSourceType(TEST_SOURCE_TYPE)
							.setRequestUuid(UUID.randomUUID().toString())
							.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
							.setPairs(pairs).build();
					session.getBasicRemote().sendBinary(ByteBuffer.wrap(iote2eRequestReuseItem.toByteArray(iote2eRequest)));
					try {
						sleep(1000L);
					} catch (InterruptedException e) { }
				}
				logger.info("Rcvd Messages:");
				while (!iote2eResultBytes.isEmpty()) {
					byte[] bytes = iote2eResultBytes.poll();
					if( bytes != null ) {
						Iote2eResult iote2eResult = iote2eResultReuseItem.fromByteArray(bytes);
						logger.info(iote2eResult.toString());
					}
				}
				try {
					sleep(2000L);
				} catch (InterruptedException e) { }
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			} finally {
				if (session != null && session.isOpen()) {
					try {
						session.close();
					} catch (Exception e) {
					}
				}
			}
		}

		public void shutdown() {
			shutdown = true;
			interrupt();
		}

		public String getLogin() {
			return login;
		}

		public IotClientSocketThread setLogin(String login) {
			this.login = login;
			return this;
		}

		public URI getUri() {
			return uri;
		}

		public IotClientSocketThread setUri(URI uri) {
			this.uri = uri;
			return this;
		}

		public WebSocketContainer getContainer() {
			return container;
		}

		public IotClientSocketThread setContainer(WebSocketContainer container) {
			this.container = container;
			return this;
		}
	}
	
	
	private class ThreadPollResult extends Thread {
		private boolean shutdown;

		public ThreadPollResult( ) {
			super();
		}
		
		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			while( true ) {
				Iote2eResult iote2eResult = queueIote2eResults.poll();
				if( iote2eResult != null ) {
					try {
						logger.info("iote2eResult {}", iote2eResult);
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
				try {
					sleep(5000);
				} catch( InterruptedException e ) {}
				if( this.shutdown ) break;
			}
		}
	}
}