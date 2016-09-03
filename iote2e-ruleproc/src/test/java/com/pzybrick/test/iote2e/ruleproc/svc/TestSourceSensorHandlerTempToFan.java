package com.pzybrick.test.iote2e.ruleproc.svc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

import junit.framework.Assert;

public class TestSourceSensorHandlerTempToFan extends TestSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestSourceSensorHandlerTempToFan.class);
	private static String testSourceUuid = "8043c648-a45d-4352-b024-1b4dd72fe9bc";
	private static String testSensorUuid = "3c3122da-6db6-4eb2-bbd3-55456e65d76d";
	
	public TestSourceSensorHandlerTempToFan() {
		super();
	}
	
	@Test
	public void testTempFanRuleFireFanOff() {
		log.info("begins");

		String testValue = "50";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "off" );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() {
		log.info("begins");
		String testValue = "100";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", ruleEvalResults.get(0).getActuatorTargetValue(), "on" );
	}
	
	@Test
	public void testTempFanRuleNotFire() {
		log.info("begins");
		String testValue = "78";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNull("ruleEvalResults is not null", ruleEvalResults );
	}
}
