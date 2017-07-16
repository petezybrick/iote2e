package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HeartRateVo implements OmhVo  {
	private static final Logger logger = LogManager.getLogger(HeartRateVo.class);
	private String heartRateUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
	private Timestamp effectiveTimeFrame;
	private String userNotes;
	private String temporalRelationshipToPhysicalActivity;
	private String heartRateUnit;
	private int heartRateValue;
	private Timestamp insertTs;


	public HeartRateVo() {
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
	public HeartRateVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public HeartRateVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public HeartRateVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public HeartRateVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public HeartRateVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public HeartRateVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((heartRateUnit == null) ? 0 : heartRateUnit.hashCode());
		result = prime * result + ((heartRateUuid == null) ? 0 : heartRateUuid.hashCode());
		result = prime * result + heartRateValue;
		result = prime * result + ((temporalRelationshipToPhysicalActivity == null) ? 0
				: temporalRelationshipToPhysicalActivity.hashCode());
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
		HeartRateVo other = (HeartRateVo) obj;
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
		if (heartRateUnit == null) {
			if (other.heartRateUnit != null)
				return false;
		} else if (!heartRateUnit.equals(other.heartRateUnit))
			return false;
		if (heartRateUuid == null) {
			if (other.heartRateUuid != null)
				return false;
		} else if (!heartRateUuid.equals(other.heartRateUuid))
			return false;
		if (heartRateValue != other.heartRateValue)
			return false;
		if (temporalRelationshipToPhysicalActivity == null) {
			if (other.temporalRelationshipToPhysicalActivity != null)
				return false;
		} else if (!temporalRelationshipToPhysicalActivity.equals(other.temporalRelationshipToPhysicalActivity))
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		return true;
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
