package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eResultFromByteArrayReuseItem {
	private Iote2eResult iote2eResult;
	private BinaryDecoder binaryDecoder;
	private DatumReader<Iote2eResult> datumReaderIote2eResult;
	
	
	public Iote2eResultFromByteArrayReuseItem() {
		datumReaderIote2eResult = new SpecificDatumReader<Iote2eResult>(Iote2eResult.getClassSchema());
	}
	
	public Iote2eResult getIote2eResult() {
		return iote2eResult;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<Iote2eResult> getDatumReaderIote2eResult() {
		return datumReaderIote2eResult;
	}
	public Iote2eResultFromByteArrayReuseItem setIote2eResult(Iote2eResult actuatorResponse) {
		this.iote2eResult = actuatorResponse;
		return this;
	}
	public Iote2eResultFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public Iote2eResultFromByteArrayReuseItem setDatumReaderIote2eResult(DatumReader<Iote2eResult> datumReaderIote2eResult) {
		this.datumReaderIote2eResult = datumReaderIote2eResult;
		return this;
	}

}
