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
public class TestIgniteHandlerTempToFan extends TestIgniteHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerTempToFan.class);
	private static String testLoginName = "lo1";
	private static String testSourceName = "lo1so1";
	private static String testSourceType = "temp";
	private static String testSensorName = "temp1";
	private String filterKey;
	
	public TestIgniteHandlerTempToFan() {
		super();
		filterKey = testLoginName + "|" + testSourceName + "|" + testSensorName + "|";
	}
	
	@Test
	public void testTempFanRuleFireFanOff() throws Exception {
		logger.info("begins");
		String testValue = "50";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size());
		Assert.assertEquals("iote2eResults getActuatorValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testTempFanRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testValue = "100";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testTempFanRuleNotFire() throws Exception {
		logger.info("begins");
		String testValue = "78";
		commonRun( testLoginName, testSourceName, testSourceType, testSensorName, testValue, filterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000 );
		Assert.assertEquals("iote2eResults must empty", 0, iote2eResults.size() );
	}
}
