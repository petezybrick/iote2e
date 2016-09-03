package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleSourceSensor {
	@Expose
	private String sourceUuid;
	@Expose
	private String sensorUuid;
	@Expose
	private String ruleUuid;
	@Expose
	private String desc;
	
	public RuleSourceSensor() {
		super();
	}

	public String getSensorUuid() {
		return sensorUuid;
	}

	public String getRuleUuid() {
		return ruleUuid;
	}

	public RuleSourceSensor setSensorUuid(String sensorUuid) {
		this.sensorUuid = sensorUuid;
		return this;
	}

	public RuleSourceSensor setRuleUuid(String ruleUuid) {
		this.ruleUuid = ruleUuid;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public RuleSourceSensor setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getSourceUuid() {
		return sourceUuid;
	}

	public RuleSourceSensor setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
		return this;
	}

	@Override
	public String toString() {
		return "RuleSourceSensor [sourceUuid=" + sourceUuid + ", sensorUuid=" + sensorUuid + ", ruleUuid=" + ruleUuid
				+ ", desc=" + desc + "]";
	}
	
	

}
