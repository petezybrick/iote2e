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
package com.pzybrick.iote2e.tests.local;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.stream.svc.RuleEvalResult;


/**
 * The Class TestLocalHandlerTempToFan.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLocalHandlerTempToFan extends TestLocalHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerTempToFan.class);
	
	/**
	 * Instantiates a new test local handler temp to fan.
	 *
	 * @throws Exception the exception
	 */
	public TestLocalHandlerTempToFan() throws Exception {
		super();
	}
	
	/**
	 * Test temp to fan temp fan rule fire fan off.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testTempToFanTempFanRuleFireFanOff() throws Exception {
		logger.info("begins");

		String testTempToFanValue = "50";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "off", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	/**
	 * Test temp to fan temp fan rule fire fan on.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testTempToFanTempFanRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testTempToFanValue = "100";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", ruleEvalResults.size(), 1 );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "on", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	/**
	 * Test temp to fan temp fan rule not fire.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testTempToFanTempFanRuleNotFire() throws Exception {
		logger.info("begins");
		String testTempToFanValue = "78";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue);
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNull("ruleEvalResults is not null", ruleEvalResults );
	}
}
