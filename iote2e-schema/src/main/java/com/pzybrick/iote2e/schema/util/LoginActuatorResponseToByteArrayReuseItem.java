package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;

public class LoginActuatorResponseToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<LoginActuatorResponse> datumWriterLoginActuatorResponse;
	
	
	public LoginActuatorResponseToByteArrayReuseItem() {
		datumWriterLoginActuatorResponse = new SpecificDatumWriter<LoginActuatorResponse>(LoginActuatorResponse.getClassSchema());
	}

	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<LoginActuatorResponse> getDatumWriterLoginActuatorResponse() {
		return datumWriterLoginActuatorResponse;
	}
	public LoginActuatorResponseToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public LoginActuatorResponseToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public LoginActuatorResponseToByteArrayReuseItem setDatumWriterLoginActuatorResponse(DatumWriter<LoginActuatorResponse> datumWriterLoginActuatorResponse) {
		this.datumWriterLoginActuatorResponse = datumWriterLoginActuatorResponse;
		return this;
	}
}

