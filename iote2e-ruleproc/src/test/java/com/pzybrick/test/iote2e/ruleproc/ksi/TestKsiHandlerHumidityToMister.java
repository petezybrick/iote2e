package com.pzybrick.test.iote2e.ruleproc.ksi;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKsiHandlerHumidityToMister extends TestKsiHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestKsiHandlerHumidityToMister.class);
	
	public TestKsiHandlerHumidityToMister() {
		super();
	}
	
	@Test
	public void testHumidityHumidityToMisterRuleFireFanOff() throws Exception {
		logger.info("begins");

		String testHumidityValue = "50";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", iote2eResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testHumidityHumidityToMisterRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testHumidityValue = "100";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", iote2eResults.size(), 1 );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", iote2eResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testHumidityHumidityToMisterRuleNotFire() throws Exception {
		logger.info("begins");
		String testHumidityValue = "87";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testHumidityValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNull("iote2eResults must be null", iote2eResults );
	}
}
