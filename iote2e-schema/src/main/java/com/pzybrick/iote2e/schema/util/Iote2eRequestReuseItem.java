package com.pzybrick.iote2e.schema.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

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

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestReuseItem implements Serializable {
	private static final long serialVersionUID = 7618508500668288196L;
	private static final Log log = LogFactory.getLog(Iote2eRequestReuseItem.class);	
	private Iote2eRequest iote2eRequest;
	private BinaryDecoder binaryDecoder;
	private DatumReader<Iote2eRequest> datumReaderIote2eRequest;
	private byte[] bytes;
	private BinaryEncoder binaryEncoder;
	private DatumWriter<Iote2eRequest> datumWriterIote2eRequest;
	
	
	public Iote2eRequestReuseItem() {
	}	
	
	public Iote2eRequest fromByteArray( byte[] bytes ) throws Exception {

		try {
			if( this.datumReaderIote2eRequest == null ) {
				this.datumReaderIote2eRequest = new SpecificDatumReader<Iote2eRequest>(Iote2eRequest.getClassSchema());
			}
			this.binaryDecoder = DecoderFactory.get().binaryDecoder( bytes, this.binaryDecoder );
			this.iote2eRequest = this.datumReaderIote2eRequest.read (null, this.binaryDecoder );
			return this.iote2eRequest;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public byte[] toByteArray( Iote2eRequest iote2eRequest ) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if( this.datumWriterIote2eRequest == null ) {
				this.datumWriterIote2eRequest = new SpecificDatumWriter<Iote2eRequest>(Iote2eRequest.getClassSchema());
			}
			this.binaryEncoder = EncoderFactory.get().binaryEncoder(baos, this.binaryEncoder );
			this.datumWriterIote2eRequest.write(iote2eRequest, this.binaryEncoder );
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
	
	public Iote2eRequest getIote2eRequest() {
		return iote2eRequest;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<Iote2eRequest> getDatumReaderIote2eRequest() {
		return datumReaderIote2eRequest;
	}
	public Iote2eRequestReuseItem setIote2eRequest(Iote2eRequest iote2eRequest) {
		this.iote2eRequest = iote2eRequest;
		return this;
	}
	public Iote2eRequestReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public Iote2eRequestReuseItem setDatumReaderIote2eRequest(DatumReader<Iote2eRequest> datumReaderIote2eRequest) {
		this.datumReaderIote2eRequest = datumReaderIote2eRequest;
		return this;
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
	public Iote2eRequestReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	public Iote2eRequestReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	public Iote2eRequestReuseItem setDatumWriterIote2eRequest(DatumWriter<Iote2eRequest> datumWriterIote2eRequest) {
		this.datumWriterIote2eRequest = datumWriterIote2eRequest;
		return this;
	}
}
