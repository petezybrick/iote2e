package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPointHeader;


public class BloodPressureVo extends OmhVo {
	private static final Logger logger = LogManager.getLogger(BloodPressureVo.class);
	private String bloodPressureUuid;
	private Timestamp effectiveTimeFrame;
	private String descriptiveStatistic;
	private String userNotes;
	private String positionDuringMeasurement;
	private String systolicBloodPressureUnit;
	private int systolicBloodPressureValue;
	private String diastolicBloodPressureUnit;
	private int diastolicBloodPressureValue;
	private Timestamp insertTs;


	public BloodPressureVo() {
	}


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


	public String getBloodPressureUuid() {
		return bloodPressureUuid;
	}


	public Timestamp getEffectiveTimeFrame() {
		return effectiveTimeFrame;
	}


	public String getDescriptiveStatistic() {
		return descriptiveStatistic;
	}


	public String getUserNotes() {
		return userNotes;
	}


	public String getPositionDuringMeasurement() {
		return positionDuringMeasurement;
	}


	public String getSystolicBloodPressureUnit() {
		return systolicBloodPressureUnit;
	}


	public int getSystolicBloodPressureValue() {
		return systolicBloodPressureValue;
	}


	public String getDiastolicBloodPressureUnit() {
		return diastolicBloodPressureUnit;
	}


	public int getDiastolicBloodPressureValue() {
		return diastolicBloodPressureValue;
	}


	public Timestamp getInsertTs() {
		return insertTs;
	}


	public BloodPressureVo setBloodPressureUuid(String bloodPressureUuid) {
		this.bloodPressureUuid = bloodPressureUuid;
		return this;
	}


	public BloodPressureVo setEffectiveTimeFrame(Timestamp effectiveTimeFrame) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}


	public BloodPressureVo setDescriptiveStatistic(String descriptiveStatistic) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}


	public BloodPressureVo setUserNotes(String userNotes) {
		this.userNotes = userNotes;
		return this;
	}


	public BloodPressureVo setPositionDuringMeasurement(String positionDuringMeasurement) {
		this.positionDuringMeasurement = positionDuringMeasurement;
		return this;
	}


	public BloodPressureVo setSystolicBloodPressureUnit(String systolicBloodPressureUnit) {
		this.systolicBloodPressureUnit = systolicBloodPressureUnit;
		return this;
	}


	public BloodPressureVo setSystolicBloodPressureValue(int systolicBloodPressureValue) {
		this.systolicBloodPressureValue = systolicBloodPressureValue;
		return this;
	}


	public BloodPressureVo setDiastolicBloodPressureUnit(String diastolicBloodPressureUnit) {
		this.diastolicBloodPressureUnit = diastolicBloodPressureUnit;
		return this;
	}


	public BloodPressureVo setDiastolicBloodPressureValue(int diastolicBloodPressureValue) {
		this.diastolicBloodPressureValue = diastolicBloodPressureValue;
		return this;
	}


	public BloodPressureVo setInsertTs(Timestamp insertTs) {
		this.insertTs = insertTs;
		return this;
	}

	public BloodPressureVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	public BloodPressureVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	public BloodPressureVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	public BloodPressureVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	public BloodPressureVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

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
