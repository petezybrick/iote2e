package com.pzybrick.iote2e.schema.util;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class AvroSchemaUtils {
	private static final Log log = LogFactory.getLog(AvroSchemaUtils.class);	

	public static void iote2eResultFromByteArray( Iote2eResultFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setIote2eResult( fromByteArrayReuseItem.getDatumReaderIote2eResult().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void iote2eResultValueToByteArray( Iote2eResultToByteArrayReuseItem toByteArrayReuseItem, Iote2eResult iote2eResult
			) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterLoginActuatorResponse().write(iote2eResult, toByteArrayReuseItem.getBinaryEncoder() );
			toByteArrayReuseItem.getBinaryEncoder().flush();
			toByteArrayReuseItem.setBytes( baos.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}
	
	public static void iote2eRequestFromByteArray( Iote2eRequestFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setIote2eRequest( fromByteArrayReuseItem.getDatumReaderIote2eRequest().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void iote2eRequestToByteArray( Iote2eRequestToByteArrayReuseItem toByteArrayReuseItem,
			Iote2eRequest iote2eRequest ) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterIote2eRequest().write(iote2eRequest, toByteArrayReuseItem.getBinaryEncoder() );
			toByteArrayReuseItem.getBinaryEncoder().flush();
			toByteArrayReuseItem.setBytes( baos.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}

}
