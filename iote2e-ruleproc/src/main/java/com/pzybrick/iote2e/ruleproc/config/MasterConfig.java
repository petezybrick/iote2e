package com.pzybrick.iote2e.ruleproc.config;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class MasterConfig {
	@Expose
	private String ruleSvcClassName;
	@Expose
	private String requestSvcClassName;
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
	

	public MasterConfig() {
		super();
	}
	
	public String getRuleSvcClassName() {
		return ruleSvcClassName;
	}
	public String getRequestSvcClassName() {
		return requestSvcClassName;
	}
	public String getActuatorStateKey() {
		return actuatorStateKey;
	}
	public String getRuleLoginSourceSensorKey() {
		return ruleLoginSourceSensorKey;
	}
	public String getRuleDefItemKey() {
		return ruleDefItemKey;
	}
	public String getSourceResponseIgniteCacheName() {
		return sourceResponseIgniteCacheName;
	}
	public String getSourceResponseIgniteConfigFile() {
		return sourceResponseIgniteConfigFile;
	}
	public String getSourceResponseIgniteConfigName() {
		return sourceResponseIgniteConfigName;
	}
	public boolean isIgniteClientMode() {
		return igniteClientMode;
	}
	public boolean isForceRefreshActuatorState() {
		return forceRefreshActuatorState;
	}
	public boolean isForceResetActuatorState() {
		return forceResetActuatorState;
	}
	public MasterConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}
	public MasterConfig setRequestSvcClassName(String requestSvcClassName) {
		this.requestSvcClassName = requestSvcClassName;
		return this;
	}
	public MasterConfig setActuatorStateKey(String actuatorStateKey) {
		this.actuatorStateKey = actuatorStateKey;
		return this;
	}
	public MasterConfig setRuleLoginSourceSensorKey(String ruleLoginSourceSensorKey) {
		this.ruleLoginSourceSensorKey = ruleLoginSourceSensorKey;
		return this;
	}
	public MasterConfig setRuleDefItemKey(String ruleDefItemKey) {
		this.ruleDefItemKey = ruleDefItemKey;
		return this;
	}
	public MasterConfig setSourceResponseIgniteCacheName(String sourceResponseIgniteCacheName) {
		this.sourceResponseIgniteCacheName = sourceResponseIgniteCacheName;
		return this;
	}
	public MasterConfig setSourceResponseIgniteConfigFile(String sourceResponseIgniteConfigFile) {
		this.sourceResponseIgniteConfigFile = sourceResponseIgniteConfigFile;
		return this;
	}
	public MasterConfig setSourceResponseIgniteConfigName(String sourceResponseIgniteConfigName) {
		this.sourceResponseIgniteConfigName = sourceResponseIgniteConfigName;
		return this;
	}
	public MasterConfig setIgniteClientMode(boolean igniteClientMode) {
		this.igniteClientMode = igniteClientMode;
		return this;
	}
	public MasterConfig setForceRefreshActuatorState(boolean forceRefreshActuatorState) {
		this.forceRefreshActuatorState = forceRefreshActuatorState;
		return this;
	}
	public MasterConfig setForceResetActuatorState(boolean forceResetActuatorState) {
		this.forceResetActuatorState = forceResetActuatorState;
		return this;
	}

}
