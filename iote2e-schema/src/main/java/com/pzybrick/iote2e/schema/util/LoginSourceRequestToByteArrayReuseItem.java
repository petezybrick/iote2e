package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.iote2e.schema.avro.LoginSourceRequest;

public class LoginSourceRequestToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<LoginSourceRequest> datumWriterLoginSourceRequest;
	
	
	public LoginSourceRequestToByteArrayReuseItem() {
		datumWriterLoginSourceRequest = new SpecificDatumWriter<LoginSourceRequest>(LoginSourceRequest.getClassSchema());
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<LoginSourceRequest> getDatumWriterLoginSourceRequest() {
		return datumWriterLoginSourceRequest;
	}
	public LoginSourceRequestToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public LoginSourceRequestToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public LoginSourceRequestToByteArrayReuseItem setDatumWriterLoginSourceRequest(DatumWriter<LoginSourceRequest> datumWriterLoginSourceRequest) {
		this.datumWriterLoginSourceRequest = datumWriterLoginSourceRequest;
		return this;
	}
}

