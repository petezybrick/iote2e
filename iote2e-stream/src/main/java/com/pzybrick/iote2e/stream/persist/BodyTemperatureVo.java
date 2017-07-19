package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPointHeader;


public class BodyTemperatureVo extends OmhVo  {
	private static final Logger logger = LogManager.getLogger(BodyTemperatureVo.class);
	private String bodyTemperatureUuid;
	private Timestamp effectiveTimeFrame;
	private String descriptiveStatistic;
	private String userNotes;
	private String measurementLocation;
	private String bodyTemperatureUnit;
	private float bodyTemperatureValue;
	private Timestamp insertTs;


	public BodyTemperatureVo() {
	}
	

	public BodyTemperatureVo( DataPointHeader header, BodyTemperature bodyTemperature ) throws SQLException {
		this.bodyTemperatureUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( bodyTemperature.getEffectiveTimeFrame().getDateTime() ));
		this.descriptiveStatistic = bodyTemperature.getDescriptiveStatistic().name();
		this.userNotes = bodyTemperature.getUserNotes();
		this.measurementLocation = bodyTemperature.getMeasurementLocation().name();
		this.bodyTemperatureUnit = bodyTemperature.getBodyTemperature().getUnit();
		this.bodyTemperatureValue = bodyTemperature.getBodyTemperature().getValue().floatValue();
	}


	public BodyTemperatureVo(ResultSet rs) throws SQLException {
		this.bodyTemperatureUuid = rs.getString("body_temperature_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.descriptiveStatistic = rs.getString("descriptive_statistic");
		this.userNotes = rs.getString("user_notes");
		this.measurementLocation = rs.getString("measurement_location");
		this.bodyTemperatureUnit = rs.getString("body_temperature_unit");
		this.bodyTemperatureValue = rs.getFloat("body_temperature_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getBodyTemperatureUuid() {
		return this.bodyTemperatureUuid;
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
	public String getMeasurementLocation() {
		return this.measurementLocation;
	}
	public String getBodyTemperatureUnit() {
		return this.bodyTemperatureUnit;
	}
	public float getBodyTemperatureValue() {
		return this.bodyTemperatureValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public BodyTemperatureVo setBodyTemperatureUuid( String bodyTemperatureUuid ) {
		this.bodyTemperatureUuid = bodyTemperatureUuid;
		return this;
	}
	public BodyTemperatureVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public BodyTemperatureVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	public BodyTemperatureVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public BodyTemperatureVo setMeasurementLocation( String measurementLocation ) {
		this.measurementLocation = measurementLocation;
		return this;
	}
	public BodyTemperatureVo setBodyTemperatureUnit( String bodyTemperatureUnit ) {
		this.bodyTemperatureUnit = bodyTemperatureUnit;
		return this;
	}
	public BodyTemperatureVo setBodyTemperatureValue( float bodyTemperatureValue ) {
		this.bodyTemperatureValue = bodyTemperatureValue;
		return this;
	}
	public BodyTemperatureVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	public BodyTemperatureVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	public BodyTemperatureVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	public BodyTemperatureVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	public BodyTemperatureVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	public BodyTemperatureVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	public BodyTemperatureVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
}

// BodyTemperatureVo bodyTemperatureVo = new BodyTemperatureVo()
//	 .setBodyTemperatureUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setDescriptiveStatistic("xxx")
//	 .setUserNotes("xxx")
//	 .setMeasurementLocation("xxx")
//	 .setBodyTemperatureUnit("xxx")
//	 .setBodyTemperatureValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
