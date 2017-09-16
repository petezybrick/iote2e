/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.PhysicalActivity;



/**
 * The Class HkWorkoutVo.
 */
public class HkWorkoutVo extends OmhVo  {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(HkWorkoutVo.class);
	
	/** The hk workout uuid. */
	private String hkWorkoutUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The user notes. */
	private String userNotes;
	
	/** The activity name. */
	private String activityName;
	
	/** The distance unit. */
	private String distanceUnit;
	
	/** The distance value. */
	private int distanceValue;
	
	/** The kcal burned unit. */
	private String kcalBurnedUnit;
	
	/** The kcal burned value. */
	private int kcalBurnedValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new hk workout vo.
	 */
	public HkWorkoutVo() {
	}


	/**
	 * Instantiates a new hk workout vo.
	 *
	 * @param header the header
	 * @param hkWorkout the hk workout
	 * @throws SQLException the SQL exception
	 */
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
	
	
	/**
	 * Instantiates a new hk workout vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the hk workout uuid.
	 *
	 * @return the hk workout uuid
	 */
	public String getHkWorkoutUuid() {
		return this.hkWorkoutUuid;
	}
	
	/**
	 * Gets the effective time frame.
	 *
	 * @return the effective time frame
	 */
	public Timestamp getEffectiveTimeFrame() {
		return this.effectiveTimeFrame;
	}
	
	/**
	 * Gets the user notes.
	 *
	 * @return the user notes
	 */
	public String getUserNotes() {
		return this.userNotes;
	}
	
	/**
	 * Gets the activity name.
	 *
	 * @return the activity name
	 */
	public String getActivityName() {
		return this.activityName;
	}
	
	/**
	 * Gets the distance unit.
	 *
	 * @return the distance unit
	 */
	public String getDistanceUnit() {
		return this.distanceUnit;
	}
	
	/**
	 * Gets the distance value.
	 *
	 * @return the distance value
	 */
	public int getDistanceValue() {
		return this.distanceValue;
	}
	
	/**
	 * Gets the kcal burned unit.
	 *
	 * @return the kcal burned unit
	 */
	public String getKcalBurnedUnit() {
		return this.kcalBurnedUnit;
	}
	
	/**
	 * Gets the kcal burned value.
	 *
	 * @return the kcal burned value
	 */
	public int getKcalBurnedValue() {
		return this.kcalBurnedValue;
	}
	
	/**
	 * Gets the insert ts.
	 *
	 * @return the insert ts
	 */
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	/**
	 * Sets the hk workout uuid.
	 *
	 * @param hkWorkoutUuid the hk workout uuid
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHkWorkoutUuid( String hkWorkoutUuid ) {
		this.hkWorkoutUuid = hkWorkoutUuid;
		return this;
	}
	
	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	
	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	
	/**
	 * Sets the activity name.
	 *
	 * @param activityName the activity name
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setActivityName( String activityName ) {
		this.activityName = activityName;
		return this;
	}
	
	/**
	 * Sets the distance unit.
	 *
	 * @param distanceUnit the distance unit
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setDistanceUnit( String distanceUnit ) {
		this.distanceUnit = distanceUnit;
		return this;
	}
	
	/**
	 * Sets the distance value.
	 *
	 * @param distanceValue the distance value
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setDistanceValue( int distanceValue ) {
		this.distanceValue = distanceValue;
		return this;
	}
	
	/**
	 * Sets the kcal burned unit.
	 *
	 * @param kcalBurnedUnit the kcal burned unit
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setKcalBurnedUnit( String kcalBurnedUnit ) {
		this.kcalBurnedUnit = kcalBurnedUnit;
		return this;
	}
	
	/**
	 * Sets the kcal burned value.
	 *
	 * @param kcalBurnedValue the kcal burned value
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setKcalBurnedValue( int kcalBurnedValue ) {
		this.kcalBurnedValue = kcalBurnedValue;
		return this;
	}
	
	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the hk workout vo
	 */
	public HkWorkoutVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the hk workout vo
	 */
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
