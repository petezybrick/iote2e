package com.pzybrick.iote2e.ws.nrt;

import java.time.Instant;
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
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;

public class ThreadEntryPointNearRealTime extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointNearRealTime.class);
	public static final Map<String, ServerSideSocketNearRealTime> serverSideSocketNearRealTimes = new ConcurrentHashMap<String, ServerSideSocketNearRealTime>();
	public static final ConcurrentLinkedQueue<Iote2eResult> toClientIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	private Server server;
	private ServerConnector connector;
	public static MasterConfig masterConfig;
	
	
	public ThreadEntryPointNearRealTime( ) {
	}
	
	
	public ThreadEntryPointNearRealTime( MasterConfig masterConfig ) {
		ThreadEntryPointNearRealTime.masterConfig = masterConfig;
	}


	public void run( ) {
		logger.info(masterConfig.toString());
		try {
			server = new Server();
			connector = new ServerConnector(server);
			connector.setPort(masterConfig.getWsNrtServerListenPort());
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
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
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
			TemperatureSensorItem temperatureSensorItem = new TemperatureSensorItem();
			logger.info("ThreadToBrowserNrtMonitor Run");
			try {
				while (true) {
					while (!toClientIote2eResults.isEmpty()) {
						Iote2eResult iote2eResult = toClientIote2eResults.poll();
						if( iote2eResult != null ) {
							ServerSideSocketNearRealTime socket = null;
							socket = serverSideSocketNearRealTimes.get(Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE);						
							if( socket != null ) {
								try {
									// Create TemperatureSensorItem from values in Iote2eResult
									float degreesC = Float.parseFloat(iote2eResult.getPairs().get(new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_VALUE)).toString());
									temperatureSensorItem
											.setSourceName(iote2eResult.getSourceName().toString())
											.setDegreesC(degreesC)
											.setTimeMillis( Instant.parse( iote2eResult.getRequestTimestamp() ).toEpochMilli());
									String rawJson = Iote2eUtils.getGsonInstance().toJson(temperatureSensorItem);
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