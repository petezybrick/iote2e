package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerTempToFan extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerTempToFan.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so1";
	private static String testSensorName = "temp1";
	private String filterKey;
	
	public TestIgniteSourceSensorHandlerTempToFan() {
		super();
		filterKey = testLoginUuid + "|" + testSourceUuid + "|" + testSensorName + "|";
	}
	
	@Test
	public void testTempFanRuleFireFanOff() throws Exception {
		log.info("begins");
		String testValue = "50";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertNotNull("loginActuatorResponses is null", loginActuatorResponses == null );
		Assert.assertEquals("loginActuatorResponses must have size=1", 1, loginActuatorResponses.size());
		Assert.assertEquals("loginActuatorResponses getActuatorValue", "off", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() throws Exception {
		log.info("begins");
		String testValue = "100";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertNotNull("loginActuatorResponses is null", loginActuatorResponses == null );
		Assert.assertEquals("loginActuatorResponses must have size=1", 1, loginActuatorResponses.size() );
		Assert.assertEquals("loginActuatorResponses getActuatorTargetValue", "on", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testTempFanRuleNotFire() throws Exception {
		log.info("begins");
		String testValue = "78";
		commonRun( testLoginUuid, testSourceUuid, testSensorName, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertEquals("loginActuatorResponses must empty", 0, loginActuatorResponses.size() );
	}
}
