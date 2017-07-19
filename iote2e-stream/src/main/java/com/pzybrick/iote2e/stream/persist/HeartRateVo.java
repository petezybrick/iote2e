package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.HeartRate;


public class HeartRateVo extends OmhVo {
	private static final Logger logger = LogManager.getLogger(HeartRateVo.class);
	private String heartRateUuid;
	private Timestamp effectiveTimeFrame;
	private String userNotes;
	private String temporalRelationshipToPhysicalActivity;
	private String heartRateUnit;
	private int heartRateValue;
	private Timestamp insertTs;


	public HeartRateVo() {
	}
	

	public HeartRateVo( DataPointHeader header, HeartRate heartRate ) throws SQLException {
		this.heartRateUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( heartRate.getEffectiveTimeFrame().getDateTime() ));
		this.userNotes = heartRate.getUserNotes();
		this.temporalRelationshipToPhysicalActivity = heartRate.getTemporalRelationshipToPhysicalActivity().name();
		this.heartRateUnit = heartRate.getHeartRate().getUnit();
		this.heartRateValue = heartRate.getHeartRate().getValue().intValue();
	}


	public HeartRateVo(ResultSet rs) throws SQLException {
		this.heartRateUuid = rs.getString("heart_rate_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.userNotes = rs.getString("user_notes");
		this.temporalRelationshipToPhysicalActivity = rs.getString("temporal_relationship_to_physical_activity");
		this.heartRateUnit = rs.getString("heart_rate_unit");
		this.heartRateValue = rs.getInt("heart_rate_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getHeartRateUuid() {
		return this.heartRateUuid;
	}
	public Timestamp getEffectiveTimeFrame() {
		return this.effectiveTimeFrame;
	}
	public String getUserNotes() {
		return this.userNotes;
	}
	public String getTemporalRelationshipToPhysicalActivity() {
		return this.temporalRelationshipToPhysicalActivity;
	}
	public String getHeartRateUnit() {
		return this.heartRateUnit;
	}
	public int getHeartRateValue() {
		return this.heartRateValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public HeartRateVo setHeartRateUuid( String heartRateUuid ) {
		this.heartRateUuid = heartRateUuid;
		return this;
	}
	public HeartRateVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public HeartRateVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public HeartRateVo setTemporalRelationshipToPhysicalActivity( String temporalRelationshipToPhysicalActivity ) {
		this.temporalRelationshipToPhysicalActivity = temporalRelationshipToPhysicalActivity;
		return this;
	}
	public HeartRateVo setHeartRateUnit( String heartRateUnit ) {
		this.heartRateUnit = heartRateUnit;
		return this;
	}
	public HeartRateVo setHeartRateValue( int heartRateValue ) {
		this.heartRateValue = heartRateValue;
		return this;
	}
	public HeartRateVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	public HeartRateVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	public HeartRateVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	public HeartRateVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	public HeartRateVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	public HeartRateVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	public HeartRateVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
}

// HeartRateVo heartRateVo = new HeartRateVo()
//	 .setHeartRateUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setUserNotes("xxx")
//	 .setTemporalRelationshipToPhysicalActivity("xxx")
//	 .setHeartRateUnit("xxx")
//	 .setHeartRateValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
