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
package com.pzybrick.iote2e.stream.bdbb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class EngineStatus.
 */
@Generated("org.jsonschema2pojo")
public class EngineStatus {
	
	/** The engine status uuid. */
	@Expose
	private String engineStatusUuid;
	
	/** The flight status uuid. */
	@Expose
	private String flightStatusUuid;
	
	/** The engine uuid. */
	@Expose
	private String engineUuid;
	
	/** The engine number. */
	@Expose
	private Integer engineNumber;
	
	/** The oil temp C. */
	@Expose
	private Float oilTempC;
	
	/** The oil pressure. */
	@Expose
	private Float oilPressure;
	
	/** The exhaust gas temp C. */
	@Expose
	private Float exhaustGasTempC;
	
	/** The n 1 pct. */
	@Expose
	private Float n1Pct;
	
	/** The n 2 pct. */
	@Expose
	private Float n2Pct;
	
	/** The engine status ts. */
	@Expose
	private Long engineStatusTs;

	/**
	 * Instantiates a new engine status.
	 */
	public EngineStatus() {
		super();
	}

	/**
	 * Instantiates a new engine status.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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
	
	/**
	 * Gets the engine uuid.
	 *
	 * @return the engine uuid
	 */
	public String getEngineUuid() {
		return engineUuid;
	}
	
	/**
	 * Gets the oil temp C.
	 *
	 * @return the oil temp C
	 */
	public Float getOilTempC() {
		return oilTempC;
	}
	
	/**
	 * Gets the oil pressure.
	 *
	 * @return the oil pressure
	 */
	public Float getOilPressure() {
		return oilPressure;
	}
	
	/**
	 * Gets the exhaust gas temp C.
	 *
	 * @return the exhaust gas temp C
	 */
	public Float getExhaustGasTempC() {
		return exhaustGasTempC;
	}
	
	/**
	 * Gets the n 1 pct.
	 *
	 * @return the n 1 pct
	 */
	public Float getN1Pct() {
		return n1Pct;
	}
	
	/**
	 * Gets the n 2 pct.
	 *
	 * @return the n 2 pct
	 */
	public Float getN2Pct() {
		return n2Pct;
	}
	
	/**
	 * Sets the engine uuid.
	 *
	 * @param engineUuid the engine uuid
	 * @return the engine status
	 */
	public EngineStatus setEngineUuid(String engineUuid) {
		this.engineUuid = engineUuid;
		return this;
	}
	
	/**
	 * Sets the oil temp C.
	 *
	 * @param oilTempC the oil temp C
	 * @return the engine status
	 */
	public EngineStatus setOilTempC(Float oilTempC) {
		this.oilTempC = oilTempC;
		return this;
	}
	
	/**
	 * Sets the oil pressure.
	 *
	 * @param oilPressure the oil pressure
	 * @return the engine status
	 */
	public EngineStatus setOilPressure(Float oilPressure) {
		this.oilPressure = oilPressure;
		return this;
	}
	
	/**
	 * Sets the exhaust gas temp C.
	 *
	 * @param exhaustGasTempC the exhaust gas temp C
	 * @return the engine status
	 */
	public EngineStatus setExhaustGasTempC(Float exhaustGasTempC) {
		this.exhaustGasTempC = exhaustGasTempC;
		return this;
	}
	
	/**
	 * Sets the N 1 pct.
	 *
	 * @param n1Pct the n 1 pct
	 * @return the engine status
	 */
	public EngineStatus setN1Pct(Float n1Pct) {
		this.n1Pct = n1Pct;
		return this;
	}
	
	/**
	 * Sets the N 2 pct.
	 *
	 * @param n2Pct the n 2 pct
	 * @return the engine status
	 */
	public EngineStatus setN2Pct(Float n2Pct) {
		this.n2Pct = n2Pct;
		return this;
	}
	
	/**
	 * Gets the engine number.
	 *
	 * @return the engine number
	 */
	public Integer getEngineNumber() {
		return engineNumber;
	}
	
	/**
	 * Sets the engine number.
	 *
	 * @param engineNumber the engine number
	 * @return the engine status
	 */
	public EngineStatus setEngineNumber(Integer engineNumber) {
		this.engineNumber = engineNumber;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EngineStatus [engineStatusUuid=" + engineStatusUuid + ", flightStatusUuid=" + flightStatusUuid
				+ ", engineUuid=" + engineUuid + ", engineNumber=" + engineNumber + ", oilTempC=" + oilTempC
				+ ", oilPressure=" + oilPressure + ", exhaustGasTempC=" + exhaustGasTempC + ", n1Pct=" + n1Pct
				+ ", n2Pct=" + n2Pct + ", flightStatusTs=" + engineStatusTs + "]";
	}
	
	/**
	 * Gets the engine status uuid.
	 *
	 * @return the engine status uuid
	 */
	public String getEngineStatusUuid() {
		return engineStatusUuid;
	}
	
	/**
	 * Sets the engine status uuid.
	 *
	 * @param engineStatusUuid the engine status uuid
	 * @return the engine status
	 */
	public EngineStatus setEngineStatusUuid(String engineStatusUuid) {
		this.engineStatusUuid = engineStatusUuid;
		return this;
	}
	
	/**
	 * Gets the flight status uuid.
	 *
	 * @return the flight status uuid
	 */
	public String getFlightStatusUuid() {
		return flightStatusUuid;
	}
	
	/**
	 * Sets the flight status uuid.
	 *
	 * @param flightStatusUuid the flight status uuid
	 * @return the engine status
	 */
	public EngineStatus setFlightStatusUuid(String flightStatusUuid) {
		this.flightStatusUuid = flightStatusUuid;
		return this;
	}
	
	/**
	 * Gets the engine status ts.
	 *
	 * @return the engine status ts
	 */
	public Long getEngineStatusTs() {
		return engineStatusTs;
	}
	
	/**
	 * Sets the engine status ts.
	 *
	 * @param engineStatusTs the engine status ts
	 * @return the engine status
	 */
	public EngineStatus setEngineStatusTs(Long engineStatusTs) {
		this.engineStatusTs = engineStatusTs;
		return this;
	}
	
	
}
