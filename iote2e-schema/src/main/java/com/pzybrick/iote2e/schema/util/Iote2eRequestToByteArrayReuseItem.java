package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<Iote2eRequest> datumWriterIote2eRequest;
	
	
	public Iote2eRequestToByteArrayReuseItem() {
		datumWriterIote2eRequest = new SpecificDatumWriter<Iote2eRequest>(Iote2eRequest.getClassSchema());
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<Iote2eRequest> getDatumWriterIote2eRequest() {
		return datumWriterIote2eRequest;
	}
	public Iote2eRequestToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public Iote2eRequestToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public Iote2eRequestToByteArrayReuseItem setDatumWriterIote2eRequest(DatumWriter<Iote2eRequest> datumWriterIote2eRequest) {
		this.datumWriterIote2eRequest = datumWriterIote2eRequest;
		return this;
	}
}

