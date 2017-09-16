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
package com.pzybrick.iote2e.ws.bdbb;

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
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.ws.route.RouteBdbbByteBuffer;


/**
 * The Class ThreadEntryPointBdbb.
 */
public class ThreadEntryPointBdbb extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointBdbb.class);
	
	/** The Constant serverSideSocketByteBuffer. */
	public static final Map<String, ServerSideSocketBdbb> serverSideSocketByteBuffer = new ConcurrentHashMap<String, ServerSideSocketBdbb>();
	
	/** The Constant fromClientByteArrays. */
	public static final ConcurrentLinkedQueue<ByteBuffer> fromClientByteArrays = new ConcurrentLinkedQueue<ByteBuffer>();
	
	/** The route bdbb byte buffer. */
	private RouteBdbbByteBuffer routeBdbbByteBuffer;
	
	/** The server. */
	private Server server;
	
	/** The connector. */
	private ServerConnector connector;
	
	/** The master config. */
	public static MasterConfig masterConfig;
	
	
	/**
	 * Instantiates a new thread entry point bdbb.
	 *
	 * @param masterConfig the master config
	 */
	public ThreadEntryPointBdbb( MasterConfig masterConfig ) {
		ThreadEntryPointBdbb.masterConfig = masterConfig;
	}


	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			String routerImplClassName = masterConfig.getWsBdbbRouterImplClassName();
			if( null == routerImplClassName || routerImplClassName.length() == 0 ) 
				throw new Exception("WS BDBB routerImplClassName is required entry in MasterConfig but is null");
			Class clazz = Class.forName(routerImplClassName);
			routeBdbbByteBuffer = (RouteBdbbByteBuffer)clazz.newInstance();
			routeBdbbByteBuffer.init(masterConfig);

			server = new Server();
			connector = new ServerConnector(server);
			connector.setPort(masterConfig.getWsBdbbServerListenPort());
			server.addConnector(connector);
	
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
	
			try {
				ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
				wscontainer.addEndpoint(ServerSideSocketBdbb.class);
				ThreadFromClientBdbb threadFromClientBdbb = new ThreadFromClientBdbb( routeBdbbByteBuffer );
				threadFromClientBdbb.start();
	
				logger.info("Server starting");
				server.start();
				logger.info("Server started");
				server.join();
				threadFromClientBdbb.shutdown();
				threadFromClientBdbb.join(15 * 1000L);
	
			} catch (Throwable t) {
				logger.error("Server Exception",t);
			} finally {
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	
	/**
	 * The Class ThreadFromClientBdbb.
	 */
	public class ThreadFromClientBdbb extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;
		
		/** The route bdbb byte buffer. */
		private RouteBdbbByteBuffer routeBdbbByteBuffer;

		/**
		 * Instantiates a new thread from client bdbb.
		 *
		 * @param routeBdbbByteBuffer the route bdbb byte buffer
		 */
		public ThreadFromClientBdbb( RouteBdbbByteBuffer routeBdbbByteBuffer ) {
			super();
			this.routeBdbbByteBuffer = routeBdbbByteBuffer;
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
			logger.info("ThreadFromClientBdbb Run");
			List<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>();
			try {
				while (true) {
					byteBuffers.clear();
					while (!fromClientByteArrays.isEmpty()) {
						ByteBuffer byteBuffer = fromClientByteArrays.poll();
						if( byteBuffer != null ) byteBuffers.add(byteBuffer);
					}
					for (ByteBuffer byteBuffer : byteBuffers) {
						logger.debug(">>> bytebuffer length {}",  byteBuffer.array().length );
						routeBdbbByteBuffer.routeToTarget(byteBuffer);
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