package com.pzybrick.iote2e.schema.util;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.schema.avro.ActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginSourceRequest;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
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

	public static void loginSourceSensorValueToByteArray( LoginSourceSensorValueToByteArrayReuseItem toByteArrayReuseItem,
			String loginUuid, String sourceUuid, String sensorUuid, String sensorValue ) throws Exception {
		LoginSourceSensorValue loginSourceSensorValue = LoginSourceSensorValue.newBuilder()
				.setLoginUuid(loginUuid)
				.setSourceUuid(sourceUuid)
				.setSensorUuid(sensorUuid)
				.setSensorValue(sensorValue)
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterLoginSourceSensorValue().write(loginSourceSensorValue, toByteArrayReuseItem.getBinaryEncoder() );
			toByteArrayReuseItem.getBinaryEncoder().flush();
			toByteArrayReuseItem.setBytes( baos.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}

	public static void loginSourceSensorActuatorFromByteArray( LoginSourceSensorValueFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setLoginSourceSensorValue(fromByteArrayReuseItem.getDatumReaderLoginSourceSensorValue().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
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


	public static void loginActuatorResponseFromByteArray( LoginActuatorResponseFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setLoginActuatorResponse( fromByteArrayReuseItem.getDatumReaderLoginActuatorResponse().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void loginActuatorResponseValueToByteArray( LoginActuatorResponseToByteArrayReuseItem toByteArrayReuseItem,
			String loginUuid, String sourceUuid, String sensorUuid, String actuatorUuid, String actuatorValue, String actuatorValueUpdatedAt 
			) throws Exception {
		LoginActuatorResponse loginActuatorResponse = LoginActuatorResponse.newBuilder()
				.setLoginUuid(loginUuid)
				.setSourceUuid(sourceUuid)
				.setSensorUuid(sensorUuid)
				.setActuatorUuid(actuatorUuid)
				.setActuatorValue(actuatorValue)
				.setActuatorValueUpdatedAt(actuatorValueUpdatedAt)
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterLoginActuatorResponse().write(loginActuatorResponse, toByteArrayReuseItem.getBinaryEncoder() );
			toByteArrayReuseItem.getBinaryEncoder().flush();
			toByteArrayReuseItem.setBytes( baos.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
			baos.close();
		}
	}

	
	public static void loginSourceRequestFromByteArray( LoginSourceRequestFromByteArrayReuseItem fromByteArrayReuseItem,
			byte[] bytes ) throws Exception {

		try {
			fromByteArrayReuseItem.setBinaryDecoder( DecoderFactory.get().binaryDecoder(bytes, fromByteArrayReuseItem.getBinaryDecoder()));
			fromByteArrayReuseItem.setLoginSourceRequest( fromByteArrayReuseItem.getDatumReaderLoginSourceRequest().read(null,fromByteArrayReuseItem.getBinaryDecoder()));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		} finally {
		}
	}

	public static void loginSourceRequestValueToByteArray( LoginSourceRequestToByteArrayReuseItem toByteArrayReuseItem,
			String loginUuid, String sourceUuid, String sensorUuid, String actuatorUuid, String actuatorValue, String actuatorValueUpdatedAt 
			) throws Exception {
		LoginSourceRequest loginSourceRequest = LoginSourceRequest.newBuilder()
				.setLoginUuid(loginUuid)
				.setSourceUuid(sourceUuid)
				.build();
	}

	public static void loginSourceRequestValueToByteArray( LoginSourceRequestToByteArrayReuseItem toByteArrayReuseItem,
			LoginSourceRequest loginSourceRequest ) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			toByteArrayReuseItem.setBinaryEncoder( EncoderFactory.get().binaryEncoder(baos, toByteArrayReuseItem.getBinaryEncoder() ));
			toByteArrayReuseItem.getDatumWriterLoginSourceRequest().write(loginSourceRequest, toByteArrayReuseItem.getBinaryEncoder() );
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
