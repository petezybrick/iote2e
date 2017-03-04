package com.pzybrick.iote2e.tests.kafka;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKafkaHandlerLed extends TestKafkaHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestKafkaHandlerLed.class);

	public TestKafkaHandlerLed() throws Exception {
		super();
	}
	
	@Test
	public void testLedLedGreenOn() throws Exception {
		logger.info("begins");

		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "green", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedLedGreenOff() throws Exception {
		logger.info("begins");

		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedLedRedOn() throws Exception {
		logger.info("begins");

		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "red", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedLedRedOff() throws Exception {
		logger.info("begins");

		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedLedYellowOn() throws Exception {
		logger.info("begins");

		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "yellow", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedLedYellowOff() throws Exception {
		logger.info("begins");

		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
}
