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

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteSingleton;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.ws.route.RouteIote2eRequest;

public class EntryPointIote2eRequest {
	private static final Logger logger = LogManager.getLogger(EntryPointIote2eRequest.class);
	public static final Map<String, ServerSideSocketIote2eRequest> serverSideSocketSourceSensorValues = new ConcurrentHashMap<String, ServerSideSocketIote2eRequest>();
	public static final ConcurrentLinkedQueue<Iote2eResult> toClientIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	public static final ConcurrentLinkedQueue<Iote2eRequest> fromClientIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	public static IgniteSingleton igniteSingleton;
	private RouteIote2eRequest routeIote2eRequest;
	private Server server;
	private ServerConnector connector;
	private MasterConfig masterConfig;
	

	public static void main(String[] args) {
		logger.info("Starting");
		try {
			EntryPointIote2eRequest entryPointIote2eRequest = new EntryPointIote2eRequest();
			entryPointIote2eRequest.process( );
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}

	public void process( ) throws Exception {
		masterConfig = MasterConfig.getInstance();
		logger.info(masterConfig.toString());
		igniteSingleton = IgniteSingleton.getInstance(masterConfig);
		String routerImplClassName = masterConfig.getWsRouterImplClassName();
		if( null == routerImplClassName || routerImplClassName.length() == 0 ) 
			throw new Exception("routerImplClassName is required entry in MasterConfig but is null");
		Class clazz = Class.forName(routerImplClassName);
		routeIote2eRequest = (RouteIote2eRequest)clazz.newInstance();
		server = new Server();
		connector = new ServerConnector(server);
		connector.setPort(masterConfig.getWsServerListenPort());
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		try {
			ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
			wscontainer.addEndpoint(ServerSideSocketIote2eRequest.class);
			ThreadFromClientIote2eRequest threadFromClientIote2eRequest = new ThreadFromClientIote2eRequest(
					routeIote2eRequest);
			threadFromClientIote2eRequest.start();
			ThreadToClientLoginIote2eResult threadToClientLoginIote2eResult = new ThreadToClientLoginIote2eResult();
			threadToClientLoginIote2eResult.start();

			logger.info("Server starting");
			server.start();
			logger.info("Server started");
			server.join();
			threadToClientLoginIote2eResult.shutdown();
			threadFromClientIote2eRequest.shutdown();
			threadToClientLoginIote2eResult.join(15 * 1000L);
			threadFromClientIote2eRequest.join(15 * 1000L);

		} catch (Throwable t) {
			logger.error("Server Exception",t);
		} finally {
		}
	}

	public static class ThreadFromClientIote2eRequest extends Thread {
		private RouteIote2eRequest routeIote2eRequest;
		private boolean shutdown;

		public ThreadFromClientIote2eRequest(RouteIote2eRequest routeIote2eRequest) {
			super();
			this.routeIote2eRequest = routeIote2eRequest;
		}

		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			logger.info("Run");
			List<Iote2eRequest> iote2eRequests = new ArrayList<Iote2eRequest>();
			try {
				while (true) {
					while (!fromClientIote2eRequests.isEmpty()) {
						iote2eRequests.add(fromClientIote2eRequests.poll());
					}
					for (Iote2eRequest iote2eRequest : iote2eRequests) {
						// TODO: error recovery
						routeIote2eRequest.routeToTarget(iote2eRequest);
					}
					iote2eRequests.clear();
					try {
						sleep(500L);
					} catch (InterruptedException e) {
					}
					if (shutdown)
						break;
				}
			} catch (Exception e) {
				logger.error("Exception in source thread processing", e);
			}
			logger.info("Exit");
		}
	}

	public static class ThreadToClientLoginIote2eResult extends Thread {
		private boolean shutdown;

		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			logger.info("Run");
			Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
			Map<String,List<Iote2eResult>> iote2eResultsByLoginName = new HashMap<String,List<Iote2eResult>>();
			try {
				while (true) {
					iote2eResultsByLoginName.clear();
					while (!toClientIote2eResults.isEmpty()) {
						Iote2eResult iote2eResult = toClientIote2eResults.poll();
						if( iote2eResult != null ) {
							// First try to find login|sourceName|sensorName, if not exists then login|sourceName
							String sensorName = null;
							if( iote2eResult.getOperation() == OPERATION.ACTUATOR_VALUES ) {
								if( iote2eResult.getPairs().containsKey( Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME ) ) 
									sensorName = iote2eResult.getPairs().get( Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME ).toString();
							}
							ServerSideSocketIote2eRequest socket = null;
							String key = null;
							if( sensorName != null ) {
								key = iote2eResult.getLoginName() + "|" + iote2eResult.getSourceName() + "|" + sensorName;
								socket = serverSideSocketSourceSensorValues.get(key);
							}
							if( socket == null ) {
								key = iote2eResult.getLoginName() + "|" + iote2eResult.getSourceName();
								socket = serverSideSocketSourceSensorValues.get(key);
							}
							if( socket != null ) {
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								byte[] bytes = null;
								try {
									baos.write( iote2eResultReuseItem.toByteArray(iote2eResult));
									bytes = baos.toByteArray();
									socket.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
								} catch (Exception e) {
									logger.error("Exception sending byte message",e);
									break;
								} finally {
									baos.close();
								}
							} else logger.error("Can't find socket with key: {}", key);
						}
					}

					try {
						sleep(500L);
					} catch (InterruptedException e) {}
					if (shutdown)
						break;
				}
			} catch (Exception e) {
				logger.error("Exception processing target byte message", e);
			}
			logger.info("Exit");
		}
	}

	public RouteIote2eRequest getRouteIote2eRequest() {
		return routeIote2eRequest;
	}

	public EntryPointIote2eRequest setRouteIote2eRequest(RouteIote2eRequest routeLoginSourceSensorValue) {
		this.routeIote2eRequest = routeLoginSourceSensorValue;
		return this;
	}

}