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
	private static String testSourceUuid = "lo1so2";
	private static String testSensorUuidGreen = "lo1so2se1";
	private static String testSensorUuidRed = "lo1so2se2";
	private static String testSensorUuidYellow = "lo1so2se3";
	

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
