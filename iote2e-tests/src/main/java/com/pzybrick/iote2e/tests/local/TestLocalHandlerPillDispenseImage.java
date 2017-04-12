package com.pzybrick.iote2e.tests.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.stream.svc.RuleEvalResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLocalHandlerPillDispenseImage extends TestLocalHandlerBase {
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerPillDispenseImage.class);	

	public TestLocalHandlerPillDispenseImage() throws Exception {
		super();
	}
	
	@Test
	public void testImageActualEqualToDispense() throws Exception {
		logger.info("begins");

		Map<CharSequence, CharSequence> metadata = new HashMap<CharSequence, CharSequence>();
		metadata.put( new Utf8("num_pills_to_dispense"), new Utf8("3"));
		String testByte64Image = "test-image";
		commonRun( testPillDispenseImageLoginName, testPillDispenseImageSourceName, testPillDispenseImageSourceType, testPillDispenseImageSensorName, testByte64Image, metadata );
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "green", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	@Test
	public void testImageActualNotEqualToDispense() throws Exception {
		logger.info("begins");

		Map<CharSequence, CharSequence> metadata = new HashMap<CharSequence, CharSequence>();
		metadata.put( new Utf8("num_pills_to_dispense"), new Utf8("2"));
		String testByte64Image = "test-image";
		commonRun( testPillDispenseImageLoginName, testPillDispenseImageSourceName, testPillDispenseImageSourceType, testPillDispenseImageSensorName, testByte64Image, metadata );
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "red", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
}
