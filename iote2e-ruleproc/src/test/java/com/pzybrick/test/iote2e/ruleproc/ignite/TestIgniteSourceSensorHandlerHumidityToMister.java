package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerHumidityToMister extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerHumidityToMister.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so1";
	private static String testSensorName = "humidity1";
	private String filterKey;


	public TestIgniteSourceSensorHandlerHumidityToMister() {
		super();
		filterKey = testLoginUuid + "|" + testSourceUuid + "|" + testSensorName + "|";
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOff() throws Exception {
		log.info("begins");
		String testValue = "50";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertNotNull("loginActuatorResponses must not be null", loginActuatorResponses );
		Assert.assertEquals("loginActuatorResponses must have size=1", 1, loginActuatorResponses.size() );
		Assert.assertEquals("loginActuatorResponses getActuatorTargetValue", "on", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOn() throws Exception {
		log.info("begins");
		String testValue = "100";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertNotNull("loginActuatorResponses must not be null", loginActuatorResponses );
		Assert.assertEquals("loginActuatorResponses must have size=1", loginActuatorResponses.size(), 1 );
		Assert.assertEquals("loginActuatorResponses getActuatorTargetValue", "off", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleNotFire() throws Exception {
		log.info("begins");
		String testValue = "87";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertEquals("loginActuatorResponses must be empty", 0, loginActuatorResponses.size() );
	}
}
