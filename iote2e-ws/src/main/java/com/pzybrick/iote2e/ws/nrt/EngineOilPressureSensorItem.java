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
 * The Class EngineOilPressureSensorItem.
 */
@Generated("org.jsonschema2pojo")
public class EngineOilPressureSensorItem {
	
	/** The flight number. */
	@Expose
	private String flightNumber;
	
	/** The time millis. */
	@Expose
	private long timeMillis;
	
	/** The engine 1. */
	@Expose
	private Float engine1;
	
	/** The engine 2. */
	@Expose
	private Float engine2;
	
	/** The engine 3. */
	@Expose
	private Float engine3;
	
	/** The engine 4. */
	@Expose
	private Float engine4;
	
	/**
	 * Gets the flight number.
	 *
	 * @return the flight number
	 */
	public String getFlightNumber() {
		return flightNumber;
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
	 * Gets the engine 1.
	 *
	 * @return the engine 1
	 */
	public Float getEngine1() {
		return engine1;
	}
	
	/**
	 * Gets the engine 2.
	 *
	 * @return the engine 2
	 */
	public Float getEngine2() {
		return engine2;
	}
	
	/**
	 * Gets the engine 3.
	 *
	 * @return the engine 3
	 */
	public Float getEngine3() {
		return engine3;
	}
	
	/**
	 * Gets the engine 4.
	 *
	 * @return the engine 4
	 */
	public Float getEngine4() {
		return engine4;
	}
	
	/**
	 * Sets the flight number.
	 *
	 * @param flightNumber the flight number
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
	
	/**
	 * Sets the time millis.
	 *
	 * @param timeMillis the time millis
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
		return this;
	}
	
	/**
	 * Sets the engine 1.
	 *
	 * @param engine1 the engine 1
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setEngine1(Float engine1) {
		this.engine1 = engine1;
		return this;
	}
	
	/**
	 * Sets the engine 2.
	 *
	 * @param engine2 the engine 2
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setEngine2(Float engine2) {
		this.engine2 = engine2;
		return this;
	}
	
	/**
	 * Sets the engine 3.
	 *
	 * @param engine3 the engine 3
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setEngine3(Float engine3) {
		this.engine3 = engine3;
		return this;
	}
	
	/**
	 * Sets the engine 4.
	 *
	 * @param engine4 the engine 4
	 * @return the engine oil pressure sensor item
	 */
	public EngineOilPressureSensorItem setEngine4(Float engine4) {
		this.engine4 = engine4;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EngineOilPressureSensorItem [flightNumber=" + flightNumber + ", timeMillis=" + timeMillis + ", engine1="
				+ engine1 + ", engine2=" + engine2 + ", engine3=" + engine3 + ", engine4=" + engine4 + "]";
	}

	
	
}
