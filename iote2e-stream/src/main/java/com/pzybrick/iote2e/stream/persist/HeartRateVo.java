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
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.HeartRate;



/**
 * The Class HeartRateVo.
 */
public class HeartRateVo extends OmhVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(HeartRateVo.class);
	
	/** The heart rate uuid. */
	private String heartRateUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The user notes. */
	private String userNotes;
	
	/** The temporal relationship to physical activity. */
	private String temporalRelationshipToPhysicalActivity;
	
	/** The heart rate unit. */
	private String heartRateUnit;
	
	/** The heart rate value. */
	private int heartRateValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new heart rate vo.
	 */
	public HeartRateVo() {
	}
	

	/**
	 * Instantiates a new heart rate vo.
	 *
	 * @param header the header
	 * @param heartRate the heart rate
	 * @throws SQLException the SQL exception
	 */
	public HeartRateVo( DataPointHeader header, HeartRate heartRate ) throws SQLException {
		this.heartRateUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( heartRate.getEffectiveTimeFrame().getDateTime() ));
		this.userNotes = heartRate.getUserNotes();
		this.temporalRelationshipToPhysicalActivity = heartRate.getTemporalRelationshipToPhysicalActivity().name();
		this.heartRateUnit = heartRate.getHeartRate().getUnit();
		this.heartRateValue = heartRate.getHeartRate().getValue().intValue();
	}


	/**
	 * Instantiates a new heart rate vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the heart rate uuid.
	 *
	 * @return the heart rate uuid
	 */
	public String getHeartRateUuid() {
		return this.heartRateUuid;
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
	 * Gets the temporal relationship to physical activity.
	 *
	 * @return the temporal relationship to physical activity
	 */
	public String getTemporalRelationshipToPhysicalActivity() {
		return this.temporalRelationshipToPhysicalActivity;
	}
	
	/**
	 * Gets the heart rate unit.
	 *
	 * @return the heart rate unit
	 */
	public String getHeartRateUnit() {
		return this.heartRateUnit;
	}
	
	/**
	 * Gets the heart rate value.
	 *
	 * @return the heart rate value
	 */
	public int getHeartRateValue() {
		return this.heartRateValue;
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
	 * Sets the heart rate uuid.
	 *
	 * @param heartRateUuid the heart rate uuid
	 * @return the heart rate vo
	 */
	public HeartRateVo setHeartRateUuid( String heartRateUuid ) {
		this.heartRateUuid = heartRateUuid;
		return this;
	}
	
	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the heart rate vo
	 */
	public HeartRateVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	
	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the heart rate vo
	 */
	public HeartRateVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	
	/**
	 * Sets the temporal relationship to physical activity.
	 *
	 * @param temporalRelationshipToPhysicalActivity the temporal relationship to physical activity
	 * @return the heart rate vo
	 */
	public HeartRateVo setTemporalRelationshipToPhysicalActivity( String temporalRelationshipToPhysicalActivity ) {
		this.temporalRelationshipToPhysicalActivity = temporalRelationshipToPhysicalActivity;
		return this;
	}
	
	/**
	 * Sets the heart rate unit.
	 *
	 * @param heartRateUnit the heart rate unit
	 * @return the heart rate vo
	 */
	public HeartRateVo setHeartRateUnit( String heartRateUnit ) {
		this.heartRateUnit = heartRateUnit;
		return this;
	}
	
	/**
	 * Sets the heart rate value.
	 *
	 * @param heartRateValue the heart rate value
	 * @return the heart rate vo
	 */
	public HeartRateVo setHeartRateValue( int heartRateValue ) {
		this.heartRateValue = heartRateValue;
		return this;
	}
	
	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the heart rate vo
	 */
	public HeartRateVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the heart rate vo
	 */
	public HeartRateVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the heart rate vo
	 */
	public HeartRateVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the heart rate vo
	 */
	public HeartRateVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the heart rate vo
	 */
	public HeartRateVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the heart rate vo
	 */
	public HeartRateVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the heart rate vo
	 */
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
