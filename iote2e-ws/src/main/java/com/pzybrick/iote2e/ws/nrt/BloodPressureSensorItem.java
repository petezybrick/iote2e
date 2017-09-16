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
package com.pzybrick.iote2e.ws.nrt;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class BloodPressureSensorItem.
 */
@Generated("org.jsonschema2pojo")
public class BloodPressureSensorItem {
	
	/** The source name. */
	@Expose
	private String sourceName;
	
	/** The time millis. */
	@Expose
	private long timeMillis;
	
	/** The systolic. */
	@Expose
	private int systolic;
	
	/** The diastolic. */
	@Expose
	private int diastolic;
	
	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}
	
	/**
	 * Gets the time millis.
	 *
	 * @return the time millis
	 */
	public long getTimeMillis() {
		return timeMillis;
	}
	
	/**
	 * Gets the systolic.
	 *
	 * @return the systolic
	 */
	public int getSystolic() {
		return systolic;
	}
	
	/**
	 * Gets the diastolic.
	 *
	 * @return the diastolic
	 */
	public int getDiastolic() {
		return diastolic;
	}
	
	/**
	 * Sets the source name.
	 *
	 * @param sourceName the source name
	 * @return the blood pressure sensor item
	 */
	public BloodPressureSensorItem setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}
	
	/**
	 * Sets the time millis.
	 *
	 * @param timeMillis the time millis
	 * @return the blood pressure sensor item
	 */
	public BloodPressureSensorItem setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
		return this;
	}
	
	/**
	 * Sets the systolic.
	 *
	 * @param systolic the systolic
	 * @return the blood pressure sensor item
	 */
	public BloodPressureSensorItem setSystolic(int systolic) {
		this.systolic = systolic;
		return this;
	}
	
	/**
	 * Sets the diastolic.
	 *
	 * @param diastolic the diastolic
	 * @return the blood pressure sensor item
	 */
	public BloodPressureSensorItem setDiastolic(int diastolic) {
		this.diastolic = diastolic;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BloodPressureSensorItem [sourceName=" + sourceName + ", timeMillis=" + timeMillis + ", systolic="
				+ systolic + ", diastolic=" + diastolic + "]";
	}
	
}
