package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleEvalResult implements Serializable {
	private static final long serialVersionUID = -8181825861935306007L;
	private String sensorName;
	private boolean ruleActuatorHit;
	private String actuatorTargetValue;
	private ActuatorState actuatorState;
	private Map<String,String> metadata;

	public RuleEvalResult() {
		this.sensorName = null;
		this.ruleActuatorHit = false;
		this.actuatorState = null;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<String,String>();
	}

	public RuleEvalResult(boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = null;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<String,String>();
	}

	public RuleEvalResult(String sensorName, boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<String,String>();
	}

	public RuleEvalResult(String sensorName, String actuatorTargetValue, Map<String,String> metadata) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = true;
		this.actuatorState = null;
		this.actuatorTargetValue = actuatorTargetValue;
		this.metadata = metadata;
	}

	public boolean isRuleActuatorHit() {
		return ruleActuatorHit;
	}

	public ActuatorState getActuatorState() {
		return actuatorState;
	}

	public RuleEvalResult setRuleActuatorHit(boolean ruleActuatorHit) {
		this.ruleActuatorHit = ruleActuatorHit;
		return this;
	}

	public RuleEvalResult setSourceSensorActuator(ActuatorState actuatorState) {
		this.actuatorState = actuatorState;
		return this;
	}

	@Override
	public String toString() {
		return "RuleEvalResult [sensorName=" + sensorName + ", ruleActuatorHit=" + ruleActuatorHit
				+ ", actuatorTargetValue=" + actuatorTargetValue + ", actuatorState=" + actuatorState
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