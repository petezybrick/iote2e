package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.avro.schema.ActuatorResponse;
import com.pzybrick.iote2e.ruleproc.svc.SourceSensorActuator;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerTempToFan extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerTempToFan.class);
	private static String testSourceUuid = "lo1so1";
	private static String testSensorUuid = "lo1so1se1";
	private String filterKey;
	
	public TestIgniteSourceSensorHandlerTempToFan() {
		super();
		filterKey = testSourceUuid + "|" + testSensorUuid + "|";
	}
	
	@Test
	public void testTempFanRuleFireFanOff() throws Exception {
		log.info("begins");
		String testValue = "50";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertNotNull("actuatorResponses is null", actuatorResponses == null );
		Assert.assertEquals("actuatorResponses must have size=1", 1, actuatorResponses.size());
		Assert.assertEquals("actuatorResponses getActuatorValue", "off", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() throws Exception {
		log.info("begins");
		String testValue = "100";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertNotNull("actuatorResponses is null", actuatorResponses == null );
		Assert.assertEquals("actuatorResponses must have size=1", 1, actuatorResponses.size() );
		Assert.assertEquals("actuatorResponses getActuatorTargetValue", "on", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testTempFanRuleNotFire() throws Exception {
		log.info("begins");
		String testValue = "78";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertEquals("actuatorResponses must empty", 0, actuatorResponses.size() );
	}
}
