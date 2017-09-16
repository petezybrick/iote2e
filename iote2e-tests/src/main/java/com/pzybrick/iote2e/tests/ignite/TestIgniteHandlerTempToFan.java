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
package com.pzybrick.iote2e.tests.ignite;

import java.util.List;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;


/**
 * The Class TestIgniteHandlerTempToFan.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIgniteHandlerTempToFan extends TestIgniteHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerTempToFan.class);
	
	/**
	 * Instantiates a new test ignite handler temp to fan.
	 *
	 * @throws Exception the exception
	 */
	public TestIgniteHandlerTempToFan() throws Exception {
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
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size());
		Assert.assertEquals("iote2eResults getActuatorValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
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
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
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
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertEquals("iote2eResults must empty", 0, iote2eResults.size() );
	}
}
