package com.pzybrick.iote2e.ruleproc.ignite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.AvroSchemaUtils;
import com.pzybrick.iote2e.schema.util.Iote2eResultToByteArrayReuseItem;

public class Iote2eSvcIgniteImpl implements Iote2eSvc {
	private static final Log log = LogFactory.getLog(Iote2eSvcIgniteImpl.class);
	private IgniteSingleton igniteSingleton;
	private Iote2eResultToByteArrayReuseItem iote2eResultToByteArray;

	public Iote2eSvcIgniteImpl() throws Exception {
		this.iote2eResultToByteArray = new Iote2eResultToByteArrayReuseItem();
	}

	@Override
	public void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			if( ruleEvalResult.isRuleActuatorHit() ) {
				log.info("Updating Actuator: loginName="+ iote2eRequest.getLoginName().toString() + 
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
				pairs.put( "sensorName", ruleEvalResult.getSourceSensorActuator().getSensorName() );
				pairs.put( "actuatorName", ruleEvalResult.getSourceSensorActuator().getActuatorName() );
				pairs.put( "actuatorValue", ruleEvalResult.getSourceSensorActuator().getActuatorValue() );
				pairs.put( "actuatorValueUpdatedAt", ruleEvalResult.getSourceSensorActuator().getActuatorValueUpdatedAt() );
				
				Iote2eResult iote2eResult = Iote2eResult.newBuilder()
					.setPairs(pairs)
					.setLoginName(iote2eRequest.getLoginName().toString())
					.setSourceName(iote2eRequest.getSourceName().toString())
					.setSourceType(iote2eRequest.getSourceType().toString())
					.setRequestUuid(iote2eRequest.getRequestUuid().toString())
					.setRequestTimestamp(iote2eRequest.getRequestTimestamp().toString())
					.setOperation(iote2eRequest.getOperation())
					.setResultCode(8)
					.setResultErrorMessage("testErrorMessage")
					.setResultTimestamp(Iote2eUtils.getDateNowUtc8601())
					.setResultUuid(UUID.randomUUID().toString())
					.build();
				AvroSchemaUtils.iote2eResultValueToByteArray(iote2eResultToByteArray, iote2eResult );
				
				if( log.isDebugEnabled() ) log.debug(ruleEvalResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteSingleton.getCache().put(key, iote2eResultToByteArray.getBytes());
						log.debug("cache.put successful");
						break;
					} catch( CacheException inte ) {
						cntRetry++;
						if( log.isDebugEnabled() ) log.debug("cache.put failed with CacheException, will retry, cntRetry=" + cntRetry );
						try { Thread.sleep(1000L); } catch(Exception e ) {}
					} catch( Exception e ) {
						log.error(e.getMessage(),e);
						throw e;
					}
				}				
			}
		}
	}

	@Override
	public void init(RuleConfig ruleConfig) throws Exception {
		try {
			log.info("Getting IgniteCache for: " + ruleConfig.getSourceResponseIgniteCacheName());
			this.igniteSingleton = IgniteSingleton.getInstance(ruleConfig);
		} catch (Exception e) {
			log.error("Ignite create cache failure", e);
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
			log.warn("Ignite close failure", e);
		}
	}
}
