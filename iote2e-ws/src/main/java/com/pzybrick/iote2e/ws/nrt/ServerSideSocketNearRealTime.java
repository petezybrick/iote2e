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


/**
 * The Class ServerSideSocketNearRealTime.
 */
@ClientEndpoint
@ServerEndpoint(value = "/nrt/")
public class ServerSideSocketNearRealTime {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ServerSideSocketNearRealTime.class);
	
	/** The session. */
	private Session session;
	
	/** The thread ignite subscribe temperature. */
	private ThreadIgniteSubscribe threadIgniteSubscribeTemperature;
	
	/** The thread ignite subscribe omh. */
	private ThreadIgniteSubscribe threadIgniteSubscribeOmh;
	
	/** The thread ignite subscribe bdbb. */
	private ThreadIgniteSubscribe threadIgniteSubscribeBdbb;

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Instantiates a new server side socket near real time.
	 */
	public ServerSideSocketNearRealTime() {

	}

	/**
	 * On web socket connect.
	 *
	 * @param session the session
	 * @throws Exception the exception
	 */
	@OnOpen
	public void onWebSocketConnect(Session session) throws Exception {
		this.session = session;
		ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.put(Iote2eConstants.SOCKET_KEY_NRT, this);
		threadIgniteSubscribeTemperature = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointNearRealTime.masterConfig, Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE,
				ThreadEntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		threadIgniteSubscribeOmh = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointNearRealTime.masterConfig, Iote2eConstants.IGNITE_KEY_NRT_OMH,
				ThreadEntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		threadIgniteSubscribeBdbb = ThreadIgniteSubscribe.startThreadSubscribe( ThreadEntryPointNearRealTime.masterConfig, Iote2eConstants.IGNITE_KEY_NRT_BDBB,
				ThreadEntryPointNearRealTime.toClientIote2eResults, (Thread)null );
		//new ThreadPumpTestData().start();
		logger.info("Socket Connected: " + session.getId());
	}

	/**
	 * On web socket text.
	 *
	 * @param message the message
	 */
	@OnMessage
	public void onWebSocketText(String message) {
		logger.debug("onWebSocketText " + message);
	}

	/**
	 * On web socket byte.
	 *
	 * @param bytes the bytes
	 */
	@OnMessage
	public void onWebSocketByte(byte[] bytes) {
		logger.debug("onWebSocketByte len=" + bytes.length);
	}

	/**
	 * On web socket close.
	 *
	 * @param reason the reason
	 */
	@OnClose
	public void onWebSocketClose(CloseReason reason) {
		boolean isRemove = ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.remove(Iote2eConstants.SOCKET_KEY_NRT, this);
		logger.info("Socket Closed: " + reason + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}

	/**
	 * On web socket error.
	 *
	 * @param cause the cause
	 */
	@OnError
	public void onWebSocketError(Throwable cause) {
		boolean isRemove = ThreadEntryPointNearRealTime.serverSideSocketNearRealTimes.remove(Iote2eConstants.SOCKET_KEY_NRT, this);
		logger.info("Socket Error: " + cause.getMessage() + ", isRemove=" + isRemove);
		shutdownThreadIgniteSubscribe();
	}
	
	/**
	 * Shutdown thread ignite subscribe.
	 */
	private void shutdownThreadIgniteSubscribe() {
		logger.debug("Shutting down threadIgniteSubscribe");
		try {
			threadIgniteSubscribeTemperature.shutdown();
			threadIgniteSubscribeOmh.shutdown();
			threadIgniteSubscribeBdbb.shutdown();
			threadIgniteSubscribeTemperature.join(5000);
			threadIgniteSubscribeOmh.join(5000);
			threadIgniteSubscribeBdbb.join(5000);
		} catch( InterruptedException e ) {
		} catch( Exception e ) {
			logger.error(e.getMessage());
		}
	}
	


	/**
	 * The Class ThreadPumpTestData.
	 */
	public class ThreadPumpTestData extends Thread {
		
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
	
	/**
	 * The Class PumpTemperatureItem.
	 */
	private class PumpTemperatureItem {
		
		/** The source name. */
		private String sourceName;
		
		/** The degrees C. */
		private float degreesC;
		
		/** The is increasing. */
		private boolean isIncreasing = true;
		
		/**
		 * Instantiates a new pump temperature item.
		 *
		 * @param rpiName the rpi name
		 * @param temperature the temperature
		 */
		public PumpTemperatureItem(String rpiName, float temperature) {
			super();
			this.sourceName = rpiName;
			this.degreesC = temperature;
		}
		
		/**
		 * Gets the source name.
		 *
		 * @return the source name
		 */
		public String getSourceName() {
			return sourceName;
		}
		
		/**
		 * Sets the source name.
		 *
		 * @param sourceName the source name
		 * @return the pump temperature item
		 */
		public PumpTemperatureItem setSourceName(String sourceName) {
			this.sourceName = sourceName;
			return this;
		}
		
		/**
		 * Gets the degrees C.
		 *
		 * @return the degrees C
		 */
		public float getDegreesC() {
			return degreesC;
		}
		
		/**
		 * Checks if is increasing.
		 *
		 * @return true, if is increasing
		 */
		public boolean isIncreasing() {
			return isIncreasing;
		}
		
		/**
		 * Sets the degrees C.
		 *
		 * @param degreesC the degrees C
		 * @return the pump temperature item
		 */
		public PumpTemperatureItem setDegreesC(float degreesC) {
			this.degreesC = degreesC;
			return this;
		}
		
		/**
		 * Sets the increasing.
		 *
		 * @param isIncreasing the is increasing
		 * @return the pump temperature item
		 */
		public PumpTemperatureItem setIncreasing(boolean isIncreasing) {
			this.isIncreasing = isIncreasing;
			return this;
		}
	}

}