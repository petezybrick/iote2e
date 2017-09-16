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


/**
 * The Class RuleLoginSourceSensor.
 */
@Generated("org.jsonschema2pojo")
public class RuleLoginSourceSensor implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7594284561642969987L;
	
	/** The login name. */
	@Expose
	private String loginName;
	
	/** The source name. */
	@Expose
	private String sourceName;
	
	/** The sensor name. */
	@Expose
	private String sensorName;
	
	/** The rule uuid. */
	@Expose
	private String ruleUuid;
	
	/** The desc. */
	@Expose
	private String desc;
	
	/**
	 * Instantiates a new rule login source sensor.
	 */
	public RuleLoginSourceSensor() {
		super();
	}

	/**
	 * Gets the sensor name.
	 *
	 * @return the sensor name
	 */
	public String getSensorName() {
		return sensorName;
	}

	/**
	 * Gets the rule name.
	 *
	 * @return the rule name
	 */
	public String getRuleName() {
		return ruleUuid;
	}

	/**
	 * Sets the sensor name.
	 *
	 * @param sensorName the sensor name
	 * @return the rule login source sensor
	 */
	public RuleLoginSourceSensor setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	/**
	 * Sets the rule name.
	 *
	 * @param ruleName the rule name
	 * @return the rule login source sensor
	 */
	public RuleLoginSourceSensor setRuleName(String ruleName) {
		this.ruleUuid = ruleName;
		return this;
	}

	/**
	 * Gets the desc.
	 *
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the desc.
	 *
	 * @param desc the desc
	 * @return the rule login source sensor
	 */
	public RuleLoginSourceSensor setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Sets the source name.
	 *
	 * @param sourceName the source name
	 * @return the rule login source sensor
	 */
	public RuleLoginSourceSensor setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleLoginSourceSensor [loginName=" + loginName + ", sourceName=" + sourceName + ", sensorName="
				+ sensorName + ", ruleName=" + ruleUuid + ", desc=" + desc + "]";
	}

	/**
	 * Gets the login name.
	 *
	 * @return the login name
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Sets the login name.
	 *
	 * @param loginName the login name
	 * @return the rule login source sensor
	 */
	public RuleLoginSourceSensor setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
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
