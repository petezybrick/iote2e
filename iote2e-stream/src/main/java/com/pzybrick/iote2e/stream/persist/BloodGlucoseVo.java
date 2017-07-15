package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BloodGlucoseVo {
	private static final Logger logger = LogManager.getLogger(BloodGlucoseVo.class);
	private String bloodGlucoseUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
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
	public BloodGlucoseVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public BloodGlucoseVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public BloodGlucoseVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public BloodGlucoseVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public BloodGlucoseVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public BloodGlucoseVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bloodGlucoseUnit == null) ? 0 : bloodGlucoseUnit.hashCode());
		result = prime * result + ((bloodGlucoseUuid == null) ? 0 : bloodGlucoseUuid.hashCode());
		result = prime * result + bloodGlucoseValue;
		result = prime * result + ((bloodSpecimenType == null) ? 0 : bloodSpecimenType.hashCode());
		result = prime * result + ((descriptiveStatistic == null) ? 0 : descriptiveStatistic.hashCode());
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((temporalRelationshipToMeal == null) ? 0 : temporalRelationshipToMeal.hashCode());
		result = prime * result + ((temporalRelationshipToSleep == null) ? 0 : temporalRelationshipToSleep.hashCode());
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
		BloodGlucoseVo other = (BloodGlucoseVo) obj;
		if (bloodGlucoseUnit == null) {
			if (other.bloodGlucoseUnit != null)
				return false;
		} else if (!bloodGlucoseUnit.equals(other.bloodGlucoseUnit))
			return false;
		if (bloodGlucoseUuid == null) {
			if (other.bloodGlucoseUuid != null)
				return false;
		} else if (!bloodGlucoseUuid.equals(other.bloodGlucoseUuid))
			return false;
		if (bloodGlucoseValue != other.bloodGlucoseValue)
			return false;
		if (bloodSpecimenType == null) {
			if (other.bloodSpecimenType != null)
				return false;
		} else if (!bloodSpecimenType.equals(other.bloodSpecimenType))
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
		if (temporalRelationshipToMeal == null) {
			if (other.temporalRelationshipToMeal != null)
				return false;
		} else if (!temporalRelationshipToMeal.equals(other.temporalRelationshipToMeal))
			return false;
		if (temporalRelationshipToSleep == null) {
			if (other.temporalRelationshipToSleep != null)
				return false;
		} else if (!temporalRelationshipToSleep.equals(other.temporalRelationshipToSleep))
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		return true;
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
