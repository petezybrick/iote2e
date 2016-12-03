package com.pzybrick.test.iote2e.schema.avro;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;

public class TestIote2eRequest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
		metadata.put("testMetadataNamea", "testMetadataValuea");
		metadata.put("testMetadataNameb", "testMetadataValueb");
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put("testSensorNamea", "testSensorValuea");
		pairs.put("testSensorNameb", "testSensorValueb");
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
		System.out.println(iote2eRequest.toString());

		Assert.assertEquals("login name", "testLoginName", iote2eRequest.getLoginName());
		Assert.assertEquals("source name", "testSourceName", iote2eRequest.getSourceName());
		Assert.assertEquals("source type", "testSourceType", iote2eRequest.getSourceType());
		Assert.assertEquals("request uuid", "testRequestUuid", iote2eRequest.getRequestUuid());
		Assert.assertNotNull("timestamp", iote2eRequest.getRequestUuid());
		Assert.assertEquals("sensor name a", "testSensorValuea",
				iote2eRequest.getPairs().get("testSensorNamea"));
		Assert.assertEquals("sensor name b", "testSensorValueb",
				iote2eRequest.getPairs().get("testSensorNameb"));
		Assert.assertNull("sensor name c", iote2eRequest.getPairs().get("testSensorNamec"));

		Assert.assertEquals("metadata name a", "testMetadataValuea",
				iote2eRequest.getMetadata().get("testMetadataNamea"));
		Assert.assertEquals("metadata name b", "testMetadataValueb",
				iote2eRequest.getMetadata().get("testMetadataNameb"));
		Assert.assertNull("metadata name c", iote2eRequest.getMetadata().get("testMetadataNamec"));

	}

}
