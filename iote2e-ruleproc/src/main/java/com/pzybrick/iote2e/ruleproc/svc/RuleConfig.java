package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleConfig {
	@Expose 
	private String jsonFileLoginSourceSensorActuator;
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
	
	public RuleConfig() {
		
	}

	public String getJsonFileLoginSourceSensorActuator() {
		return jsonFileLoginSourceSensorActuator;
	}

	public RuleConfig setJsonFileLoginSourceSensorActuator(String jsonFilePathNameExt) {
		this.jsonFileLoginSourceSensorActuator = jsonFilePathNameExt;
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
		return "RuleConfig [jsonFileSourceSensorActuator=" + jsonFileLoginSourceSensorActuator
				+ ", jsonFileRuleSourceSensor=" + jsonFileRuleLoginSourceSensor + ", jsonFileRuleDefItem="
				+ jsonFileRuleDefItem + ", sourceResponseIgniteCacheName=" + sourceResponseIgniteCacheName
				+ ", sourceResponseIgniteConfigFile=" + sourceResponseIgniteConfigFile
				+ ", sourceResponseIgniteConfigName=" + sourceResponseIgniteConfigName + "]";
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

}
