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

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class RuleConfig.
 */
@Generated("org.jsonschema2pojo")
public class RuleConfig implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5229495159598744855L;
	
	/** The actuator state key. */
	@Expose 
	private String actuatorStateKey;
	
	/** The rule login source sensor key. */
	@Expose 
	private String ruleLoginSourceSensorKey;
	
	/** The rule def item key. */
	@Expose 
	private String ruleDefItemKey;
	
	/** The source response ignite cache name. */
	@Expose
	private String sourceResponseIgniteCacheName;
	
	/** The source response ignite config file. */
	@Expose
	private String sourceResponseIgniteConfigFile;
	
	/** The source response ignite config name. */
	@Expose
	private String sourceResponseIgniteConfigName;
	
	/** The ignite client mode. */
	@Expose
	private boolean igniteClientMode;
	
	/** The force refresh actuator state. */
	@Expose
	private boolean forceRefreshActuatorState;
	
	/** The force reset actuator state. */
	@Expose
	private boolean forceResetActuatorState;
	
	/**
	 * Instantiates a new rule config.
	 */
	public RuleConfig() {
		
	}

	/**
	 * Gets the actuator state key.
	 *
	 * @return the actuator state key
	 */
	public String getActuatorStateKey() {
		return actuatorStateKey;
	}

	/**
	 * Sets the actuator state key.
	 *
	 * @param actuatorStateKey the actuator state key
	 * @return the rule config
	 */
	public RuleConfig setActuatorStateKey(String actuatorStateKey) {
		this.actuatorStateKey = actuatorStateKey;
		return this;
	}

	/**
	 * Gets the rule login source sensor key.
	 *
	 * @return the rule login source sensor key
	 */
	public String getRuleLoginSourceSensorKey() {
		return ruleLoginSourceSensorKey;
	}

	/**
	 * Gets the rule def item key.
	 *
	 * @return the rule def item key
	 */
	public String getRuleDefItemKey() {
		return ruleDefItemKey;
	}

	/**
	 * Sets the rule login source sensor key.
	 *
	 * @param jsonFileRuleSourceSensor the json file rule source sensor
	 * @return the rule config
	 */
	public RuleConfig setRuleLoginSourceSensorKey(String jsonFileRuleSourceSensor) {
		this.ruleLoginSourceSensorKey = jsonFileRuleSourceSensor;
		return this;
	}

	/**
	 * Sets the rule def item key.
	 *
	 * @param ruleDefItemKey the rule def item key
	 * @return the rule config
	 */
	public RuleConfig setRuleDefItemKey(String ruleDefItemKey) {
		this.ruleDefItemKey = ruleDefItemKey;
		return this;
	}

	/**
	 * Gets the source response ignite cache name.
	 *
	 * @return the source response ignite cache name
	 */
	public String getSourceResponseIgniteCacheName() {
		return sourceResponseIgniteCacheName;
	}

	/**
	 * Sets the source response ignite cache name.
	 *
	 * @param sourceResponseIgniteCacheName the source response ignite cache name
	 * @return the rule config
	 */
	public RuleConfig setSourceResponseIgniteCacheName(String sourceResponseIgniteCacheName) {
		this.sourceResponseIgniteCacheName = sourceResponseIgniteCacheName;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleConfig [actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey="
				+ ruleLoginSourceSensorKey + ", ruleDefItemKey=" + ruleDefItemKey + ", sourceResponseIgniteCacheName="
				+ sourceResponseIgniteCacheName + ", sourceResponseIgniteConfigFile=" + sourceResponseIgniteConfigFile
				+ ", sourceResponseIgniteConfigName=" + sourceResponseIgniteConfigName + ", igniteClientMode="
				+ igniteClientMode + ", forceRefreshActuatorState=" + forceRefreshActuatorState
				+ ", forceResetActuatorState=" + forceResetActuatorState + "]";
	}

	/**
	 * Gets the source response ignite config name.
	 *
	 * @return the source response ignite config name
	 */
	public String getSourceResponseIgniteConfigName() {
		return sourceResponseIgniteConfigName;
	}

	/**
	 * Sets the source response ignite config name.
	 *
	 * @param sourceResponseIgniteConfigName the source response ignite config name
	 * @return the rule config
	 */
	public RuleConfig setSourceResponseIgniteConfigName(String sourceResponseIgniteConfigName) {
		this.sourceResponseIgniteConfigName = sourceResponseIgniteConfigName;
		return this;
	}

	/**
	 * Gets the source response ignite config file.
	 *
	 * @return the source response ignite config file
	 */
	public String getSourceResponseIgniteConfigFile() {
		return sourceResponseIgniteConfigFile;
	}

	/**
	 * Sets the source response ignite config file.
	 *
	 * @param sourceResponseIgniteConfigFile the source response ignite config file
	 * @return the rule config
	 */
	public RuleConfig setSourceResponseIgniteConfigFile(String sourceResponseIgniteConfigFile) {
		this.sourceResponseIgniteConfigFile = sourceResponseIgniteConfigFile;
		return this;
	}

	/**
	 * Checks if is ignite client mode.
	 *
	 * @return true, if is ignite client mode
	 */
	public boolean isIgniteClientMode() {
		return igniteClientMode;
	}

	/**
	 * Sets the ignite client mode.
	 *
	 * @param igniteClientMode the ignite client mode
	 * @return the rule config
	 */
	public RuleConfig setIgniteClientMode(boolean igniteClientMode) {
		this.igniteClientMode = igniteClientMode;
		return this;
	}

	/**
	 * Checks if is force refresh actuator state.
	 *
	 * @return true, if is force refresh actuator state
	 */
	public boolean isForceRefreshActuatorState() {
		return forceRefreshActuatorState;
	}

	/**
	 * Sets the force refresh actuator state.
	 *
	 * @param forceRefreshActuatorState the force refresh actuator state
	 * @return the rule config
	 */
	public RuleConfig setForceRefreshActuatorState(boolean forceRefreshActuatorState) {
		this.forceRefreshActuatorState = forceRefreshActuatorState;
		return this;
	}

	/**
	 * Checks if is force reset actuator state.
	 *
	 * @return true, if is force reset actuator state
	 */
	public boolean isForceResetActuatorState() {
		return forceResetActuatorState;
	}

	/**
	 * Sets the force reset actuator state.
	 *
	 * @param forceResetActuatorState the force reset actuator state
	 * @return the rule config
	 */
	public RuleConfig setForceResetActuatorState(boolean forceResetActuatorState) {
		this.forceResetActuatorState = forceResetActuatorState;
		return this;
	}

}
