package com.pzybrick.iote2e.tests.bdbb;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


@Generated("org.jsonschema2pojo")
public class SimCourseDef {
	@Expose
	private Integer numWayPts;
	@Expose
	private Long freqMsecs;
	@Expose
	private List<SimFlightDef> simFlightDefs;
	
	public SimCourseDef() {
		super();
	}
	
	public Integer getNumWayPts() {
		return numWayPts;
	}
	public Long getFreqMsecs() {
		return freqMsecs;
	}
	public List<SimFlightDef> getSimFlightDefs() {
		return simFlightDefs;
	}
	public SimCourseDef setNumWayPts(Integer numWayPts) {
		this.numWayPts = numWayPts;
		return this;
	}
	public SimCourseDef setFreqMsecs(Long freqMsecs) {
		this.freqMsecs = freqMsecs;
		return this;
	}
	public SimCourseDef setSimFlightDefs(List<SimFlightDef> simFlightDefs) {
		this.simFlightDefs = simFlightDefs;
		return this;
	}
	
	
}
