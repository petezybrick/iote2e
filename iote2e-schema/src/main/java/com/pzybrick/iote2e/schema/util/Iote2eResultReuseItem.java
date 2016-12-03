package com.pzybrick.iote2e.schema.util;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eResultReuseItem {
	private static final Log log = LogFactory.getLog(Iote2eResultReuseItem.class);	
	private Iote2eResult iote2eResult;
	private BinaryDecoder binaryDecoder;
	private DatumReader<Iote2eResult> datumReaderIote2eResult;
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<Iote2eResult> datumWriterIote2eResult;
	
	
	public Iote2eResultReuseItem() {
	}

	public Iote2eResult fromByteArray( byte[] bytes ) throws Exception {

		try {
			if( this.datumReaderIote2eResult == null ) {
				this.datumReaderIote2eResult = new SpecificDatumReader<Iote2eResult>(Iote2eResult.getClassSchema());
			}
			this.binaryDecoder = DecoderFactory.get().binaryDecoder(bytes, this.binaryDecoder);
			this.iote2eResult = this.datumReaderIote2eResult.read(null,this.binaryDecoder);
			return this.iote2eResult;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public byte[] toByteArray( Iote2eResult iote2eResult ) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if( this.datumWriterIote2eResult == null ) {
				this.datumWriterIote2eResult = new SpecificDatumWriter<Iote2eResult>(Iote2eResult.getClassSchema());
			}
			this.binaryEncoder = EncoderFactory.get().binaryEncoder(baos, this.binaryEncoder );
			this.datumWriterIote2eResult.write(iote2eResult, this.binaryEncoder );
			this.binaryEncoder.flush();
			this.bytes = baos.toByteArray();
			return this.bytes;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
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
	public Iote2eResultReuseItem setIote2eResult(Iote2eResult actuatorResponse) {
		this.iote2eResult = actuatorResponse;
		return this;
	}
	public Iote2eResultReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public Iote2eResultReuseItem setDatumReaderIote2eResult(DatumReader<Iote2eResult> datumReaderIote2eResult) {
		this.datumReaderIote2eResult = datumReaderIote2eResult;
		return this;
	}

	public byte[] getBytes() {
		return bytes;
	}
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	public DatumWriter<Iote2eResult> getDatumWriterIote2eResult() {
		return datumWriterIote2eResult;
	}
	public Iote2eResultReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public Iote2eResultReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public Iote2eResultReuseItem setDatumWriterIote2eResult(DatumWriter<Iote2eResult> datumWriterIote2eResult) {
		this.datumWriterIote2eResult = datumWriterIote2eResult;
		return this;
	}
}
