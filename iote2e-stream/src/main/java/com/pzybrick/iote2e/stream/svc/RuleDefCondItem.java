/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


// IF <sensorSourceValue> <sensorComparator> <sensorCompareValue> 
//    AND <actuatorSourceValue> <actuatorComparator> <actuatorTypeValue>
/**
 * The Class RuleDefCondItem.
 */
// THEN SendCmd <actuatorTargetCmd> <actuatorTargetValue>
@Generated("org.jsonschema2pojo")
public class RuleDefCondItem implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8907426605716836373L;
	
	/** The sensor comparator. */
	@Expose
	private String sensorComparator;
	
	/** The sensor compare value. */
	@Expose
	private String sensorCompareValue;
	
	/** The sensor type value. */
	@Expose
	private String sensorTypeValue;
	
	/** The actuator comparator. */
	@Expose
	private String actuatorComparator;
	
	/** The actuator compare value. */
	@Expose
	private String actuatorCompareValue;
	
	/** The actuator type value. */
	@Expose
	private String actuatorTypeValue;
	
	/** The actuator target cmd. */
	@Expose
	private String actuatorTargetCmd;
	
	/** The actuator target value. */
	@Expose
	private String actuatorTargetValue;
	
	/** The stop eval on match. */
	@Expose
	private Boolean stopEvalOnMatch;
	
	/** The dbl sensor compare value. */
	// pre-processed values to increase rule evaluation performance
	private Double dblSensorCompareValue;
	
	/** The int sensor compare value. */
	private Integer intSensorCompareValue;
	
	/** The dbl actuator compare value. */
	private Double dblActuatorCompareValue;
	
	/** The int actuator compare value. */
	private Integer intActuatorCompareValue;
	
	/**
	 * The Enum RuleComparator.
	 */
	public enum RuleComparator {
/** The eq. */
EQ,
/** The ne. */
NE,
/** The le. */
LE,
/** The ge. */
GE,
/** The lt. */
LT,
/** The gt. */
GT};
	
	/** The rule comparator sensor. */
	private RuleComparator ruleComparatorSensor;
	
	/** The rule comparator actuator. */
	private RuleComparator ruleComparatorActuator;
	
	/**
	 * Instantiates a new rule def cond item.
	 */
	public RuleDefCondItem() {
		
	}
	
	/**
	 * Rule comparator from string.
	 *
	 * @param strComparator the str comparator
	 * @return the rule comparator
	 * @throws Exception the exception
	 */
	public static RuleComparator ruleComparatorFromString( String strComparator ) throws Exception {
		if( strComparator.equalsIgnoreCase("EQ") ) return RuleComparator.EQ;
		else if( strComparator.equalsIgnoreCase("NE") ) return RuleComparator.NE;
		else if( strComparator.equalsIgnoreCase("LE") ) return RuleComparator.LE;
		else if( strComparator.equalsIgnoreCase("GE") ) return RuleComparator.GE;
		else if( strComparator.equalsIgnoreCase("LT") ) return RuleComparator.LT;
		else if( strComparator.equalsIgnoreCase("GT") ) return RuleComparator.GT;
		else throw new Exception( "Invalid comparator: " + strComparator );
	}
	
	/**
	 * Gets the sensor compare value.
	 *
	 * @return the sensor compare value
	 */
	public String getSensorCompareValue() {
		return sensorCompareValue;
	}
	
	/**
	 * Gets the sensor type value.
	 *
	 * @return the sensor type value
	 */
	public String getSensorTypeValue() {
		return sensorTypeValue;
	}
	
	/**
	 * Gets the sensor comparator.
	 *
	 * @return the sensor comparator
	 */
	public String getSensorComparator() {
		return sensorComparator;
	}

	/**
	 * Gets the actuator compare value.
	 *
	 * @return the actuator compare value
	 */
	public String getActuatorCompareValue() {
		return actuatorCompareValue;
	}
	
	/**
	 * Gets the actuator type value.
	 *
	 * @return the actuator type value
	 */
	public String getActuatorTypeValue() {
		return actuatorTypeValue;
	}
	
	/**
	 * Gets the actuator comparator.
	 *
	 * @return the actuator comparator
	 */
	public String getActuatorComparator() {
		return actuatorComparator;
	}
	
	/**
	 * Gets the actuator target cmd.
	 *
	 * @return the actuator target cmd
	 */
	public String getActuatorTargetCmd() {
		return actuatorTargetCmd;
	}
	
	/**
	 * Gets the actuator target value.
	 *
	 * @return the actuator target value
	 */
	public String getActuatorTargetValue() {
		return actuatorTargetValue;
	}
	
	/**
	 * Sets the sensor compare value.
	 *
	 * @param sensorCompareValue the sensor compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setSensorCompareValue(String sensorCompareValue) {
		this.sensorCompareValue = sensorCompareValue;
		return this;
	}
	
	/**
	 * Sets the sensor type value.
	 *
	 * @param sensorTypeValue the sensor type value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setSensorTypeValue(String sensorTypeValue) {
		this.sensorTypeValue = sensorTypeValue;
		return this;
	}
	
	/**
	 * Sets the sensor comparator.
	 *
	 * @param sensorComparator the sensor comparator
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setSensorComparator(String sensorComparator) {
		this.sensorComparator = sensorComparator;
		return this;
	}
	
	/**
	 * Sets the actuator compare value.
	 *
	 * @param actuatorCompareValue the actuator compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setActuatorCompareValue(String actuatorCompareValue) {
		this.actuatorCompareValue = actuatorCompareValue;
		return this;
	}
	
	/**
	 * Sets the actuator type value.
	 *
	 * @param actuatorTypeValue the actuator type value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setActuatorTypeValue(String actuatorTypeValue) {
		this.actuatorTypeValue = actuatorTypeValue;
		return this;
	}
	
	/**
	 * Sets the actuator comparator.
	 *
	 * @param actuatorComparator the actuator comparator
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setActuatorComparator(String actuatorComparator) {
		this.actuatorComparator = actuatorComparator;
		return this;
	}
	
	/**
	 * Sets the actuator target cmd.
	 *
	 * @param actuatorTargetCmd the actuator target cmd
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setActuatorTargetCmd(String actuatorTargetCmd) {
		this.actuatorTargetCmd = actuatorTargetCmd;
		return this;
	}
	
	/**
	 * Sets the actuator target value.
	 *
	 * @param actuatorTargetValue the actuator target value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setActuatorTargetValue(String actuatorTargetValue) {
		this.actuatorTargetValue = actuatorTargetValue;
		return this;
	}

	/**
	 * Gets the stop eval on match.
	 *
	 * @return the stop eval on match
	 */
	public Boolean getStopEvalOnMatch() {
		return stopEvalOnMatch;
	}

	/**
	 * Sets the stop eval on match.
	 *
	 * @param stopEvalOnMatch the stop eval on match
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setStopEvalOnMatch(Boolean stopEvalOnMatch) {
		this.stopEvalOnMatch = stopEvalOnMatch;
		return this;
	}

	/**
	 * Gets the dbl sensor compare value.
	 *
	 * @return the dbl sensor compare value
	 */
	public Double getDblSensorCompareValue() {
		return dblSensorCompareValue;
	}

	/**
	 * Gets the int sensor compare value.
	 *
	 * @return the int sensor compare value
	 */
	public Integer getIntSensorCompareValue() {
		return intSensorCompareValue;
	}

	/**
	 * Gets the dbl actuator compare value.
	 *
	 * @return the dbl actuator compare value
	 */
	public Double getDblActuatorCompareValue() {
		return dblActuatorCompareValue;
	}

	/**
	 * Gets the int actuator compare value.
	 *
	 * @return the int actuator compare value
	 */
	public Integer getIntActuatorCompareValue() {
		return intActuatorCompareValue;
	}

	/**
	 * Sets the dbl sensor compare value.
	 *
	 * @param dblSensorCompareValue the dbl sensor compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setDblSensorCompareValue(Double dblSensorCompareValue) {
		this.dblSensorCompareValue = dblSensorCompareValue;
		return this;
	}

	/**
	 * Sets the int sensor compare value.
	 *
	 * @param intSensorCompareValue the int sensor compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setIntSensorCompareValue(Integer intSensorCompareValue) {
		this.intSensorCompareValue = intSensorCompareValue;
		return this;
	}

	/**
	 * Sets the dbl actuator compare value.
	 *
	 * @param dblActuatorCompareValue the dbl actuator compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setDblActuatorCompareValue(Double dblActuatorCompareValue) {
		this.dblActuatorCompareValue = dblActuatorCompareValue;
		return this;
	}

	/**
	 * Sets the int actuator compare value.
	 *
	 * @param intActuatorCompareValue the int actuator compare value
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setIntActuatorCompareValue(Integer intActuatorCompareValue) {
		this.intActuatorCompareValue = intActuatorCompareValue;
		return this;
	}

	/**
	 * Gets the rule comparator actuator.
	 *
	 * @return the rule comparator actuator
	 */
	public RuleComparator getRuleComparatorActuator() {
		return ruleComparatorActuator;
	}

	/**
	 * Sets the rule comparator actuator.
	 *
	 * @param ruleComparatorActuator the rule comparator actuator
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setRuleComparatorActuator(RuleComparator ruleComparatorActuator) {
		this.ruleComparatorActuator = ruleComparatorActuator;
		return this;
	}

	/**
	 * Gets the rule comparator sensor.
	 *
	 * @return the rule comparator sensor
	 */
	public RuleComparator getRuleComparatorSensor() {
		return ruleComparatorSensor;
	}

	/**
	 * Sets the rule comparator sensor.
	 *
	 * @param ruleComparatorSensor the rule comparator sensor
	 * @return the rule def cond item
	 */
	public RuleDefCondItem setRuleComparatorSensor(RuleComparator ruleComparatorSensor) {
		this.ruleComparatorSensor = ruleComparatorSensor;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
