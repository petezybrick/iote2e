package com.pzybrick.test.iote2e.ruleproc.local;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLocalHandlerTempToFan extends TestLocalHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerTempToFan.class);
	private static String testLoginName = "lo1";
	private static String testSourceName = "lo1so1";
	private static String testSourceType = "temp";
	private static String testSensorName = "temp1";
	
	public TestLocalHandlerTempToFan() {
		super();
	}
	
	@Test
	public void testTempFanRuleFireFanOff() throws Exception {
		logger.info("begins");

		String testValue = "50";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testValue = "100";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "on", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testTempFanRuleNotFire() throws Exception {
		logger.info("begins");
		String testValue = "78";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNull("ruleEvalResults is not null", ruleEvalResults );
	}
}
