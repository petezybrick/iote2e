package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eResultToByteArrayReuseItem {
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<Iote2eResult> datumWriterIote2eResult;
	
	
	public Iote2eResultToByteArrayReuseItem() {
		datumWriterIote2eResult = new SpecificDatumWriter<Iote2eResult>(Iote2eResult.getClassSchema());
	}

	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<Iote2eResult> getDatumWriterLoginActuatorResponse() {
		return datumWriterIote2eResult;
	}
	public Iote2eResultToByteArrayReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public Iote2eResultToByteArrayReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public Iote2eResultToByteArrayReuseItem setDatumWriterLoginActuatorResponse(DatumWriter<Iote2eResult> datumWriterIote2eResult) {
		this.datumWriterIote2eResult = datumWriterIote2eResult;
		return this;
	}
}

