/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.tests.kafka;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.stream.svc.RuleEvalResult;


/**
 * The Class TestKafkaHandlerLed.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKafkaHandlerLed extends TestKafkaHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestKafkaHandlerLed.class);

	/**
	 * Instantiates a new test kafka handler led.
	 *
	 * @throws Exception the exception
	 */
	public TestKafkaHandlerLed() throws Exception {
		super();
	}
	
	/**
	 * Test led led green on.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Test led led green off.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Test led led red on.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Test led led red off.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Test led led yellow on.
	 *
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Test led led yellow off.
	 *
	 * @throws Exception the exception
	 */
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
