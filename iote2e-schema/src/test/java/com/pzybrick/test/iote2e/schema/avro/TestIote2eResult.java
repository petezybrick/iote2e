package com.pzybrick.test.iote2e.schema.avro;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;

public class TestIote2eResult {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Integer resultCode = 8;
		String requestTimestamp = Iote2eUtils.getDateNowUtc8601();
		String resultTimestamp = Iote2eUtils.getDateNowUtc8601();
		Map<CharSequence,CharSequence> metadata = new HashMap<CharSequence,CharSequence>();
		metadata.put("testMetadataNamea", "testMetadataValuea");
		metadata.put("testMetadataNameb", "testMetadataValueb");
		Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
		pairs.put("testActuatorNamea", "testActuatorValuea");
		pairs.put("testActuatorNameb", "testActuatorValueb");
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
		System.out.println(iote2eResult.toString());

		Assert.assertEquals("login name", "testLoginName", iote2eResult.getLoginName());
		Assert.assertEquals("source name", "testSourceName", iote2eResult.getSourceName());
		Assert.assertEquals("source type", "testSourceType", iote2eResult.getSourceType());
		Assert.assertEquals("request uuid", "testRequestUuid", iote2eResult.getRequestUuid());
		Assert.assertEquals("request timestamp", iote2eResult.getRequestTimestamp(), requestTimestamp);
		
		Assert.assertEquals("result code", iote2eResult.getResultCode(), resultCode);
		Assert.assertEquals("result error message", iote2eResult.getResultErrorMessage(), "testErrorMessage");
		Assert.assertEquals("result timestamp", iote2eResult.getResultUuid(), "testResultUuid");
		Assert.assertEquals("result timestamp", iote2eResult.getResultTimestamp(), resultTimestamp);

		Assert.assertEquals("actuator name a", "testActuatorValuea",
				iote2eResult.getPairs().get("testActuatorNamea"));
		Assert.assertEquals("actuator name b", "testActuatorValueb",
				iote2eResult.getPairs().get("testActuatorNameb"));
		Assert.assertNull("actuator name c", iote2eResult.getPairs().get("testActuatorNamec"));
		
		Assert.assertEquals("metadata name a", "testMetadataValuea",
				iote2eResult.getMetadata().get("testMetadataNamea"));
		Assert.assertEquals("metadata name b", "testMetadataValueb",
				iote2eResult.getMetadata().get("testMetadataNameb"));
		Assert.assertNull("metadata name c", iote2eResult.getMetadata().get("testMetadataNamec"));

	}
}