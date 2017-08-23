package com.pzybrick.iote2e.tests.bdbb;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Airframe {
	@Expose
	private String airframeUuid;
	@Expose
	private String model;
	@Expose
	private String tailNumber;
	@Expose
	private String airlineId;
	@Expose
	private List<Engine> engines;
	
	public String getAirframeUuid() {
		return airframeUuid;
	}
	public String getTailNumber() {
		return tailNumber;
	}
	public String getAirlineId() {
		return airlineId;
	}
	public List<Engine> getEngines() {
		return engines;
	}
	public Airframe setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	public Airframe setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
		return this;
	}
	public Airframe setAirlineId(String airlineId) {
		this.airlineId = airlineId;
		return this;
	}
	public Airframe setEngines(List<Engine> engines) {
		this.engines = engines;
		return this;
	}
	@Override
	public String toString() {
		return "Airframe [airframeUuid=" + airframeUuid + ", model=" + model + ", tailNumber=" + tailNumber
				+ ", airlineId=" + airlineId + ", engines=" + engines + "]";
	}
	public String getModel() {
		return model;
	}
	public Airframe setModel(String model) {
		this.model = model;
		return this;
	}

	
}
