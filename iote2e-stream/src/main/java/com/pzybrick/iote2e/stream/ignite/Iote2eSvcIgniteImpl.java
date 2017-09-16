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
package com.pzybrick.iote2e.stream.ignite;

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
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;


/**
 * The Class Iote2eSvcIgniteImpl.
 */
public class Iote2eSvcIgniteImpl implements Iote2eSvc {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eSvcIgniteImpl.class);
	
	/** The iote 2 e result reuse item. */
	private Iote2eResultReuseItem iote2eResultReuseItem;
	
	/** The keyspace name. */
	private String keyspaceName;
	
	/** The ignite grid connection. */
	private IgniteGridConnection igniteGridConnection;
	
	/** The master config. */
	private MasterConfig masterConfig;

	/**
	 * Instantiates a new iote 2 e svc ignite impl.
	 *
	 * @throws Exception the exception
	 */
	public Iote2eSvcIgniteImpl() throws Exception {
		this.iote2eResultReuseItem = new Iote2eResultReuseItem();
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#processRuleEvalResults(com.pzybrick.iote2e.schema.avro.Iote2eRequest, java.util.List)
	 */
	@Override
	public synchronized void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			logger.debug( ruleEvalResult.toString() );
			if( ruleEvalResult.isRuleActuatorHit() ) {
				Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
				if( ruleEvalResult.isUpdateActuatorState() ) {
					logger.info("Updating Actuator: loginName="+ iote2eRequest.getLoginName().toString() + 
							", sourceName="+ iote2eRequest.getSourceName().toString() + 
							", actuatorName=" + ruleEvalResult.getActuatorState().getActuatorName() +
							", old value=" + ruleEvalResult.getActuatorState().getActuatorValue() + 
							", new value=" + ruleEvalResult.getActuatorTargetValue() );
					// Update the SourceSensorActuator
					ruleEvalResult.getActuatorState().setActuatorValue(ruleEvalResult.getActuatorTargetValue());
					ruleEvalResult.getActuatorState().setActuatorValueUpdatedAt(Iote2eUtils.getDateNowUtc8601() );
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), new Utf8(ruleEvalResult.getActuatorState().getSensorName() ));
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME), new Utf8(ruleEvalResult.getActuatorState().getActuatorName() ));
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE), new Utf8(ruleEvalResult.getActuatorState().getActuatorValue() ));
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT), new Utf8(ruleEvalResult.getActuatorState().getActuatorValueUpdatedAt() ));
				} else {
					logger.info("Processing Actuator: loginName="+ iote2eRequest.getLoginName().toString() + 
							", sourceName="+ iote2eRequest.getSourceName().toString() + 
							", value=" + ruleEvalResult.getActuatorTargetValue() );
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE), new Utf8(ruleEvalResult.getActuatorTargetValue()));
					pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT), new Utf8(Iote2eUtils.getDateNowUtc8601()));
				}
				
				
				Iote2eResult iote2eResult = Iote2eResult.newBuilder()
					.setPairs(pairs)
					.setMetadata(ruleEvalResult.getMetadata())
					.setLoginName(iote2eRequest.getLoginName())
					.setSourceName(iote2eRequest.getSourceName())
					.setSourceType(iote2eRequest.getSourceType())
					.setRequestUuid(iote2eRequest.getRequestUuid())
					.setRequestTimestamp(iote2eRequest.getRequestTimestamp())
					.setOperation(OPERATION.ACTUATOR_VALUES)
					.setResultCode(0)
					.setResultTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()))
					.setResultUuid( new Utf8(UUID.randomUUID().toString()))
					.build();
				
				if( logger.isDebugEnabled() ) logger.debug("iote2eResult: " + iote2eResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				boolean isSuccess = false;
				Exception lastException = null;
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				String pkIgnite = null;
				if( ruleEvalResult.isUseLongIgniteKey() ) {
					pkIgnite = iote2eRequest.getLoginName().toString()+"|"+iote2eRequest.getSourceName().toString() +
						"|"+ruleEvalResult.getSensorName()+"|"+ruleEvalResult.getActuatorState().getActuatorName();
				} else {
					pkIgnite = iote2eRequest.getLoginName().toString()+"|"+iote2eRequest.getSourceName().toString() + "|";
				}

				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteGridConnection.getCache().put(pkIgnite, iote2eResultReuseItem.toByteArray(iote2eResult));
						isSuccess = true;
						logger.info("cache.put successful, cache name={}, pk={}, value={}", igniteGridConnection.getCache().getName(), pkIgnite, ruleEvalResult.getActuatorTargetValue());
						break;
					} catch( CacheException cacheException ) {
						lastException = cacheException;
						cntRetry++;
						logger.warn("cache.put failed with CacheException, will retry, cntRetry={}"  );
						try { Thread.sleep(1000L); } catch(Exception e ) {}
					} catch( Exception e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
				}
				if( !isSuccess ) {
					logger.error("Ignite cache write failure, pk={}, value={}, lastException: {}", pkIgnite, ruleEvalResult.getActuatorTargetValue(), lastException.getLocalizedMessage(), lastException);
					throw new Exception( lastException);
				}
				
				// TODO: need better mechanism to do the Ignite and Cassandra updates
				// Update Cassandra
				if( ruleEvalResult.isUpdateActuatorState() ) {
					isSuccess = false;
					lastException = null;
					timeoutAt = System.currentTimeMillis() + (15*1000L);
					cntRetry = 0;
					String pkActuatorState = iote2eRequest.getLoginName().toString()+"|"+iote2eRequest.getSourceName().toString() +
							"|"+ruleEvalResult.getSensorName()+"|";
					while( System.currentTimeMillis() < timeoutAt ) {
						try {
							ActuatorStateDao.updateActuatorValue(pkActuatorState, ruleEvalResult.getActuatorState().getActuatorValue());
							isSuccess = true;
							logger.debug("actuator_state updated, pk={}, value={}", pkActuatorState, ruleEvalResult.getActuatorState().getActuatorValue());
							break;
						} catch( Exception e ) {
							logger.error(e.getMessage(),e);
							throw e;
						}
					}
					if( !isSuccess ) {
						logger.error("actuator_state update failure, pk={}, value={}, lastException: {}", lastException.getLocalizedMessage(), 
								pkActuatorState, ruleEvalResult.getActuatorState().getActuatorValue(), lastException);
						throw new Exception( lastException);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	@Override
	public synchronized void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
			this.keyspaceName = masterConfig.getKeyspaceName();
			ActuatorStateDao.connect(masterConfig.getContactPoint(), keyspaceName);
			logger.info("Getting IgniteCache for: " + masterConfig.getIgniteCacheName());
			igniteGridConnection = new IgniteGridConnection().connect(masterConfig);
		} catch (Exception e) {
			logger.error("Ignite create cache failure", e);
			throw e;
		} finally {
		}
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#close()
	 */
	@Override
	public synchronized void close() throws Exception {
		try {
			if( igniteGridConnection != null )  {
				// Be careful - ignite is a singleton, only close after last usage
				igniteGridConnection.getCache().close();
				igniteGridConnection.getIgnite().close();
				igniteGridConnection = null;
			}
			ActuatorStateDao.disconnect();
		} catch (Exception e) {
			logger.warn("Ignite close failure", e);
		}
	}
}
