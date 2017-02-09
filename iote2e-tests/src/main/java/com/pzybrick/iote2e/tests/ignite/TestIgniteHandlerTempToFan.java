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
public class TestIgniteHandlerTempToFan extends TestIgniteHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerTempToFan.class);
	
	public TestIgniteHandlerTempToFan() {
		super();
	}
	
	@Test
	public void testTempToFanTempFanRuleFireFanOff() throws Exception {
		logger.info("begins");
		String testTempToFanValue = "50";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size());
		Assert.assertEquals("iote2eResults getActuatorValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testTempToFanTempFanRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testTempToFanValue = "100";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertNotNull("iote2eResults is null", iote2eResults == null );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString()  );
	}
	
	@Test
	public void testTempToFanTempFanRuleNotFire() throws Exception {
		logger.info("begins");
		String testTempToFanValue = "78";
		commonRun( testTempToFanLoginName, testTempToFanSourceName, testTempToFanSourceType, testTempToFanSensorName, testTempToFanValue, testTempToFanFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, queueIote2eResults );
		Assert.assertEquals("iote2eResults must empty", 0, iote2eResults.size() );
	}
}
