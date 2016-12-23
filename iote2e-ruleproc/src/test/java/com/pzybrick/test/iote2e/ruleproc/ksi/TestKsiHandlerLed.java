package com.pzybrick.test.iote2e.ruleproc.ksi;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKsiHandlerLed extends TestKsiHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestKsiHandlerLed.class);

	public TestKsiHandlerLed() {
		super();
	}
	
	@Test
	public void testLedLedGreenOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue, testLedGreenFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameGreen, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledGreen1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "green", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());

	}
	@Test
	public void testLedLedGreenOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue, testLedGreenFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameGreen, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledGreen1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
	}
	
	@Test
	public void testLedLedRedOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue, testLedRedFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameRed, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledRed1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "red", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
	}
	
	@Test
	public void testLedLedRedOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue, testLedRedFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameRed, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledRed1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
	}
	
	@Test
	public void testLedLedYellowOn() throws Exception {
		logger.info("begins");
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue, testLedYellowFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameYellow, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledYellow1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "yellow", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
	}
	
	@Test
	public void testLedLedYellowOff() throws Exception {
		logger.info("begins");
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue, testLedYellowFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults PAIRNAME_SENSOR_NAME", testLedSensorNameYellow, iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_NAME", "ledYellow1", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME).toString());
		Assert.assertEquals("iote2eResults PAIRNAME_ACTUATOR_VALUE", "off", iote2eResults.get(0).getPairs().get(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE).toString());
	}
	
}
