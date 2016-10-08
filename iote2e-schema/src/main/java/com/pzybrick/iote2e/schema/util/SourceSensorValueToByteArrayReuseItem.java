package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;

import com.pzybrick.iote2e.schema.avro.SourceSensorValue;

public class SourceSensorValueToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<SourceSensorValue> datumWriterSourceSensorValue;
	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<SourceSensorValue> getDatumWriterSourceSensorValue() {
		return datumWriterSourceSensorValue;
	}
	public SourceSensorValueToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public SourceSensorValueToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public SourceSensorValueToByteArrayReuseItem setDatumWriterSourceSensorValue(DatumWriter<SourceSensorValue> datumWriterSourceSensorValue) {
		this.datumWriterSourceSensorValue = datumWriterSourceSensorValue;
		return this;
	}
}

