package com.pzybrick.test.iote2e.ruleproc.kafka;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

import junit.framework.Assert;

public class TestKafkaHandlerLed extends TestKafkaHandlerBase {
	private static final Log log = LogFactory.getLog(TestKafkaHandlerLed.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so2";
	private static String testSensorType = "switch";
	private static String testSensorNameGreen = "switch0";
	private static String testSensorNameRed = "switch1";
	private static String testSensorNameYellow = "switch2";
	

	public TestKafkaHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() throws Exception {
		log.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "green", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedGreenOff() throws Exception {
		log.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		log.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "red", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	@Test
	public void testLedRedOff() throws Exception {
		log.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		log.info("begins");

		String testValue = "1";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "yellow", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		log.info("begins");

		String testValue = "0";
		commonRun( testLoginUuid, testSourceUuid, testSensorType, testSensorNameYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
}
