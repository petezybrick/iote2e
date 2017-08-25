package com.pzybrick.iote2e.stream.bdbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class EngineStatus {
	@Expose
	private String engineStatusUuid;
	@Expose
	private String flightStatusUuid;
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
	@Expose
	private Long engineStatusTs;

	public EngineStatus() {
		super();
	}

	public EngineStatus(ResultSet rs) throws SQLException {
		this.engineStatusUuid = rs.getString("engine_status_uuid");
		this.flightStatusUuid = rs.getString("flight_status_uuid");
		this.engineUuid = rs.getString("engine_uuid");
		this.engineNumber = rs.getInt("engine_number");
		this.oilTempC = rs.getFloat("oil_temp_c");
		this.oilPressure = rs.getFloat("oil_pressure");
		this.exhaustGasTempC = rs.getFloat("exhaust_gas_temp_c");
		this.n1Pct = rs.getFloat("n1_pct");
		this.n2Pct = rs.getFloat("n2_pct");
		this.engineStatusTs = rs.getTimestamp("engine_status_ts").getTime();
		//this.insertTs = rs.getTimestamp("insert_ts");
	}
	
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
		return "EngineStatus [engineStatusUuid=" + engineStatusUuid + ", flightStatusUuid=" + flightStatusUuid
				+ ", engineUuid=" + engineUuid + ", engineNumber=" + engineNumber + ", oilTempC=" + oilTempC
				+ ", oilPressure=" + oilPressure + ", exhaustGasTempC=" + exhaustGasTempC + ", n1Pct=" + n1Pct
				+ ", n2Pct=" + n2Pct + ", flightStatusTs=" + engineStatusTs + "]";
	}
	public String getEngineStatusUuid() {
		return engineStatusUuid;
	}
	public EngineStatus setEngineStatusUuid(String engineStatusUuid) {
		this.engineStatusUuid = engineStatusUuid;
		return this;
	}
	public String getFlightStatusUuid() {
		return flightStatusUuid;
	}
	public EngineStatus setFlightStatusUuid(String flightStatusUuid) {
		this.flightStatusUuid = flightStatusUuid;
		return this;
	}
	public Long getEngineStatusTs() {
		return engineStatusTs;
	}
	public EngineStatus setEngineStatusTs(Long engineStatusTs) {
		this.engineStatusTs = engineStatusTs;
		return this;
	}
	
	
}
