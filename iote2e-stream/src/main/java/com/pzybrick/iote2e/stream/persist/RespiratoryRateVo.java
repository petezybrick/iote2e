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
import org.openmhealth.schema.domain.omh.RespiratoryRate;



/**
 * The Class RespiratoryRateVo.
 */
public class RespiratoryRateVo extends OmhVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RespiratoryRateVo.class);
	
	/** The respiratory rate uuid. */
	private String respiratoryRateUuid;
	
	/** The effective time frame. */
	private Timestamp effectiveTimeFrame;
	
	/** The user notes. */
	private String userNotes;
	
	/** The descriptive statistic. */
	private String descriptiveStatistic;
	
	/** The temporal relationship to physical activity. */
	private String temporalRelationshipToPhysicalActivity;
	
	/** The respiratory rate unit. */
	private String respiratoryRateUnit;
	
	/** The respiratory rate value. */
	private float respiratoryRateValue;
	
	/** The insert ts. */
	private Timestamp insertTs;


	/**
	 * Instantiates a new respiratory rate vo.
	 */
	public RespiratoryRateVo() {
	}


	/**
	 * Instantiates a new respiratory rate vo.
	 *
	 * @param header the header
	 * @param respiratoryRate the respiratory rate
	 * @throws SQLException the SQL exception
	 */
	public RespiratoryRateVo( DataPointHeader header, RespiratoryRate respiratoryRate ) throws SQLException {
		this.respiratoryRateUuid = header.getId();
		setHeaderCommon(header);
		this.effectiveTimeFrame = new Timestamp( offsetDateTimeToMillis( respiratoryRate.getEffectiveTimeFrame().getDateTime() ));
		this.descriptiveStatistic = respiratoryRate.getDescriptiveStatistic().name();
		this.userNotes = respiratoryRate.getUserNotes();
		this.temporalRelationshipToPhysicalActivity = respiratoryRate.getTemporalRelationshipToPhysicalActivity().name();
		this.respiratoryRateUnit = respiratoryRate.getRespiratoryRate().getUnit();
		this.respiratoryRateValue = respiratoryRate.getRespiratoryRate().getValue().floatValue();
	}
	

	/**
	 * Instantiates a new respiratory rate vo.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
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


	/**
	 * Gets the respiratory rate uuid.
	 *
	 * @return the respiratory rate uuid
	 */
	public String getRespiratoryRateUuid() {
		return this.respiratoryRateUuid;
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
	 * Gets the descriptive statistic.
	 *
	 * @return the descriptive statistic
	 */
	public String getDescriptiveStatistic() {
		return this.descriptiveStatistic;
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
	 * Gets the respiratory rate unit.
	 *
	 * @return the respiratory rate unit
	 */
	public String getRespiratoryRateUnit() {
		return this.respiratoryRateUnit;
	}
	
	/**
	 * Gets the respiratory rate value.
	 *
	 * @return the respiratory rate value
	 */
	public float getRespiratoryRateValue() {
		return this.respiratoryRateValue;
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
	 * Sets the respiratory rate uuid.
	 *
	 * @param respiratoryRateUuid the respiratory rate uuid
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setRespiratoryRateUuid( String respiratoryRateUuid ) {
		this.respiratoryRateUuid = respiratoryRateUuid;
		return this;
	}
	
	/**
	 * Sets the effective time frame.
	 *
	 * @param effectiveTimeFrame the effective time frame
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	
	/**
	 * Sets the user notes.
	 *
	 * @param userNotes the user notes
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	
	/**
	 * Sets the descriptive statistic.
	 *
	 * @param descriptiveStatistic the descriptive statistic
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	
	/**
	 * Sets the temporal relationship to physical activity.
	 *
	 * @param temporalRelationshipToPhysicalActivity the temporal relationship to physical activity
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setTemporalRelationshipToPhysicalActivity( String temporalRelationshipToPhysicalActivity ) {
		this.temporalRelationshipToPhysicalActivity = temporalRelationshipToPhysicalActivity;
		return this;
	}
	
	/**
	 * Sets the respiratory rate unit.
	 *
	 * @param respiratoryRateUnit the respiratory rate unit
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setRespiratoryRateUnit( String respiratoryRateUnit ) {
		this.respiratoryRateUnit = respiratoryRateUnit;
		return this;
	}
	
	/**
	 * Sets the respiratory rate value.
	 *
	 * @param respiratoryRateValue the respiratory rate value
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setRespiratoryRateValue( float respiratoryRateValue ) {
		this.respiratoryRateValue = respiratoryRateValue;
		return this;
	}
	
	/**
	 * Sets the insert ts.
	 *
	 * @param insertTs the insert ts
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}

	/**
	 * Sets the hdr source name.
	 *
	 * @param hdrSourceName the hdr source name
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrSourceName(String hdrSourceName) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}

	/**
	 * Sets the hdr source creation date time.
	 *
	 * @param hdrSourceCreationDateTime the hdr source creation date time
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrSourceCreationDateTime(Timestamp hdrSourceCreationDateTime) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}

	/**
	 * Sets the hdr user id.
	 *
	 * @param hdrUserId the hdr user id
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrUserId(String hdrUserId) {
		this.hdrUserId = hdrUserId;
		return this;
	}

	/**
	 * Sets the hdr modality.
	 *
	 * @param hdrModality the hdr modality
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrModality(String hdrModality) {
		this.hdrModality = hdrModality;
		return this;
	}

	/**
	 * Sets the hdr schema namespace.
	 *
	 * @param hdrSchemaNamespace the hdr schema namespace
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrSchemaNamespace(String hdrSchemaNamespace) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}

	/**
	 * Sets the hdr schema version.
	 *
	 * @param hdrSchemaVersion the hdr schema version
	 * @return the respiratory rate vo
	 */
	public RespiratoryRateVo setHdrSchemaVersion(String hdrSchemaVersion) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
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
