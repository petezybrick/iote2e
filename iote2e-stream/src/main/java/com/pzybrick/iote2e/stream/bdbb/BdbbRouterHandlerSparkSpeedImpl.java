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
package com.pzybrick.iote2e.stream.bdbb;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.cache.CacheException;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteGridConnection;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.stream.email.Email;


/**
 * The Class BdbbRouterHandlerSparkSpeedImpl.
 */
public class BdbbRouterHandlerSparkSpeedImpl implements BdbbRouterHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BdbbRouterHandlerSparkSpeedImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/** The ignite grid connection. */
	private IgniteGridConnection igniteGridConnection;
	
	/** The iote 2 e result reuse item. */
	private Iote2eResultReuseItem iote2eResultReuseItem;
	
	/** The object mapper. */
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;
	
	/** The nrt filter oil pressure. */
	private Set<String> nrtFilterOilPressure = new HashSet<String>();


	/**
	 * Instantiates a new bdbb router handler spark speed impl.
	 *
	 * @throws Exception the exception
	 */
	public BdbbRouterHandlerSparkSpeedImpl( ) throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.bdbb.BdbbRouterHandler#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
			this.igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
			this.iote2eResultReuseItem = new Iote2eResultReuseItem();
			// TODO: dynamically update this list based on inbound request(s)
			// For now, one user and one schema
			this.nrtFilterOilPressure = new HashSet<String>();
			this.nrtFilterOilPressure.add( "LH411" );
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.bdbb.BdbbRouterHandler#processRequests(java.util.List)
	 */
	public void processRequests( List<ByteBuffer> byteBuffers ) throws Exception {
		try {
			if( byteBuffers != null && byteBuffers.size() > 0 ) {
				FlightStatus flightStatus = null;
				for( ByteBuffer byteBuffer : byteBuffers) {
					// Decompress the JSON string
					String rawJson = new String( CompressionUtils.decompress(byteBuffer.array()) );
					try {
						flightStatus = Iote2eUtils.getGsonInstance().fromJson(rawJson, FlightStatus.class);
				        if( nrtFilterOilPressure.contains(flightStatus.getFlightNumber())) {
				        	nearRealTimeOilPressure( flightStatus );
							checkOilPressureExceeded( flightStatus );
				        }
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
						throw e;
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
		}
	}
	
	
	/**
	 * Check oil pressure exceeded.
	 *
	 * @param flightStatus the flight status
	 * @throws Exception the exception
	 */
	/*
	 * TODO: need a true rule processor here, this is just a simple stub check Oil Pressure exceeded
	 */
	private void checkOilPressureExceeded( FlightStatus flightStatus ) throws Exception {
		int engineNumber = 1;
		for( EngineStatus engineStatus : flightStatus.getEngineStatuss() ) {
			if( engineStatus.getOilPressure() >= 90f ) {
				Instant instant = Instant.ofEpochMilli(flightStatus.getFlightStatusTs());
				String fmtNow = DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneId.of("UTC")).format(instant);
				String summary = String.format("FlightNumber %s, Engine %d had an Oil Pressure of %4.2f on %s", flightStatus.getFlightNumber(), engineNumber, engineStatus.getOilPressure() , fmtNow );
				Email.sendEmailBdbb( masterConfig.getSmtpLoginBdbb(), masterConfig.getSmtpPasswordBdbb(), masterConfig.getSmtpEmailBdbb(), "chief.mechanic.zybrick@gmail.com", "Chief Mechanic Zybrick", summary );				
			}
			engineNumber++;
		}
	}
	
	
	/**
	 * Near real time oil pressure.
	 *
	 * @param flightStatus the flight status
	 * @throws Exception the exception
	 */
	private void nearRealTimeOilPressure( FlightStatus flightStatus ) throws Exception {	
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		int engineNumber = 1;
		for( EngineStatus engineStatus : flightStatus.getEngineStatuss() ) {
			CharSequence oilPressure = new Utf8( String.valueOf(engineStatus.getOilPressure().floatValue() ));
			pairs.put( new Utf8("engine"+engineNumber), oilPressure );
			engineNumber++;
		}

		Iote2eResult iote2eResult = Iote2eResult.newBuilder()
				.setPairs(pairs)
				//.setMetadata(ruleEvalResult.getMetadata())
				.setLoginName("$bdbb$")
				.setSourceName( new Utf8(flightStatus.getFlightNumber()))
				.setSourceType( new Utf8("oil-pressure"))
				.setRequestUuid( new Utf8(UUID.randomUUID().toString()))
				.setRequestTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()) )
				.setOperation(OPERATION.SENSORS_VALUES)
				.setResultCode(0)
				.setResultTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()))
				.setResultUuid( new Utf8(UUID.randomUUID().toString()))
				.build();
		
		boolean isSuccess = false;
		Exception lastException = null;
		long timeoutAt = System.currentTimeMillis() + (15*1000L);
		while( System.currentTimeMillis() < timeoutAt ) {
			try {
				igniteGridConnection.getCache().put(Iote2eConstants.IGNITE_KEY_NRT_BDBB, iote2eResultReuseItem.toByteArray(iote2eResult));
				isSuccess = true;
				logger.debug("cache.put successful, cache name={}, pk={}, iote2eResult={}", igniteGridConnection.getCache().getName(), Iote2eConstants.IGNITE_KEY_NRT_OMH, iote2eResult.toString() );
				break;
			} catch( CacheException cacheException ) {
				lastException = cacheException;
				logger.warn("cache.put failed with CacheException, will retry, cntRetry={}"  );
				try { Thread.sleep(1000L); } catch(Exception e ) {}
			} catch( Exception e ) {
				logger.error(e.getMessage(),e);
			}
		}
		if( !isSuccess ) {
			logger.error("Ignite cache write failure, pk={}, iote2eResult={}, lastException: {}", Iote2eConstants.IGNITE_KEY_NRT_BDBB, iote2eResult.toString(), lastException.getLocalizedMessage(), lastException);
		}
		
	}

}
		
