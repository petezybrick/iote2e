package com.pzybrick.iote2e.ruleproc.ignite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.CacheException;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;

public class Iote2eSvcIgniteImpl implements Iote2eSvc {
	private static final Logger logger = LogManager.getLogger(Iote2eSvcIgniteImpl.class);
	private IgniteSingleton igniteSingleton;
	private Iote2eResultReuseItem iote2eResultReuseItem;
	private String keyspaceName;

	public Iote2eSvcIgniteImpl() throws Exception {
		this.iote2eResultReuseItem = new Iote2eResultReuseItem();
		this.keyspaceName = System.getenv("CASSANDRA_KEYSPACE_NAME");
		ActuatorStateDao.useKeyspace(keyspaceName);
	}

	@Override
	public synchronized void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			logger.debug( ruleEvalResult.toString() );
			if( ruleEvalResult.isRuleActuatorHit() ) {
				logger.info("Updating Actuator: loginName="+ iote2eRequest.getLoginName().toString() + 
						", sourceName="+ iote2eRequest.getSourceName().toString() + 
						", actuatorName=" + ruleEvalResult.getActuatorState().getActuatorName() +
						", old value=" + ruleEvalResult.getActuatorState().getActuatorValue() + 
						", new value=" + ruleEvalResult.getActuatorTargetValue() );
				// Update the SourceSensorActuator
				ruleEvalResult.getActuatorState().setActuatorValue(ruleEvalResult.getActuatorTargetValue());
				ruleEvalResult.getActuatorState().setActuatorValueUpdatedAt(Iote2eUtils.getDateNowUtc8601() );
				String pkActuatorState = iote2eRequest.getLoginName().toString()+"|"+iote2eRequest.getSourceName().toString() +
						"|"+ruleEvalResult.getSensorName();
				String pkIgnite = pkActuatorState+"|"+ruleEvalResult.getActuatorState().getActuatorName();
				
				Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME), new Utf8(ruleEvalResult.getActuatorState().getSensorName() ));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME), new Utf8(ruleEvalResult.getActuatorState().getActuatorName() ));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE), new Utf8(ruleEvalResult.getActuatorState().getActuatorValue() ));
				pairs.put( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT), new Utf8(ruleEvalResult.getActuatorState().getActuatorValueUpdatedAt() ));
				
				Iote2eResult iote2eResult = Iote2eResult.newBuilder()
					.setPairs(pairs)
					.setLoginName(iote2eRequest.getLoginName())
					.setSourceName(iote2eRequest.getSourceName())
					.setSourceType(iote2eRequest.getSourceType())
					.setRequestUuid(iote2eRequest.getRequestUuid())
					.setRequestTimestamp(iote2eRequest.getRequestTimestamp())
					.setOperation(iote2eRequest.getOperation())
					.setResultCode(8)
					.setResultErrorMessage( new Utf8("testErrorMessage"))
					.setResultTimestamp( new Utf8(Iote2eUtils.getDateNowUtc8601()))
					.setResultUuid(  new Utf8(UUID.randomUUID().toString()))
					.build();
				
				if( logger.isDebugEnabled() ) logger.debug("iote2eResult: " + iote2eResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				boolean isSuccess = false;
				Exception lastException = null;
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteSingleton.getCache().put(pkIgnite, iote2eResultReuseItem.toByteArray(iote2eResult));
						isSuccess = true;
						logger.debug("cache.put successful, pk={}, value={}", pkIgnite, ruleEvalResult.getActuatorState().getActuatorValue());
						break;
					} catch( CacheException cacheException ) {
						lastException = cacheException;
						cntRetry++;
						if( logger.isDebugEnabled() ) logger.debug("cache.put failed with CacheException, will retry, cntRetry=" + cntRetry );
						try { Thread.sleep(1000L); } catch(Exception e ) {}
					} catch( Exception e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
				}
				if( !isSuccess ) {
					logger.error("Ignite cache write failure, pk={}, value={}, lastException: {}", pkIgnite, ruleEvalResult.getActuatorState().getActuatorValue(), lastException.getLocalizedMessage(), lastException);
					throw new Exception( lastException);
				}
				// TODO: need better mechanism to do the Ignite and Cassandra updates
				// Update Cassandra
				isSuccess = false;
				lastException = null;
				timeoutAt = System.currentTimeMillis() + (15*1000L);
				cntRetry = 0;
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

	@Override
	public synchronized void init(RuleConfig ruleConfig) throws Exception {
		try {
			logger.info("Getting IgniteCache for: " + ruleConfig.getSourceResponseIgniteCacheName());
			this.igniteSingleton = IgniteSingleton.getInstance(ruleConfig);
		} catch (Exception e) {
			logger.error("Ignite create cache failure", e);
			throw e;
		} finally {
		}
	}

	@Override
	public synchronized void close() throws Exception {
		try {
			if( igniteSingleton != null )  {
				// Be careful - ignite is a singleton, only close after last usage
				igniteSingleton.getIgnite().close();
				igniteSingleton = null;
			}
		} catch (Exception e) {
			logger.warn("Ignite close failure", e);
		}
	}
}
