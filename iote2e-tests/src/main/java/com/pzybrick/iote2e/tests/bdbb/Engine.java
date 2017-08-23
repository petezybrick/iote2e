package com.pzybrick.iote2e.tests.bdbb;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Engine {
	@Expose
	private String engineUuid;
	@Expose
	private String airframeUuid;
	@Expose
	private Integer engineNumber;
	@Expose
	private String model;
	
	public String getEngineUuid() {
		return engineUuid;
	}
	public String getAirframeUuid() {
		return airframeUuid;
	}
	public Integer getEngineNumber() {
		return engineNumber;
	}
	public String getModel() {
		return model;
	}
	public Engine setEngineUuid(String engineUuid) {
		this.engineUuid = engineUuid;
		return this;
	}
	public Engine setAirframeUuid(String airframeUuid) {
		this.airframeUuid = airframeUuid;
		return this;
	}
	public Engine setEngineNumber(Integer engineNumber) {
		this.engineNumber = engineNumber;
		return this;
	}
	public Engine setModel(String model) {
		this.model = model;
		return this;
	}
	
	
}
