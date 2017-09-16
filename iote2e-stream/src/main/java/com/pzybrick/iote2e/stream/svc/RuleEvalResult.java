/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * The Class RuleEvalResult.
 */
public class RuleEvalResult implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8181825861935306007L;
	
	/** The sensor name. */
	private String sensorName;
	
	/** The rule actuator hit. */
	private boolean ruleActuatorHit;
	
	/** The actuator target value. */
	private String actuatorTargetValue;
	
	/** The actuator state. */
	private ActuatorState actuatorState;
	
	/** The metadata. */
	private Map<CharSequence,CharSequence> metadata;
	
	/** The update actuator state. */
	private boolean updateActuatorState;
	
	/** The use long ignite key. */
	private boolean useLongIgniteKey;

	/**
	 * Instantiates a new rule eval result.
	 */
	public RuleEvalResult() {
		this.sensorName = null;
		this.ruleActuatorHit = false;
		this.actuatorState = null;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = false;
		this.useLongIgniteKey = false;
	}

	/**
	 * Instantiates a new rule eval result.
	 *
	 * @param ruleActuatorHit the rule actuator hit
	 * @param actuatorState the actuator state
	 */
	public RuleEvalResult(boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = null;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = true;
		this.useLongIgniteKey = true;
	}

	/**
	 * Instantiates a new rule eval result.
	 *
	 * @param sensorName the sensor name
	 * @param ruleActuatorHit the rule actuator hit
	 * @param actuatorState the actuator state
	 */
	public RuleEvalResult(String sensorName, boolean ruleActuatorHit, ActuatorState actuatorState) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = ruleActuatorHit;
		this.actuatorState = actuatorState;
		this.actuatorTargetValue = null;
		this.metadata = new HashMap<CharSequence,CharSequence>();
		this.updateActuatorState = true;
		this.useLongIgniteKey = true;
	}

	/**
	 * Instantiates a new rule eval result.
	 *
	 * @param sensorName the sensor name
	 * @param actuatorTargetValue the actuator target value
	 * @param metadata the metadata
	 */
	public RuleEvalResult(String sensorName, String actuatorTargetValue, Map<CharSequence,CharSequence> metadata) {
		this.sensorName = sensorName;
		this.ruleActuatorHit = true;
		this.actuatorState = null;
		this.actuatorTargetValue = actuatorTargetValue;
		this.metadata = metadata;
		this.updateActuatorState = false;
		this.useLongIgniteKey = false;
	}

	/**
	 * Checks if is rule actuator hit.
	 *
	 * @return true, if is rule actuator hit
	 */
	public boolean isRuleActuatorHit() {
		return ruleActuatorHit;
	}

	/**
	 * Gets the actuator state.
	 *
	 * @return the actuator state
	 */
	public ActuatorState getActuatorState() {
		return actuatorState;
	}

	/**
	 * Sets the rule actuator hit.
	 *
	 * @param ruleActuatorHit the rule actuator hit
	 * @return the rule eval result
	 */
	public RuleEvalResult setRuleActuatorHit(boolean ruleActuatorHit) {
		this.ruleActuatorHit = ruleActuatorHit;
		return this;
	}

	/**
	 * Sets the source sensor actuator.
	 *
	 * @param actuatorState the actuator state
	 * @return the rule eval result
	 */
	public RuleEvalResult setSourceSensorActuator(ActuatorState actuatorState) {
		this.actuatorState = actuatorState;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleEvalResult [sensorName=" + sensorName + ", ruleActuatorHit=" + ruleActuatorHit
				+ ", actuatorTargetValue=" + actuatorTargetValue + ", actuatorState=" + actuatorState + ", metadata="
				+ metadata + "]";
	}

	/**
	 * Gets the actuator target value.
	 *
	 * @return the actuator target value
	 */
	public String getActuatorTargetValue() {
		return actuatorTargetValue;
	}

	/**
	 * Sets the actuator target value.
	 *
	 * @param actuatorTargetValue the actuator target value
	 * @return the rule eval result
	 */
	public RuleEvalResult setActuatorTargetValue(String actuatorTargetValue) {
		this.actuatorTargetValue = actuatorTargetValue;
		return this;
	}

	/**
	 * Gets the sensor name.
	 *
	 * @return the sensor name
	 */
	public String getSensorName() {
		return sensorName;
	}

	/**
	 * Sets the sensor name.
	 *
	 * @param sensorName the sensor name
	 * @return the rule eval result
	 */
	public RuleEvalResult setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	/**
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public Map<CharSequence, CharSequence> getMetadata() {
		return metadata;
	}

	/**
	 * Sets the actuator state.
	 *
	 * @param actuatorState the actuator state
	 * @return the rule eval result
	 */
	public RuleEvalResult setActuatorState(ActuatorState actuatorState) {
		this.actuatorState = actuatorState;
		return this;
	}

	/**
	 * Sets the metadata.
	 *
	 * @param metadata the metadata
	 * @return the rule eval result
	 */
	public RuleEvalResult setMetadata(Map<CharSequence, CharSequence> metadata) {
		this.metadata = metadata;
		return this;
	}

	/**
	 * Checks if is update actuator state.
	 *
	 * @return true, if is update actuator state
	 */
	public boolean isUpdateActuatorState() {
		return updateActuatorState;
	}

	/**
	 * Checks if is use long ignite key.
	 *
	 * @return true, if is use long ignite key
	 */
	public boolean isUseLongIgniteKey() {
		return useLongIgniteKey;
	}

	/**
	 * Sets the update actuator state.
	 *
	 * @param updateActuatorState the update actuator state
	 * @return the rule eval result
	 */
	public RuleEvalResult setUpdateActuatorState(boolean updateActuatorState) {
		this.updateActuatorState = updateActuatorState;
		return this;
	}

	/**
	 * Sets the use long ignite key.
	 *
	 * @param useLongIgniteKey the use long ignite key
	 * @return the rule eval result
	 */
	public RuleEvalResult setUseLongIgniteKey(boolean useLongIgniteKey) {
		this.useLongIgniteKey = useLongIgniteKey;
		return this;
	}

}