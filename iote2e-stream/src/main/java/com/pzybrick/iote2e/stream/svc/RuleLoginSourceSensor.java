package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleLoginSourceSensor implements Serializable {
	private static final long serialVersionUID = 7594284561642969987L;
	@Expose
	private String loginName;
	@Expose
	private String sourceName;
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

	public String getRuleName() {
		return ruleUuid;
	}

	public RuleLoginSourceSensor setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	public RuleLoginSourceSensor setRuleName(String ruleName) {
		this.ruleUuid = ruleName;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public RuleLoginSourceSensor setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getSourceName() {
		return sourceName;
	}

	public RuleLoginSourceSensor setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	@Override
	public String toString() {
		return "RuleLoginSourceSensor [loginName=" + loginName + ", sourceName=" + sourceName + ", sensorName="
				+ sensorName + ", ruleName=" + ruleUuid + ", desc=" + desc + "]";
	}

	public String getLoginName() {
		return loginName;
	}

	public RuleLoginSourceSensor setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	@Override
	protected RuleLoginSourceSensor clone() throws CloneNotSupportedException {
		RuleLoginSourceSensor clone = new RuleLoginSourceSensor();
		clone.loginName = this.loginName;
		clone.sourceName = sourceName;
		clone.sensorName = sensorName;
		clone.ruleUuid = ruleUuid;
		clone.desc = desc;
		return clone;
	}
	
	

}
