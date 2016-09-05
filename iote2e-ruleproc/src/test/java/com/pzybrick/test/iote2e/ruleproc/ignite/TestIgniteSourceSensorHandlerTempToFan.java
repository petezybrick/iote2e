package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import junit.framework.Assert;

public class TestIgniteSourceSensorHandlerTempToFan extends TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerTempToFan.class);
	private static String testSourceUuid = "8043c648-a45d-4352-b024-1b4dd72fe9bc";
	private static String testSensorUuid = "3c3122da-6db6-4eb2-bbd3-55456e65d76d";
	
	public TestIgniteSourceSensorHandlerTempToFan() {
		super();
	}
	
	@Test
	public void testTempFanRuleFireFanOff() {
		log.info("begins");
		String testValue = "50";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<String> readCacheResults = commonReadCacheResults( 2000 );
		Assert.assertNotNull("readCacheResults is null", readCacheResults == null );
		Assert.assertEquals("readCacheResults must have size=1", 1, readCacheResults.size());
		//Assert.assertEquals("readCacheResults getActuatorTargetValue", "off", readCacheResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() {
		log.info("begins");
		String testValue = "100";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<String> readCacheResults = commonReadCacheResults( 2000 );
		Assert.assertNotNull("readCacheResults is null", readCacheResults == null );
		Assert.assertEquals("readCacheResults must have size=1", 1, readCacheResults.size() );
		//Assert.assertEquals("readCacheResults getActuatorTargetValue", "on", readCacheResults.get(0).getActuatorTargetValue() );
	}
	
	//@Test
	public void testTempFanRuleNotFire() {
		log.info("begins");
		String testValue = "78";
		commonRun( testSourceUuid, testSensorUuid, testValue);
		List<String> readCacheResults = commonReadCacheResults( 2000 );
		//Assert.assertNull("readCacheResults is not null", readCacheResults );
	}
}
