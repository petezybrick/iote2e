package com.pzybrick.test.iote2e.schema.avro;

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

public class TestIote2eRequest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() throws Exception {
		Iote2eRequest iote2eRequest = createTestIote2eRequest();
		verifyIote2eRequest( iote2eRequest );
	}
	
	@Test
	public void testEncodeDecode() throws Exception {
		Iote2eRequestReuseItem iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		Iote2eRequest iote2eRequestBefore = createTestIote2eRequest();
		byte[] bytes = iote2eRequestReuseItem.toByteArray(iote2eRequestBefore);
		Iote2eRequest iote2eRequestAfter = iote2eRequestReuseItem.fromByteArray(bytes);
		verifyIote2eRequest( iote2eRequestAfter );
	}
	
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

