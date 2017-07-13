package com.pzybrick.iote2e.ws.omh;

import java.nio.ByteBuffer;
import java.util.ArrayList;
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
import com.pzybrick.iote2e.ws.route.RouteOmhByteBuffer;

public class ThreadEntryPointOmh extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointOmh.class);
	public static final Map<String, ServerSideSocketOmh> serverSideSocketByteBuffer = new ConcurrentHashMap<String, ServerSideSocketOmh>();
	public static final ConcurrentLinkedQueue<ByteBuffer> fromClientByteArrays = new ConcurrentLinkedQueue<ByteBuffer>();
	private RouteOmhByteBuffer routeOmhByteBuffer;
	private Server server;
	private ServerConnector connector;
	public static MasterConfig masterConfig;
	
	
	public ThreadEntryPointOmh( MasterConfig masterConfig ) {
		ThreadEntryPointOmh.masterConfig = masterConfig;
	}


	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			String routerImplClassName = masterConfig.getWsOmhRouterImplClassName();
			if( null == routerImplClassName || routerImplClassName.length() == 0 ) 
				throw new Exception("WS OMH routerImplClassName is required entry in MasterConfig but is null");
			Class clazz = Class.forName(routerImplClassName);
			routeOmhByteBuffer = (RouteOmhByteBuffer)clazz.newInstance();
			routeOmhByteBuffer.init(masterConfig);

			server = new Server();
			connector = new ServerConnector(server);
			connector.setPort(masterConfig.getWsOmhServerListenPort());
			server.addConnector(connector);
	
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
	
			try {
				ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
				wscontainer.addEndpoint(ServerSideSocketOmh.class);
				ThreadFromClientOmh threadFromClientOmh = new ThreadFromClientOmh( routeOmhByteBuffer );
				threadFromClientOmh.start();
	
				logger.info("Server starting");
				server.start();
				logger.info("Server started");
				server.join();
				threadFromClientOmh.shutdown();
				threadFromClientOmh.join(15 * 1000L);
	
			} catch (Throwable t) {
				logger.error("Server Exception",t);
			} finally {
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	
	public class ThreadFromClientOmh extends Thread {
		private boolean shutdown;
		private RouteOmhByteBuffer routeOmhByteBuffer;

		public ThreadFromClientOmh( RouteOmhByteBuffer routeOmhByteBuffer ) {
			super();
			this.routeOmhByteBuffer = routeOmhByteBuffer;
		}

		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			logger.info("ThreadFromClientOmh Run");
			List<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();
			try {
				while (true) {
					byteBuffers.clear();
					while (!fromClientByteArrays.isEmpty()) {
						ByteBuffer byteBuffer = fromClientByteArrays.poll();
						if( byteBuffer != null ) byteBuffers.add(byteBuffer);
					}
					for (ByteBuffer byteBuffer : byteBuffers) {
						routeOmhByteBuffer.routeToTarget(byteBuffer);
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
			}
			logger.info("Exit");
		}
	}

}