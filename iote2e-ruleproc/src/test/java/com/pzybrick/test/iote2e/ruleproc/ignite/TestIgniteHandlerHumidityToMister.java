package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.avro.util.Utf8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

import junit.framework.Assert;

public class TestIgniteHandlerHumidityToMister extends TestIgniteHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteHandlerHumidityToMister.class);
	private static String testLoginUuid = "lo1";
	private static String testSourceUuid = "lo1so1";
	private static String testSourceType = "humidity";
	private static String testSensorName = "humidity1";
	private String filterKey;


	public TestIgniteHandlerHumidityToMister() {
		super();
		filterKey = testLoginUuid + "|" + testSourceUuid + "|" + testSensorName + "|";
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOff() throws Exception {
		log.info("begins");
		String testValue = "50";
		commonRun( testLoginUuid, testSourceUuid, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		System.out.println(iote2eResults.get(0).getPairs());
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", 
				iote2eResults.get(0).getPairs().get( new Utf8("actuatorValue")).toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOn() throws Exception {
		log.info("begins");
		String testValue = "100";
		commonRun( testLoginUuid, testSourceUuid, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", iote2eResults.size(), 1 );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleNotFire() throws Exception {
		log.info("begins");
		String testValue = "87";
		commonRun( testLoginUuid, testSourceUuid, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertEquals("iote2eResults must be empty", 0, iote2eResults.size() );
	}
}
