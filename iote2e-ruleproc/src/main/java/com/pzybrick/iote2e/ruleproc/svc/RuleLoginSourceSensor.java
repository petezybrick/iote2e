package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleLoginSourceSensor {
	@Expose
	private String loginUuid;
	@Expose
	private String sourceUuid;
	@Expose
	private String sensorName;
	@Expose
	private String ruleUuid;
	@Expose
	private String desc;
	
	public RuleLoginSourceSensor() {
		super();
	}

	public String getSensorName() {
		return sensorName;
	}

	public String getRuleUuid() {
		return ruleUuid;
	}

	public RuleLoginSourceSensor setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	public RuleLoginSourceSensor setRuleUuid(String ruleUuid) {
		this.ruleUuid = ruleUuid;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public RuleLoginSourceSensor setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getSourceUuid() {
		return sourceUuid;
	}

	public RuleLoginSourceSensor setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
		return this;
	}

	@Override
	public String toString() {
		return "RuleLoginSourceSensor [loginUuid=" + loginUuid + ", sourceUuid=" + sourceUuid + ", sensorName="
				+ sensorName + ", ruleUuid=" + ruleUuid + ", desc=" + desc + "]";
	}

	public String getLoginUuid() {
		return loginUuid;
	}

	public RuleLoginSourceSensor setLoginUuid(String loginUuid) {
		this.loginUuid = loginUuid;
		return this;
	}
	
	

}
