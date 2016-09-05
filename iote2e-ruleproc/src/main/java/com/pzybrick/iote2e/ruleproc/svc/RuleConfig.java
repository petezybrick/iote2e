package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleConfig {
	@Expose 
	private String jsonFileSourceSensorActuator;
	@Expose 
	private String jsonFileRuleSourceSensor;
	@Expose 
	private String jsonFileRuleDefItem;
	@Expose
	private String sourceResponseIgniteCacheName;
	@Expose
	private String sourceResponseIgniteConfigFile;
	@Expose
	private String sourceResponseIgniteConfigName;
	
	public RuleConfig() {
		
	}

	public String getJsonFileSourceSensorActuator() {
		return jsonFileSourceSensorActuator;
	}

	public RuleConfig setJsonFileSourceSensorActuator(String jsonFilePathNameExt) {
		this.jsonFileSourceSensorActuator = jsonFilePathNameExt;
		return this;
	}

	public String getJsonFileRuleSourceSensor() {
		return jsonFileRuleSourceSensor;
	}

	public String getJsonFileRuleDefItem() {
		return jsonFileRuleDefItem;
	}

	public RuleConfig setJsonFileRuleSourceSensor(String jsonFileRuleSourceSensor) {
		this.jsonFileRuleSourceSensor = jsonFileRuleSourceSensor;
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
		return "RuleConfig [jsonFileSourceSensorActuator=" + jsonFileSourceSensorActuator
				+ ", jsonFileRuleSourceSensor=" + jsonFileRuleSourceSensor + ", jsonFileRuleDefItem="
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

}
