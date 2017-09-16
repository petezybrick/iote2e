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

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.ws.route.RouteIote2eRequest;


/**
 * The Class ThreadEntryPointIote2eRequest.
 */
public class ThreadEntryPointIote2eRequest extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointIote2eRequest.class);
	
	/** The Constant serverSideSocketIote2eRequest. */
	public static final Map<String, ServerSideSocketIote2eRequest> serverSideSocketIote2eRequest = new ConcurrentHashMap<String, ServerSideSocketIote2eRequest>();
	
	/** The Constant toClientIote2eResults. */
	public static final ConcurrentLinkedQueue<Iote2eResult> toClientIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	
	/** The Constant fromClientIote2eRequests. */
	public static final ConcurrentLinkedQueue<Iote2eRequest> fromClientIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	
	/** The route iote 2 e request. */
	private RouteIote2eRequest routeIote2eRequest;
	
	/** The server. */
	private Server server;
	
	/** The connector. */
	private ServerConnector connector;
	
	/** The master config. */
	public static MasterConfig masterConfig;
	
	
	/**
	 * Instantiates a new thread entry point iote 2 e request.
	 *
	 * @param masterConfig the master config
	 */
	public ThreadEntryPointIote2eRequest( MasterConfig masterConfig ) {
		ThreadEntryPointIote2eRequest.masterConfig = masterConfig;
	}


	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			String routerImplClassName = masterConfig.getWsRouterImplClassName();
			if( null == routerImplClassName || routerImplClassName.length() == 0 ) 
				throw new Exception("routerImplClassName is required entry in MasterConfig but is null");
			Class clazz = Class.forName(routerImplClassName);
			routeIote2eRequest = (RouteIote2eRequest)clazz.newInstance();
			routeIote2eRequest.init(masterConfig);
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
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * The Class ThreadFromClientIote2eRequest.
	 */
	public class ThreadFromClientIote2eRequest extends Thread {
		
		/** The route iote 2 e request. */
		private RouteIote2eRequest routeIote2eRequest;
		
		/** The shutdown. */
		private boolean shutdown;

		/**
		 * Instantiates a new thread from client iote 2 e request.
		 *
		 * @param routeIote2eRequest the route iote 2 e request
		 */
		public ThreadFromClientIote2eRequest(RouteIote2eRequest routeIote2eRequest) {
			super();
			this.routeIote2eRequest = routeIote2eRequest;
		}

		/**
		 * Shutdown.
		 */
		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			logger.info("ThreadFromClientIote2eRequest Run");
			List<Iote2eRequest> iote2eRequests = new ArrayList<Iote2eRequest>();
			try {
				while (true) {
					iote2eRequests.clear();
					while (!fromClientIote2eRequests.isEmpty()) {
						Iote2eRequest iote2eRequest = fromClientIote2eRequests.poll();
						if( iote2eRequest != null ) iote2eRequests.add(iote2eRequest);
					}
					for (Iote2eRequest iote2eRequest : iote2eRequests) {
						routeIote2eRequest.routeToTarget(iote2eRequest);
					}
					try {
						sleep(500L);
					} catch (InterruptedException e) {
					}
					if (shutdown) {
						logger.debug("exiting due to shutdown");
						break;
					}
				}
			} catch (Exception e) {
				logger.error("Exception in source thread processing", e);
			} catch (Throwable t) {
				logger.error("Exception in source thread processing {}", t.getMessage(),t);
			} finally {
				logger.debug("+++++++++++ How did I get here?");
			}
			logger.info("Exit");
		}
	}

	/**
	 * The Class ThreadToClientLoginIote2eResult.
	 */
	public class ThreadToClientLoginIote2eResult extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;

		/**
		 * Shutdown.
		 */
		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			logger.info("ThreadToClientLoginIote2eResult Run");
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
								key = iote2eResult.getLoginName() + "|" + iote2eResult.getSourceName() + "|" + sensorName + "|";
								socket = serverSideSocketIote2eRequest.get(key);
							}
							if( socket == null ) {
								key = iote2eResult.getLoginName() + "|" + iote2eResult.getSourceName() + "|";
								socket = serverSideSocketIote2eRequest.get(key);
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

	/**
	 * Gets the route iote 2 e request.
	 *
	 * @return the route iote 2 e request
	 */
	public RouteIote2eRequest getRouteIote2eRequest() {
		return routeIote2eRequest;
	}

	/**
	 * Sets the route iote 2 e request.
	 *
	 * @param routeLoginSourceSensorValue the route login source sensor value
	 * @return the thread entry point iote 2 e request
	 */
	public ThreadEntryPointIote2eRequest setRouteIote2eRequest(RouteIote2eRequest routeLoginSourceSensorValue) {
		this.routeIote2eRequest = routeLoginSourceSensorValue;
		return this;
	}

}