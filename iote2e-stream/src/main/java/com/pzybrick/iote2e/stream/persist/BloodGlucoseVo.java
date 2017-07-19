package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.DataPointHeader;


public class BloodGlucoseVo extends OmhVo {
	private static final Logger logger = LogManager.getLogger(BloodGlucoseVo.class);
	private String bloodGlucoseUuid;
	private Timestamp effectiveTimeFrame;
	private String descriptiveStatistic;
	private String userNotes;
	private String bloodSpecimenType;
	private String temporalRelationshipToMeal;
	private String temporalRelationshipToSleep;
	private String bloodGlucoseUnit;
	private int bloodGlucoseValue;
	private Timestamp insertTs;


	public BloodGlucoseVo() {
	}


	public BloodGlucoseVo( DataPointHeader header, BloodGlucose bloodGlucose ) throws SQLException {
		this.bloodGlucoseUuid = header.getId();
		setHeaderCommon(header);
		this.hdrSourceName = header.getAcquisitionProvenance().getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getAcquisitionProvenance().getSourceCreationDateTime()) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  header.getAcquisitionProvenance().getModality().name();
		this.hdrSchemaNamespace = header.getSchemaId().getNamespace();
		this.hdrSchemaVersion = header.getSchemaId().getVersion().toString();
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( bloodGlucose.getEffectiveTimeFrame().getDateTime() ));
		this.descriptiveStatistic = bloodGlucose.getDescriptiveStatistic().name();
		this.userNotes = bloodGlucose.getUserNotes();
		this.bloodSpecimenType = bloodGlucose.getBloodSpecimenType().name();
		this.temporalRelationshipToMeal = bloodGlucose.getTemporalRelationshipToMeal().name();
		this.temporalRelationshipToSleep = bloodGlucose.getTemporalRelationshipToSleep().name();
		this.bloodGlucoseUnit = bloodGlucose.getBloodGlucose().getUnit();
		this.bloodGlucoseValue = bloodGlucose.getBloodGlucose().getValue().intValue();
	}


	public BloodGlucoseVo(ResultSet rs) throws SQLException {
		this.bloodGlucoseUuid = rs.getString("blood_glucose_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.descriptiveStatistic = rs.getString("descriptive_statistic");
		this.userNotes = rs.getString("user_notes");
		this.bloodSpecimenType = rs.getString("blood_specimen_type");
		this.temporalRelationshipToMeal = rs.getString("temporal_relationship_to_meal");
		this.temporalRelationshipToSleep = rs.getString("temporal_relationship_to_sleep");
		this.bloodGlucoseUnit = rs.getString("blood_glucose_unit");
		this.bloodGlucoseValue = rs.getInt("blood_glucose_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getBloodGlucoseUuid() {
		return this.bloodGlucoseUuid;
	}
	public Timestamp getEffectiveTimeFrame() {
		return this.effectiveTimeFrame;
	}
	public String getDescriptiveStatistic() {
		return this.descriptiveStatistic;
	}
	public String getUserNotes() {
		return this.userNotes;
	}
	public String getBloodSpecimenType() {
		return this.bloodSpecimenType;
	}
	public String getTemporalRelationshipToMeal() {
		return this.temporalRelationshipToMeal;
	}
	public String getTemporalRelationshipToSleep() {
		return this.temporalRelationshipToSleep;
	}
	public String getBloodGlucoseUnit() {
		return this.bloodGlucoseUnit;
	}
	public int getBloodGlucoseValue() {
		return this.bloodGlucoseValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public BloodGlucoseVo setBloodGlucoseUuid( String bloodGlucoseUuid ) {
		this.bloodGlucoseUuid = bloodGlucoseUuid;
		return this;
	}

	public BloodGlucoseVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public BloodGlucoseVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	public BloodGlucoseVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public BloodGlucoseVo setBloodSpecimenType( String bloodSpecimenType ) {
		this.bloodSpecimenType = bloodSpecimenType;
		return this;
	}
	public BloodGlucoseVo setTemporalRelationshipToMeal( String temporalRelationshipToMeal ) {
		this.temporalRelationshipToMeal = temporalRelationshipToMeal;
		return this;
	}
	public BloodGlucoseVo setTemporalRelationshipToSleep( String temporalRelationshipToSleep ) {
		this.temporalRelationshipToSleep = temporalRelationshipToSleep;
		return this;
	}
	public BloodGlucoseVo setBloodGlucoseUnit( String bloodGlucoseUnit ) {
		this.bloodGlucoseUnit = bloodGlucoseUnit;
		return this;
	}
	public BloodGlucoseVo setBloodGlucoseValue( int bloodGlucoseValue ) {
		this.bloodGlucoseValue = bloodGlucoseValue;
		return this;
	}
	public BloodGlucoseVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	public BloodGlucoseVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	public BloodGlucoseVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	public BloodGlucoseVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	public BloodGlucoseVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	public BloodGlucoseVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	public BloodGlucoseVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
}

// BloodGlucoseVo bloodGlucoseVo = new BloodGlucoseVo()
//	 .setBloodGlucoseUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setDescriptiveStatistic("xxx")
//	 .setUserNotes("xxx")
//	 .setBloodSpecimenType("xxx")
//	 .setTemporalRelationshipToMeal("xxx")
//	 .setTemporalRelationshipToSleep("xxx")
//	 .setBloodGlucoseUnit("xxx")
//	 .setBloodGlucoseValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
