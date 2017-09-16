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
package com.pzybrick.iote2e.tests.spark;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;


/**
 * The Class TestSparkHandlerTempToFan.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSparkHandlerTempToFan extends TestSparkHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestSparkHandlerTempToFan.class);
	
	/** The Constant pkActuatorState. */
	private static final String pkActuatorState = testTempToFanLoginName + "|" + testTempToFanSourceName + "|" + testTempToFanSensorName;
	
	/**
	 * Instantiates a new test spark handler temp to fan.
	 *
	 * @throws Exception the exception
	 */
	public TestSparkHandlerTempToFan() throws Exception {
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
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 10000, queueIote2eResults); 
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		for( Iote2eResult iote2eResult : iote2eResults ) {
			logger.info("++++++++++++++++++++++++++++++ iote2eResult {}", iote2eResult);
		}
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testTempToFanSensorName, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "fan1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		Assert.assertEquals("Cassandra actuator_state value=off", "off", ActuatorStateDao.findActuatorValue(pkActuatorState));
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
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 10000, queueIote2eResults); 
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testTempToFanSensorName, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "fan1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "on", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		Assert.assertEquals("Cassandra actuator_state value=on", "on", ActuatorStateDao.findActuatorValue(pkActuatorState));
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
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 10000, queueIote2eResults); 
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=0", 0, iote2eResults.size() );
		Assert.assertEquals("Cassandra actuator_state value=null", null, ActuatorStateDao.findActuatorValue(pkActuatorState));
	}
}
