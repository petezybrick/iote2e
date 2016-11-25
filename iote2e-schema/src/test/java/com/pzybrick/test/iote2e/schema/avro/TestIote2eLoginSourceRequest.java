package com.pzybrick.test.iote2e.schema.avro;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.schema.avro.LoginSourceRequest;
import com.pzybrick.iote2e.schema.avro.SensorValue;

public class TestIote2eLoginSourceRequest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		List<SensorValue> sensorValues = new ArrayList<SensorValue>();
		sensorValues.add(
				SensorValue.newBuilder().setSensorName("testSensorName1").setSensorValue("testSensorValue1").build());
		sensorValues.add(
				SensorValue.newBuilder().setSensorName("testSensorName2").setSensorValue("testSensorValue2").build());
		LoginSourceRequest loginSourceRequest = LoginSourceRequest.newBuilder()
				.setLoginUuid("testLoginUuid").setSourceUuid("testSourceUuid").setTimestamp(IotE2eUtils.getDateNowUtc8601())
				.setSensorValues(sensorValues).build();
		System.out.println(loginSourceRequest.toString());

		Assert.assertEquals("login uuid", "testLoginUuid", loginSourceRequest.getLoginUuid());
		Assert.assertEquals("source uuid", "testSourceUuid", loginSourceRequest.getSourceUuid());
		Assert.assertEquals("login uuid", "testLoginUuid", loginSourceRequest.getLoginUuid());
		Assert.assertEquals("sensor name 1", "testSensorName1",
				loginSourceRequest.getSensorValues().get(0).getSensorName());
		Assert.assertEquals("sensor value 1", "testSensorValue1",
				loginSourceRequest.getSensorValues().get(0).getSensorValue());
		Assert.assertEquals("sensor name 2", "testSensorName2",
				loginSourceRequest.getSensorValues().get(1).getSensorName());
		Assert.assertEquals("sensor value 2", "testSensorValue2",
				loginSourceRequest.getSensorValues().get(1).getSensorValue());

	}

}

