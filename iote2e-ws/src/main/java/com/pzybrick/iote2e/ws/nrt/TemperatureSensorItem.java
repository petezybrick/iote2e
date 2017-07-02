package com.pzybrick.iote2e.ws.nrt;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class TemperatureSensorItem {
	@Expose
	private String loginName;
	@Expose
	private long timeMillis;
	@Expose
	private float degreesC;
	public String getLoginName() {
		return loginName;
	}
	public long getTimeMillis() {
		return timeMillis;
	}
	public float getDegreesC() {
		return degreesC;
	}
	public TemperatureSensorItem setLoginName(String loginName) {
		this.loginName = loginName;
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
	
}
