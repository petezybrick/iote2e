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
			final CharSequence checkTemp = new Utf8("temperature");
			final CharSequence checkBloodPressure = new Utf8("blood-pressure");
			final CharSequence checkSystolic = new Utf8("SYSTOLIC");
			final CharSequence checkDiastolic = new Utf8("DIASTOLIC");
			final CharSequence checkEngineOilPressure = new Utf8("oil-pressure");
			final CharSequence checkEngine1 = new Utf8("engine1");
			final CharSequence checkEngine2 = new Utf8("engine2");
			final CharSequence checkEngine3 = new Utf8("engine3");
			final CharSequence checkEngine4 = new Utf8("engine4");
			TemperatureSensorItem temperatureSensorItem = new TemperatureSensorItem();
			BloodPressureSensorItem bloodPressureSensorItem = new BloodPressureSensorItem();
			EngineOilPressureSensorItem engineOilPressureSensorItem = new EngineOilPressureSensorItem();
			logger.info("ThreadToBrowserNrtMonitor Run");
			try {
				while (true) {
					logger.info("ThreadToBrowserNrtMonitor alive");
					while (!toClientIote2eResults.isEmpty()) {
						Iote2eResult iote2eResult = toClientIote2eResults.poll();
						if( iote2eResult != null ) {
							logger.debug("sourceType {}", iote2eResult.getSourceType() );
							ServerSideSocketNearRealTime socket = null;
							socket = serverSideSocketNearRealTimes.get(Iote2eConstants.SOCKET_KEY_NRT);
							logger.debug("socket {}", socket );
							if( socket != null ) {
								try {
									if( checkTemp.equals(iote2eResult.getSourceType())) {
										logger.debug("processing temperature");
										// Create TemperatureSensorItem from values in Iote2eResult
										float degreesC = Float.parseFloat(iote2eResult.getPairs().get(new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_VALUE)).toString());
										temperatureSensorItem
												.setSourceName(iote2eResult.getSourceName().toString())
												.setDegreesC(degreesC)
												.setTimeMillis( Instant.parse( iote2eResult.getRequestTimestamp() ).toEpochMilli());
										String rawJson = Iote2eUtils.getGsonInstance().toJson(temperatureSensorItem);
										socket.getSession().getBasicRemote().sendText(rawJson);
									} else if( checkBloodPressure.equals(iote2eResult.getSourceType())) {
										logger.debug("processing blood pressure systolic: {}", iote2eResult.getPairs().get(checkSystolic));
										int systolic = Integer.parseInt(iote2eResult.getPairs().get(checkSystolic).toString());
										int diastolic = Integer.parseInt(iote2eResult.getPairs().get(checkDiastolic).toString());
										logger.debug("systolic {}, diastolic {}", systolic, diastolic);
										bloodPressureSensorItem
												.setSourceName(iote2eResult.getSourceName().toString())
												.setTimeMillis( Instant.parse( iote2eResult.getRequestTimestamp() ).toEpochMilli())
												.setSystolic(systolic)
												.setDiastolic(diastolic);
										String rawJson = Iote2eUtils.getGsonInstance().toJson(bloodPressureSensorItem);
										logger.debug("blood pressure raw json: {}", rawJson );
										socket.getSession().getBasicRemote().sendText(rawJson);
									} else if( checkEngineOilPressure.equals(iote2eResult.getSourceType())) {
										// this is a hack
										Float engine1 = null;
										Float engine2 = null;
										Float engine3 = null;
										Float engine4 = null;
										if(iote2eResult.getPairs().containsKey(checkEngine1)) {
											engine1 = Float.parseFloat(iote2eResult.getPairs().get(checkEngine1).toString());
										}
										if(iote2eResult.getPairs().containsKey(checkEngine2)) {
											engine2 = Float.parseFloat(iote2eResult.getPairs().get(checkEngine2).toString());
										}
										if(iote2eResult.getPairs().containsKey(checkEngine3)) {
											engine3 = Float.parseFloat(iote2eResult.getPairs().get(checkEngine3).toString());
										}
										if(iote2eResult.getPairs().containsKey(checkEngine4)) {
											engine4 = Float.parseFloat(iote2eResult.getPairs().get(checkEngine4).toString());
										}
										logger.debug("processing engine oil pressure 1: {}, 2: {}, 3: {}, 4: {}", engine1, engine2, engine3, engine4 );
										engineOilPressureSensorItem
												.setFlightNumber(iote2eResult.getSourceName().toString())
												.setTimeMillis( Instant.parse( iote2eResult.getRequestTimestamp() ).toEpochMilli())
												.setEngine1(engine1)
												.setEngine2(engine2)
												.setEngine3(engine3)
												.setEngine4(engine4);
										String rawJson = Iote2eUtils.getGsonInstance().toJson(engineOilPressureSensorItem);
										logger.debug("engine oil pressure raw json: {}", rawJson );
										socket.getSession().getBasicRemote().sendText(rawJson);
									} else logger.warn("No match on sourceType: {} ", iote2eResult.getSourceType());
								} catch (Throwable e) {
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