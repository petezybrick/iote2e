package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;

public class LoginActuatorResponseFromByteArrayReuseItem {
	private LoginActuatorResponse actuatorResponse;
	private BinaryDecoder binaryDecoder;
	private DatumReader<LoginActuatorResponse> datumReaderLoginActuatorResponse;
	
	
	public LoginActuatorResponseFromByteArrayReuseItem() {
		datumReaderLoginActuatorResponse = new SpecificDatumReader<LoginActuatorResponse>(LoginActuatorResponse.getClassSchema());
	}
	
	public LoginActuatorResponse getLoginActuatorResponse() {
		return actuatorResponse;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<LoginActuatorResponse> getDatumReaderLoginActuatorResponse() {
		return datumReaderLoginActuatorResponse;
	}
	public LoginActuatorResponseFromByteArrayReuseItem setLoginActuatorResponse(LoginActuatorResponse actuatorResponse) {
		this.actuatorResponse = actuatorResponse;
		return this;
	}
	public LoginActuatorResponseFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public LoginActuatorResponseFromByteArrayReuseItem setDatumReaderLoginActuatorResponse(DatumReader<LoginActuatorResponse> datumReaderLoginActuatorResponse) {
		this.datumReaderLoginActuatorResponse = datumReaderLoginActuatorResponse;
		return this;
	}

}
