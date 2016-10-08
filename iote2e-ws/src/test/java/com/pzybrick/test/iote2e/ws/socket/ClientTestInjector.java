package com.pzybrick.test.iote2e.ws.socket;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.util.component.LifeCycle;

import com.pzybrick.iote2e.common.utils.IotE2eConstants;
import com.pzybrick.iote2e.schema.avro.ActuatorResponse;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;
import com.pzybrick.iote2e.schema.util.ActuatorResponseFromByteArrayReuseItem;
import com.pzybrick.iote2e.schema.util.AvroSchemaUtils;
import com.pzybrick.iote2e.schema.util.SourceSensorValueToByteArrayReuseItem;

public class ClientTestInjector {
	private static final Log log = LogFactory.getLog(ClientTestInjector.class);
	private URI uri;
	private WebSocketContainer container;
	private static String testSourceUuid = "8043c648-a45d-4352-b024-1b4dd72fe9bc";
	private static String testSensorUuid = "3c3122da-6db6-4eb2-bbd3-55456e65d76d";	// fan


	public static void main(String[] args) {
		// "ws://localhost:8090/iote2e/"
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
				for (int i = 1; i < 2; i++) {
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
		private SourceSensorValueToByteArrayReuseItem toByteArrayReuseItem;
		private ActuatorResponseFromByteArrayReuseItem fromByteArrayReuseItem;

		public IotClientSocketThread() throws Exception {
			toByteArrayReuseItem = new SourceSensorValueToByteArrayReuseItem().setDatumWriterSourceSensorValue(
					new SpecificDatumWriter<SourceSensorValue>(SourceSensorValue.getClassSchema()));
			fromByteArrayReuseItem = new ActuatorResponseFromByteArrayReuseItem().setDatumReaderActuatorResponse(
					new SpecificDatumReader<ActuatorResponse>(ActuatorResponse.getClassSchema()));
		}

		@Override
		public void run() {
			Session session = null;
			try {
				ConcurrentLinkedQueue<byte[]> rcvdAvroByteArrays = new ConcurrentLinkedQueue<byte[]>();
				ClientSocketAvro iotClientSocketAvro = new ClientSocketAvro(this,rcvdAvroByteArrays);
				session = container.connectToServer(iotClientSocketAvro, uri);
				session.getBasicRemote().sendText(IotE2eConstants.LOGIN_HDR + login);
				for (int i = 45; i < 56; i++) {
					AvroSchemaUtils.sourceSensorValueToByteArray(
							toByteArrayReuseItem, testSourceUuid, testSensorUuid, String.valueOf(i));
					session.getBasicRemote().sendBinary(ByteBuffer.wrap(toByteArrayReuseItem.getBytes()));
					try {
						sleep(1000L);
					} catch (InterruptedException e) { }
				}
				log.info("Rcvd Messages:");
				while (!rcvdAvroByteArrays.isEmpty()) {
					byte[] rcvdAvroByteArray = rcvdAvroByteArrays.poll();
					AvroSchemaUtils.actuatorResponseFromByteArray(fromByteArrayReuseItem, rcvdAvroByteArray );
					log.info(fromByteArrayReuseItem.getActuatorResponse().toString());
				}
				try {
					sleep(2000L);
				} catch (InterruptedException e) { }
			} catch (Exception e) {
				log.error(e.getMessage(),e);
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