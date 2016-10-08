package com.pzybrick.iote2e.ws.socket;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.server.ServerContainer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.pzybrick.iote2e.schema.avro.ActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;
import com.pzybrick.iote2e.ws.route.RouteLoginSourceSensorValue;
import com.pzybrick.iote2e.ws.route.RouteLoginSourceSensorValueLoopbackImpl;

public class EntryPointServerSourceSensorValue {
	private static final Log log = LogFactory.getLog(EntryPointServerSourceSensorValue.class);
	public static final Map<String, ServerSideSocketSourceSensorValue> serverSideSocketSourceSensorValues = new ConcurrentHashMap<String, ServerSideSocketSourceSensorValue>();
	public static final ConcurrentLinkedQueue<LoginActuatorResponse> toClientActuatorResponses = new ConcurrentLinkedQueue<LoginActuatorResponse>();
	public static final ConcurrentLinkedQueue<LoginSourceSensorValue> fromClientLoginSourceSensorValues = new ConcurrentLinkedQueue<LoginSourceSensorValue>();
	private RouteLoginSourceSensorValue routeLoginSourceSensorValue;
	private Server server;
	private ServerConnector connector;

	public static void main(String[] args) {
		log.info("Starting");
		try {
			EntryPointServerSourceSensorValue entryPointServerSourceSensorValue = new EntryPointServerSourceSensorValue();
			entryPointServerSourceSensorValue.setRouteLoginSourceSensorValue( new RouteLoginSourceSensorValueLoopbackImpl() );
			entryPointServerSourceSensorValue.process();
		} catch( Exception e ) {
			log.error(e.getMessage(),e);
		}
	}

	public void process() throws Exception {
		if( routeLoginSourceSensorValue == null ) throw new Exception("routeLoginSourceSensorValue is required but is null");
		server = new Server();
		connector = new ServerConnector(server);
		connector.setPort(8090);
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		try {
			ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
			wscontainer.addEndpoint(ServerSideSocketSourceSensorValue.class);
			ThreadFromClientLoginSourceSensorValue threadFromClientLoginSourceSensorValue = new ThreadFromClientLoginSourceSensorValue(
					routeLoginSourceSensorValue);
			threadFromClientLoginSourceSensorValue.start();
			ThreadToClientLoginActuatorResponse threadToClientLoginActuatorResponse = new ThreadToClientLoginActuatorResponse();
			threadToClientLoginActuatorResponse.start();

			log.info("Server starting");
			server.start();
			log.info("Server started");
			server.join();
			threadToClientLoginActuatorResponse.shutdown();
			threadFromClientLoginSourceSensorValue.shutdown();
			threadToClientLoginActuatorResponse.join(15 * 1000L);
			threadFromClientLoginSourceSensorValue.join(15 * 1000L);

		} catch (Throwable t) {
			log.error("Server Exception",t);
		} finally {
		}
	}

	public static class ThreadFromClientLoginSourceSensorValue extends Thread {
		private RouteLoginSourceSensorValue routeLoginSourceSensorValue;
		private boolean shutdown;

		public ThreadFromClientLoginSourceSensorValue(RouteLoginSourceSensorValue routeLoginSourceSensorValue) {
			super();
			this.routeLoginSourceSensorValue = routeLoginSourceSensorValue;
		}

		public void shutdown() {
			log.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			log.info("Run");
			List<LoginSourceSensorValue> loginSourceSensorValues = new ArrayList<LoginSourceSensorValue>();
			try {
				while (true) {
					while (!fromClientLoginSourceSensorValues.isEmpty()) {
						loginSourceSensorValues.add(fromClientLoginSourceSensorValues.poll());
					}
					for (LoginSourceSensorValue loginSourceSensorValue : loginSourceSensorValues) {
						// TODO: error recovery
						routeLoginSourceSensorValue.routeToTarget(loginSourceSensorValue);
					}
					loginSourceSensorValues.clear();
					try {
						sleep(500L);
					} catch (InterruptedException e) {
					}
					if (shutdown)
						break;
				}
			} catch (Exception e) {
				log.error("Exception in source thread processing", e);
			}
			log.info("Exit");
		}
	}

	public static class ThreadToClientLoginActuatorResponse extends Thread {
		private boolean shutdown;

		public void shutdown() {
			log.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			log.info("Run");
			DatumWriter<ActuatorResponse> datumWriterActuatorResponse = new SpecificDatumWriter<ActuatorResponse>(ActuatorResponse.getClassSchema());
			BinaryEncoder binaryEncoder = null;
			//List<IotServerMessage> iotServerMessages = new ArrayList<IotServerMessage>();
			Map<String,List<LoginActuatorResponse>> loginActuatorResponsesByLoginUuid = new HashMap<String,List<LoginActuatorResponse>>();
			try {
				while (true) {
					loginActuatorResponsesByLoginUuid.clear();
					while (!toClientActuatorResponses.isEmpty()) {
						LoginActuatorResponse loginActuatorResponse = toClientActuatorResponses.poll();
						List<LoginActuatorResponse> loginActuatorResponses = loginActuatorResponsesByLoginUuid.get(loginActuatorResponse.getLoginUuid());
						if( loginActuatorResponses == null ) {
							loginActuatorResponses = new ArrayList<LoginActuatorResponse>();
							loginActuatorResponsesByLoginUuid.put(loginActuatorResponse.getLoginUuid().toString(),loginActuatorResponses);
						}
						loginActuatorResponses.add(loginActuatorResponse);
					}
					// Block up by target Login UUID so multiple Avro instances can go on a single send to a single Login UUID
					// TODO: this assumes a single login/single device.  To support single login/multiple device, 
					//       will have to group by login uuid and source uuid.
					for( Map.Entry<String,List<LoginActuatorResponse>> entry : loginActuatorResponsesByLoginUuid.entrySet()) {
						ServerSideSocketSourceSensorValue socket = serverSideSocketSourceSensorValues.get(entry.getKey());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] bytes = null;
						try {
							binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
							for (LoginActuatorResponse loginActuatorResponse : entry.getValue() ) {
								// Convert to ActuatorResponse so the client doeesn't get the LoginUuid
								ActuatorResponse actuatorResponse = ActuatorResponse.newBuilder()
										.setSourceUuid(loginActuatorResponse.getActuatorUuid())
										.setSensorUuid(loginActuatorResponse.getSensorUuid())
										.setActuatorUuid(loginActuatorResponse.getActuatorUuid())
										.setActuatorValue(loginActuatorResponse.getActuatorValue())
										.setActuatorValueUpdatedAt(loginActuatorResponse.getActuatorValueUpdatedAt())
										.build();
								datumWriterActuatorResponse.write(actuatorResponse, binaryEncoder);
								binaryEncoder.flush();
							}
							bytes = baos.toByteArray();
							socket.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));

						} catch (Exception e) {
							log.error("Exception sending byte message",e);
							break;
						} finally {
							baos.close();
						}
					}

					try {
						sleep(500L);
					} catch (InterruptedException e) {
					}
					if (shutdown)
						break;
				}
			} catch (Exception e) {
				log.error("Exception processing target byte message", e);
			}
			log.info("Exit");
		}
	}

	public RouteLoginSourceSensorValue getRouteLoginSourceSensorValue() {
		return routeLoginSourceSensorValue;
	}

	public EntryPointServerSourceSensorValue setRouteLoginSourceSensorValue(RouteLoginSourceSensorValue routeLoginSourceSensorValue) {
		this.routeLoginSourceSensorValue = routeLoginSourceSensorValue;
		return this;
	}

}