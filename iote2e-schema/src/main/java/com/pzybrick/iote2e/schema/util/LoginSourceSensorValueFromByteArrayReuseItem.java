package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;

public class LoginSourceSensorValueFromByteArrayReuseItem {
	private LoginSourceSensorValue sourceSensorValue;
	private BinaryDecoder binaryDecoder;
	private DatumReader<LoginSourceSensorValue> datumReaderLoginSourceSensorValue;
	
	
	public LoginSourceSensorValueFromByteArrayReuseItem() {
		datumReaderLoginSourceSensorValue = new SpecificDatumReader<LoginSourceSensorValue>(LoginSourceSensorValue.getClassSchema());
	}

	public LoginSourceSensorValue getLoginSourceSensorValue() {
		return sourceSensorValue;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<LoginSourceSensorValue> getDatumReaderLoginSourceSensorValue() {
		return datumReaderLoginSourceSensorValue;
	}
	public LoginSourceSensorValueFromByteArrayReuseItem setLoginSourceSensorValue(LoginSourceSensorValue sourceSensorValue) {
		this.sourceSensorValue = sourceSensorValue;
		return this;
	}
	public LoginSourceSensorValueFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public LoginSourceSensorValueFromByteArrayReuseItem setDatumReaderLoginSourceSensorValue(DatumReader<LoginSourceSensorValue> datumReaderLoginSourceSensorValue) {
		this.datumReaderLoginSourceSensorValue = datumReaderLoginSourceSensorValue;
		return this;
	}
}
