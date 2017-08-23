package com.pzybrick.iote2e.tests.bdbb;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class EngineStatus {
	@Expose
	private String engineUuid;
	@Expose
	private Integer engineNumber;
	@Expose
	private Float oilTempC;
	@Expose
	private Float oilPressure;
	@Expose
	private Float exhaustGasTempC;
	@Expose
	private Float n1Pct;
	@Expose
	private Float n2Pct;
	
	public String getEngineUuid() {
		return engineUuid;
	}
	public Float getOilTempC() {
		return oilTempC;
	}
	public Float getOilPressure() {
		return oilPressure;
	}
	public Float getExhaustGasTempC() {
		return exhaustGasTempC;
	}
	public Float getN1Pct() {
		return n1Pct;
	}
	public Float getN2Pct() {
		return n2Pct;
	}
	public EngineStatus setEngineUuid(String engineUuid) {
		this.engineUuid = engineUuid;
		return this;
	}
	public EngineStatus setOilTempC(Float oilTempC) {
		this.oilTempC = oilTempC;
		return this;
	}
	public EngineStatus setOilPressure(Float oilPressure) {
		this.oilPressure = oilPressure;
		return this;
	}
	public EngineStatus setExhaustGasTempC(Float exhaustGasTempC) {
		this.exhaustGasTempC = exhaustGasTempC;
		return this;
	}
	public EngineStatus setN1Pct(Float n1Pct) {
		this.n1Pct = n1Pct;
		return this;
	}
	public EngineStatus setN2Pct(Float n2Pct) {
		this.n2Pct = n2Pct;
		return this;
	}
	public Integer getEngineNumber() {
		return engineNumber;
	}
	public EngineStatus setEngineNumber(Integer engineNumber) {
		this.engineNumber = engineNumber;
		return this;
	}
	@Override
	public String toString() {
		return "EngineStatus [engineUuid=" + engineUuid + ", engineNumber=" + engineNumber + ", oilTempC=" + oilTempC
				+ ", oilPressure=" + oilPressure + ", exhaustGasTempC=" + exhaustGasTempC + ", n1Pct=" + n1Pct
				+ ", n2Pct=" + n2Pct + "]";
	}
	
	
}
