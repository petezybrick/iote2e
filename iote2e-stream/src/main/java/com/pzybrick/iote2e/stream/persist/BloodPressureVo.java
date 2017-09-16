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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPointHeader;



/**
 * The Class BloodPressureVo.
 */
public class BloodPressureVo extends OmhVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BloodPressureVo.class);
	
	/** The blood pressure uuid. */
	private String bloodPressureUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The descriptive statistic. */
	private String descriptiveStatistic;
	
	/** The user notes. */
	private String userNotes;
	
	/** The position during measurement. */
	private String positionDuringMeasurement;
	
	/** The systolic blood pressure unit. */
	private String systolicBloodPressureUnit;
	
	/** The systolic blood pressure value. */
	private int systolicBloodPressureValue;
	
	/** The diastolic blood pressure unit. */
	private String diastolicBloodPressureUnit;
	
	/** The diastolic blood pressure value. */
	private int diastolicBloodPressureValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new blood pressure vo.
	 */
	public BloodPressureVo() {
	}


	/**
	 * Instantiates a new blood pressure vo.
	 *
	 * @param header the header
	 * @param bloodPressure the blood pressure
	 * @throws SQLException the SQL exception
	 */
	public BloodPressureVo( DataPointHeader header, BloodPressure bloodPressure ) throws SQLException {
		this.bloodPressureUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( bloodPressure.getEffectiveTimeFrame().getDateTime() ));
		this.descriptiveStatistic = bloodPressure.getDescriptiveStatistic().name();
		this.userNotes = bloodPressure.getUserNotes();
		this.positionDuringMeasurement = bloodPressure.getPositionDuringMeasurement().name();
		this.systolicBloodPressureUnit = bloodPressure.getSystolicBloodPressure().getUnit();
		this.systolicBloodPressureValue = bloodPressure.getSystolicBloodPressure().getValue().intValue();
		this.diastolicBloodPressureUnit = bloodPressure.getDiastolicBloodPressure().getUnit();
		this.diastolicBloodPressureValue = bloodPressure.getDiastolicBloodPressure().getValue().intValue();
	}


	/**
	 * Instantiates a new blood pressure vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
	public BloodPressureVo(ResultSet rs) throws SQLException {
		this.bloodPressureUuid = rs.getString("blood_pressure_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.descriptiveStatistic = rs.getString("descriptive_statistic");
		this.userNotes = rs.getString("user_notes");
		this.positionDuringMeasurement = rs.getString("position_during_measurement");
		this.systolicBloodPressureUnit = rs.getString("systolic_blood_pressure_unit");
		this.systolicBloodPressureValue = rs.getInt("systolic_blood_pressure_value");
		this.diastolicBloodPressureUnit = rs.getString("diastolic_blood_pressure_unit");
		this.diastolicBloodPressureValue = rs.getInt("diastolic_blood_pressure_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	/**
	 * Gets the blood pressure uuid.
	 *
	 * @return the blood pressure uuid
	 */
	public String getBloodPressureUuid() {
		return bloodPressureUuid;
	}


	/**
	 * Gets the effective time frame.
	 *
	 * @return the effective time frame
	 */
	public Timestamp getEffectiveTimeFrame() {
		return effectiveTimeFrame;
	}


	/**
	 * Gets the descriptive statistic.
	 *
	 * @return the descriptive statistic
	 */
	public String getDescriptiveStatistic() {
		return descriptiveStatistic;
	}


	/**
	 * Gets the user notes.
	 *
	 * @return the user notes
	 */
	public String getUserNotes() {
		return userNotes;
	}


	/**
	 * Gets the position during measurement.
	 *
	 * @return the position during measurement
	 */
	public String getPositionDuringMeasurement() {
		return positionDuringMeasurement;
	}


	/**
	 * Gets the systolic blood pressure unit.
	 *
	 * @return the systolic blood pressure unit
	 */
	public String getSystolicBloodPressureUnit() {
		return systolicBloodPressureUnit;
	}


	/**
	 * Gets the systolic blood pressure value.
	 *
	 * @return the systolic blood pressure value
	 */
	public int getSystolicBloodPressureValue() {
		return systolicBloodPressureValue;
	}


	/**
	 * Gets the diastolic blood pressure unit.
	 *
	 * @return the diastolic blood pressure unit
	 */
	public String getDiastolicBloodPressureUnit() {
		return diastolicBloodPressureUnit;
	}


	/**
	 * Gets the diastolic blood pressure value.
	 *
	 * @return the diastolic blood pressure value
	 */
	public int getDiastolicBloodPressureValue() {
		return diastolicBloodPressureValue;
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
	 * Sets the blood pressure uuid.
	 *
	 * @param bloodPressureUuid the blood pressure uuid
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setBloodPressureUuid(String bloodPressureUuid) {
		this.bloodPressureUuid = bloodPressureUuid;
		return this;
	}


	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setEffectiveTimeFrame(Timestamp effectiveTimeFrame) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}


	/**
	 * Sets the descriptive statistic.
	 *
	 * @param descriptiveStatistic the descriptive statistic
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setDescriptiveStatistic(String descriptiveStatistic) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}


	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setUserNotes(String userNotes) {
		this.userNotes = userNotes;
		return this;
	}


	/**
	 * Sets the position during measurement.
	 *
	 * @param positionDuringMeasurement the position during measurement
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setPositionDuringMeasurement(String positionDuringMeasurement) {
		this.positionDuringMeasurement = positionDuringMeasurement;
		return this;
	}


	/**
	 * Sets the systolic blood pressure unit.
	 *
	 * @param systolicBloodPressureUnit the systolic blood pressure unit
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setSystolicBloodPressureUnit(String systolicBloodPressureUnit) {
		this.systolicBloodPressureUnit = systolicBloodPressureUnit;
		return this;
	}


	/**
	 * Sets the systolic blood pressure value.
	 *
	 * @param systolicBloodPressureValue the systolic blood pressure value
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setSystolicBloodPressureValue(int systolicBloodPressureValue) {
		this.systolicBloodPressureValue = systolicBloodPressureValue;
		return this;
	}


	/**
	 * Sets the diastolic blood pressure unit.
	 *
	 * @param diastolicBloodPressureUnit the diastolic blood pressure unit
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setDiastolicBloodPressureUnit(String diastolicBloodPressureUnit) {
		this.diastolicBloodPressureUnit = diastolicBloodPressureUnit;
		return this;
	}


	/**
	 * Sets the diastolic blood pressure value.
	 *
	 * @param diastolicBloodPressureValue the diastolic blood pressure value
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setDiastolicBloodPressureValue(int diastolicBloodPressureValue) {
		this.diastolicBloodPressureValue = diastolicBloodPressureValue;
		return this;
	}


	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setInsertTs(Timestamp insertTs) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the blood pressure vo
	 */
	public BloodPressureVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
}

// BloodPressureVo bloodPressureVo = new BloodPressureVo()
//	 .setBloodPressureUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setDescriptiveStatistic("xxx")
//	 .setUserNotes("xxx")
//	 .setPositionDuringMeasurement("xxx")
//	 .setSystolicBloodPressureUnit("xxx")
//	 .setSystolicBloodPressureValue("xxx")
//	 .setDiastolicBloodPressureUnit("xxx")
//	 .setDiastolicBloodPressureValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
