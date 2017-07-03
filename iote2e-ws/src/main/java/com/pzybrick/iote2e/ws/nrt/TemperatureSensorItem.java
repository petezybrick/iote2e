package com.pzybrick.iote2e.ws.nrt;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class TemperatureSensorItem {
	@Expose
	private String sourceName;
	@Expose
	private long timeMillis;
	@Expose
	private float degreesC;
	
	
	public String getSourceName() {
		return sourceName;
	}
	
	public long getTimeMillis() {
		return timeMillis;
	}
	
	public float getDegreesC() {
		return degreesC;
	}
	
	public TemperatureSensorItem setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}
	
	public TemperatureSensorItem setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
		return this;
	}
	
	public TemperatureSensorItem setDegreesC(float degreesC) {
		this.degreesC = degreesC;
		return this;
	}

	@Override
	public String toString() {
		return "TemperatureSensorItem [sourceName=" + sourceName + ", timeMillis=" + timeMillis + ", degreesC="
				+ degreesC + "]";
	}
	
}
