package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eRequestFromByteArrayReuseItem {
	private Iote2eRequest iote2eRequest;
	private BinaryDecoder binaryDecoder;
	private DatumReader<Iote2eRequest> datumReaderIote2eRequest;
	
	
	public Iote2eRequestFromByteArrayReuseItem() {
		datumReaderIote2eRequest = new SpecificDatumReader<Iote2eRequest>(Iote2eRequest.getClassSchema());
	}
	
	public Iote2eRequest getIote2eRequest() {
		return iote2eRequest;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<Iote2eRequest> getDatumReaderIote2eRequest() {
		return datumReaderIote2eRequest;
	}
	public Iote2eRequestFromByteArrayReuseItem setIote2eRequest(Iote2eRequest iote2eRequest) {
		this.iote2eRequest = iote2eRequest;
		return this;
	}
	public Iote2eRequestFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public Iote2eRequestFromByteArrayReuseItem setDatumReaderIote2eRequest(DatumReader<Iote2eRequest> datumReaderIote2eRequest) {
		this.datumReaderIote2eRequest = datumReaderIote2eRequest;
		return this;
	}

}
