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
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.DataPointHeader;

import com.pzybrick.iote2e.stream.validic.Diabete;
import com.pzybrick.iote2e.stream.validic.ValidicHeader;



/**
 * The Class BloodGlucoseVo.
 */
public class BloodGlucoseVo extends OmhVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BloodGlucoseVo.class);
	
	/** The blood glucose uuid. */
	private String bloodGlucoseUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The descriptive statistic. */
	private String descriptiveStatistic;
	
	/** The user notes. */
	private String userNotes;
	
	/** The blood specimen type. */
	private String bloodSpecimenType;
	
	/** The temporal relationship to meal. */
	private String temporalRelationshipToMeal;
	
	/** The temporal relationship to sleep. */
	private String temporalRelationshipToSleep;
	
	/** The blood glucose unit. */
	private String bloodGlucoseUnit;
	
	/** The blood glucose value. */
	private int bloodGlucoseValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new blood glucose vo.
	 */
	public BloodGlucoseVo() {
	}


	/**
	 * Instantiates a new blood glucose vo.
	 *
	 * @param header the header
	 * @param bloodGlucose the blood glucose
	 * @throws SQLException the SQL exception
	 */
	public BloodGlucoseVo( DataPointHeader header, BloodGlucose bloodGlucose ) throws SQLException {
		this.bloodGlucoseUuid = header.getId();
		setHeaderCommon(header);
		this.hdrSourceName = header.getAcquisitionProvenance().getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getAcquisitionProvenance().getSourceCreationDateTime()) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  header.getAcquisitionProvenance().getModality().name();
		this.hdrSchemaNamespace = header.getSchemaId().getNamespace();
		this.hdrSchemaVersion = header.getSchemaId().getVersion().toString();
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( bloodGlucose.getEffectiveTimeFrame().getDateTime() ));
		this.descriptiveStatistic = bloodGlucose.getDescriptiveStatistic().name();
		this.userNotes = bloodGlucose.getUserNotes();
		this.bloodSpecimenType = bloodGlucose.getBloodSpecimenType().name();
		this.temporalRelationshipToMeal = bloodGlucose.getTemporalRelationshipToMeal().name();
		this.temporalRelationshipToSleep = bloodGlucose.getTemporalRelationshipToSleep().name();
		this.bloodGlucoseUnit = bloodGlucose.getBloodGlucose().getUnit();
		this.bloodGlucoseValue = bloodGlucose.getBloodGlucose().getValue().intValue();
	}


	public BloodGlucoseVo( ValidicHeader header, Diabete diabete ) throws SQLException {
		this.bloodGlucoseUuid = diabete.getId();
		this.hdrSourceName = diabete.getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getCreationDateTime() ) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  "NA";
		this.hdrSchemaNamespace = diabete.getSchemaName();
		this.hdrSchemaVersion = "NA";
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( diabete.getTimestamp()) );
		this.descriptiveStatistic = "NA";
		this.userNotes = "NA";
		this.bloodSpecimenType = "NA";
		this.temporalRelationshipToMeal = diabete.getRelationshipToMeal();
		this.temporalRelationshipToSleep = "NA";
		this.bloodGlucoseUnit = "NA";
		this.bloodGlucoseValue = diabete.getBloodGlucose().intValue();
	}


	/**
	 * Instantiates a new blood glucose vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the blood glucose uuid.
	 *
	 * @return the blood glucose uuid
	 */
	public String getBloodGlucoseUuid() {
		return this.bloodGlucoseUuid;
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
	 * Gets the descriptive statistic.
	 *
	 * @return the descriptive statistic
	 */
	public String getDescriptiveStatistic() {
		return this.descriptiveStatistic;
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
	 * Gets the blood specimen type.
	 *
	 * @return the blood specimen type
	 */
	public String getBloodSpecimenType() {
		return this.bloodSpecimenType;
	}
	
	/**
	 * Gets the temporal relationship to meal.
	 *
	 * @return the temporal relationship to meal
	 */
	public String getTemporalRelationshipToMeal() {
		return this.temporalRelationshipToMeal;
	}
	
	/**
	 * Gets the temporal relationship to sleep.
	 *
	 * @return the temporal relationship to sleep
	 */
	public String getTemporalRelationshipToSleep() {
		return this.temporalRelationshipToSleep;
	}
	
	/**
	 * Gets the blood glucose unit.
	 *
	 * @return the blood glucose unit
	 */
	public String getBloodGlucoseUnit() {
		return this.bloodGlucoseUnit;
	}
	
	/**
	 * Gets the blood glucose value.
	 *
	 * @return the blood glucose value
	 */
	public int getBloodGlucoseValue() {
		return this.bloodGlucoseValue;
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
	 * Sets the blood glucose uuid.
	 *
	 * @param bloodGlucoseUuid the blood glucose uuid
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setBloodGlucoseUuid( String bloodGlucoseUuid ) {
		this.bloodGlucoseUuid = bloodGlucoseUuid;
		return this;
	}

	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	
	/**
	 * Sets the descriptive statistic.
	 *
	 * @param descriptiveStatistic the descriptive statistic
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	
	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	
	/**
	 * Sets the blood specimen type.
	 *
	 * @param bloodSpecimenType the blood specimen type
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setBloodSpecimenType( String bloodSpecimenType ) {
		this.bloodSpecimenType = bloodSpecimenType;
		return this;
	}
	
	/**
	 * Sets the temporal relationship to meal.
	 *
	 * @param temporalRelationshipToMeal the temporal relationship to meal
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setTemporalRelationshipToMeal( String temporalRelationshipToMeal ) {
		this.temporalRelationshipToMeal = temporalRelationshipToMeal;
		return this;
	}
	
	/**
	 * Sets the temporal relationship to sleep.
	 *
	 * @param temporalRelationshipToSleep the temporal relationship to sleep
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setTemporalRelationshipToSleep( String temporalRelationshipToSleep ) {
		this.temporalRelationshipToSleep = temporalRelationshipToSleep;
		return this;
	}
	
	/**
	 * Sets the blood glucose unit.
	 *
	 * @param bloodGlucoseUnit the blood glucose unit
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setBloodGlucoseUnit( String bloodGlucoseUnit ) {
		this.bloodGlucoseUnit = bloodGlucoseUnit;
		return this;
	}
	
	/**
	 * Sets the blood glucose value.
	 *
	 * @param bloodGlucoseValue the blood glucose value
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setBloodGlucoseValue( int bloodGlucoseValue ) {
		this.bloodGlucoseValue = bloodGlucoseValue;
		return this;
	}
	
	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the blood glucose vo
	 */
	public BloodGlucoseVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
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
