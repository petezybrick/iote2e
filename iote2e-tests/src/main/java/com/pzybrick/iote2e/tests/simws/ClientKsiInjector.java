/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
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
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;
import com.pzybrick.iote2e.ws.security.LoginVo;


/**
 * The Class ClientKsiInjector.
 */
public class ClientKsiInjector {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ClientKsiInjector.class);
	
	/** The uri. */
	private URI uri;
	
	/** The container. */
	private WebSocketContainer container;
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/** The thread ignite subscribe. */
	private ThreadIgniteSubscribe threadIgniteSubscribe;
	
	/** The thread poll result. */
	private ThreadPollResult threadPollResult;
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<ByteBuffer> queueIote2eRequests = new ConcurrentLinkedQueue<ByteBuffer>();
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	
	/** The iote 2 e request spark consumer. */
	protected Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	
	/** The thread spark run. */
	protected ThreadSparkRun threadSparkRun;
	
	/** The iot client socket thread. */
	protected IotClientSocketThread iotClientSocketThread;
	
	/** The test source login. */
	private static Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");
	
	/** The test source name. */
	private static Utf8 TEST_SOURCE_NAME = new Utf8("rpi-999");
	
	/** The test source type. */
	private static Utf8 TEST_SOURCE_TYPE = new Utf8("temp");
	
	/** The test sensor name. */
	private static Utf8 TEST_SENSOR_NAME = new Utf8("temp1");	// fan
    
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
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
	
	
	/**
	 * Instantiates a new client ksi injector.
	 *
	 * @throws Exception the exception
	 */
	public ClientKsiInjector() throws Exception {
		this.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), 
				System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
	}

	
	/**
	 * Process.
	 *
	 * @param url the url
	 * @throws Exception the exception
	 */
	public void process(String url) throws Exception {
		try {
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
	
	
	/**
	 * Start ksi threads.
	 *
	 * @throws Exception the exception
	 */
	private void startKsiThreads() throws Exception {
		threadPollResult.start();
		threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig,
			TestCommonHandler.testTempToFanFilterKey, queueIote2eResults, threadPollResult);
		// if Spark not running standalone then start
		if( masterConfig.getSparkMaster().startsWith("local")) {
	    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
	    	threadSparkRun = new ThreadSparkRun( masterConfig, iote2eRequestSparkConsumer);
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
	
	/**
	 * Stop ksi threads.
	 */
	private void stopKsiThreads() {
		try {
			if( iotClientSocketThread != null ) {
				iotClientSocketThread.shutdown();
				iotClientSocketThread.join();
			}
			
			if( masterConfig.getSparkMaster().startsWith("local")) {
		    	iote2eRequestSparkConsumer.stop();
				threadSparkRun.join();
			}

			threadIgniteSubscribe.shutdown();
			threadIgniteSubscribe.join();
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}

	}
	
	/**
	 * The Class IotClientSocketThread.
	 */
	private static class IotClientSocketThread extends Thread {
		
		/** The login. */
		private String login;
		
		/** The uri. */
		private URI uri;
		
		/** The container. */
		private WebSocketContainer container;
		
		/** The shutdown. */
		private boolean shutdown;
		
		/** The iote 2 e request reuse item. */
		private Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		
		/** The iote 2 e result reuse item. */
		private Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();

		/**
		 * Instantiates a new iot client socket thread.
		 *
		 * @throws Exception the exception
		 */
		public IotClientSocketThread() throws Exception {

		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			Session session = null;
			try {
				LoginVo loginVo = new LoginVo().setLoginName(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString()).setOptionalFilterSensorName(TEST_SENSOR_NAME.toString());
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

		/**
		 * Shutdown.
		 */
		public void shutdown() {
			shutdown = true;
			interrupt();
		}

		/**
		 * Gets the login.
		 *
		 * @return the login
		 */
		public String getLogin() {
			return login;
		}

		/**
		 * Sets the login.
		 *
		 * @param login the login
		 * @return the iot client socket thread
		 */
		public IotClientSocketThread setLogin(String login) {
			this.login = login;
			return this;
		}

		/**
		 * Gets the uri.
		 *
		 * @return the uri
		 */
		public URI getUri() {
			return uri;
		}

		/**
		 * Sets the uri.
		 *
		 * @param uri the uri
		 * @return the iot client socket thread
		 */
		public IotClientSocketThread setUri(URI uri) {
			this.uri = uri;
			return this;
		}

		/**
		 * Gets the container.
		 *
		 * @return the container
		 */
		public WebSocketContainer getContainer() {
			return container;
		}

		/**
		 * Sets the container.
		 *
		 * @param container the container
		 * @return the iot client socket thread
		 */
		public IotClientSocketThread setContainer(WebSocketContainer container) {
			this.container = container;
			return this;
		}
	}
	
	
	/**
	 * The Class ThreadPollResult.
	 */
	private class ThreadPollResult extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;

		/**
		 * Instantiates a new thread poll result.
		 */
		public ThreadPollResult( ) {
			super();
		}
		
		/**
		 * Shutdown.
		 */
		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
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