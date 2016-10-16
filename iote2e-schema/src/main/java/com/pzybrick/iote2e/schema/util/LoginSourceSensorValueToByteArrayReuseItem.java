package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;

public class LoginSourceSensorValueToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<LoginSourceSensorValue> datumWriterLoginSourceSensorValue;
	
	
	public LoginSourceSensorValueToByteArrayReuseItem() {
		datumWriterLoginSourceSensorValue = new SpecificDatumWriter<LoginSourceSensorValue>(LoginSourceSensorValue.getClassSchema());
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<LoginSourceSensorValue> getDatumWriterLoginSourceSensorValue() {
		return datumWriterLoginSourceSensorValue;
	}
	public LoginSourceSensorValueToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public LoginSourceSensorValueToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public LoginSourceSensorValueToByteArrayReuseItem setDatumWriterLoginSourceSensorValue(DatumWriter<LoginSourceSensorValue> datumWriterLoginSourceSensorValue) {
		this.datumWriterLoginSourceSensorValue = datumWriterLoginSourceSensorValue;
		return this;
	}
}

