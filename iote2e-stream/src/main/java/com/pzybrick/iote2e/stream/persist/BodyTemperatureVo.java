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

import com.pzybrick.iote2e.stream.validic.Biometric;
import com.pzybrick.iote2e.stream.validic.ValidicHeader;



/**
 * The Class BodyTemperatureVo.
 */
public class BodyTemperatureVo extends OmhVo  {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BodyTemperatureVo.class);
	
	/** The body temperature uuid. */
	private String bodyTemperatureUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The descriptive statistic. */
	private String descriptiveStatistic;
	
	/** The user notes. */
	private String userNotes;
	
	/** The measurement location. */
	private String measurementLocation;
	
	/** The body temperature unit. */
	private String bodyTemperatureUnit;
	
	/** The body temperature value. */
	private float bodyTemperatureValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new body temperature vo.
	 */
	public BodyTemperatureVo() {
	}
	

	/**
	 * Instantiates a new body temperature vo.
	 *
	 * @param header the header
	 * @param bodyTemperature the body temperature
	 * @throws SQLException the SQL exception
	 */
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
	
	
	public BodyTemperatureVo( ValidicHeader header, Biometric biometric ) throws SQLException {
		this.bodyTemperatureUuid = biometric.getId();
		this.hdrSourceName = biometric.getSourceName();
		this.hdrSourceCreationDateTime = new Timestamp( offsetDateTimeToMillis(header.getCreationDateTime() ) ) ;
		this.hdrUserId = header.getUserId();
		this.hdrModality =  "NA";
		this.hdrSchemaNamespace = biometric.getSchemaName();
		this.hdrSchemaVersion = "NA";
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( biometric.getTimestamp()) );
		this.descriptiveStatistic = "NA";
		this.userNotes = "NA";
		this.measurementLocation = "NA";
		this.bodyTemperatureUnit = "F";
		this.bodyTemperatureValue = biometric.getTemperature().floatValue();
	}


	/**
	 * Instantiates a new body temperature vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the body temperature uuid.
	 *
	 * @return the body temperature uuid
	 */
	public String getBodyTemperatureUuid() {
		return this.bodyTemperatureUuid;
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
	 * Gets the measurement location.
	 *
	 * @return the measurement location
	 */
	public String getMeasurementLocation() {
		return this.measurementLocation;
	}
	
	/**
	 * Gets the body temperature unit.
	 *
	 * @return the body temperature unit
	 */
	public String getBodyTemperatureUnit() {
		return this.bodyTemperatureUnit;
	}
	
	/**
	 * Gets the body temperature value.
	 *
	 * @return the body temperature value
	 */
	public float getBodyTemperatureValue() {
		return this.bodyTemperatureValue;
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
	 * Sets the body temperature uuid.
	 *
	 * @param bodyTemperatureUuid the body temperature uuid
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setBodyTemperatureUuid( String bodyTemperatureUuid ) {
		this.bodyTemperatureUuid = bodyTemperatureUuid;
		return this;
	}
	
	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	
	/**
	 * Sets the descriptive statistic.
	 *
	 * @param descriptiveStatistic the descriptive statistic
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	
	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	
	/**
	 * Sets the measurement location.
	 *
	 * @param measurementLocation the measurement location
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setMeasurementLocation( String measurementLocation ) {
		this.measurementLocation = measurementLocation;
		return this;
	}
	
	/**
	 * Sets the body temperature unit.
	 *
	 * @param bodyTemperatureUnit the body temperature unit
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setBodyTemperatureUnit( String bodyTemperatureUnit ) {
		this.bodyTemperatureUnit = bodyTemperatureUnit;
		return this;
	}
	
	/**
	 * Sets the body temperature value.
	 *
	 * @param bodyTemperatureValue the body temperature value
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setBodyTemperatureValue( float bodyTemperatureValue ) {
		this.bodyTemperatureValue = bodyTemperatureValue;
		return this;
	}
	
	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the body temperature vo
	 */
	public BodyTemperatureVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the body temperature vo
	 */
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
