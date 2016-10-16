package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.avro.schema.ActuatorResponse;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerHumidityToMister extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerHumidityToMister.class);
	private static String testSourceUuid = "lo1so1";
	private static String testSensorUuid = "lo1so1se2";
	private String filterKey;


	public TestIgniteSourceSensorHandlerHumidityToMister() {
		super();
		filterKey = testSourceUuid + "|" + testSensorUuid + "|";
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOff() throws Exception {
		log.info("begins");
		String testValue = "50";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertNotNull("actuatorResponses must not be null", actuatorResponses );
		Assert.assertEquals("actuatorResponses must have size=1", 1, actuatorResponses.size() );
		Assert.assertEquals("actuatorResponses getActuatorTargetValue", "on", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOn() throws Exception {
		log.info("begins");
		String testValue = "100";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertNotNull("actuatorResponses must not be null", actuatorResponses );
		Assert.assertEquals("actuatorResponses must have size=1", actuatorResponses.size(), 1 );
		Assert.assertEquals("actuatorResponses getActuatorTargetValue", "off", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleNotFire() throws Exception {
		log.info("begins");
		String testValue = "87";
		commonRun( testSourceUuid, testSensorUuid, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertEquals("actuatorResponses must be empty", 0, actuatorResponses.size() );
	}
}
