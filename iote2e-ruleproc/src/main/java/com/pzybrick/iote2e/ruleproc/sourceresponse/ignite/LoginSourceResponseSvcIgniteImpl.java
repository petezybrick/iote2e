package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import java.util.List;

import javax.cache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.ruleproc.sourceresponse.LoginSourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.util.AvroSchemaUtils;
import com.pzybrick.iote2e.schema.util.LoginActuatorResponseToByteArrayReuseItem;

public class LoginSourceResponseSvcIgniteImpl implements LoginSourceResponseSvc {
	private static final Log log = LogFactory.getLog(LoginSourceResponseSvcIgniteImpl.class);
	private IgniteSingleton igniteSingleton;
	private Gson gson;
	private LoginActuatorResponseToByteArrayReuseItem loginActuatorResponseToByteArray;


	public LoginSourceResponseSvcIgniteImpl() throws Exception {
		this.gson = new GsonBuilder().create();
		this.loginActuatorResponseToByteArray = new LoginActuatorResponseToByteArrayReuseItem();
	}

	@Override
	public void processRuleEvalResults(String loginUuid, String sourceUuid, String sensorName, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			if( ruleEvalResult.isRuleActuatorHit() ) {
				log.info("Updating Actuator: loginUuid="+ loginUuid + ", sourceUuid="+sourceUuid + 
						", actuatorName=" + ruleEvalResult.getSourceSensorActuator().getActuatorName() +
						", old value=" + ruleEvalResult.getSourceSensorActuator().getActuatorValue() + 
						", new value=" + ruleEvalResult.getActuatorTargetValue() );
				// Update the SourceSensorActuator
				ruleEvalResult.getSourceSensorActuator().setActuatorValue(ruleEvalResult.getActuatorTargetValue());
				ruleEvalResult.getSourceSensorActuator().setActuatorValueUpdatedAt(IotE2eUtils.getDateNowUtc8601() );
				String key = loginUuid+"|"+sourceUuid+"|"+sensorName+"|"+ruleEvalResult.getSourceSensorActuator().getActuatorName();
				
				AvroSchemaUtils.loginActuatorResponseValueToByteArray(loginActuatorResponseToByteArray, loginUuid, sourceUuid, sensorName, 
						ruleEvalResult.getSourceSensorActuator().getActuatorName(), 
						ruleEvalResult.getSourceSensorActuator().getActuatorValue(), 
						ruleEvalResult.getSourceSensorActuator().getActuatorValueUpdatedAt());
				
				if( log.isDebugEnabled() ) log.debug(ruleEvalResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteSingleton.getCache().put(key, loginActuatorResponseToByteArray.getBytes());
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
			this.igniteSingleton = IgniteSingleton.getInstance( ruleConfig );
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
