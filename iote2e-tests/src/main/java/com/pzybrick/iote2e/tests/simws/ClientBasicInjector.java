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

public class ClientBasicInjector {
	private static final Logger logger = LogManager.getLogger(ClientBasicInjector.class);
	private URI uri;
	private WebSocketContainer container;
	private static Utf8 TEST_SOURCE_LOGIN = new Utf8("pzybrick1");
	private static Utf8 TEST_SOURCE_NAME = new Utf8("local_t001");
	private static Utf8 TEST_SOURCE_TYPE = new Utf8("testSourceType");
	private static Utf8 TEST_SENSOR_NAME = new Utf8("testSensorName");	// fan


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
}