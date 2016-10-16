package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.ActuatorResponse;

public class ActuatorResponseFromByteArrayReuseItem {
	private ActuatorResponse actuatorResponse;
	private BinaryDecoder binaryDecoder;
	private DatumReader<ActuatorResponse> datumReaderActuatorResponse;
	
	
	public ActuatorResponseFromByteArrayReuseItem() {
		datumReaderActuatorResponse = new SpecificDatumReader<ActuatorResponse>(ActuatorResponse.getClassSchema());
	}
	
	public ActuatorResponse getActuatorResponse() {
		return actuatorResponse;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<ActuatorResponse> getDatumReaderActuatorResponse() {
		return datumReaderActuatorResponse;
	}
	public ActuatorResponseFromByteArrayReuseItem setActuatorResponse(ActuatorResponse actuatorResponse) {
		this.actuatorResponse = actuatorResponse;
		return this;
	}
	public ActuatorResponseFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public ActuatorResponseFromByteArrayReuseItem setDatumReaderActuatorResponse(DatumReader<ActuatorResponse> datumReaderActuatorResponse) {
		this.datumReaderActuatorResponse = datumReaderActuatorResponse;
		return this;
	}

}
