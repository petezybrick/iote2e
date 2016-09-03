package com.pzybrick.test.iote2e.ruleproc.svc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

import junit.framework.Assert;

public class TestSourceSensorHandlerLed extends TestSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestSourceSensorHandlerLed.class);
	private static String testSourceUuid = "102030a8-9cc9-47e5-8978-898ab010bd94";
	private static String testSensorUuidGreen = "3fc0397a-9044-4c32-aaee-0d40924a0c30";
	private static String testSensorUuidRed = "1767121a-d0f4-46b2-b568-da01e1dc950d";
	private static String testSensorUuidYellow = "82abbfa2-7b08-4167-9d5f-8e99ba810a1a";
	

	public TestSourceSensorHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() {
		log.info("begins");

		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "green" );
	}
	@Test
	public void testLedGreenOff() {
		log.info("begins");

		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidGreen, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "off" );
	}
	
	@Test
	public void testLedRedOn() {
		log.info("begins");

		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "red" );
	}
	@Test
	public void testLedRedOff() {
		log.info("begins");

		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidRed, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "off" );
	}
	
	@Test
	public void testLedYellowOn() {
		log.info("begins");

		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "yellow" );
	}
	
	@Test
	public void testLedYellowOff() {
		log.info("begins");

		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidYellow, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "off" );
	}
	
}
