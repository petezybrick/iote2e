package com.pzybrick.iote2e.ws.nrt;

import java.nio.ByteBuffer;
import java.time.Instant;
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
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.ws.route.RouteIote2eRequest;

public class EntryPointNearRealTime {
	private static final Logger logger = LogManager.getLogger(EntryPointNearRealTime.class);
	public static final Map<String, ServerSideSocketNearRealTime> serverSideSocketNearRealTimes = new ConcurrentHashMap<String, ServerSideSocketNearRealTime>();
	public static final ConcurrentLinkedQueue<Iote2eResult> toClientIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	private Server server;
	private ServerConnector connector;
	public static MasterConfig masterConfig;
	
	
	public EntryPointNearRealTime( ) {
	}
	
	
	public static void main(String[] args) {
		logger.info("Starting");
		try {
			masterConfigWithRetries( args );
			EntryPointNearRealTime entryPointNearRealTime = new EntryPointNearRealTime();
			entryPointNearRealTime.process( );
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}

	/*
	 * Give Cassandra a minute to start
	 */
	private static void masterConfigWithRetries( String args[] ) throws Exception {
		final int RETRY_MINUTES = 10;
		long maxWait = System.currentTimeMillis() + (RETRY_MINUTES * 60 * 1000);
		Exception exception = null;
		while( true ) {
			try {
				masterConfig = MasterConfig.getInstance( args[0], args[1], args[2] );
				return;
			} catch(Exception e ) {
				exception = e;
			}
			if( System.currentTimeMillis() > maxWait ) break;
			logger.debug("retrying Cassandra connection");
			try { Thread.sleep(5000); } catch(Exception e) {}
		}
		throw exception;
	}

	public void process( ) throws Exception {
		logger.info(masterConfig.toString());
		server = new Server();
		connector = new ServerConnector(server);
		connector.setPort(masterConfig.getWsServerListenPort());
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		try {
			ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
			wscontainer.addEndpoint(ServerSideSocketNearRealTime.class);
			ThreadToBrowserNrtMonitor threadToBrowserNrtMonitor = new ThreadToBrowserNrtMonitor();
			threadToBrowserNrtMonitor.start();

			logger.info("Server starting");
			server.start();
			logger.info("Server started");
			server.join();
			threadToBrowserNrtMonitor.shutdown();
			threadToBrowserNrtMonitor.join(15 * 1000L);

		} catch (Throwable t) {
			logger.error("Server Exception",t);
		} finally {
		}
	}


	public class ThreadToBrowserNrtMonitor extends Thread {
		private boolean shutdown;

		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			logger.info("ThreadToBrowserNrtMonitor Run");
			try {
				while (true) {
					while (!toClientIote2eResults.isEmpty()) {
						Iote2eResult iote2eResult = toClientIote2eResults.poll();
						if( iote2eResult != null ) {
							ServerSideSocketNearRealTime socket = null;
							socket = serverSideSocketNearRealTimes.get(ServerSideSocketNearRealTime.NRT_IGNITE_KEY);						
							if( socket != null ) {
								try {
									// Create TemperatureSensorItem from values in Iote2eResult
									float degreesC = Float.parseFloat(iote2eResult.getPairs().get("temp1").toString());
									TemperatureSensorItem TemperatureSensorItem = new TemperatureSensorItem()
											.setLoginName(iote2eResult.getLoginName().toString())
											.setDegreesC(degreesC)
											.setTimeMillis( Instant.parse( iote2eResult.getRequestTimestamp() ).toEpochMilli());
									String rawJson = Iote2eUtils.getGsonInstance().toJson(TemperatureSensorItem);
									socket.getSession().getBasicRemote().sendText(rawJson);
								} catch (Exception e) {
									logger.error("Exception sending text message",e);
									break;
								} finally {
								}
							} 
						}
					}

					try {
						sleep(500L);
					} catch (InterruptedException e) {}
					if (shutdown)
						break;
				}
			} catch (Exception e) {
				logger.error("Exception processing target text message", e);
			}
			logger.info("Exit");
		}
	}

}