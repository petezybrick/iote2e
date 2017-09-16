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

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class Engine.
 */
@Generated("org.jsonschema2pojo")
public class Engine {
	
	/** The engine uuid. */
	@Expose
	private String engineUuid;
	
	/** The airframe uuid. */
	@Expose
	private String airframeUuid;
	
	/** The engine number. */
	@Expose
	private Integer engineNumber;
	
	/** The model. */
	@Expose
	private String model;
	
	/**
	 * Gets the engine uuid.
	 *
	 * @return the engine uuid
	 */
	public String getEngineUuid() {
		return engineUuid;
	}
	
	/**
	 * Gets the airframe uuid.
	 *
	 * @return the airframe uuid
	 */
	public String getAirframeUuid() {
		return airframeUuid;
	}
	
	/**
	 * Gets the engine number.
	 *
	 * @return the engine number
	 */
	public Integer getEngineNumber() {
		return engineNumber;
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
	 * Sets the engine uuid.
	 *
	 * @param engineUuid the engine uuid
	 * @return the engine
	 */
	public Engine setEngineUuid(String engineUuid) {
		this.engineUuid = engineUuid;
		return this;
	}
	
	/**
	 * Sets the airframe uuid.
	 *
	 * @param airframeUuid the airframe uuid
	 * @return the engine
	 */
	public Engine setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	
	/**
	 * Sets the engine number.
	 *
	 * @param engineNumber the engine number
	 * @return the engine
	 */
	public Engine setEngineNumber(Integer engineNumber) {
		this.engineNumber = engineNumber;
		return this;
	}
	
	/**
	 * Sets the model.
	 *
	 * @param model the model
	 * @return the engine
	 */
	public Engine setModel(String model) {
		this.model = model;
		return this;
	}
	
	
}
