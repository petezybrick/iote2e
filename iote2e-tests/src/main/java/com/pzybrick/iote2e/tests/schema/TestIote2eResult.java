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
package com.pzybrick.iote2e.tests.schema;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.util.Utf8;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.common.utils.Iote2eConstants;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;


/**
 * The Class TestIote2eResult.
 */
public class TestIote2eResult {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test create.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreate() throws Exception {
		Integer resultCode = 8;
		String requestTimestamp = Iote2eUtils.getDateNowUtc8601();
		String resultTimestamp = Iote2eUtils.getDateNowUtc8601();
		Iote2eResult iote2eResult = createTestIote2eResult(resultCode, requestTimestamp, resultTimestamp); 
		verifyIote2eResult(iote2eResult, resultCode, requestTimestamp, resultTimestamp); 
	}

	/**
	 * Test encode decode.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEncodeDecode() throws Exception {
		Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();
		Integer resultCode = 8;
		String requestTimestamp = Iote2eUtils.getDateNowUtc8601();
		String resultTimestamp = Iote2eUtils.getDateNowUtc8601();
		Iote2eResult iote2eResultBefore = createTestIote2eResult(resultCode, requestTimestamp, resultTimestamp);
		byte[] bytes = iote2eResultReuseItem.toByteArray(iote2eResultBefore);
		Iote2eResult iote2eResultAfter = iote2eResultReuseItem.fromByteArray(bytes);
		verifyIote2eResult(iote2eResultAfter, resultCode, requestTimestamp, resultTimestamp); 
	}
	
	/**
	 * Creates the test iote 2 e result.
	 *
	 * @param resultCode the result code
	 * @param requestTimestamp the request timestamp
	 * @param resultTimestamp the result timestamp
	 * @return the iote 2 e result
	 */
	private Iote2eResult createTestIote2eResult(Integer resultCode, String requestTimestamp, String resultTimestamp) {
		Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
		metadata.put( new Utf8("testMetadataNamea"), new Utf8("testMetadataValuea"));
		metadata.put( new Utf8("testMetadataNameb"), new Utf8("testMetadataValueb"));
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( Iote2eSchemaConstants.PAIRNAME_SENSOR_NAME, new Utf8("testSensorNamea"));
		pairs.put( Iote2eSchemaConstants.PAIRNAME_ACTUATOR_NAME, new Utf8("testActuatorNamea"));
		pairs.put( Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE, new Utf8("testActuatorValuea"));
		pairs.put( Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE_UPDATED_AT, new Utf8("2017-01-02T03:04:05.678" ));
		
		Iote2eResult iote2eResult = Iote2eResult.newBuilder()
				.setLoginName("testLoginName")
				.setSourceName("testSourceName")
				.setSourceType("testSourceType")
				.setMetadata(metadata)
				.setRequestUuid("testRequestUuid")
				.setRequestTimestamp(requestTimestamp)
				.setOperation(OPERATION.ACTUATOR_VALUES)
				.setResultCode(resultCode)
				.setResultErrorMessage("testErrorMessage")
				.setResultUuid("testResultUuid")
				.setResultTimestamp(resultTimestamp)
				.setPairs(pairs)
				.build();
		return iote2eResult;
	}
	
	/**
	 * Verify iote 2 e result.
	 *
	 * @param iote2eResult the iote 2 e result
	 * @param resultCode the result code
	 * @param requestTimestamp the request timestamp
	 * @param resultTimestamp the result timestamp
	 */
	private void verifyIote2eResult( Iote2eResult iote2eResult, Integer resultCode, String requestTimestamp, String resultTimestamp ) {
		Assert.assertEquals("login name", "testLoginName", iote2eResult.getLoginName().toString());
		Assert.assertEquals("source name", "testSourceName", iote2eResult.getSourceName().toString());
		Assert.assertEquals("source type", "testSourceType", iote2eResult.getSourceType().toString());
		Assert.assertEquals("request uuid", "testRequestUuid", iote2eResult.getRequestUuid().toString());
		Assert.assertEquals("request timestamp", requestTimestamp, iote2eResult.getRequestTimestamp().toString());
		
		Assert.assertEquals("result code", resultCode, iote2eResult.getResultCode());
		Assert.assertEquals("result error message", "testErrorMessage", iote2eResult.getResultErrorMessage().toString());
		Assert.assertEquals("result uuid", "testResultUuid", iote2eResult.getResultUuid().toString());
		Assert.assertEquals("result timestamp", resultTimestamp, iote2eResult.getResultTimestamp().toString());

		Assert.assertEquals("actuator name a", "testActuatorValuea",
				iote2eResult.getPairs().get(new Utf8("testActuatorNamea")).toString());
		Assert.assertEquals("actuator name b", "testActuatorValueb",
				iote2eResult.getPairs().get(new Utf8("testActuatorNameb")).toString());
		Assert.assertNull("actuator name c", iote2eResult.getPairs().get(new Utf8("testActuatorNamec")));
		
		Assert.assertEquals("metadata name a", "testMetadataValuea",
				iote2eResult.getMetadata().get(new Utf8("testMetadataNamea")).toString());
		Assert.assertEquals("metadata name b", "testMetadataValueb",
				iote2eResult.getMetadata().get(new Utf8("testMetadataNameb")).toString());
		Assert.assertNull("metadata name c", iote2eResult.getMetadata().get(new Utf8("testMetadataNamec")));
	}

}