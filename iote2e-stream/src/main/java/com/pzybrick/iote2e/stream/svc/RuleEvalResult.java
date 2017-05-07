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
	private Map<CharSequence,CharSequence> metadata;
	private boolean updateActuatorState;
	private boolean useLongIgniteKey;

	public RuleEvalResult() {
		this.sensorName = null;
		this.ruleActuatorHit = false;
		this.actuatorState = null;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = false;
		this.useLongIgniteKey = false;
	}

	public RuleEvalResult(boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = null;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = true;
		this.useLongIgniteKey = true;
	}

	public RuleEvalResult(String sensorName, boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = true;
		this.useLongIgniteKey = true;
	}

	public RuleEvalResult(String sensorName, String actuatorTargetValue, Map<CharSequence,CharSequence> metadata) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = true;
		this.actuatorState = null;
		this.actuatorTargetValue = actuatorTargetValue;
		this.metadata = metadata;
		this.updateActuatorState = false;
		this.useLongIgniteKey = false;
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
				+ ", actuatorTargetValue=" + actuatorTargetValue + ", actuatorState=" + actuatorState + ", metadata="
				+ metadata + "]";
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

	public Map<CharSequence, CharSequence> getMetadata() {
		return metadata;
	}

	public RuleEvalResult setActuatorState(ActuatorState actuatorState) {
		this.actuatorState = actuatorState;
		return this;
	}

	public RuleEvalResult setMetadata(Map<CharSequence, CharSequence> metadata) {
		this.metadata = metadata;
		return this;
	}

	public boolean isUpdateActuatorState() {
		return updateActuatorState;
	}

	public boolean isUseLongIgniteKey() {
		return useLongIgniteKey;
	}

	public RuleEvalResult setUpdateActuatorState(boolean updateActuatorState) {
		this.updateActuatorState = updateActuatorState;
		return this;
	}

	public RuleEvalResult setUseLongIgniteKey(boolean useLongIgniteKey) {
		this.useLongIgniteKey = useLongIgniteKey;
		return this;
	}

}