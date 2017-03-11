package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

// IF <sensorSourceValue> <sensorComparator> <sensorCompareValue> 
//    AND <actuatorSourceValue> <actuatorComparator> <actuatorTypeValue>
// THEN SendCmd <actuatorTargetCmd> <actuatorTargetValue>
@Generated("org.jsonschema2pojo")
public class RuleDefCondItem implements Serializable {
	private static final long serialVersionUID = 8907426605716836373L;
	@Expose
	private String sensorComparator;
	@Expose
	private String sensorCompareValue;
	@Expose
	private String sensorTypeValue;
	@Expose
	private String actuatorComparator;
	@Expose
	private String actuatorCompareValue;
	@Expose
	private String actuatorTypeValue;
	@Expose
	private String actuatorTargetCmd;
	@Expose
	private String actuatorTargetValue;
	@Expose
	private Boolean stopEvalOnMatch;
	
	// pre-processed values to increase rule evaluation performance
	private Double dblSensorCompareValue;
	private Integer intSensorCompareValue;
	private Double dblActuatorCompareValue;
	private Integer intActuatorCompareValue;
	public enum RuleComparator {EQ,NE,LE,GE,LT,GT};
	private RuleComparator ruleComparatorSensor;
	private RuleComparator ruleComparatorActuator;
	
	public RuleDefCondItem() {
		
	}
	
	public static RuleComparator ruleComparatorFromString( String strComparator ) throws Exception {
		if( strComparator.equalsIgnoreCase("EQ") ) return RuleComparator.EQ;
		else if( strComparator.equalsIgnoreCase("NE") ) return RuleComparator.NE;
		else if( strComparator.equalsIgnoreCase("LE") ) return RuleComparator.LE;
		else if( strComparator.equalsIgnoreCase("GE") ) return RuleComparator.GE;
		else if( strComparator.equalsIgnoreCase("LT") ) return RuleComparator.LT;
		else if( strComparator.equalsIgnoreCase("GT") ) return RuleComparator.GT;
		else throw new Exception( "Invalid comparator: " + strComparator );
	}
	
	public String getSensorCompareValue() {
		return sensorCompareValue;
	}
	public String getSensorTypeValue() {
		return sensorTypeValue;
	}
	public String getSensorComparator() {
		return sensorComparator;
	}

	public String getActuatorCompareValue() {
		return actuatorCompareValue;
	}
	public String getActuatorTypeValue() {
		return actuatorTypeValue;
	}
	public String getActuatorComparator() {
		return actuatorComparator;
	}
	public String getActuatorTargetCmd() {
		return actuatorTargetCmd;
	}
	public String getActuatorTargetValue() {
		return actuatorTargetValue;
	}
	public RuleDefCondItem setSensorCompareValue(String sensorCompareValue) {
		this.sensorCompareValue = sensorCompareValue;
		return this;
	}
	public RuleDefCondItem setSensorTypeValue(String sensorTypeValue) {
		this.sensorTypeValue = sensorTypeValue;
		return this;
	}
	public RuleDefCondItem setSensorComparator(String sensorComparator) {
		this.sensorComparator = sensorComparator;
		return this;
	}
	public RuleDefCondItem setActuatorCompareValue(String actuatorCompareValue) {
		this.actuatorCompareValue = actuatorCompareValue;
		return this;
	}
	public RuleDefCondItem setActuatorTypeValue(String actuatorTypeValue) {
		this.actuatorTypeValue = actuatorTypeValue;
		return this;
	}
	public RuleDefCondItem setActuatorComparator(String actuatorComparator) {
		this.actuatorComparator = actuatorComparator;
		return this;
	}
	public RuleDefCondItem setActuatorTargetCmd(String actuatorTargetCmd) {
		this.actuatorTargetCmd = actuatorTargetCmd;
		return this;
	}
	public RuleDefCondItem setActuatorTargetValue(String actuatorTargetValue) {
		this.actuatorTargetValue = actuatorTargetValue;
		return this;
	}

	public Boolean getStopEvalOnMatch() {
		return stopEvalOnMatch;
	}

	public RuleDefCondItem setStopEvalOnMatch(Boolean stopEvalOnMatch) {
		this.stopEvalOnMatch = stopEvalOnMatch;
		return this;
	}

	public Double getDblSensorCompareValue() {
		return dblSensorCompareValue;
	}

	public Integer getIntSensorCompareValue() {
		return intSensorCompareValue;
	}

	public Double getDblActuatorCompareValue() {
		return dblActuatorCompareValue;
	}

	public Integer getIntActuatorCompareValue() {
		return intActuatorCompareValue;
	}

	public RuleDefCondItem setDblSensorCompareValue(Double dblSensorCompareValue) {
		this.dblSensorCompareValue = dblSensorCompareValue;
		return this;
	}

	public RuleDefCondItem setIntSensorCompareValue(Integer intSensorCompareValue) {
		this.intSensorCompareValue = intSensorCompareValue;
		return this;
	}

	public RuleDefCondItem setDblActuatorCompareValue(Double dblActuatorCompareValue) {
		this.dblActuatorCompareValue = dblActuatorCompareValue;
		return this;
	}

	public RuleDefCondItem setIntActuatorCompareValue(Integer intActuatorCompareValue) {
		this.intActuatorCompareValue = intActuatorCompareValue;
		return this;
	}

	public RuleComparator getRuleComparatorActuator() {
		return ruleComparatorActuator;
	}

	public RuleDefCondItem setRuleComparatorActuator(RuleComparator ruleComparatorActuator) {
		this.ruleComparatorActuator = ruleComparatorActuator;
		return this;
	}

	public RuleComparator getRuleComparatorSensor() {
		return ruleComparatorSensor;
	}

	public RuleDefCondItem setRuleComparatorSensor(RuleComparator ruleComparatorSensor) {
		this.ruleComparatorSensor = ruleComparatorSensor;
		return this;
	}

	@Override
	public String toString() {
		return "RuleDefCondItem [sensorComparator=" + sensorComparator + ", sensorCompareValue=" + sensorCompareValue
				+ ", sensorTypeValue=" + sensorTypeValue + ", actuatorComparator=" + actuatorComparator
				+ ", actuatorCompareValue=" + actuatorCompareValue + ", actuatorTypeValue=" + actuatorTypeValue
				+ ", actuatorTargetCmd=" + actuatorTargetCmd + ", actuatorTargetValue=" + actuatorTargetValue
				+ ", stopEvalOnMatch=" + stopEvalOnMatch + ", dblSensorCompareValue=" + dblSensorCompareValue
				+ ", intSensorCompareValue=" + intSensorCompareValue + ", dblActuatorCompareValue="
				+ dblActuatorCompareValue + ", intActuatorCompareValue=" + intActuatorCompareValue
				+ ", ruleComparatorSensor=" + ruleComparatorSensor + ", ruleComparatorActuator="
				+ ruleComparatorActuator + "]";
	}
	
}
