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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.ws.security.LoginVo;


/**
 * The Class ClientBasicInjector.
 */
public class ClientBasicInjector {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ClientBasicInjector.class);
	
	/** The uri. */
	private URI uri;
	
	/** The container. */
	private WebSocketContainer container;
	
	/** The test source login. */
	private static Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");
	
	/** The test source name. */
	private static Utf8 TEST_SOURCE_NAME = new Utf8("local_t001");
	
	/** The test source type. */
	private static Utf8 TEST_SOURCE_TYPE = new Utf8("testSourceType");
	
	/** The test sensor name. */
	private static Utf8 TEST_SENSOR_NAME = new Utf8("testSensorName");	// fan


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// "ws://localhost:8090/iote2e/"
		try {
			ClientBasicInjector clientBasicInjector = new ClientBasicInjector();
			clientBasicInjector.process(args[0]);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
	}

	/**
	 * Process.
	 *
	 * @param url the url
	 * @throws Exception the exception
	 */
	public void process(String url) throws Exception {
		try {
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
				List<IotClientSocketThread> iotClientSocketThreads = new ArrayList<IotClientSocketThread>();
				for (int i = 1; i < 2; i++) {
					IotClientSocketThread iotClientSocketThread = new IotClientSocketThread().setLogin( TEST_SOURCE_LOGIN.toString() )
							.setUri(uri).setContainer(container);
					iotClientSocketThreads.add(iotClientSocketThread);
					iotClientSocketThread.start();
				}

				for (IotClientSocketThread iotClientSocketThread : iotClientSocketThreads) {
					iotClientSocketThread.join();
				}

			} finally {
				// Force lifecycle stop when done with container.
				// This is to free up threads and resources that the
				// JSR-356 container allocates. But unfortunately
				// the JSR-356 spec does not handle lifecycles (yet)
				if (container instanceof LifeCycle) {
					((LifeCycle) container).stop();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
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
				//LoginVo loginVo = new LoginVo().setLogin(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString()).setOptionalFilterSensorName(TEST_SENSOR_NAME.toString());
				LoginVo loginVo = new LoginVo().setLoginName(TEST_SOURCE_LOGIN.toString()).setSourceName(TEST_SOURCE_NAME.toString());
				ConcurrentLinkedQueue<byte[]> iote2eResultBytes = new ConcurrentLinkedQueue<byte[]>();
				ClientSocketAvro iotClientSocketAvro = new ClientSocketAvro(this,iote2eResultBytes);
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText( Iote2eUtils.getGsonInstance().toJson(loginVo));
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
}