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
 * The Class TestKafkaHandlerHumidityToMister.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKafkaHandlerHumidityToMister extends TestKafkaHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestKafkaHandlerHumidityToMister.class);
	
	/**
	 * Instantiates a new test kafka handler humidity to mister.
	 *
	 * @throws Exception the exception
	 */
	public TestKafkaHandlerHumidityToMister() throws Exception {
		super();
	}
	
	/**
	 * Test humidity humidity to mister rule fire fan off.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHumidityHumidityToMisterRuleFireFanOff() throws Exception {
		logger.info("begins");

		String testHumidityValue = "50";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults must not be null", ruleEvalResults );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "on", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	/**
	 * Test humidity humidity to mister rule fire fan on.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHumidityHumidityToMisterRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testHumidityValue = "100";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults must not be null", ruleEvalResults );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	/**
	 * Test humidity humidity to mister rule not fire.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHumidityHumidityToMisterRuleNotFire() throws Exception {
		logger.info("begins");
		String testHumidityValue = "87";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNull("ruleEvalResults must be null", ruleEvalResults );
	}
}
