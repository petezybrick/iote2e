package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.LoginSourceRequest;

public class LoginSourceRequestFromByteArrayReuseItem {
	private LoginSourceRequest loginSourceRequest;
	private BinaryDecoder binaryDecoder;
	private DatumReader<LoginSourceRequest> datumReaderLoginSourceRequest;
	
	
	public LoginSourceRequestFromByteArrayReuseItem() {
		datumReaderLoginSourceRequest = new SpecificDatumReader<LoginSourceRequest>(LoginSourceRequest.getClassSchema());
	}
	
	public LoginSourceRequest getLoginSourceRequest() {
		return loginSourceRequest;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<LoginSourceRequest> getDatumReaderLoginSourceRequest() {
		return datumReaderLoginSourceRequest;
	}
	public LoginSourceRequestFromByteArrayReuseItem setLoginSourceRequest(LoginSourceRequest actuatorResponse) {
		this.loginSourceRequest = actuatorResponse;
		return this;
	}
	public LoginSourceRequestFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public LoginSourceRequestFromByteArrayReuseItem setDatumReaderLoginSourceRequest(DatumReader<LoginSourceRequest> datumReaderLoginSourceRequest) {
		this.datumReaderLoginSourceRequest = datumReaderLoginSourceRequest;
		return this;
	}

}
