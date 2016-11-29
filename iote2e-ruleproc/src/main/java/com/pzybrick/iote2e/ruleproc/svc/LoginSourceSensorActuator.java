package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class LoginSourceSensorActuator {
	@Expose
	private String loginName;
	@Expose
	private String sourceName;
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

	public String getSourceName() {
		return sourceName;
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

	public LoginSourceSensorActuator setSourceName(String sourceName) {
		this.sourceName = sourceName;
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
		return "LoginSourceSensorActuator [loginName=" + loginName + ", sourceName=" + sourceName + ", sensorName="
				+ sensorName + ", actuatorName=" + actuatorName + ", actuatorValue=" + actuatorValue + ", desc=" + desc
				+ ", actuatorValueUpdatedAt=" + actuatorValueUpdatedAt + "]";
	}

	public String getLoginName() {
		return loginName;
	}

	public LoginSourceSensorActuator setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}
	
}
