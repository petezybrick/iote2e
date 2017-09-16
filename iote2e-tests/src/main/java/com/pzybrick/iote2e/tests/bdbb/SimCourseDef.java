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
package com.pzybrick.iote2e.tests.bdbb;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;



/**
 * The Class SimCourseDef.
 */
@Generated("org.jsonschema2pojo")
public class SimCourseDef {
	
	/** The num way pts. */
	@Expose
	private Integer numWayPts;
	
	/** The freq msecs. */
	@Expose
	private Long freqMsecs;
	
	/** The sim flight defs. */
	@Expose
	private List<SimFlightDef> simFlightDefs;
	
	/**
	 * Instantiates a new sim course def.
	 */
	public SimCourseDef() {
		super();
	}
	
	/**
	 * Gets the num way pts.
	 *
	 * @return the num way pts
	 */
	public Integer getNumWayPts() {
		return numWayPts;
	}
	
	/**
	 * Gets the freq msecs.
	 *
	 * @return the freq msecs
	 */
	public Long getFreqMsecs() {
		return freqMsecs;
	}
	
	/**
	 * Gets the sim flight defs.
	 *
	 * @return the sim flight defs
	 */
	public List<SimFlightDef> getSimFlightDefs() {
		return simFlightDefs;
	}
	
	/**
	 * Sets the num way pts.
	 *
	 * @param numWayPts the num way pts
	 * @return the sim course def
	 */
	public SimCourseDef setNumWayPts(Integer numWayPts) {
		this.numWayPts = numWayPts;
		return this;
	}
	
	/**
	 * Sets the freq msecs.
	 *
	 * @param freqMsecs the freq msecs
	 * @return the sim course def
	 */
	public SimCourseDef setFreqMsecs(Long freqMsecs) {
		this.freqMsecs = freqMsecs;
		return this;
	}
	
	/**
	 * Sets the sim flight defs.
	 *
	 * @param simFlightDefs the sim flight defs
	 * @return the sim course def
	 */
	public SimCourseDef setSimFlightDefs(List<SimFlightDef> simFlightDefs) {
		this.simFlightDefs = simFlightDefs;
		return this;
	}
	
	
}
