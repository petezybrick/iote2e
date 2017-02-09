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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.ws.route.RouteIote2eRequest;
import com.pzybrick.iote2e.ws.route.RouteIote2eRequestLoopbackImpl;

public class EntryPointIote2eRequest {
	private static final Logger logger = LogManager.getLogger(EntryPointIote2eRequest.class);
	public static final Map<String, ServerSideSocketIote2eRequest> serverSideSocketSourceSensorValues = new ConcurrentHashMap<String, ServerSideSocketIote2eRequest>();
	public static final ConcurrentLinkedQueue<Iote2eResult> toClientIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	public static final ConcurrentLinkedQueue<Iote2eRequest> fromClientIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	private RouteIote2eRequest routeIote2eRequest;
	private Server server;
	private ServerConnector connector;

	public static void main(String[] args) {
		logger.info("Starting");
		try {
			EntryPointIote2eRequest entryPointIote2eRequest = new EntryPointIote2eRequest();
			entryPointIote2eRequest.setRouteIote2eRequest( new RouteIote2eRequestLoopbackImpl() );
			int port = 8090;
			if( args.length > 0 ) port = Integer.parseInt(args[0]);
			entryPointIote2eRequest.process( port );
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}

	public void process(int port) throws Exception {
		if( routeIote2eRequest == null ) throw new Exception("routeIote2eRequest is required but is null");
		server = new Server();
		connector = new ServerConnector(server);
		connector.setPort(port);
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
			Map<String,List<Iote2eResult>> iote2eResultsByLoginName = new HashMap<String,List<Iote2eResult>>();
			try {
				while (true) {
					iote2eResultsByLoginName.clear();
					while (!toClientIote2eResults.isEmpty()) {
						Iote2eResult iote2eResult = toClientIote2eResults.poll();
						if( iote2eResult != null ) {
							List<Iote2eResult> iote2eResults = iote2eResultsByLoginName.get(iote2eResult.getLoginName());
							if( iote2eResults == null ) {
								iote2eResults = new ArrayList<Iote2eResult>();
								iote2eResultsByLoginName.put(iote2eResult.getLoginName().toString(),iote2eResults);
							}
							iote2eResults.add(iote2eResult);
						}
					}
					// Block up by target LoginName so multiple Avro instances can go on a single send to a single LoginName
					// TODO: this assumes a single login/single device.  To support single login/multiple device, 
					//       will have to group by LoginName and SourceName.
					Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
					for( Map.Entry<String,List<Iote2eResult>> entry : iote2eResultsByLoginName.entrySet()) {
						ServerSideSocketIote2eRequest socket = serverSideSocketSourceSensorValues.get(entry.getKey());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] bytes = null;
						try {
							for (Iote2eResult iote2eResult : entry.getValue() ) {
								baos.write( iote2eResultReuseItem.toByteArray(iote2eResult));
								// ? need to flush? binaryEncoder.flush();
							}
							bytes = baos.toByteArray();
							socket.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));

						} catch (Exception e) {
							logger.error("Exception sending byte message",e);
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