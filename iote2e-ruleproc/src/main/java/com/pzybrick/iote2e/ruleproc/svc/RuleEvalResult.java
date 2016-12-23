package com.pzybrick.iote2e.ruleproc.svc;

import java.io.Serializable;

public class RuleEvalResult implements Serializable {
	private static final long serialVersionUID = -8181825861935306007L;
	private String sensorName;
	private boolean ruleActuatorHit;
	private String actuatorTargetValue;
	private LoginSourceSensorActuator sourceSensorActuator;

	public RuleEvalResult() {
		this.sensorName = null;
		this.ruleActuatorHit = false;
		this.sourceSensorActuator = null;
		this.actuatorTargetValue = null;
	}

	public RuleEvalResult(boolean ruleActuatorHit, LoginSourceSensorActuator sourceSensorActuator) {
		this.sensorName = null;
		this.ruleActuatorHit = ruleActuatorHit;
		this.sourceSensorActuator = sourceSensorActuator;
		this.actuatorTargetValue = null;
	}

	public RuleEvalResult(String sensorName, boolean ruleActuatorHit, LoginSourceSensorActuator sourceSensorActuator) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = ruleActuatorHit;
		this.sourceSensorActuator = sourceSensorActuator;
		this.actuatorTargetValue = null;
	}

	public boolean isRuleActuatorHit() {
		return ruleActuatorHit;
	}

	public LoginSourceSensorActuator getSourceSensorActuator() {
		return sourceSensorActuator;
	}

	public RuleEvalResult setRuleActuatorHit(boolean ruleActuatorHit) {
		this.ruleActuatorHit = ruleActuatorHit;
		return this;
	}

	public RuleEvalResult setSourceSensorActuator(LoginSourceSensorActuator sourceSensorActuator) {
		this.sourceSensorActuator = sourceSensorActuator;
		return this;
	}

	@Override
	public String toString() {
		return "RuleEvalResult [sensorName=" + sensorName + ", ruleActuatorHit=" + ruleActuatorHit
				+ ", actuatorTargetValue=" + actuatorTargetValue + ", sourceSensorActuator=" + sourceSensorActuator
				+ "]";
	}

	public String getActuatorTargetValue() {
		return actuatorTargetValue;
	}

	public RuleEvalResult setActuatorTargetValue(String actuatorTargetValue) {
		this.actuatorTargetValue = actuatorTargetValue;
		return this;
	}

	public String getSensorName() {
		return sensorName;
	}

	public RuleEvalResult setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

}