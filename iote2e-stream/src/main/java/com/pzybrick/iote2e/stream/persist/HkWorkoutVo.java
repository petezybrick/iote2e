package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.PhysicalActivity;


public class HkWorkoutVo extends OmhVo  {
	private static final Logger logger = LogManager.getLogger(HkWorkoutVo.class);
	private String hkWorkoutUuid;
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


	public HkWorkoutVo( DataPointHeader header, PhysicalActivity hkWorkout ) throws SQLException {
		this.hkWorkoutUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( hkWorkout.getEffectiveTimeFrame().getDateTime() ));
		this.userNotes = hkWorkout.getUserNotes();		
		this.activityName = hkWorkout.getActivityName();
		this.distanceUnit = hkWorkout.getDistance().getUnit();
		this.distanceValue = hkWorkout.getDistance().getValue().intValue();
		this.kcalBurnedUnit = hkWorkout.getCaloriesBurned().getUnit();
		this.kcalBurnedValue = hkWorkout.getCaloriesBurned().getValue().intValue();
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

	public HkWorkoutVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	public HkWorkoutVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	public HkWorkoutVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	public HkWorkoutVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	public HkWorkoutVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	public HkWorkoutVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
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
