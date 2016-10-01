package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.cache.CacheException;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.avro.schema.ActuatorResponse;
import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.SourceSensorActuator;

public class SourceResponseSvcIgniteImpl implements SourceResponseSvc {
	private static final Log log = LogFactory.getLog(SourceResponseSvcIgniteImpl.class);
	private IgniteSingleton igniteSingleton;
	private Gson gson;
	private DatumWriter<ActuatorResponse> datumWriterActuatorResponse;
	private BinaryEncoder binaryEncoder;

	public SourceResponseSvcIgniteImpl() throws Exception {
		gson = new GsonBuilder().create();
		datumWriterActuatorResponse = new SpecificDatumWriter<ActuatorResponse>(ActuatorResponse.getClassSchema());
		binaryEncoder = null;
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
				ruleEvalResult.getSourceSensorActuator().setActuatorValueUpdatedAt(IotE2eUtils.getDateNowUtc8601() );
				String key = sourceUuid+"|"+sensorUuid+"|"+ruleEvalResult.getSourceSensorActuator().getActuatorUuid();
				byte[] actuatorResponseBytes = createAvroActuatorResponseByteArray(ruleEvalResult.getSourceSensorActuator());
				if( log.isDebugEnabled() ) log.debug(ruleEvalResult.toString());
				// TODO: need circuit breaker here
				// For now, just retry once/second for 15 seconds
				long timeoutAt = System.currentTimeMillis() + (15*1000L);
				int cntRetry = 0;
				while( System.currentTimeMillis() < timeoutAt ) {
					try {
						igniteSingleton.getCache().put(key, actuatorResponseBytes);
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

	private byte[] createAvroActuatorResponseByteArray( SourceSensorActuator sourceSensorActuator ) throws Exception {
		ActuatorResponse actuatorResponse = ActuatorResponse.newBuilder()
				.setSourceUuid(sourceSensorActuator.getSourceUuid())
				.setSensorUuid(sourceSensorActuator.getSensorUuid())
				.setActuatorUuid(sourceSensorActuator.getActuatorUuid())
				.setActuatorValue(sourceSensorActuator.getActuatorValue())
				.setActuatorValueUpdatedAt(sourceSensorActuator.getActuatorValueUpdatedAt())
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
			datumWriterActuatorResponse.write(actuatorResponse, binaryEncoder);
			binaryEncoder.flush();
			bytes = baos.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
		return bytes;
	}

}
