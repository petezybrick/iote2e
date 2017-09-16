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
package com.pzybrick.iote2e.stream.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.CacheException;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteGridConnection;
import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;
import com.pzybrick.iote2e.stream.svc.RuleSvc;


/**
 * The Class Iote2eRequestRouterHandlerSparkRuleImpl.
 */
public class Iote2eRequestRouterHandlerSparkRuleImpl implements Iote2eRequestRouterHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eRequestRouterHandlerSparkRuleImpl.class);
	
	/** The rule svc. */
	private RuleSvc ruleSvc;
	
	/** The iote 2 e svc. */
	private Iote2eSvc iote2eSvc;
	
	/** The ignite grid connection. */
	private IgniteGridConnection igniteGridConnection;
	
	/** The iote 2 e result reuse item. */
	private Iote2eResultReuseItem iote2eResultReuseItem;

	/**
	 * Instantiates a new iote 2 e request router handler spark rule impl.
	 *
	 * @throws Exception the exception
	 */
	public Iote2eRequestRouterHandlerSparkRuleImpl( ) throws Exception {

	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eRequestRouterHandler#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			Class cls = Class.forName(masterConfig.getRuleSvcClassName());
			ruleSvc = (RuleSvc) cls.newInstance();
			cls = Class.forName(masterConfig.getRequestSvcClassName());
			iote2eSvc = (Iote2eSvc) cls.newInstance();
			
			ruleSvc.init(masterConfig);
			iote2eSvc.init(masterConfig);
			
			igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
			iote2eResultReuseItem = new Iote2eResultReuseItem();

		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eRequestRouterHandler#processRequests(java.util.List)
	 */
	public void processRequests( List<Iote2eRequest> iote2eRequests ) throws Exception {
		try {
			for( Iote2eRequest iote2eRequest : iote2eRequests ) {
				if (iote2eRequest != null) {
					List<RuleEvalResult> ruleEvalResults = ruleSvc.process( iote2eRequest);
					if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
						iote2eSvc.processRuleEvalResults( iote2eRequest, ruleEvalResults);
					}
					// Near real time temperature display
					final Utf8 sourceTypeTemperature = new Utf8("temperature");
					if( iote2eRequest.getSourceType().equals(sourceTypeTemperature) ) {
						nearRealTimeTemperature( iote2eRequest );
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	
	/**
	 * Near real time temperature.
	 *
	 * @param iote2eRequest the iote 2 e request
	 */
	private void nearRealTimeTemperature( Iote2eRequest iote2eRequest ) {
		final Utf8 keyTemp1 = new Utf8("temp1");
		CharSequence degreesC = iote2eRequest.getPairs().get(keyTemp1);
		logger.debug("Near Real Time Temp: sourceName={}, degreesC={}, requestTimestamp={}, requestUuid={}", 
				iote2eRequest.getSourceName(), degreesC, iote2eRequest.getRequestTimestamp(), iote2eRequest.getRequestUuid() );		
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), keyTemp1 );
		pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_VALUE), degreesC );

		Iote2eResult iote2eResult = Iote2eResult.newBuilder()
				.setPairs(pairs)
				//.setMetadata(ruleEvalResult.getMetadata())
				.setLoginName("$nrt$")
				.setSourceName( iote2eRequest.getSourceName())
				.setSourceType(iote2eRequest.getSourceType())
				.setRequestUuid(iote2eRequest.getRequestUuid())
				.setRequestTimestamp( iote2eRequest.getRequestTimestamp() )
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
			}
		}
		if( !isSuccess ) {
			logger.error("Ignite cache write failure, pk={}, iote2eResult={}, lastException: {}", Iote2eConstants.IGNITE_KEY_NRT_TEMPERATURE, iote2eResult.toString(), lastException.getLocalizedMessage(), lastException);
		}
		
	}

}
