package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;

import junit.framework.Assert;

public class TestIgniteHandlerLed extends TestIgniteHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteHandlerLed.class);
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
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameGreen + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameGreen, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );
		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "green", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedGreenOff() throws Exception {
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameGreen + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameGreen, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameRed + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameRed, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "red", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedRedOff() throws Exception {
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameRed + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameRed, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameYellow + "|";
		String testValue = "1";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameYellow, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "yellow", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		log.info("begins");
		String filterKey = testLoginName + "|" + testSourceName + "|" + testSensorNameYellow + "|";
		String testValue = "0";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorNameYellow, testValue, filterKey);
		List<LoginActuatorResponse> loginActuatorResponses = commonThreadSubscribeGetLoginActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", loginActuatorResponses.get(0).getActuatorValue().toString() );
	}
	
}
