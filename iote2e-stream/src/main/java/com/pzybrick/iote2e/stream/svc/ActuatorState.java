package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class ActuatorState implements Serializable {
	private static final long serialVersionUID = 5180186654964075339L;
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
	private String actuatorDesc;
	@Expose
	private String actuatorValueUpdatedAt;
	
	private String pk;

	public ActuatorState() {
		
	}
	
	public String getPk() {
		if( pk != null ) return pk;
		pk = String.format( "%s|%s|%s", loginName,sourceName,sensorName);
		return pk;
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

	public String getActuatorDesc() {
		return actuatorDesc;
	}

	public ActuatorState setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public ActuatorState setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	public ActuatorState setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
		return this;
	}

	public ActuatorState setActuatorValue(String actuatorValue) {
		this.actuatorValue = actuatorValue;
		return this;
	}

	public ActuatorState setActuatorDesc(String actuatorDesc) {
		this.actuatorDesc = actuatorDesc;
		return this;
	}

	public String getActuatorValueUpdatedAt() {
		return actuatorValueUpdatedAt;
	}

	public ActuatorState setActuatorValueUpdatedAt(String actuatorValueUpdatedAt) {
		this.actuatorValueUpdatedAt = actuatorValueUpdatedAt;
		return this;
	}

	@Override
	public String toString() {
		return "ActuatorState [loginName=" + loginName + ", sourceName=" + sourceName + ", sensorName=" + sensorName
				+ ", actuatorName=" + actuatorName + ", actuatorValue=" + actuatorValue + ", desc=" + actuatorDesc
				+ ", actuatorValueUpdatedAt=" + actuatorValueUpdatedAt + "]";
	}

	public String getLoginName() {
		return loginName;
	}

	public ActuatorState setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}
	
}
