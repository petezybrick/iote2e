package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class LoginSourceSensorActuator {
	@Expose
	private String loginUuid;
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
	@Expose
	private String actuatorValueUpdatedAt;

	public LoginSourceSensorActuator() {
		
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

	public LoginSourceSensorActuator setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
		return this;
	}

	public LoginSourceSensorActuator setSensorUuid(String sensorUuid) {
		this.sensorUuid = sensorUuid;
		return this;
	}

	public LoginSourceSensorActuator setActuatorUuid(String actuatorUuid) {
		this.actuatorUuid = actuatorUuid;
		return this;
	}

	public LoginSourceSensorActuator setActuatorValue(String actuatorValue) {
		this.actuatorValue = actuatorValue;
		return this;
	}

	public LoginSourceSensorActuator setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getActuatorValueUpdatedAt() {
		return actuatorValueUpdatedAt;
	}

	public LoginSourceSensorActuator setActuatorValueUpdatedAt(String actuatorValueUpdatedAt) {
		this.actuatorValueUpdatedAt = actuatorValueUpdatedAt;
		return this;
	}

	@Override
	public String toString() {
		return "LoginSourceSensorActuator [loginUuid=" + loginUuid + ", sourceUuid=" + sourceUuid + ", sensorUuid="
				+ sensorUuid + ", actuatorUuid=" + actuatorUuid + ", actuatorValue=" + actuatorValue + ", desc=" + desc
				+ ", actuatorValueUpdatedAt=" + actuatorValueUpdatedAt + "]";
	}

	public String getLoginUuid() {
		return loginUuid;
	}

	public LoginSourceSensorActuator setLoginUuid(String loginUuid) {
		this.loginUuid = loginUuid;
		return this;
	}
	
}
