package com.pzybrick.iote2e.ws.nrt;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class EngineOilPressureSensorItem {
	@Expose
	private String flightNumber;
	@Expose
	private long timeMillis;
	@Expose
	private Float engine1;
	@Expose
	private Float engine2;
	@Expose
	private Float engine3;
	@Expose
	private Float engine4;
	
	public String getFlightNumber() {
		return flightNumber;
	}
	public long getTimeMillis() {
		return timeMillis;
	}
	public Float getEngine1() {
		return engine1;
	}
	public Float getEngine2() {
		return engine2;
	}
	public Float getEngine3() {
		return engine3;
	}
	public Float getEngine4() {
		return engine4;
	}
	public EngineOilPressureSensorItem setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
		return this;
	}
	public EngineOilPressureSensorItem setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
		return this;
	}
	public EngineOilPressureSensorItem setEngine1(Float engine1) {
		this.engine1 = engine1;
		return this;
	}
	public EngineOilPressureSensorItem setEngine2(Float engine2) {
		this.engine2 = engine2;
		return this;
	}
	public EngineOilPressureSensorItem setEngine3(Float engine3) {
		this.engine3 = engine3;
		return this;
	}
	public EngineOilPressureSensorItem setEngine4(Float engine4) {
		this.engine4 = engine4;
		return this;
	}
	@Override
	public String toString() {
		return "EngineOilPressureSensorItem [flightNumber=" + flightNumber + ", timeMillis=" + timeMillis + ", engine1="
				+ engine1 + ", engine2=" + engine2 + ", engine3=" + engine3 + ", engine4=" + engine4 + "]";
	}

	
	
}
