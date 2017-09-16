/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.tests.local;

import java.util.Base64;
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
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class TestLocalHandlerPillDispenseImage.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLocalHandlerPillDispenseImage extends TestLocalHandlerBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerPillDispenseImage.class);	
	
	/** The path test png. */
	public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/images/iote2e-test.png";

	/**
	 * Instantiates a new test local handler pill dispense image.
	 *
	 * @throws Exception the exception
	 */
	public TestLocalHandlerPillDispenseImage() throws Exception {
		super();
	}
	
	/**
	 * Test image actual equal to dispense.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testImageActualEqualToDispense() throws Exception {
		logger.info("begins");

		Map<CharSequence, CharSequence> metadata = new HashMap<CharSequence, CharSequence>();
		metadata.put( new Utf8("num_pills_to_dispense"), new Utf8("3"));
		byte[] imageBytes = TestCommonHandler.fileToByteArray(PATH_TEST_PNG);
		String testByte64Image = Base64.getEncoder().encodeToString(imageBytes);
		commonRun( testPillDispenseImageLoginName, testPillDispenseImageSourceName, testPillDispenseImageSourceType, testPillDispenseImageSensorName, testByte64Image, metadata );
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "green", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
	/**
	 * Test image actual not equal to dispense.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testImageActualNotEqualToDispense() throws Exception {
		logger.info("begins");

		Map<CharSequence, CharSequence> metadata = new HashMap<CharSequence, CharSequence>();
		metadata.put( new Utf8("num_pills_to_dispense"), new Utf8("2"));
		byte[] imageBytes = TestCommonHandler.fileToByteArray(PATH_TEST_PNG);
		String testByte64Image = Base64.getEncoder().encodeToString(imageBytes);
		commonRun( testPillDispenseImageLoginName, testPillDispenseImageSourceName, testPillDispenseImageSourceType, testPillDispenseImageSensorName, testByte64Image, metadata );
		List<RuleEvalResult> ruleEvalResults = commonGetRuleEvalResults( 2000 );
		Assert.assertNotNull("ruleEvalResults is null", ruleEvalResults == null );
		Assert.assertEquals("ruleEvalResults must have size=1", 1, ruleEvalResults.size() );
		Assert.assertEquals("ruleEvalResults getActuatorTargetValue", "red", ruleEvalResults.get(0).getActuatorTargetValue() );
	}
	
}
