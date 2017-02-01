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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIgniteHandlerLed extends TestIgniteHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerLed.class);

	public TestIgniteHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen + "|";
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "green", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedGreenOff() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen + "|";
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameGreen, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );

		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed + "|";
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );

		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "red", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedRedOff() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed + "|";
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameRed, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow + "|";
		String testLedValue = "1";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );

		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "yellow", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		logger.info("begins");
		String filterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow + "|";
		String testLedValue = "0";
		commonRun( testLedLoginName, testLedSourceName, testLedSourceType, testLedSensorNameYellow, testLedValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );

		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
}
