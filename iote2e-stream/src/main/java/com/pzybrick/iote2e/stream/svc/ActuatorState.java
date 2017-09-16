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
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class ActuatorState.
 */
@Generated("org.jsonschema2pojo")
public class ActuatorState implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5180186654964075339L;
	
	/** The login name. */
	@Expose
	private String loginName;
	
	/** The source name. */
	@Expose
	private String sourceName;
	
	/** The sensor name. */
	@Expose
	private String sensorName;
	
	/** The actuator name. */
	@Expose
	private String actuatorName;
	
	/** The actuator value. */
	@Expose
	private String actuatorValue;
	
	/** The actuator desc. */
	@Expose
	private String actuatorDesc;
	
	/** The actuator value updated at. */
	@Expose
	private String actuatorValueUpdatedAt;
	
	/** The pk. */
	private String pk;

	/**
	 * Instantiates a new actuator state.
	 */
	public ActuatorState() {
		
	}
	
	/**
	 * Gets the pk.
	 *
	 * @return the pk
	 */
	public String getPk() {
		if( pk != null ) return pk;
		pk = String.format( "%s|%s|%s|", loginName,sourceName,sensorName);
		return pk;
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
	 * Gets the sensor name.
	 *
	 * @return the sensor name
	 */
	public String getSensorName() {
		return sensorName;
	}

	/**
	 * Gets the actuator name.
	 *
	 * @return the actuator name
	 */
	public String getActuatorName() {
		return actuatorName;
	}

	/**
	 * Gets the actuator value.
	 *
	 * @return the actuator value
	 */
	public String getActuatorValue() {
		return actuatorValue;
	}

	/**
	 * Gets the actuator desc.
	 *
	 * @return the actuator desc
	 */
	public String getActuatorDesc() {
		return actuatorDesc;
	}

	/**
	 * Sets the source name.
	 *
	 * @param sourceName the source name
	 * @return the actuator state
	 */
	public ActuatorState setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	/**
	 * Sets the sensor name.
	 *
	 * @param sensorName the sensor name
	 * @return the actuator state
	 */
	public ActuatorState setSensorName(String sensorName) {
		this.sensorName = sensorName;
		return this;
	}

	/**
	 * Sets the actuator name.
	 *
	 * @param actuatorName the actuator name
	 * @return the actuator state
	 */
	public ActuatorState setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
		return this;
	}

	/**
	 * Sets the actuator value.
	 *
	 * @param actuatorValue the actuator value
	 * @return the actuator state
	 */
	public ActuatorState setActuatorValue(String actuatorValue) {
		this.actuatorValue = actuatorValue;
		return this;
	}

	/**
	 * Sets the actuator desc.
	 *
	 * @param actuatorDesc the actuator desc
	 * @return the actuator state
	 */
	public ActuatorState setActuatorDesc(String actuatorDesc) {
		this.actuatorDesc = actuatorDesc;
		return this;
	}

	/**
	 * Gets the actuator value updated at.
	 *
	 * @return the actuator value updated at
	 */
	public String getActuatorValueUpdatedAt() {
		return actuatorValueUpdatedAt;
	}

	/**
	 * Sets the actuator value updated at.
	 *
	 * @param actuatorValueUpdatedAt the actuator value updated at
	 * @return the actuator state
	 */
	public ActuatorState setActuatorValueUpdatedAt(String actuatorValueUpdatedAt) {
		this.actuatorValueUpdatedAt = actuatorValueUpdatedAt;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActuatorState [loginName=" + loginName + ", sourceName=" + sourceName + ", sensorName=" + sensorName
				+ ", actuatorName=" + actuatorName + ", actuatorValue=" + actuatorValue + ", desc=" + actuatorDesc
				+ ", actuatorValueUpdatedAt=" + actuatorValueUpdatedAt + "]";
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
	 * @return the actuator state
	 */
	public ActuatorState setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ActuatorState clone() throws CloneNotSupportedException {
		ActuatorState clone = new ActuatorState();
		clone.loginName = this.loginName;
		clone.sourceName = this.sourceName;
		clone.sensorName = this.sensorName;
		clone.actuatorName = this.actuatorName;
		clone.actuatorValue = this.actuatorValue;
		clone.actuatorDesc = this.actuatorDesc;
		clone.actuatorValueUpdatedAt = this.actuatorValueUpdatedAt;
		clone.pk = this.pk;
		return clone;
	}
	
}
