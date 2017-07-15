package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BodyTemperatureVo {
	private static final Logger logger = LogManager.getLogger(BodyTemperatureVo.class);
	private String bodyTemperatureUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
	private Timestamp effectiveTimeFrame;
	private String descriptiveStatistic;
	private String userNotes;
	private String measurementLocation;
	private String bodyTemperatureUnit;
	private float bodyTemperatureValue;
	private Timestamp insertTs;


	public BodyTemperatureVo() {
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
	public BodyTemperatureVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public BodyTemperatureVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public BodyTemperatureVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public BodyTemperatureVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public BodyTemperatureVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public BodyTemperatureVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bodyTemperatureUnit == null) ? 0 : bodyTemperatureUnit.hashCode());
		result = prime * result + ((bodyTemperatureUuid == null) ? 0 : bodyTemperatureUuid.hashCode());
		result = prime * result + Float.floatToIntBits(bodyTemperatureValue);
		result = prime * result + ((descriptiveStatistic == null) ? 0 : descriptiveStatistic.hashCode());
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((measurementLocation == null) ? 0 : measurementLocation.hashCode());
		result = prime * result + ((userNotes == null) ? 0 : userNotes.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BodyTemperatureVo other = (BodyTemperatureVo) obj;
		if (bodyTemperatureUnit == null) {
			if (other.bodyTemperatureUnit != null)
				return false;
		} else if (!bodyTemperatureUnit.equals(other.bodyTemperatureUnit))
			return false;
		if (bodyTemperatureUuid == null) {
			if (other.bodyTemperatureUuid != null)
				return false;
		} else if (!bodyTemperatureUuid.equals(other.bodyTemperatureUuid))
			return false;
		if (Float.floatToIntBits(bodyTemperatureValue) != Float.floatToIntBits(other.bodyTemperatureValue))
			return false;
		if (descriptiveStatistic == null) {
			if (other.descriptiveStatistic != null)
				return false;
		} else if (!descriptiveStatistic.equals(other.descriptiveStatistic))
			return false;
		if (effectiveTimeFrame == null) {
			if (other.effectiveTimeFrame != null)
				return false;
		} else if (!effectiveTimeFrame.equals(other.effectiveTimeFrame))
			return false;
		if (hdrModality == null) {
			if (other.hdrModality != null)
				return false;
		} else if (!hdrModality.equals(other.hdrModality))
			return false;
		if (hdrSchemaNamespace == null) {
			if (other.hdrSchemaNamespace != null)
				return false;
		} else if (!hdrSchemaNamespace.equals(other.hdrSchemaNamespace))
			return false;
		if (hdrSchemaVersion == null) {
			if (other.hdrSchemaVersion != null)
				return false;
		} else if (!hdrSchemaVersion.equals(other.hdrSchemaVersion))
			return false;
		if (hdrSourceCreationDateTime == null) {
			if (other.hdrSourceCreationDateTime != null)
				return false;
		} else if (!hdrSourceCreationDateTime.equals(other.hdrSourceCreationDateTime))
			return false;
		if (hdrSourceName == null) {
			if (other.hdrSourceName != null)
				return false;
		} else if (!hdrSourceName.equals(other.hdrSourceName))
			return false;
		if (hdrUserId == null) {
			if (other.hdrUserId != null)
				return false;
		} else if (!hdrUserId.equals(other.hdrUserId))
			return false;
		if (measurementLocation == null) {
			if (other.measurementLocation != null)
				return false;
		} else if (!measurementLocation.equals(other.measurementLocation))
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		return true;
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
