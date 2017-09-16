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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.temporal.TemporalField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.DataPointHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;



/**
 * The Class BloodGlucoseDao.
 */
public class BloodGlucoseDao extends OmhDao  {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BloodGlucoseDao.class);
	
	/** The sql delete by pk. */
	private static String sqlDeleteByPk = "DELETE FROM blood_glucose WHERE blood_glucose_uuid=?";
	
	/** The sql insert. */
	private static String sqlInsert = "INSERT INTO blood_glucose (blood_glucose_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,blood_specimen_type,temporal_relationship_to_meal,temporal_relationship_to_sleep,blood_glucose_unit,blood_glucose_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/** The sql find by pk. */
	private static String sqlFindByPk = "SELECT blood_glucose_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,blood_specimen_type,temporal_relationship_to_meal,temporal_relationship_to_sleep,blood_glucose_unit,blood_glucose_value,insert_ts FROM blood_glucose WHERE blood_glucose_uuid=?";

	/**
	 * Insert from json schema.
	 *
	 * @param pstmt the pstmt
	 * @param dataPointHeader the data point header
	 * @param rawJson the raw json
	 * @param objectMapper the object mapper
	 * @throws Exception the exception
	 */
	public static void insertFromJsonSchema( PreparedStatement pstmt, DataPointHeader dataPointHeader, String rawJson, ObjectMapper objectMapper ) throws Exception {
		try {
			BloodGlucose bloodGlucose = objectMapper.readValue(rawJson, BloodGlucose.class);
			int offset = 1;
			pstmt.setString( offset++, dataPointHeader.getId() );
			pstmt.setString( offset++, dataPointHeader.getAcquisitionProvenance().getSourceName() );
			pstmt.setTimestamp( offset++, 
					new Timestamp( dataPointHeader.getAcquisitionProvenance().getSourceCreationDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, dataPointHeader.getUserId());
			pstmt.setString( offset++, dataPointHeader.getAcquisitionProvenance().getModality().name() );
			pstmt.setString( offset++, dataPointHeader.getSchemaId().getNamespace() );
			pstmt.setString( offset++, dataPointHeader.getSchemaId().getVersion().toString());			
			pstmt.setTimestamp( offset++, 
					new Timestamp(bloodGlucose.getEffectiveTimeFrame().getDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, bloodGlucose.getDescriptiveStatistic().name() );
			pstmt.setString( offset++, bloodGlucose.getUserNotes() );
			pstmt.setString( offset++, bloodGlucose.getBloodSpecimenType().name() );
			pstmt.setString( offset++, bloodGlucose.getTemporalRelationshipToMeal().name() );
			pstmt.setString( offset++, bloodGlucose.getTemporalRelationshipToSleep().name() );
			pstmt.setString( offset++, bloodGlucose.getBloodGlucose().getUnit() );
			pstmt.setInt( offset++, bloodGlucose.getBloodGlucose().getValue().intValue() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	/**
	 * Insert batch mode.
	 *
	 * @param con the con
	 * @param bloodGlucoseVo the blood glucose vo
	 * @throws Exception the exception
	 */
	public static void insertBatchMode( Connection con, BloodGlucoseVo bloodGlucoseVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUuid() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bloodGlucoseVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrUserId() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrModality() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bloodGlucoseVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bloodGlucoseVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bloodGlucoseVo.getUserNotes() );
			pstmt.setString( offset++, bloodGlucoseVo.getBloodSpecimenType() );
			pstmt.setString( offset++, bloodGlucoseVo.getTemporalRelationshipToMeal() );
			pstmt.setString( offset++, bloodGlucoseVo.getTemporalRelationshipToSleep() );
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUnit() );
			pstmt.setInt( offset++, bloodGlucoseVo.getBloodGlucoseValue() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
				pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}

	/**
	 * Insert.
	 *
	 * @param masterConfig the master config
	 * @param bloodGlucoseVo the blood glucose vo
	 * @throws Exception the exception
	 */
	public static void insert( MasterConfig masterConfig, BloodGlucoseVo bloodGlucoseVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUuid() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bloodGlucoseVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrUserId() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrModality() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bloodGlucoseVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bloodGlucoseVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bloodGlucoseVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bloodGlucoseVo.getUserNotes() );
			pstmt.setString( offset++, bloodGlucoseVo.getBloodSpecimenType() );
			pstmt.setString( offset++, bloodGlucoseVo.getTemporalRelationshipToMeal() );
			pstmt.setString( offset++, bloodGlucoseVo.getTemporalRelationshipToSleep() );
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUnit() );
			pstmt.setInt( offset++, bloodGlucoseVo.getBloodGlucoseValue() );
			pstmt.execute();
			con.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if( con != null ) {
				try {
					con.rollback();
				} catch(Exception erb ) {
					logger.warn(e.getMessage(), e);
				}
			}
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}

	/**
	 * Delete by pk.
	 *
	 * @param masterConfig the master config
	 * @param bloodGlucoseVo the blood glucose vo
	 * @throws Exception the exception
	 */
	public static void deleteByPk( MasterConfig masterConfig, BloodGlucoseVo bloodGlucoseVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUuid() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if( con != null ) {
				try {
					con.rollback();
				} catch(Exception erb ) {
					logger.warn(e.getMessage(), e);
				}
			}
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}

	/**
	 * Delete batch mode.
	 *
	 * @param con the con
	 * @param bloodGlucoseVo the blood glucose vo
	 * @throws Exception the exception
	 */
	public static void deleteBatchMode( Connection con, BloodGlucoseVo bloodGlucoseVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUuid() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}

	/**
	 * Find by pk.
	 *
	 * @param masterConfig the master config
	 * @param bloodGlucoseVo the blood glucose vo
	 * @return the blood glucose vo
	 * @throws Exception the exception
	 */
	public static BloodGlucoseVo findByPk( MasterConfig masterConfig, BloodGlucoseVo bloodGlucoseVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodGlucoseVo.getBloodGlucoseUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new BloodGlucoseVo(rs);
			else return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}
}
