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
package com.pzybrick.iote2e.stream.bdbb;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class Airframe.
 */
@Generated("org.jsonschema2pojo")
public class Airframe {
	
	/** The airframe uuid. */
	@Expose
	private String airframeUuid;
	
	/** The model. */
	@Expose
	private String model;
	
	/** The tail number. */
	@Expose
	private String tailNumber;
	
	/** The airline id. */
	@Expose
	private String airlineId;
	
	/** The engines. */
	@Expose
	private List<Engine> engines;
	
	/**
	 * Gets the airframe uuid.
	 *
	 * @return the airframe uuid
	 */
	public String getAirframeUuid() {
		return airframeUuid;
	}
	
	/**
	 * Gets the tail number.
	 *
	 * @return the tail number
	 */
	public String getTailNumber() {
		return tailNumber;
	}
	
	/**
	 * Gets the airline id.
	 *
	 * @return the airline id
	 */
	public String getAirlineId() {
		return airlineId;
	}
	
	/**
	 * Gets the engines.
	 *
	 * @return the engines
	 */
	public List<Engine> getEngines() {
		return engines;
	}
	
	/**
	 * Sets the airframe uuid.
	 *
	 * @param airframeUuid the airframe uuid
	 * @return the airframe
	 */
	public Airframe setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	
	/**
	 * Sets the tail number.
	 *
	 * @param tailNumber the tail number
	 * @return the airframe
	 */
	public Airframe setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
		return this;
	}
	
	/**
	 * Sets the airline id.
	 *
	 * @param airlineId the airline id
	 * @return the airframe
	 */
	public Airframe setAirlineId(String airlineId) {
		this.airlineId = airlineId;
		return this;
	}
	
	/**
	 * Sets the engines.
	 *
	 * @param engines the engines
	 * @return the airframe
	 */
	public Airframe setEngines(List<Engine> engines) {
		this.engines = engines;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Airframe [airframeUuid=" + airframeUuid + ", model=" + model + ", tailNumber=" + tailNumber
				+ ", airlineId=" + airlineId + ", engines=" + engines + "]";
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	
	/**
	 * Sets the model.
	 *
	 * @param model the model
	 * @return the airframe
	 */
	public Airframe setModel(String model) {
		this.model = model;
		return this;
	}

	
}
