package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.List;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;
import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.test.iote2e.ruleproc.common.ThreadIgniteSubscribe;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIgniteHandlerHumidityToMister extends TestIgniteHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerHumidityToMister.class);


	public TestIgniteHandlerHumidityToMister() {
		super();
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOff() throws Exception {
		logger.info("begins");
		String testValue = "50";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", 1, iote2eResults.size() );
		System.out.println(iote2eResults.get(0).getPairs());
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "on", 
				iote2eResults.get(0).getPairs().get( new Utf8("actuatorValue")).toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleFireFanOn() throws Exception {
		logger.info("begins");
		String testValue = "100";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertNotNull("iote2eResults must not be null", iote2eResults );
		Assert.assertEquals("iote2eResults must have size=1", iote2eResults.size(), 1 );
		Assert.assertEquals("iote2eResults getActuatorTargetValue", "off", 
				iote2eResults.get(0).getPairs().get(new Utf8("actuatorValue")).toString() );
	}
	
	@Test
	public void testHumidityToMisterRuleNotFire() throws Exception {
		logger.info("begins");
		String testValue = "87";
		commonRun( testHumidityLoginName, testHumiditySourceName, testHumiditySourceType, testHumiditySensorName, testValue, testHumidityFilterKey);
		List<Iote2eResult> iote2eResults = commonThreadSubscribeGetIote2eResults( 2000, subscribeResults, iote2eResultReuseItem  );
		Assert.assertEquals("iote2eResults must be empty", 0, iote2eResults.size() );
	}
}
