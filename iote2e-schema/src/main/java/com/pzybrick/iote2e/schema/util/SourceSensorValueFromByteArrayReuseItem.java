package com.pzybrick.iote2e.schema.util;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.iote2e.schema.avro.SourceSensorValue;

public class SourceSensorValueFromByteArrayReuseItem {
	private SourceSensorValue sourceSensorValue;
	private BinaryDecoder binaryDecoder;
	private DatumReader<SourceSensorValue> datumReaderSourceSensorValue;
	
	
	public SourceSensorValueFromByteArrayReuseItem() {
		datumReaderSourceSensorValue = new SpecificDatumReader<SourceSensorValue>(SourceSensorValue.getClassSchema());
	}
	
	public SourceSensorValue getSourceSensorValue() {
		return sourceSensorValue;
	}
	public BinaryDecoder getBinaryDecoder() {
		return binaryDecoder;
	}
	public DatumReader<SourceSensorValue> getDatumReaderSourceSensorValue() {
		return datumReaderSourceSensorValue;
	}
	public SourceSensorValueFromByteArrayReuseItem setSourceSensorValue(SourceSensorValue sourceSensorValue) {
		this.sourceSensorValue = sourceSensorValue;
		return this;
	}
	public SourceSensorValueFromByteArrayReuseItem setBinaryDecoder(BinaryDecoder binaryDecoder) {
		this.binaryDecoder = binaryDecoder;
		return this;
	}
	public SourceSensorValueFromByteArrayReuseItem setDatumReaderSourceSensorValue(DatumReader<SourceSensorValue> datumReaderSourceSensorValue) {
		this.datumReaderSourceSensorValue = datumReaderSourceSensorValue;
		return this;
	}
}
