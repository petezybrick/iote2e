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
public class TestLocalHandlerLed extends TestLocalHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerLed.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so2";
	private static String testSensorType = "switch";
	private static String testSensorNameGreen = "switch0";
	private static String testSensorNameRed = "switch1";
	private static String testSensorNameYellow = "switch2";
	

	public TestLocalHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() throws Exception {
		logger.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "green", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedGreenOff() throws Exception {
		logger.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		logger.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "red", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedRedOff() throws Exception {
		logger.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		logger.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "yellow", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		logger.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
}
