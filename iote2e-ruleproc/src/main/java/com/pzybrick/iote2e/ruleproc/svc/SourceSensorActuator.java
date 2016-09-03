package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class SourceSensorActuator {
	@Expose
	private String sourceUuid;
	@Expose
	private String sensorUuid;
	@Expose
	private String actuatorUuid;
	@Expose
	private String actuatorValue;
	@Expose
	private String desc;

	public SourceSensorActuator() {
		
	}

	public String getSourceUuid() {
		return sourceUuid;
	}

	public String getSensorUuid() {
		return sensorUuid;
	}

	public String getActuatorUuid() {
		return actuatorUuid;
	}

	public String getActuatorValue() {
		return actuatorValue;
	}

	public String getDesc() {
		return desc;
	}

	public SourceSensorActuator setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
		return this;
	}

	public SourceSensorActuator setSensorUuid(String sensorUuid) {
		this.sensorUuid = sensorUuid;
		return this;
	}

	public SourceSensorActuator setActuatorUuid(String actuatorUuid) {
		this.actuatorUuid = actuatorUuid;
		return this;
	}

	public SourceSensorActuator setActuatorValue(String actuatorValue) {
		this.actuatorValue = actuatorValue;
		return this;
	}

	public SourceSensorActuator setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	@Override
	public String toString() {
		return "SourceSensorActuator [sourceUuid=" + sourceUuid + ", sensorUuid=" + sensorUuid + ", actuatorUuid="
				+ actuatorUuid + ", actuatorValue=" + actuatorValue + ", desc=" + desc + "]";
	}
	
}
