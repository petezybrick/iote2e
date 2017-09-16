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
package com.pzybrick.iote2e.stream.persist;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Timestamp;


/**
 * The Class PillsDispensedVo.
 */
public class PillsDispensedVo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7663024167514190346L;
	
	/**
	 * The Enum DispenseState.
	 */
	public enum DispenseState {
/** The pending. */
PENDING, 
 /** The dispensing. */
 DISPENSING, 
 /** The dispensed. */
 DISPENSED, 
 /** The confirming. */
 CONFIRMING, 
 /** The confirmed. */
 CONFIRMED};
	
	/** The pills dispensed uuid. */
	private String pillsDispensedUuid;
	
	/** The login name. */
	private String loginName;
	
	/** The source name. */
	private String sourceName;
	
	/** The actuator name. */
	private String actuatorName;
	
	/** The dispense state. */
	private String dispenseState;
	
	/** The num to dispense. */
	private Integer numToDispense;
	
	/** The num dispensed. */
	private Integer numDispensed;
	
	/** The delta. */
	private Integer delta;
	
	/** The state pending ts. */
	private Timestamp statePendingTs;
	
	/** The state dispensing ts. */
	private Timestamp stateDispensingTs;
	
	/** The state dispensed ts. */
	private Timestamp stateDispensedTs;
	
	/** The state confirmed ts. */
	private Timestamp stateConfirmedTs;
	
	/** The insert ts. */
	private Timestamp insertTs;
	
	
	/**
	 * Instantiates a new pills dispensed vo.
	 */
	public PillsDispensedVo() {
		
	}
	
	/**
	 * Instantiates a new pills dispensed vo.
	 *
	 * @param rs the rs
	 * @throws Exception the exception
	 */
	public PillsDispensedVo( ResultSet rs ) throws Exception {
		this.pillsDispensedUuid = rs.getString("pills_dispensed_uuid");
		this.loginName = rs.getString("login_name");
		this.sourceName = rs.getString("source_name");
		this.actuatorName = rs.getString("actuator_name");
		this.dispenseState = rs.getString("dispense_state");
		this.numToDispense = rs.getInt("num_to_dispense");
		this.numDispensed = rs.getInt("num_dispensed");
		if( rs.wasNull()) this.numDispensed = null;
		this.delta = rs.getInt("delta");
		if( rs.wasNull()) this.delta = null;
		this.statePendingTs = rs.getTimestamp("state_pending_ts");
		this.stateDispensingTs = rs.getTimestamp("state_dispensing_ts");
		if( rs.wasNull()) this.stateDispensingTs = null;
		this.stateDispensedTs = rs.getTimestamp("state_dispensed_ts");
		if( rs.wasNull()) this.stateDispensedTs = null;
		this.stateConfirmedTs = rs.getTimestamp("state_confirmed_ts");
		if( rs.wasNull()) this.stateConfirmedTs = null;
		this.insertTs = rs.getTimestamp("insert_ts");
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the pills dispensed uuid.
	 *
	 * @return the pills dispensed uuid
	 */
	public String getPillsDispensedUuid() {
		return pillsDispensedUuid;
	}

	/**
	 * Gets the login name.
	 *
	 * @return the login name
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Gets the actuator name.
	 *
	 * @return the actuator name
	 */
	public String getActuatorName() {
		return actuatorName;
	}

	/**
	 * Gets the dispense state.
	 *
	 * @return the dispense state
	 */
	public String getDispenseState() {
		return dispenseState;
	}

	/**
	 * Gets the num to dispense.
	 *
	 * @return the num to dispense
	 */
	public Integer getNumToDispense() {
		return numToDispense;
	}

	/**
	 * Gets the num dispensed.
	 *
	 * @return the num dispensed
	 */
	public Integer getNumDispensed() {
		return numDispensed;
	}

	/**
	 * Gets the delta.
	 *
	 * @return the delta
	 */
	public Integer getDelta() {
		return delta;
	}

	/**
	 * Gets the state pending ts.
	 *
	 * @return the state pending ts
	 */
	public Timestamp getStatePendingTs() {
		return statePendingTs;
	}

	/**
	 * Gets the state dispensing ts.
	 *
	 * @return the state dispensing ts
	 */
	public Timestamp getStateDispensingTs() {
		return stateDispensingTs;
	}

	/**
	 * Gets the state dispensed ts.
	 *
	 * @return the state dispensed ts
	 */
	public Timestamp getStateDispensedTs() {
		return stateDispensedTs;
	}

	/**
	 * Gets the state confirmed ts.
	 *
	 * @return the state confirmed ts
	 */
	public Timestamp getStateConfirmedTs() {
		return stateConfirmedTs;
	}

	/**
	 * Gets the insert ts.
	 *
	 * @return the insert ts
	 */
	public Timestamp getInsertTs() {
		return insertTs;
	}

	/**
	 * Sets the pills dispensed uuid.
	 *
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setPillsDispensedUuid(String pillsDispensedUuid) {
		this.pillsDispensedUuid = pillsDispensedUuid;
		return this;
	}

	/**
	 * Sets the login name.
	 *
	 * @param loginName the login name
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	/**
	 * Sets the actuator name.
	 *
	 * @param actuatorName the actuator name
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
		return this;
	}

	/**
	 * Sets the dispense state.
	 *
	 * @param dispenseState the dispense state
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setDispenseState(String dispenseState) {
		this.dispenseState = dispenseState;
		return this;
	}

	/**
	 * Sets the num to dispense.
	 *
	 * @param numToDispense the num to dispense
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setNumToDispense(Integer numToDispense) {
		this.numToDispense = numToDispense;
		return this;
	}

	/**
	 * Sets the num dispensed.
	 *
	 * @param numDispensed the num dispensed
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setNumDispensed(Integer numDispensed) {
		this.numDispensed = numDispensed;
		return this;
	}

	/**
	 * Sets the delta.
	 *
	 * @param delta the delta
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setDelta(Integer delta) {
		this.delta = delta;
		return this;
	}

	/**
	 * Sets the state pending ts.
	 *
	 * @param statePendingTs the state pending ts
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setStatePendingTs(Timestamp statePendingTs) {
		this.statePendingTs = statePendingTs;
		return this;
	}

	/**
	 * Sets the state dispensing ts.
	 *
	 * @param stateDispensingTs the state dispensing ts
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setStateDispensingTs(Timestamp stateDispensingTs) {
		this.stateDispensingTs = stateDispensingTs;
		return this;
	}

	/**
	 * Sets the state dispensed ts.
	 *
	 * @param stateDispensedTs the state dispensed ts
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setStateDispensedTs(Timestamp stateDispensedTs) {
		this.stateDispensedTs = stateDispensedTs;
		return this;
	}

	/**
	 * Sets the state confirmed ts.
	 *
	 * @param stateConfirmedTs the state confirmed ts
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setStateConfirmedTs(Timestamp stateConfirmedTs) {
		this.stateConfirmedTs = stateConfirmedTs;
		return this;
	}

	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setInsertTs(Timestamp insertTs) {
		this.insertTs = insertTs;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PillsDispensedVo [pillsDispensedUuid=" + pillsDispensedUuid + ", loginName=" + loginName
				+ ", sourceName=" + sourceName + ", actuatorName=" + actuatorName + ", dispenseState=" + dispenseState
				+ ", numToDispense=" + numToDispense + ", numDispensed=" + numDispensed + ", delta=" + delta
				+ ", statePendingTs=" + statePendingTs + ", stateDispensingTs=" + stateDispensingTs
				+ ", stateDispensedTs=" + stateDispensedTs + ", stateConfirmedTs=" + stateConfirmedTs + ", insertTs="
				+ insertTs + "]";
	}

	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Sets the source name.
	 *
	 * @param sourceName the source name
	 * @return the pills dispensed vo
	 */
	public PillsDispensedVo setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}


}
