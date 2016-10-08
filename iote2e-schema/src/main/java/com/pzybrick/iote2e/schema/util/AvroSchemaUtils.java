package com.pzybrick.iote2e.schema.util;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.schema.avro.ActuatorResponse;
import com.pzybrick.iote2e.schema.avro.SourceSensorValue;

public class AvroSchemaUtils {
	private static final Log log = LogFactory.getLog(AvroSchemaUtils.class);	

	public static void sourceSensorValueToByteArray( SourceSensorValueToByteArrayReuseItem toByteArrayReuseItem,
			String sourceUuid, String sensorUuid, String sensorValue ) throws Exception {
		SourceSensorValue sourceSensorValue = SourceSensorValue.newBuilder()
				.setSourceUuid(sourceUuid)
				.setSensorUuid(sensorUuid)
				.setSensorValue(sensorValue)
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterSourceSensorValue().write(sourceSensorValue, toByteArrayReuseItem.getBinaryEncoder() );
			toByteArrayReuseItem.getBinaryEncoder().flush();
			toByteArrayReuseItem.setBytes( baos.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}

	public static void sourceSensorActuatorFromByteArray( SourceSensorValueFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setSourceSensorValue(fromByteArrayReuseItem.getDatumReaderSourceSensorValue().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void actuatorResponseFromByteArray( ActuatorResponseFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setActuatorResponse( fromByteArrayReuseItem.getDatumReaderActuatorResponse().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void actuatorResponseValueToByteArray( ActuatorResponseToByteArrayReuseItem toByteArrayReuseItem,
			String sourceUuid, String sensorUuid, String actuatorUuid, String actuatorValue, String actuatorValueUpdatedAt 
			) throws Exception {
		ActuatorResponse actuatorResponse = ActuatorResponse.newBuilder()
				.setSourceUuid(sourceUuid)
				.setSensorUuid(sensorUuid)
				.setActuatorUuid(actuatorUuid)
				.setActuatorValue(actuatorValue)
				.setActuatorValueUpdatedAt(actuatorValueUpdatedAt)
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterActuatorResponse().write(actuatorResponse, toByteArrayReuseItem.getBinaryEncoder() );
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
