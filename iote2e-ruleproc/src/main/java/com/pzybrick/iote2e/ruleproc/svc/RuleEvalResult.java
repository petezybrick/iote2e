package com.pzybrick.iote2e.ruleproc.svc;

public class RuleEvalResult {
	private boolean success;
	private String actuatorTargetValue;
	private SourceSensorActuator sourceSensorActuator;

	public RuleEvalResult() {
		this.success = false;
		this.sourceSensorActuator = null;
		this.actuatorTargetValue = null;
	}

	public RuleEvalResult(boolean success, SourceSensorActuator sourceSensorActuator) {
		this.success = success;
		this.sourceSensorActuator = sourceSensorActuator;
		this.actuatorTargetValue = null;
	}

	public boolean isSuccess() {
		return success;
	}

	public SourceSensorActuator getSourceSensorActuator() {
		return sourceSensorActuator;
	}

	public RuleEvalResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public RuleEvalResult setSourceSensorActuator(SourceSensorActuator sourceSensorActuator) {
		this.sourceSensorActuator = sourceSensorActuator;
		return this;
	}

	@Override
	public String toString() {
		return "RuleEvalResult [success=" + success + ", actuatorTargetValue=" + actuatorTargetValue
				+ ", sourceSensorActuator=" + sourceSensorActuator + "]";
	}

	public String getActuatorTargetValue() {
		return actuatorTargetValue;
	}

	public RuleEvalResult setActuatorTargetValue(String actuatorTargetValue) {
		this.actuatorTargetValue = actuatorTargetValue;
		return this;
	}

}