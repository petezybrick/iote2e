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
package com.pzybrick.iote2e.tests.ksidb;

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
 * The Class TestKsiDbHandlerHumidityToMister.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKsiDbHandlerHumidityToMister extends TestKsiDbHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestKsiDbHandlerHumidityToMister.class);
	
	/** The Constant pkActuatorState. */
	private static final String pkActuatorState = testHumidityLoginName + "|" + testHumiditySourceName + "|" + testHumiditySensorName;

	/**
	 * Instantiates a new test ksi db handler humidity to mister.
	 *
	 * @throws Exception the exception
	 */
	public TestKsiDbHandlerHumidityToMister() throws Exception {
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
//		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
//		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
//		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
//		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testHumiditySensorName, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
//		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "mister1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
//		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "on", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
//		Assert.assertEquals("Cassandra actuator_state Humidity value=on", "on", ActuatorStateDao.findActuatorValue(pkActuatorState));
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
//		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
//		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
//		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
//		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testHumiditySensorName, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
//		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "mister1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
//		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
//		Assert.assertEquals("Cassandra actuator_state Humidity value=off", "off", ActuatorStateDao.findActuatorValue(pkActuatorState));
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
//		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
//		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
//		Assert.assertEquals("iote2eResults must have size=0", 0, iote2eResults.size() );
//		Assert.assertEquals("Cassandra actuator_state Humidity value=null", null, ActuatorStateDao.findActuatorValue(pkActuatorState));
	}
}
