package com.pzybrick.iote2e.stream.persist;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class PillsDispensedVo implements Serializable {
	private static final long serialVersionUID = -7663024167514190346L;
	public enum DispenseState {PENDING, DISPENSING, DISPENSED, CONFIRMED};
	private String pillsDispensedUuid;
	private String loginName;
	private String sourceName;
	private String actuatorName;
	private String dispenseState;
	private Integer numToDispense;
	private Integer numDispensed;
	private Integer delta;
	private Timestamp statePendingTs;
	private Timestamp stateDispensingTs;
	private Timestamp stateDispensedTs;
	private Timestamp stateConfirmedTs;
	private Timestamp insertTs;
	
	
	public PillsDispensedVo() {
		
	}
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPillsDispensedUuid() {
		return pillsDispensedUuid;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getActuatorName() {
		return actuatorName;
	}

	public String getDispenseState() {
		return dispenseState;
	}

	public Integer getNumToDispense() {
		return numToDispense;
	}

	public Integer getNumDispensed() {
		return numDispensed;
	}

	public Integer getDelta() {
		return delta;
	}

	public Timestamp getStatePendingTs() {
		return statePendingTs;
	}

	public Timestamp getStateDispensingTs() {
		return stateDispensingTs;
	}

	public Timestamp getStateDispensedTs() {
		return stateDispensedTs;
	}

	public Timestamp getStateConfirmedTs() {
		return stateConfirmedTs;
	}

	public Timestamp getInsertTs() {
		return insertTs;
	}

	public PillsDispensedVo setPillsDispensedUuid(String pillsDispensedUuid) {
		this.pillsDispensedUuid = pillsDispensedUuid;
		return this;
	}

	public PillsDispensedVo setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	public PillsDispensedVo setActuatorName(String actuatorName) {
		this.actuatorName = actuatorName;
		return this;
	}

	public PillsDispensedVo setDispenseState(String dispenseState) {
		this.dispenseState = dispenseState;
		return this;
	}

	public PillsDispensedVo setNumToDispense(Integer numToDispense) {
		this.numToDispense = numToDispense;
		return this;
	}

	public PillsDispensedVo setNumDispensed(Integer numDispensed) {
		this.numDispensed = numDispensed;
		return this;
	}

	public PillsDispensedVo setDelta(Integer delta) {
		this.delta = delta;
		return this;
	}

	public PillsDispensedVo setStatePendingTs(Timestamp statePendingTs) {
		this.statePendingTs = statePendingTs;
		return this;
	}

	public PillsDispensedVo setStateDispensingTs(Timestamp stateDispensingTs) {
		this.stateDispensingTs = stateDispensingTs;
		return this;
	}

	public PillsDispensedVo setStateDispensedTs(Timestamp stateDispensedTs) {
		this.stateDispensedTs = stateDispensedTs;
		return this;
	}

	public PillsDispensedVo setStateConfirmedTs(Timestamp stateConfirmedTs) {
		this.stateConfirmedTs = stateConfirmedTs;
		return this;
	}

	public PillsDispensedVo setInsertTs(Timestamp insertTs) {
		this.insertTs = insertTs;
		return this;
	}

	@Override
	public String toString() {
		return "PillsDispensedVo [pillsDispensedUuid=" + pillsDispensedUuid + ", loginName=" + loginName
				+ ", sourceName=" + sourceName + ", actuatorName=" + actuatorName + ", dispenseState=" + dispenseState
				+ ", numToDispense=" + numToDispense + ", numDispensed=" + numDispensed + ", delta=" + delta
				+ ", statePendingTs=" + statePendingTs + ", stateDispensingTs=" + stateDispensingTs
				+ ", stateDispensedTs=" + stateDispensedTs + ", stateConfirmedTs=" + stateConfirmedTs + ", insertTs="
				+ insertTs + "]";
	}

	public String getSourceName() {
		return sourceName;
	}

	public PillsDispensedVo setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}


}
