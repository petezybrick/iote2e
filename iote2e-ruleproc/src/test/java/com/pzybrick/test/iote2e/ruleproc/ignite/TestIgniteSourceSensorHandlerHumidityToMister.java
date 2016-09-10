package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.avro.schema.ActuatorResponse;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerHumidityToMister extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerHumidityToMister.class);
	private static String testSourceUuid = "8043c648-a45d-4352-b024-1b4dd72fe9bc";
	private static String testSensorUuid = "fb0440cd-5933-47c2-b7f2-a60b99fa0ba8";
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
