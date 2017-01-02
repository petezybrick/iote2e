package com.pzybrick.iote2e.ruleproc.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleConfig implements Serializable {
	private static final long serialVersionUID = -5229495159598744855L;
	@Expose 
	private String jsonFileActuatorState;
	@Expose 
	private String jsonFileRuleLoginSourceSensor;
	@Expose 
	private String jsonFileRuleDefItem;
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

	public String getJsonFileActuatorState() {
		return jsonFileActuatorState;
	}

	public RuleConfig setJsonFileActuatorState(String jsonFileActuatorState) {
		this.jsonFileActuatorState = jsonFileActuatorState;
		return this;
	}

	public String getJsonFileRuleLoginSourceSensor() {
		return jsonFileRuleLoginSourceSensor;
	}

	public String getJsonFileRuleDefItem() {
		return jsonFileRuleDefItem;
	}

	public RuleConfig setJsonFileRuleLoginSourceSensor(String jsonFileRuleSourceSensor) {
		this.jsonFileRuleLoginSourceSensor = jsonFileRuleSourceSensor;
		return this;
	}

	public RuleConfig setJsonFileRuleDefItem(String jsonFileRuleDefItem) {
		this.jsonFileRuleDefItem = jsonFileRuleDefItem;
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
		return "RuleConfig [jsonFileActuatorState=" + jsonFileActuatorState + ", jsonFileRuleLoginSourceSensor="
				+ jsonFileRuleLoginSourceSensor + ", jsonFileRuleDefItem=" + jsonFileRuleDefItem
				+ ", sourceResponseIgniteCacheName=" + sourceResponseIgniteCacheName
				+ ", sourceResponseIgniteConfigFile=" + sourceResponseIgniteConfigFile
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
