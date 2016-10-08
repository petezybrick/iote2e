package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;

import com.pzybrick.iote2e.schema.avro.ActuatorResponse;

public class ActuatorResponseToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<ActuatorResponse> datumWriterActuatorResponse;
	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<ActuatorResponse> getDatumWriterActuatorResponse() {
		return datumWriterActuatorResponse;
	}
	public ActuatorResponseToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public ActuatorResponseToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public ActuatorResponseToByteArrayReuseItem setDatumWriterActuatorResponse(DatumWriter<ActuatorResponse> datumWriterActuatorResponse) {
		this.datumWriterActuatorResponse = datumWriterActuatorResponse;
		return this;
	}
}

