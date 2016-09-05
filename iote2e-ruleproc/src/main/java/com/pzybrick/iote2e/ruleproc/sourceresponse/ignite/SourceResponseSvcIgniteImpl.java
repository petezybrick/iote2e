package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import java.util.List;

import javax.cache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

public class SourceResponseSvcIgniteImpl implements SourceResponseSvc {
	private static final Log log = LogFactory.getLog(SourceResponseSvcIgniteImpl.class);
	private static Ignite ignite;
	private static IgniteCache<String, String> cache;

	public SourceResponseSvcIgniteImpl() throws Exception {
	}

	@Override
	public void processRuleEvalResults(String sourceUuid, String sensorUuid, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		for (RuleEvalResult ruleEvalResult : ruleEvalResults) {
			if( ruleEvalResult.isRuleActuatorHit() ) {
				log.info("Updating Actuator: sourceUuid="+sourceUuid + ", actuatorUuid=" + ruleEvalResult.getSourceSensorActuator().getActuatorUuid() +
						", old value=" + ruleEvalResult.getSourceSensorActuator().getActuatorValue() + 
						", new value=" + ruleEvalResult.getActuatorTargetValue() );
				// Update the SourceSensorActuator
				ruleEvalResult.getSourceSensorActuator().setActuatorValue(ruleEvalResult.getActuatorTargetValue());
				String now8601 = ISODateTimeFormat.dateTime().print(new DateTime().toDateTime(DateTimeZone.UTC));
				ruleEvalResult.getSourceSensorActuator().setActuatorValueUpdatedAt(now8601);
				String key = sourceUuid+"|"+sensorUuid+"|"+ruleEvalResult.getSourceSensorActuator().getActuatorUuid();
				if( log.isDebugEnabled() ) log.debug(ruleEvalResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						cache.put(key, ruleEvalResult.getSourceSensorActuator().toString());
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
	
	private static synchronized void createIgniteInstance( RuleConfig ruleConfig ) throws Exception {
		if( ignite == null ) {
			try {
				log.info("Initializing Ignite, config file=" + ruleConfig.getSourceResponseIgniteConfigFile() + ", config name=" +  ruleConfig.getSourceResponseIgniteConfigName());
				IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
						ruleConfig.getSourceResponseIgniteConfigFile(), ruleConfig.getSourceResponseIgniteConfigName());
				Ignition.setClientMode(true);
				ignite = Ignition.start(igniteConfiguration);
				if (log.isDebugEnabled()) log.debug(ignite.toString());
				cache = ignite.getOrCreateCache(ruleConfig.getSourceResponseIgniteCacheName());
			} catch (Exception e) {
				log.error("Ignite initialization failure", e);
				throw e;
			}
		}
	}

	@Override
	public void init(RuleConfig ruleConfig) throws Exception {
		try {
			log.info("Starting IgniteCache for: " + ruleConfig.getSourceResponseIgniteCacheName());
			createIgniteInstance( ruleConfig );
			//cache = ignite.getOrCreateCache(ruleConfig.getSourceResponseIgniteCacheName());
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
			ignite.close();
		} catch (Exception e) {
			log.warn("Ignite close failure", e);
		}
	}

}
