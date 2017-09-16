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

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;


/**
 * The Class TestIote2eRequest.
 */
public class TestIote2eRequest {

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
		Iote2eRequest iote2eRequest = createTestIote2eRequest();
		verifyIote2eRequest( iote2eRequest );
	}
	
	/**
	 * Test encode decode.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEncodeDecode() throws Exception {
		Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		Iote2eRequest iote2eRequestBefore = createTestIote2eRequest();
		byte[] bytes = iote2eRequestReuseItem.toByteArray(iote2eRequestBefore);
		Iote2eRequest iote2eRequestAfter = iote2eRequestReuseItem.fromByteArray(bytes);
		verifyIote2eRequest( iote2eRequestAfter );
	}
	
	/**
	 * Creates the test iote 2 e request.
	 *
	 * @return the iote 2 e request
	 */
	private Iote2eRequest createTestIote2eRequest() {
		Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
		metadata.put( new Utf8("testMetadataNamea"), new Utf8("testMetadataValuea"));
		metadata.put( new Utf8("testMetadataNameb"), new Utf8("testMetadataValueb"));
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put( new Utf8("testSensorNamea"),  new Utf8("testSensorValuea"));
		pairs.put( new Utf8("testSensorNameb"),  new Utf8("testSensorValueb"));
		Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
				.setLoginName("testLoginName")
				.setSourceName("testSourceName")
				.setSourceType("testSourceType")
				.setMetadata(metadata)
				.setRequestUuid("testRequestUuid")
				.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
				.setOperation(OPERATION.SENSORS_VALUES)
				.setPairs(pairs)
				.build();
		return iote2eRequest;
	}
	
	/**
	 * Verify iote 2 e request.
	 *
	 * @param iote2eRequest the iote 2 e request
	 */
	private void verifyIote2eRequest( Iote2eRequest iote2eRequest ) {
		Assert.assertEquals("login name", "testLoginName", iote2eRequest.getLoginName().toString());
		Assert.assertEquals("source name", "testSourceName", iote2eRequest.getSourceName().toString());
		Assert.assertEquals("source type", "testSourceType", iote2eRequest.getSourceType().toString());
		Assert.assertEquals("request uuid", "testRequestUuid", iote2eRequest.getRequestUuid().toString());
		Assert.assertNotNull("timestamp", iote2eRequest.getRequestTimestamp().toString());
		Assert.assertEquals("sensor name a", "testSensorValuea",
				iote2eRequest.getPairs().get( new Utf8("testSensorNamea")).toString());
		Assert.assertEquals("sensor name b", "testSensorValueb",
				iote2eRequest.getPairs().get(new Utf8("testSensorNameb")).toString());
		Assert.assertNull("sensor name c", iote2eRequest.getPairs().get(new Utf8("testSensorNamec")));

		Assert.assertEquals("metadata name a", "testMetadataValuea",
				iote2eRequest.getMetadata().get(new Utf8("testMetadataNamea")).toString());
		Assert.assertEquals("metadata name b", "testMetadataValueb",
				iote2eRequest.getMetadata().get(new Utf8("testMetadataNameb")).toString());
		Assert.assertNull("metadata name c", iote2eRequest.getMetadata().get(new Utf8("testMetadataNamec")));
	}

}

