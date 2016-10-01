package com.pzybrick.test.iote2e.ws.socket;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.util.component.LifeCycle;

import com.pzybrick.iote2e.common.utils.IotE2eConstants;

public class ClientTestInjector {
	private static final Log log = LogFactory.getLog(ClientTestInjector.class);
	private URI uri;
	private WebSocketContainer container;

	public static void main(String[] args) {
		// "ws://localhost:8090/e2e/"
		try {
			ClientTestInjector clientTestInjector = new ClientTestInjector();
			clientTestInjector.process(args[0]);
		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
		}
	}

	public void process(String url) throws Exception {
		try {
			uri = URI.create(url);
			container = ContainerProvider.getWebSocketContainer();

			try {
				List<IotClientSocketThread> iotClientSocketThreads = new ArrayList<IotClientSocketThread>();
				for (int i = 1; i < 9; i++) {
					IotClientSocketThread iotClientSocketThread = new IotClientSocketThread().setLogin("test000" + i)
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

		@Override
		public void run() {
			Session session = null;
			try {
				ConcurrentLinkedQueue<String> rcvdMessages = new ConcurrentLinkedQueue<String>();
				ClientSocketAvro iotClientSocketAvro = new ClientSocketAvro(this,rcvdMessages);
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText(IotE2eConstants.LOGIN_HDR + login);
				for (int i = 0; i < 10; i++) {
					try {
						sleep(1000L);
					} catch (InterruptedException e) { }
					String msg = "Message from " + login + " at " + System.currentTimeMillis();
					//session.getBasicRemote().sendText(msg);
					byte[] msgByte = msg.getBytes();
					session.getBasicRemote().sendBinary(ByteBuffer.wrap(msgByte));
				}
				log.info("Rcvd Messages:");
				while (!rcvdMessages.isEmpty()) {
					log.info(rcvdMessages.poll());
				}
				try {
					sleep(2000L);
				} catch (InterruptedException e) { }
			} catch (Exception e) {
				log.error(e);
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