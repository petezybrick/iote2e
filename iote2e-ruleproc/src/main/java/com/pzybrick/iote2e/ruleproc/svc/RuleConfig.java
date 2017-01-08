package com.pzybrick.iote2e.ruleproc.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleConfig implements Serializable {
	private static final long serialVersionUID = -5229495159598744855L;
	@Expose 
	private String actuatorStateKey;
	@Expose 
	private String ruleLoginSourceSensorKey;
	@Expose 
	private String ruleDefItemKey;
	@Expose
	private String sourceResponseIgniteCacheName;
	@Expose
	private String sourceResponseIgniteConfigFile;
	@Expose
	private String sourceResponseIgniteConfigName;
	@Expose
	private boolean igniteClientMode;
	@Expose
	private boolean forceRefreshActuatorState;
	@Expose
	private boolean forceResetActuatorState;
	
	public RuleConfig() {
		
	}

	public String getActuatorStateKey() {
		return actuatorStateKey;
	}

	public RuleConfig setActuatorStateKey(String actuatorStateKey) {
		this.actuatorStateKey = actuatorStateKey;
		return this;
	}

	public String getRuleLoginSourceSensorKey() {
		return ruleLoginSourceSensorKey;
	}

	public String getRuleDefItemKey() {
		return ruleDefItemKey;
	}

	public RuleConfig setRuleLoginSourceSensorKey(String jsonFileRuleSourceSensor) {
		this.ruleLoginSourceSensorKey = jsonFileRuleSourceSensor;
		return this;
	}

	public RuleConfig setRuleDefItemKey(String ruleDefItemKey) {
		this.ruleDefItemKey = ruleDefItemKey;
		return this;
	}

	public String getSourceResponseIgniteCacheName() {
		return sourceResponseIgniteCacheName;
	}

	public RuleConfig setSourceResponseIgniteCacheName(String sourceResponseIgniteCacheName) {
		this.sourceResponseIgniteCacheName = sourceResponseIgniteCacheName;
		return this;
	}

	@Override
	public String toString() {
		return "RuleConfig [actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey="
				+ ruleLoginSourceSensorKey + ", ruleDefItemKey=" + ruleDefItemKey + ", sourceResponseIgniteCacheName="
				+ sourceResponseIgniteCacheName + ", sourceResponseIgniteConfigFile=" + sourceResponseIgniteConfigFile
				+ ", sourceResponseIgniteConfigName=" + sourceResponseIgniteConfigName + ", igniteClientMode="
				+ igniteClientMode + ", forceRefreshActuatorState=" + forceRefreshActuatorState
				+ ", forceResetActuatorState=" + forceResetActuatorState + "]";
	}

	public String getSourceResponseIgniteConfigName() {
		return sourceResponseIgniteConfigName;
	}

	public RuleConfig setSourceResponseIgniteConfigName(String sourceResponseIgniteConfigName) {
		this.sourceResponseIgniteConfigName = sourceResponseIgniteConfigName;
		return this;
	}

	public String getSourceResponseIgniteConfigFile() {
		return sourceResponseIgniteConfigFile;
	}

	public RuleConfig setSourceResponseIgniteConfigFile(String sourceResponseIgniteConfigFile) {
		this.sourceResponseIgniteConfigFile = sourceResponseIgniteConfigFile;
		return this;
	}

	public boolean isIgniteClientMode() {
		return igniteClientMode;
	}

	public RuleConfig setIgniteClientMode(boolean igniteClientMode) {
		this.igniteClientMode = igniteClientMode;
		return this;
	}

	public boolean isForceRefreshActuatorState() {
		return forceRefreshActuatorState;
	}

	public RuleConfig setForceRefreshActuatorState(boolean forceRefreshActuatorState) {
		this.forceRefreshActuatorState = forceRefreshActuatorState;
		return this;
	}

	public boolean isForceResetActuatorState() {
		return forceResetActuatorState;
	}

	public RuleConfig setForceResetActuatorState(boolean forceResetActuatorState) {
		this.forceResetActuatorState = forceResetActuatorState;
		return this;
	}

}
