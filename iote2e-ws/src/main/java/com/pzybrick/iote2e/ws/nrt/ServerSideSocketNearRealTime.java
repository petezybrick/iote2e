package com.pzybrick.iote2e.ws.nrt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.CacheException;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.ignite.IgniteGridConnection;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;

@ClientEndpoint
@ServerEndpoint(value = "/nrt/")
public class ServerSideSocketNearRealTime {
	private static final Logger logger = LogManager.getLogger(ServerSideSocketNearRealTime.class);
	private Session session;
	private ThreadIgniteSubscribe threadIgniteSubscribeTemperature;
	private ThreadIgniteSubscribe threadIgniteSubscribeOmh;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ServerSideSocketNearRealTime() {

	}

	@OnOpen
	public void onWebSocketConnect(Session session) throws Exception {
		this.session = session;
		ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.put(Iote2eConstants.SOCKET_KEY_NRT, this);
		threadIgniteSubscribeTemperature = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointNearRealTime.masterConfig, Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE,
				ThreadEntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		threadIgniteSubscribeOmh = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointNearRealTime.masterConfig, Iote2eConstants.IGNITE_KEY_NRT_OMH,
				ThreadEntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		//new ThreadPumpTestData().start();
		logger.info("Socket Connected: " + session.getId());
	}

	@OnMessage
	public void onWebSocketText(String message) {
		logger.debug("onWebSocketText " + message);
	}

	@OnMessage
	public void onWebSocketByte(byte[] bytes) {
		logger.debug("onWebSocketByte len=" + bytes.length);
	}

	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.remove(Iote2eConstants.SOCKET_KEY_NRT, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}

	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.remove(Iote2eConstants.SOCKET_KEY_NRT, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}
	
	private void shutdownThreadIgniteSubscribe() {
		logger.debug("Shutting down threadIgniteSubscribe");
		try {
			threadIgniteSubscribeTemperature.shutdown();
			threadIgniteSubscribeOmh.shutdown();
			threadIgniteSubscribeTemperature.join(5000);
			threadIgniteSubscribeOmh.join(5000);
		} catch( InterruptedException e ) {
		} catch( Exception e ) {
			logger.error(e.getMessage());
		}
	}
	


	public class ThreadPumpTestData extends Thread {
		private boolean shutdown;

		public void shutdown() {
			logger.info("Shutdown");
			shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			logger.info("ThreadPumpTestData Run");
			final List<String> rpis = Arrays.asList("rpi-001","rpi-002","rpi-003");
			final float tempMin = 30.0f;
			final float tempMax = 50.0f;
			final float tempIncr = 4.0f;
			try {
				IgniteGridConnection igniteGridConnection = new IgniteGridConnection().connect(ThreadEntryPointNearRealTime.masterConfig);
				Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
				
				List<PumpTemperatureItem> pumpTemperatureItems = new ArrayList<PumpTemperatureItem>();
				pumpTemperatureItems.add( new PumpTemperatureItem("rpi-001", tempMin ));
				pumpTemperatureItems.add( new PumpTemperatureItem("rpi-002", tempMin + tempIncr ));
				pumpTemperatureItems.add( new PumpTemperatureItem("rpi-003", tempMin + (2*tempIncr) ));
				TemperatureSensorItem temperatureSensorItem = new TemperatureSensorItem();
				while( true ) {
					for( PumpTemperatureItem pumpTemperatureItem : pumpTemperatureItems ) {
						Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();

						pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), new Utf8("temp1"));
						pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_VALUE), new Utf8(String.valueOf(pumpTemperatureItem.getDegreesC()) ));

						Iote2eResult iote2eResult = Iote2eResult.newBuilder()
								.setPairs(pairs)
								//.setMetadata(ruleEvalResult.getMetadata())
								.setLoginName("$nrt$")
								.setSourceName(new Utf8(pumpTemperatureItem.getSourceName()))
								.setSourceType("temp")
								.setRequestUuid(new Utf8(UUID.randomUUID().toString()))
								.setRequestTimestamp(new Utf8(Iote2eUtils.getDateNowUtc8601()))
								.setOperation(OPERATION.SENSORS_VALUES)
								.setResultCode(0)
								.setResultTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()))
								.setResultUuid( new Utf8(UUID.randomUUID().toString()))
								.build();
						// EntryPointNearRealTime.toClientIote2eResults.add( iote2eResult );
						
						boolean isSuccess = false;
						Exception lastException = null;
						long timeoutAt = System.currentTimeMillis() + (15*1000L);
						while( System.currentTimeMillis() < timeoutAt ) {
							try {
								igniteGridConnection.getCache().put(Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE, iote2eResultReuseItem.toByteArray(iote2eResult));
								isSuccess = true;
								logger.debug("cache.put successful, cache name={}, pk={}, iote2eResult={}", igniteGridConnection.getCache().getName(), Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE, iote2eResult.toString() );
								break;
							} catch( CacheException cacheException ) {
								lastException = cacheException;
								logger.warn("cache.put failed with CacheException, will retry, cntRetry={}"  );
								try { Thread.sleep(1000L); } catch(Exception e ) {}
							} catch( Exception e ) {
								logger.error(e.getMessage(),e);
								throw e;
							}
						}
						if( !isSuccess ) {
							logger.error("Ignite cache write failure, pk={}, iote2eResult={}, lastException: {}", Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE, iote2eResult.toString(), lastException.getLocalizedMessage(), lastException);
							throw new Exception( lastException);
						}
						
						if( pumpTemperatureItem.isIncreasing ) pumpTemperatureItem.setDegreesC(pumpTemperatureItem.getDegreesC() + tempIncr );
						else pumpTemperatureItem.setDegreesC(pumpTemperatureItem.getDegreesC() - tempIncr);
						if( pumpTemperatureItem.getDegreesC() > tempMax ) pumpTemperatureItem.setIncreasing(false);
						else if( pumpTemperatureItem.getDegreesC() < tempMin ) pumpTemperatureItem.setIncreasing(true);
					}

					try {
						sleep(1000L);
					} catch (InterruptedException e) {}
					if (shutdown)
						return;
				}
				
			} catch (Exception e) {
				logger.error("Exception processing target text message", e);
			}
			logger.info("Exit");
		}
	}
	
	private class PumpTemperatureItem {
		private String sourceName;
		private float degreesC;
		private boolean isIncreasing = true;
		
		public PumpTemperatureItem(String rpiName, float temperature) {
			super();
			this.sourceName = rpiName;
			this.degreesC = temperature;
		}
		public String getSourceName() {
			return sourceName;
		}
		public PumpTemperatureItem setSourceName(String sourceName) {
			this.sourceName = sourceName;
			return this;
		}
		public float getDegreesC() {
			return degreesC;
		}
		public boolean isIncreasing() {
			return isIncreasing;
		}
		public PumpTemperatureItem setDegreesC(float degreesC) {
			this.degreesC = degreesC;
			return this;
		}
		public PumpTemperatureItem setIncreasing(boolean isIncreasing) {
			this.isIncreasing = isIncreasing;
			return this;
		}
	}

}