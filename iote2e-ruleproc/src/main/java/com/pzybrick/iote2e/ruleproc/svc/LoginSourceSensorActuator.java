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
	private String sensorName;
	@Expose
	private String actuatorName;
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

	public String getSensorName() {
		return sensorName;
	}

	public String getActuatorName() {
		return actuatorName;
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

	public LoginSourceSensorActuator setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	public LoginSourceSensorActuator setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
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
		return "LoginSourceSensorActuator [loginUuid=" + loginUuid + ", sourceUuid=" + sourceUuid + ", sensorName="
				+ sensorName + ", actuatorName=" + actuatorName + ", actuatorValue=" + actuatorValue + ", desc=" + desc
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
