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
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;

public class Iote2eSvcIgniteImpl implements Iote2eSvc {
	private static final Logger logger = LogManager.getLogger(Iote2eSvcIgniteImpl.class);
	private IgniteSingleton igniteSingleton;
	private Iote2eResultReuseItem iote2eResultReuseItem;

	public Iote2eSvcIgniteImpl() throws Exception {
		this.iote2eResultReuseItem = new Iote2eResultReuseItem();
	}

	@Override
	public void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			if( ruleEvalResult.isRuleActuatorHit() ) {
				logger.info("Updating Actuator: loginName="+ iote2eRequest.getLoginName().toString() + 
						", sourceName="+ iote2eRequest.getSourceName().toString() + 
						", actuatorName=" + ruleEvalResult.getSourceSensorActuator().getActuatorName() +
						", old value=" + ruleEvalResult.getSourceSensorActuator().getActuatorValue() + 
						", new value=" + ruleEvalResult.getActuatorTargetValue() );
				// Update the SourceSensorActuator
				ruleEvalResult.getSourceSensorActuator().setActuatorValue(ruleEvalResult.getActuatorTargetValue());
				ruleEvalResult.getSourceSensorActuator().setActuatorValueUpdatedAt(Iote2eUtils.getDateNowUtc8601() );
				String key = iote2eRequest.getLoginName().toString()+"|"+iote2eRequest.getSourceName().toString() +
						"|"+ruleEvalResult.getSensorName()+"|"+ruleEvalResult.getSourceSensorActuator().getActuatorName();
				
				Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
				pairs.put( new Utf8("sensorName"), new Utf8(ruleEvalResult.getSourceSensorActuator().getSensorName() ));
				pairs.put( new Utf8("actuatorName"), new Utf8(ruleEvalResult.getSourceSensorActuator().getActuatorName() ));
				pairs.put( new Utf8("actuatorValue"), new Utf8(ruleEvalResult.getSourceSensorActuator().getActuatorValue() ));
				pairs.put( new Utf8("actuatorValueUpdatedAt"), new Utf8(ruleEvalResult.getSourceSensorActuator().getActuatorValueUpdatedAt() ));
				
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
				
				if( logger.isDebugEnabled() ) logger.debug(ruleEvalResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteSingleton.getCache().put(key, iote2eResultReuseItem.toByteArray(iote2eResult));
						logger.debug("cache.put successful");
						break;
					} catch( CacheException inte ) {
						cntRetry++;
						if( logger.isDebugEnabled() ) logger.debug("cache.put failed with CacheException, will retry, cntRetry=" + cntRetry );
						try { Thread.sleep(1000L); } catch(Exception e ) {}
					} catch( Exception e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
				}				
			}
		}
	}

	@Override
	public void init(RuleConfig ruleConfig) throws Exception {
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
	public void close() throws Exception {
		try {
			// Be careful - ignite is a singleton, only close after last usage
			igniteSingleton.getIgnite().close();
		} catch (Exception e) {
			logger.warn("Ignite close failure", e);
		}
	}
}
