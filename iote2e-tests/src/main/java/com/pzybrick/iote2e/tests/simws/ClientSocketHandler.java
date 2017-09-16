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


/**
 * The Class ClientSocketHandler.
 */
public class ClientSocketHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ClientSocketHandler.class);
	
	/** The url. */
	private String url;
	
	/** The uri. */
	private URI uri;
	
	/** The container. */
	private WebSocketContainer container;
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The session. */
	protected Session session;
	
	/** The iote 2 e request send thread. */
	protected Iote2eRequestSendThread iote2eRequestSendThread;
	
	/** The iote 2 e receive receive thread. */
	protected Iote2eRequestReceiveThread iote2eReceiveReceiveThread;
	
	/** The iote 2 e result bytes. */
	protected ConcurrentLinkedQueue<byte[]> iote2eResultBytes;
	
	/** The login vo. */
	protected LoginVo loginVo;
	
	/** The poll iote 2 e results thread. */
	protected Thread pollIote2eResultsThread;

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
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

	/**
	 * Connect.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Shutdown.
	 *
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Send iote 2 e request.
	 *
	 * @param iote2eRequest the iote 2 e request
	 * @throws Exception the exception
	 */
	public void sendIote2eRequest( Iote2eRequest iote2eRequest ) throws Exception {
		queueIote2eRequests.add(iote2eRequest);
		iote2eRequestSendThread.interrupt();
	}

	/**
	 * The Class Iote2eRequestSendThread.
	 */
	protected class Iote2eRequestSendThread extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
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

		/**
		 * Shutdown.
		 */
		public void shutdown() {
			shutdown = true;
			interrupt();
		}
	}

	
	/**
	 * The Class Iote2eRequestReceiveThread.
	 */
	private class Iote2eRequestReceiveThread extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;
		
		/** The iote 2 e result reuse item. */
		private Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
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

		/**
		 * Shutdown.
		 */
		public void shutdown() {
			shutdown = true;
			interrupt();
		}
	}
	
	/**
	 * Gets the queue iote 2 e requests.
	 *
	 * @return the queue iote 2 e requests
	 */
	public ConcurrentLinkedQueue<Iote2eRequest> getQueueIote2eRequests() {
		return queueIote2eRequests;
	}

	/**
	 * Gets the queue iote 2 e results.
	 *
	 * @return the queue iote 2 e results
	 */
	public ConcurrentLinkedQueue<Iote2eResult> getQueueIote2eResults() {
		return queueIote2eResults;
	}

	/**
	 * Sets the queue iote 2 e requests.
	 *
	 * @param queueIote2eRequests the queue iote 2 e requests
	 * @return the client socket handler
	 */
	public ClientSocketHandler setQueueIote2eRequests(ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests) {
		this.queueIote2eRequests = queueIote2eRequests;
		return this;
	}

	/**
	 * Sets the queue iote 2 e results.
	 *
	 * @param queueIote2eResults the queue iote 2 e results
	 * @return the client socket handler
	 */
	public ClientSocketHandler setQueueIote2eResults(ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults) {
		this.queueIote2eResults = queueIote2eResults;
		return this;
	}

	/**
	 * Gets the login vo.
	 *
	 * @return the login vo
	 */
	public LoginVo getLoginVo() {
		return loginVo;
	}

	/**
	 * Sets the login vo.
	 *
	 * @param loginVo the login vo
	 * @return the client socket handler
	 */
	public ClientSocketHandler setLoginVo(LoginVo loginVo) {
		this.loginVo = loginVo;
		return this;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the url
	 * @return the client socket handler
	 */
	public ClientSocketHandler setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Gets the poll iote 2 e results thread.
	 *
	 * @return the poll iote 2 e results thread
	 */
	public Thread getPollIote2eResultsThread() {
		return pollIote2eResultsThread;
	}

	/**
	 * Sets the poll iote 2 e results thread.
	 *
	 * @param pollIote2eResultsThread the poll iote 2 e results thread
	 * @return the client socket handler
	 */
	public ClientSocketHandler setPollIote2eResultsThread(Thread pollIote2eResultsThread) {
		this.pollIote2eResultsThread = pollIote2eResultsThread;
		return this;
	}
}