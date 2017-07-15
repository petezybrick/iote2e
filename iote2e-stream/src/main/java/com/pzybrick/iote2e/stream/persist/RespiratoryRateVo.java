package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RespiratoryRateVo {
	private static final Logger logger = LogManager.getLogger(RespiratoryRateVo.class);
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
		this.respiratoryRateValue = rs.getFloat("respiratory_rate_value");
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descriptiveStatistic == null) ? 0 : descriptiveStatistic.hashCode());
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((respiratoryRateUnit == null) ? 0 : respiratoryRateUnit.hashCode());
		result = prime * result + ((respiratoryRateUuid == null) ? 0 : respiratoryRateUuid.hashCode());
		result = prime * result + Float.floatToIntBits(respiratoryRateValue);
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
		RespiratoryRateVo other = (RespiratoryRateVo) obj;
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
		if (respiratoryRateUnit == null) {
			if (other.respiratoryRateUnit != null)
				return false;
		} else if (!respiratoryRateUnit.equals(other.respiratoryRateUnit))
			return false;
		if (respiratoryRateUuid == null) {
			if (other.respiratoryRateUuid != null)
				return false;
		} else if (!respiratoryRateUuid.equals(other.respiratoryRateUuid))
			return false;
		if (Float.floatToIntBits(respiratoryRateValue) != Float.floatToIntBits(other.respiratoryRateValue))
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
