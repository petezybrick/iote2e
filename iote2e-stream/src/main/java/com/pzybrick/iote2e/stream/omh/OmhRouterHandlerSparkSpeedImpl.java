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
package com.pzybrick.iote2e.stream.omh;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPoint;

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
 * The Class OmhRouterHandlerSparkSpeedImpl.
 */
public class OmhRouterHandlerSparkSpeedImpl implements OmhRouterHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(OmhRouterHandlerSparkSpeedImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/** The ignite grid connection. */
	private IgniteGridConnection igniteGridConnection;
	
	/** The iote 2 e result reuse item. */
	private Iote2eResultReuseItem iote2eResultReuseItem;
	
	/** The object mapper. */
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;
	
	/** The nrt filter blood pressure. */
	private Set<String> nrtFilterBloodPressure = new HashSet<String>();


	/**
	 * Instantiates a new omh router handler spark speed impl.
	 *
	 * @throws Exception the exception
	 */
	public OmhRouterHandlerSparkSpeedImpl( ) throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.omh.OmhRouterHandler#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
			this.igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
			this.iote2eResultReuseItem = new Iote2eResultReuseItem();
			// TODO: dynamically update this list based on inbound request(s)
			// For now, one user and one schema
			this.nrtFilterBloodPressure = new HashSet<String>();
			this.nrtFilterBloodPressure.add( "nicholas.chapman@gmail.com|blood-pressure" );
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.omh.OmhRouterHandler#processRequests(java.util.List)
	 */
	public void processRequests( List<ByteBuffer> byteBuffers ) throws Exception {
		try {
			if( byteBuffers != null && byteBuffers.size() > 0 ) {
				DataPoint dataPoint = null;
				for( ByteBuffer byteBuffer : byteBuffers) {
					// Decompress the JSON string
					String rawJson = new String( CompressionUtils.decompress(byteBuffer.array()) );
					// JSON into Datapoint
					try {
				        dataPoint = objectMapper.readValue(rawJson, DataPoint.class);
				        logger.debug( "OMH Datapoint: {} {}, userId={}, uuid={}", dataPoint.getHeader().getBodySchemaId().getName(),
				        		dataPoint.getHeader().getBodySchemaId().getVersion(), dataPoint.getHeader().getUserId(),
				        		dataPoint.getHeader().getId() );
				        String nrtKey = dataPoint.getHeader().getUserId() + "|" + dataPoint.getHeader().getBodySchemaId().getName();
				        if( nrtFilterBloodPressure.contains(nrtKey)) {
				            // TODO: for some reason can get the generic on the Body to work on local tests, but not after streaming through kafka, maybe some jackson version issue
				            //		the error: java.util.LinkedHashMap cannot be cast to org.openmhealth.schema.domain.omh.BloodGlucose
				            // For now, this works - turn Body into string, then turn that string into the correct class, seems like a hack that using generics should avoid
				            String rawBody = objectMapper.writeValueAsString(dataPoint.getBody());
				    		BloodPressure bloodPressure = objectMapper.readValue(rawBody, BloodPressure.class);

				        	nearRealTimeBloodPressure( dataPoint, bloodPressure );
				        	checkBloodPressureExceeded( dataPoint, bloodPressure );
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
	 * Check blood pressure exceeded.
	 *
	 * @param dataPoint the data point
	 * @param bloodPressure the blood pressure
	 * @throws Exception the exception
	 */
	/*
	 * TODO: need a true rule processor here, this is just a simple stub check BP Diastolic exceeded
	 */
	private void checkBloodPressureExceeded( DataPoint dataPoint, BloodPressure bloodPressure ) throws Exception {
		// TODO: get patient's name from login/email address, doctor name and email, and timezone from database
        //String fmtNow = Instant.now().atZone(ZoneId.of("America/New_York")).toString();
        //final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZ");
        //String fmtNow = now.format(formatter);
		int systolic = bloodPressure.getSystolicBloodPressure().getValue().intValue();
		int diastolic = bloodPressure.getDiastolicBloodPressure().getValue().intValue();
		if( diastolic > 100 ) {
			String fmtNow = DateTimeFormatter.RFC_1123_DATE_TIME.format(dataPoint.getHeader().getCreationDateTime());
	        //String fmtNow = DateTimeFormatter.RFC_1123_DATE_TIME.format(Instant.now());
			String summary = String.format("%s had a Blood Pressure of %d/%d on %s", dataPoint.getHeader().getUserId(), systolic, diastolic, fmtNow );
			Email.sendEmailOmh( masterConfig.getSmtpLoginOmh(), masterConfig.getSmtpPasswordOmh(), masterConfig.getSmtpEmailOmh(), "drzybrick@gmail.com", "Dr. Zybrick", summary );
		}
	}
	
	
	/**
	 * Near real time blood pressure.
	 *
	 * @param dataPoint the data point
	 * @param bloodPressure the blood pressure
	 * @throws Exception the exception
	 */
	private void nearRealTimeBloodPressure( DataPoint dataPoint, BloodPressure bloodPressure ) throws Exception {
		CharSequence systolic = new Utf8( String.valueOf(bloodPressure.getSystolicBloodPressure().getValue().intValue() ));
		CharSequence diastolic = new Utf8( String.valueOf(bloodPressure.getDiastolicBloodPressure().getValue().intValue() ));
	
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( new Utf8("SYSTOLIC"), systolic );
		pairs.put( new Utf8("DIASTOLIC"), diastolic );

		Iote2eResult iote2eResult = Iote2eResult.newBuilder()
				.setPairs(pairs)
				//.setMetadata(ruleEvalResult.getMetadata())
				.setLoginName("$omh$")
				.setSourceName( dataPoint.getHeader().getUserId())
				.setSourceType("blood-pressure")
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
				igniteGridConnection.getCache().put(Iote2eConstants.IGNITE_KEY_NRT_OMH, iote2eResultReuseItem.toByteArray(iote2eResult));
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
			logger.error("Ignite cache write failure, pk={}, iote2eResult={}, lastException: {}", Iote2eConstants.IGNITE_KEY_NRT_OMH, iote2eResult.toString(), lastException.getLocalizedMessage(), lastException);
		}
		
	}

}
		
