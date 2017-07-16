package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HkWorkoutVo implements OmhVo  {
	private static final Logger logger = LogManager.getLogger(HkWorkoutVo.class);
	private String hkWorkoutUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
	private Timestamp effectiveTimeFrame;
	private String userNotes;
	private String activityName;
	private String distanceUnit;
	private int distanceValue;
	private String kcalBurnedUnit;
	private int kcalBurnedValue;
	private Timestamp insertTs;


	public HkWorkoutVo() {
	}


	public HkWorkoutVo(ResultSet rs) throws SQLException {
		this.hkWorkoutUuid = rs.getString("hk_workout_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.userNotes = rs.getString("user_notes");
		this.activityName = rs.getString("activity_name");
		this.distanceUnit = rs.getString("distance_unit");
		this.distanceValue = rs.getInt("distance_value");
		this.kcalBurnedUnit = rs.getString("kcal_burned_unit");
		this.kcalBurnedValue = rs.getInt("kcal_burned_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getHkWorkoutUuid() {
		return this.hkWorkoutUuid;
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
	public String getActivityName() {
		return this.activityName;
	}
	public String getDistanceUnit() {
		return this.distanceUnit;
	}
	public int getDistanceValue() {
		return this.distanceValue;
	}
	public String getKcalBurnedUnit() {
		return this.kcalBurnedUnit;
	}
	public int getKcalBurnedValue() {
		return this.kcalBurnedValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public HkWorkoutVo setHkWorkoutUuid( String hkWorkoutUuid ) {
		this.hkWorkoutUuid = hkWorkoutUuid;
		return this;
	}
	public HkWorkoutVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public HkWorkoutVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public HkWorkoutVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public HkWorkoutVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public HkWorkoutVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public HkWorkoutVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
	public HkWorkoutVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public HkWorkoutVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public HkWorkoutVo setActivityName( String activityName ) {
		this.activityName = activityName;
		return this;
	}
	public HkWorkoutVo setDistanceUnit( String distanceUnit ) {
		this.distanceUnit = distanceUnit;
		return this;
	}
	public HkWorkoutVo setDistanceValue( int distanceValue ) {
		this.distanceValue = distanceValue;
		return this;
	}
	public HkWorkoutVo setKcalBurnedUnit( String kcalBurnedUnit ) {
		this.kcalBurnedUnit = kcalBurnedUnit;
		return this;
	}
	public HkWorkoutVo setKcalBurnedValue( int kcalBurnedValue ) {
		this.kcalBurnedValue = kcalBurnedValue;
		return this;
	}
	public HkWorkoutVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityName == null) ? 0 : activityName.hashCode());
		result = prime * result + ((distanceUnit == null) ? 0 : distanceUnit.hashCode());
		result = prime * result + distanceValue;
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((hkWorkoutUuid == null) ? 0 : hkWorkoutUuid.hashCode());
		result = prime * result + ((kcalBurnedUnit == null) ? 0 : kcalBurnedUnit.hashCode());
		result = prime * result + kcalBurnedValue;
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
		HkWorkoutVo other = (HkWorkoutVo) obj;
		if (activityName == null) {
			if (other.activityName != null)
				return false;
		} else if (!activityName.equals(other.activityName))
			return false;
		if (distanceUnit == null) {
			if (other.distanceUnit != null)
				return false;
		} else if (!distanceUnit.equals(other.distanceUnit))
			return false;
		if (distanceValue != other.distanceValue)
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
		if (hkWorkoutUuid == null) {
			if (other.hkWorkoutUuid != null)
				return false;
		} else if (!hkWorkoutUuid.equals(other.hkWorkoutUuid))
			return false;
		if (kcalBurnedUnit == null) {
			if (other.kcalBurnedUnit != null)
				return false;
		} else if (!kcalBurnedUnit.equals(other.kcalBurnedUnit))
			return false;
		if (kcalBurnedValue != other.kcalBurnedValue)
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		return true;
	}
}

// HkWorkoutVo hkWorkoutVo = new HkWorkoutVo()
//	 .setHkWorkoutUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setUserNotes("xxx")
//	 .setActivityName("xxx")
//	 .setDistanceUnit("xxx")
//	 .setDistanceValue("xxx")
//	 .setKcalBurnedUnit("xxx")
//	 .setKcalBurnedValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
