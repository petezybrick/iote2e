package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIgniteHandlerLed extends TestIgniteHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerLed.class);
	private static String testLoginName = "lo1";
	private static String testSourceName = "lo1so2";
	private static String testSourceType = "switch";
	private static String testSensorNameGreen = "switch0";
	private static String testSensorNameRed = "switch1";
	private static String testSensorNameYellow = "switch2";
	

	public TestIgniteHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameGreen + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameGreen, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "green", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedGreenOff() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameGreen + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameGreen, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameRed + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameRed, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "red", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedRedOff() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameRed + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameRed, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameYellow + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameYellow, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "yellow", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		logger.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameYellow + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameYellow, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
}
