package com.pzybrick.iote2e.ws.nrt;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class BloodPressureSensorItem {
	@Expose
	private String sourceName;
	@Expose
	private long timeMillis;
	@Expose
	private int systolic;
	@Expose
	private int diastolic;
	
	public String getSourceName() {
		return sourceName;
	}
	public long getTimeMillis() {
		return timeMillis;
	}
	public int getSystolic() {
		return systolic;
	}
	public int getDiastolic() {
		return diastolic;
	}
	public BloodPressureSensorItem setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}
	public BloodPressureSensorItem setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
		return this;
	}
	public BloodPressureSensorItem setSystolic(int systolic) {
		this.systolic = systolic;
		return this;
	}
	public BloodPressureSensorItem setDiastolic(int diastolic) {
		this.diastolic = diastolic;
		return this;
	}
	@Override
	public String toString() {
		return "BloodPressureSensorItem [sourceName=" + sourceName + ", timeMillis=" + timeMillis + ", systolic="
				+ systolic + ", diastolic=" + diastolic + "]";
	}
	
}
