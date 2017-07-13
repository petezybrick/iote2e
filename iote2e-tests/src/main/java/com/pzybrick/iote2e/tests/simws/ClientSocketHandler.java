package com.pzybrick.iote2e.tests.simws;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.ws.security.LoginVo;

public class ClientSocketHandler {
	private static final Logger logger = LogManager.getLogger(ClientSocketHandler.class);
	private String url;
	private URI uri;
	private WebSocketContainer container;
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	protected Session session;
	protected Iote2eRequestSendThread iote2eRequestSendThread;
	protected Iote2eRequestReceiveThread iote2eReceiveReceiveThread;
	protected ConcurrentLinkedQueue<byte[]> iote2eResultBytes;
	protected LoginVo loginVo;
	protected Thread pollIote2eResultsThread;

	
	public static void main(String[] args) {
		// "ws://localhost:8090/iote2e/"
		try {
			ClientSocketHandler clientSocketHandler = new ClientSocketHandler();
			final Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");
			final Utf8 TEST_SOURCE_NAME = new Utf8("local_t001");
			final Utf8 TEST_SOURCE_TYPE = new Utf8("testSourceType");
			final Utf8 TEST_SENSOR_NAME = new Utf8("testSensorName");	// fan
			clientSocketHandler.setLoginVo( new LoginVo().setLoginName(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString()) );
			// clientSocketHandler.setLoginVo( new LoginVo().setLogin(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString()).setOptionalFilterSensorName(TEST_SENSOR_NAME.toString()) );
			clientSocketHandler.setUrl(args[0]);
			clientSocketHandler.setQueueIote2eRequests( new ConcurrentLinkedQueue<Iote2eRequest>() );
			clientSocketHandler.setQueueIote2eResults( new ConcurrentLinkedQueue<Iote2eResult>() );
			clientSocketHandler.connect();
			clientSocketHandler.wait();
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
	}

	public void connect() throws Exception {
		try {
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
				iote2eResultBytes = new ConcurrentLinkedQueue<byte[]>();
				iote2eRequestSendThread = new Iote2eRequestSendThread();
				iote2eReceiveReceiveThread = new Iote2eRequestReceiveThread();
				iote2eRequestSendThread.start();
				iote2eReceiveReceiveThread.start();
				ClientSocketAvro iotClientSocketAvro = new ClientSocketAvro(iote2eReceiveReceiveThread,iote2eResultBytes);
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText( Iote2eUtils.getGsonInstance().toJson(loginVo));
				logger.info("loginVo sent for {}", loginVo.getLoginName());
			} finally {
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
	
	public void shutdown() throws Exception {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		// Force lifecycle stop when done with container.
		// This is to free up threads and resources that the
		// JSR-356 container allocates. But unfortunately
		// the JSR-356 spec does not handle lifecycles (yet)
		if (container instanceof LifeCycle) {
			((LifeCycle) container).stop();
		}
		try {
			iote2eRequestSendThread.shutdown();
			iote2eReceiveReceiveThread.shutdown();
			iote2eRequestSendThread.join(5000);
			iote2eReceiveReceiveThread.join(5000);
		} catch( Exception e ) {
			logger.warn(e.getMessage());
		}
		// only applies if called from main()
		try {
			notify();
		} catch(Exception e) {}
	}
	
	
	public void sendIote2eRequest( Iote2eRequest iote2eRequest ) throws Exception {
		queueIote2eRequests.add(iote2eRequest);
		iote2eRequestSendThread.interrupt();
	}

	protected class Iote2eRequestSendThread extends Thread {
		private boolean shutdown;

		@Override
		public void run() {
			Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
			while( true ) {
				try {
					while( !queueIote2eRequests.isEmpty() ) {
						Iote2eRequest iote2eRequest = queueIote2eRequests.poll();
						if( iote2eRequest != null ) {
							session.getBasicRemote().sendBinary(ByteBuffer.wrap(iote2eRequestReuseItem.toByteArray(iote2eRequest)));
						}
					}	
					try {
						sleep(5*60*1000);
					} catch (InterruptedException e) { }
					if( shutdown ) break;
	
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

		public void shutdown() {
			shutdown = true;
			interrupt();
		}
	}

	
	private class Iote2eRequestReceiveThread extends Thread {
		private boolean shutdown;
		private Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();

		@Override
		public void run() {
			while( true ) {
				try {
					while (!iote2eResultBytes.isEmpty()) {
						byte[] bytes = iote2eResultBytes.poll();
						if( bytes != null ) {
							Iote2eResult iote2eResult = iote2eResultReuseItem.fromByteArray(bytes);
							queueIote2eResults.add( iote2eResult );
							if( pollIote2eResultsThread != null ) pollIote2eResultsThread.interrupt();
						}
					}
					try {
						sleep(5*60*1000);
					} catch (InterruptedException e) { }
					if( shutdown ) break;
					
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				} 
			}
		}

		public void shutdown() {
			shutdown = true;
			interrupt();
		}
	}
	
	public ConcurrentLinkedQueue<Iote2eRequest> getQueueIote2eRequests() {
		return queueIote2eRequests;
	}

	public ConcurrentLinkedQueue<Iote2eResult> getQueueIote2eResults() {
		return queueIote2eResults;
	}

	public ClientSocketHandler setQueueIote2eRequests(ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests) {
		this.queueIote2eRequests = queueIote2eRequests;
		return this;
	}

	public ClientSocketHandler setQueueIote2eResults(ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults) {
		this.queueIote2eResults = queueIote2eResults;
		return this;
	}

	public LoginVo getLoginVo() {
		return loginVo;
	}

	public ClientSocketHandler setLoginVo(LoginVo loginVo) {
		this.loginVo = loginVo;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ClientSocketHandler setUrl(String url) {
		this.url = url;
		return this;
	}

	public Thread getPollIote2eResultsThread() {
		return pollIote2eResultsThread;
	}

	public ClientSocketHandler setPollIote2eResultsThread(Thread pollIote2eResultsThread) {
		this.pollIote2eResultsThread = pollIote2eResultsThread;
		return this;
	}
}