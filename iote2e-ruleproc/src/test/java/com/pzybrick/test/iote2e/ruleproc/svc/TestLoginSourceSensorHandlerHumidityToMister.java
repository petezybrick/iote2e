package com.pzybrick.test.iote2e.ruleproc.svc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

import junit.framework.Assert;

public class TestLoginSourceSensorHandlerHumidityToMister extends TestLoginSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestLoginSourceSensorHandlerHumidityToMister.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so1";
	private static String testSensorUuid = "lo1so1se2";
	
	public TestLoginSourceSensorHandlerHumidityToMister() {
		super();
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOff() {
		log.info("begins");

		String testValue = "50";
		commonRun( testLoginUuid, testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults must not be null", ruleEvalResults );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "on", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOn() {
		log.info("begins");
		String testValue = "100";
		commonRun( testLoginUuid, testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults must not be null", ruleEvalResults );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testHumidityToMisterRuleNotFire() {
		log.info("begins");
		String testValue = "87";
		commonRun( testLoginUuid, testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNull("ruleEvalResults must be null", ruleEvalResults );
	}
}
