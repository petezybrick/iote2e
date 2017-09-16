/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


/**
 * The Class Iote2eRequestReuseItem.
 */
public class Iote2eRequestReuseItem implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7618508500668288196L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eRequestReuseItem.class);	
	
	/** The iote 2 e request. */
	private Iote2eRequest iote2eRequest;
	
	/** The binary decoder. */
	private BinaryDecoder binaryDecoder;
	
	/** The datum reader iote 2 e request. */
	private DatumReader<Iote2eRequest> datumReaderIote2eRequest;
	
	/** The bytes. */
	private byte[] bytes;
	
	/** The binary encoder. */
	private BinaryEncoder binaryEncoder;
	
	/** The datum writer iote 2 e request. */
	private DatumWriter<Iote2eRequest> datumWriterIote2eRequest;
	
	
	/**
	 * Instantiates a new iote 2 e request reuse item.
	 */
	public Iote2eRequestReuseItem() {
	}	
	
	/**
	 * From byte array.
	 *
	 * @param bytes the bytes
	 * @return the iote 2 e request
	 * @throws Exception the exception
	 */
	public Iote2eRequest fromByteArray( byte[] bytes ) throws Exception {

		try {
			if( this.datumReaderIote2eRequest == null ) {
				this.datumReaderIote2eRequest = new SpecificDatumReader<Iote2eRequest>(Iote2eRequest.getClassSchema());
			}
			this.binaryDecoder = DecoderFactory.get().binaryDecoder( bytes, this.binaryDecoder );
			this.iote2eRequest = this.datumReaderIote2eRequest.read (null, this.binaryDecoder );
			return this.iote2eRequest;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	/**
	 * To byte array.
	 *
	 * @param iote2eRequest the iote 2 e request
	 * @return the byte[]
	 * @throws Exception the exception
	 */
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
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}
	
	/**
	 * Gets the iote 2 e request.
	 *
	 * @return the iote 2 e request
	 */
	public Iote2eRequest getIote2eRequest() {
		return iote2eRequest;
	}
	
	/**
	 * Gets the binary decoder.
	 *
	 * @return the binary decoder
	 */
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	
	/**
	 * Gets the datum reader iote 2 e request.
	 *
	 * @return the datum reader iote 2 e request
	 */
	public DatumReader<Iote2eRequest> getDatumReaderIote2eRequest() {
		return datumReaderIote2eRequest;
	}
	
	/**
	 * Sets the iote 2 e request.
	 *
	 * @param iote2eRequest the iote 2 e request
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setIote2eRequest(Iote2eRequest iote2eRequest) {
		this.iote2eRequest = iote2eRequest;
		return this;
	}
	
	/**
	 * Sets the binary decoder.
	 *
	 * @param binaryDecoder the binary decoder
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	
	/**
	 * Sets the datum reader iote 2 e request.
	 *
	 * @param datumReaderIote2eRequest the datum reader iote 2 e request
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setDatumReaderIote2eRequest(DatumReader<Iote2eRequest> datumReaderIote2eRequest) {
		this.datumReaderIote2eRequest = datumReaderIote2eRequest;
		return this;
	}
	
	/**
	 * Gets the bytes.
	 *
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}
	
	/**
	 * Gets the binary encoder.
	 *
	 * @return the binary encoder
	 */
	public BinaryEncoder getBinaryEncoder() {
		return binaryEncoder;
	}
	
	/**
	 * Gets the datum writer iote 2 e request.
	 *
	 * @return the datum writer iote 2 e request
	 */
	public DatumWriter<Iote2eRequest> getDatumWriterIote2eRequest() {
		return datumWriterIote2eRequest;
	}
	
	/**
	 * Sets the bytes.
	 *
	 * @param bytes the bytes
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	
	/**
	 * Sets the binary encoder.
	 *
	 * @param binaryEncoder the binary encoder
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	
	/**
	 * Sets the datum writer iote 2 e request.
	 *
	 * @param datumWriterIote2eRequest the datum writer iote 2 e request
	 * @return the iote 2 e request reuse item
	 */
	public Iote2eRequestReuseItem setDatumWriterIote2eRequest(DatumWriter<Iote2eRequest> datumWriterIote2eRequest) {
		this.datumWriterIote2eRequest = datumWriterIote2eRequest;
		return this;
	}
}
