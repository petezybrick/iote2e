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

import com.pzybrick.iote2e.schema.avro.Iote2eResult;


/**
 * The Class Iote2eResultReuseItem.
 */
public class Iote2eResultReuseItem implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1947504976342932616L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eResultReuseItem.class);	
	
	/** The iote 2 e result. */
	private Iote2eResult iote2eResult;
	
	/** The binary decoder. */
	private BinaryDecoder binaryDecoder;
	
	/** The datum reader iote 2 e result. */
	private DatumReader<Iote2eResult> datumReaderIote2eResult;
	
	/** The bytes. */
	private byte[] bytes;
	
	/** The binary encoder. */
	private BinaryEncoder binaryEncoder;
	
	/** The datum writer iote 2 e result. */
	private DatumWriter<Iote2eResult> datumWriterIote2eResult;
	
	
	/**
	 * Instantiates a new iote 2 e result reuse item.
	 */
	public Iote2eResultReuseItem() {
	}

	/**
	 * From byte array.
	 *
	 * @param bytes the bytes
	 * @return the iote 2 e result
	 * @throws Exception the exception
	 */
	public Iote2eResult fromByteArray( byte[] bytes ) throws Exception {

		try {
			if( this.datumReaderIote2eResult == null ) {
				this.datumReaderIote2eResult = new SpecificDatumReader<Iote2eResult>(Iote2eResult.getClassSchema());
			}
			this.binaryDecoder = DecoderFactory.get().binaryDecoder(bytes, this.binaryDecoder);
			this.iote2eResult = this.datumReaderIote2eResult.read(null,this.binaryDecoder);
			return this.iote2eResult;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	/**
	 * To byte array.
	 *
	 * @param iote2eResult the iote 2 e result
	 * @return the byte[]
	 * @throws Exception the exception
	 */
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
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}
	
	/**
	 * Gets the iote 2 e result.
	 *
	 * @return the iote 2 e result
	 */
	public Iote2eResult getIote2eResult() {
		return iote2eResult;
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
	 * Gets the datum reader iote 2 e result.
	 *
	 * @return the datum reader iote 2 e result
	 */
	public DatumReader<Iote2eResult> getDatumReaderIote2eResult() {
		return datumReaderIote2eResult;
	}
	
	/**
	 * Sets the iote 2 e result.
	 *
	 * @param actuatorResponse the actuator response
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setIote2eResult(Iote2eResult actuatorResponse) {
		this.iote2eResult = actuatorResponse;
		return this;
	}
	
	/**
	 * Sets the binary decoder.
	 *
	 * @param binaryDecoder the binary decoder
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	
	/**
	 * Sets the datum reader iote 2 e result.
	 *
	 * @param datumReaderIote2eResult the datum reader iote 2 e result
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setDatumReaderIote2eResult(DatumReader<Iote2eResult> datumReaderIote2eResult) {
		this.datumReaderIote2eResult = datumReaderIote2eResult;
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
	 * Gets the datum writer iote 2 e result.
	 *
	 * @return the datum writer iote 2 e result
	 */
	public DatumWriter<Iote2eResult> getDatumWriterIote2eResult() {
		return datumWriterIote2eResult;
	}
	
	/**
	 * Sets the bytes.
	 *
	 * @param bytes the bytes
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
	
	/**
	 * Sets the binary encoder.
	 *
	 * @param binaryEncoder the binary encoder
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setBinaryEncoder(BinaryEncoder binaryEncoder) {
		this.binaryEncoder = binaryEncoder;
		return this;
	}
	
	/**
	 * Sets the datum writer iote 2 e result.
	 *
	 * @param datumWriterIote2eResult the datum writer iote 2 e result
	 * @return the iote 2 e result reuse item
	 */
	public Iote2eResultReuseItem setDatumWriterIote2eResult(DatumWriter<Iote2eResult> datumWriterIote2eResult) {
		this.datumWriterIote2eResult = datumWriterIote2eResult;
		return this;
	}
}
