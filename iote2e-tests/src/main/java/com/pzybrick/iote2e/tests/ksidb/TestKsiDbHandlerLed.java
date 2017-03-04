package com.pzybrick.iote2e.tests.ksidb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKsiDbHandlerLed extends TestKsiDbHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestKsiDbHandlerLed.class);
	private static final String pkActuatorStateGreen = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen;
	private static final String pkActuatorStateYellow = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow;
	private static final String pkActuatorStateRed = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed;

	public TestKsiDbHandlerLed() throws Exception {
		super();
	}
	
	@Test
	public void testLedLedGreenOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameGreen, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledGreen1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "green", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Green LED value=green", "green", ActuatorStateDao.findActuatorValue(pkActuatorStateGreen));
	}
	@Test
	public void testLedLedGreenOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameGreen, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledGreen1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Green LED value=off", "off", ActuatorStateDao.findActuatorValue(pkActuatorStateGreen));
	}
	
	@Test
	public void testLedLedRedOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameRed, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledRed1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "red", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Red LED value=red", "red", ActuatorStateDao.findActuatorValue(pkActuatorStateRed));
	}
	
	@Test
	public void testLedLedRedOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameRed, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledRed1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Red LED value=off", "off", ActuatorStateDao.findActuatorValue(pkActuatorStateRed));
	}
	
	@Test
	public void testLedLedYellowOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameYellow, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledYellow1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "yellow", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Yellow LED value=yellow", "yellow", ActuatorStateDao.findActuatorValue(pkActuatorStateYellow));
	}
	
	@Test
	public void testLedLedYellowOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue);
		//List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		//Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		//Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		//Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameYellow, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledYellow1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		//Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
		//Assert.assertEquals("Cassandra actuator_state Yellow LED value=off", "off", ActuatorStateDao.findActuatorValue(pkActuatorStateYellow));
	}
	
}
