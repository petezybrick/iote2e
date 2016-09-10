package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.avro.schema.ActuatorResponse;
import com.pzybrick.iote2e.ruleproc.svc.SourceSensorActuator;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerLed extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerLed.class);
	private static String testSourceUuid = "102030a8-9cc9-47e5-8978-898ab010bd94";
	private static String testSensorUuidGreen = "3fc0397a-9044-4c32-aaee-0d40924a0c30";
	private static String testSensorUuidRed = "1767121a-d0f4-46b2-b568-da01e1dc950d";
	private static String testSensorUuidYellow = "82abbfa2-7b08-4167-9d5f-8e99ba810a1a";
	

	public TestIgniteSourceSensorHandlerLed() {
		super();
	}
	
	@Test
	public void testLedGreenOn() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidGreen + "|";
		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidGreen, testValue, filterKey);
		List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );
		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "green", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	@Test
	public void testLedGreenOff() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidGreen + "|";
		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidGreen, testValue, filterKey);
				List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedRedOn() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidRed + "|";
		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidRed, testValue, filterKey);
				List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "red", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedRedOff() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidRed + "|";
		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidRed, testValue, filterKey);
				List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedYellowOn() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidYellow + "|";
		String testValue = "1";
		commonRun( testSourceUuid, testSensorUuidYellow, testValue, filterKey);
				List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "yellow", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
	@Test
	public void testLedYellowOff() throws Exception {
		log.info("begins");
		String filterKey = testSourceUuid + "|" + testSensorUuidYellow + "|";
		String testValue = "0";
		commonRun( testSourceUuid, testSensorUuidYellow, testValue, filterKey);
				List<ActuatorResponse> actuatorResponses = commonThreadSubscribeGetActuatorResponses( 2000 );

		Assert.assertNotNull("subscribeResults is null", subscribeResults == null );
		Assert.assertEquals("subscribeResults must have size=1", 1, subscribeResults.size() );
		Assert.assertEquals("subscribeResults getActuatorTargetValue", "off", actuatorResponses.get(0).getActuatorValue().toString() );
	}
	
}
