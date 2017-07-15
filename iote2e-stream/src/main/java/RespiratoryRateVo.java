package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RespiratoryRateVo {
	private String respiratoryRateUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
	private Timestamp effectiveTimeFrame;
	private String userNotes;
	private String descriptiveStatistic;
	private String temporalRelationshipToPhysicalActivity;
	private String respiratoryRateUnit;
	private float respiratoryRateValue;
	private Timestamp insertTs;


	public RespiratoryRateVo() {
	}


	public RespiratoryRateVo(ResultSet rs) throws SQLException {
		this.respiratoryRateUuid = rs.getString("respiratory_rate_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.userNotes = rs.getString("user_notes");
		this.descriptiveStatistic = rs.getString("descriptive_statistic");
		this.temporalRelationshipToPhysicalActivity = rs.getString("temporal_relationship_to_physical_activity");
		this.respiratoryRateUnit = rs.getString("respiratory_rate_unit");
		this.respiratoryRateValue = rs.getDecimal("respiratory_rate_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getRespiratoryRateUuid() {
		return this.respiratoryRateUuid;
	}
	public String getHdrSourceName() {
		return this.hdrSourceName;
	}
	public Timestamp getHdrSourceCreationDateTime() {
		return this.hdrSourceCreationDateTime;
	}
	public String getHdrUserId() {
		return this.hdrUserId;
	}
	public String getHdrModality() {
		return this.hdrModality;
	}
	public String getHdrSchemaNamespace() {
		return this.hdrSchemaNamespace;
	}
	public String getHdrSchemaVersion() {
		return this.hdrSchemaVersion;
	}
	public Timestamp getEffectiveTimeFrame() {
		return this.effectiveTimeFrame;
	}
	public String getUserNotes() {
		return this.userNotes;
	}
	public String getDescriptiveStatistic() {
		return this.descriptiveStatistic;
	}
	public String getTemporalRelationshipToPhysicalActivity() {
		return this.temporalRelationshipToPhysicalActivity;
	}
	public String getRespiratoryRateUnit() {
		return this.respiratoryRateUnit;
	}
	public float getRespiratoryRateValue() {
		return this.respiratoryRateValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public RespiratoryRateVo setRespiratoryRateUuid( String respiratoryRateUuid ) {
		this.respiratoryRateUuid = respiratoryRateUuid;
		return this;
	}
	public RespiratoryRateVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public RespiratoryRateVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public RespiratoryRateVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public RespiratoryRateVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public RespiratoryRateVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public RespiratoryRateVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
	public RespiratoryRateVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public RespiratoryRateVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public RespiratoryRateVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	public RespiratoryRateVo setTemporalRelationshipToPhysicalActivity( String temporalRelationshipToPhysicalActivity ) {
		this.temporalRelationshipToPhysicalActivity = temporalRelationshipToPhysicalActivity;
		return this;
	}
	public RespiratoryRateVo setRespiratoryRateUnit( String respiratoryRateUnit ) {
		this.respiratoryRateUnit = respiratoryRateUnit;
		return this;
	}
	public RespiratoryRateVo setRespiratoryRateValue( float respiratoryRateValue ) {
		this.respiratoryRateValue = respiratoryRateValue;
		return this;
	}
	public RespiratoryRateVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}
}

// RespiratoryRateVo respiratoryRateVo = new RespiratoryRateVo()
//	 .setRespiratoryRateUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setUserNotes("xxx")
//	 .setDescriptiveStatistic("xxx")
//	 .setTemporalRelationshipToPhysicalActivity("xxx")
//	 .setRespiratoryRateUnit("xxx")
//	 .setRespiratoryRateValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
