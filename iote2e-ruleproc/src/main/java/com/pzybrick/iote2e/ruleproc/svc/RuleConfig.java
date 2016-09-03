package com.pzybrick.iote2e.ruleproc.svc;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleConfig {
	@Expose 
	private String sourceSensorActuatorSvcClassname;
	@Expose 
	private String jsonFileSourceSensorActuator;
	@Expose 
	private String jsonFileRuleSourceSensor;
	@Expose 
	private String jsonFileRuleDefItem;
	
	public RuleConfig() {
		
	}

	public String getSourceSensorActuatorSvcClassname() {
		return sourceSensorActuatorSvcClassname;
	}

	public RuleConfig setSourceSensorActuatorSvcClassname(String sourceSensorAcuatorSvcClassname) {
		this.sourceSensorActuatorSvcClassname = sourceSensorAcuatorSvcClassname;
		return this;
	}

	public String getJsonFileSourceSensorActuator() {
		return jsonFileSourceSensorActuator;
	}

	public RuleConfig setJsonFileSourceSensorActuator(String jsonFilePathNameExt) {
		this.jsonFileSourceSensorActuator = jsonFilePathNameExt;
		return this;
	}

	@Override
	public String toString() {
		return "RuleConfig [sourceSensorActuatorSvcClassname=" + sourceSensorActuatorSvcClassname
				+ ", jsonFileSourceSensorActuator=" + jsonFileSourceSensorActuator + ", jsonFileRuleSourceSensor="
				+ jsonFileRuleSourceSensor + ", jsonFileRuleDefItem=" + jsonFileRuleDefItem + "]";
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

}
