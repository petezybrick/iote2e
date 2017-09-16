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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPointHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;



/**
 * The Class BloodPressureDao.
 */
public class BloodPressureDao extends OmhDao  {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(BloodPressureDao.class);
	
	/** The sql delete by pk. */
	private static String sqlDeleteByPk = "DELETE FROM blood_pressure WHERE blood_pressure_uuid=?";
	
	/** The sql insert. */
	private static String sqlInsert = "INSERT INTO blood_pressure (blood_pressure_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,position_during_measurement,systolic_blood_pressure_unit,systolic_blood_pressure_value,diastolic_blood_pressure_unit,diastolic_blood_pressure_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/** The sql find by pk. */
	private static String sqlFindByPk = "SELECT blood_pressure_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,position_during_measurement,systolic_blood_pressure_unit,systolic_blood_pressure_value,diastolic_blood_pressure_unit,diastolic_blood_pressure_value,insert_ts FROM blood_pressure WHERE blood_pressure_uuid=?";


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
			BloodPressure bloodPressure = objectMapper.readValue(rawJson, BloodPressure.class);
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
					new Timestamp(bloodPressure.getEffectiveTimeFrame().getDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, bloodPressure.getDescriptiveStatistic().name() );
			pstmt.setString( offset++, bloodPressure.getUserNotes() );
			pstmt.setString( offset++, bloodPressure.getPositionDuringMeasurement().name() );
			pstmt.setString( offset++, bloodPressure.getSystolicBloodPressure().getUnit() );
			pstmt.setInt( offset++, bloodPressure.getSystolicBloodPressure().getValue().intValue() );
			pstmt.setString( offset++, bloodPressure.getDiastolicBloodPressure().getUnit() );
			pstmt.setInt( offset++, bloodPressure.getDiastolicBloodPressure().getValue().intValue() );
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
	 * @param bloodPressureVo the blood pressure vo
	 * @throws Exception the exception
	 */
	public static void insertBatchMode( Connection con, BloodPressureVo bloodPressureVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bloodPressureVo.getBloodPressureUuid() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bloodPressureVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bloodPressureVo.getHdrUserId() );
			pstmt.setString( offset++, bloodPressureVo.getHdrModality() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bloodPressureVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bloodPressureVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bloodPressureVo.getUserNotes() );
			pstmt.setString( offset++, bloodPressureVo.getPositionDuringMeasurement() );
			pstmt.setString( offset++, bloodPressureVo.getSystolicBloodPressureUnit() );
			pstmt.setInt( offset++, bloodPressureVo.getSystolicBloodPressureValue() );
			pstmt.setString( offset++, bloodPressureVo.getDiastolicBloodPressureUnit() );
			pstmt.setInt( offset++, bloodPressureVo.getDiastolicBloodPressureValue() );
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
	 * @param bloodPressureVo the blood pressure vo
	 * @throws Exception the exception
	 */
	public static void insert( MasterConfig masterConfig, BloodPressureVo bloodPressureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bloodPressureVo.getBloodPressureUuid() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bloodPressureVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bloodPressureVo.getHdrUserId() );
			pstmt.setString( offset++, bloodPressureVo.getHdrModality() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bloodPressureVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bloodPressureVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bloodPressureVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bloodPressureVo.getUserNotes() );
			pstmt.setString( offset++, bloodPressureVo.getPositionDuringMeasurement() );
			pstmt.setString( offset++, bloodPressureVo.getSystolicBloodPressureUnit() );
			pstmt.setInt( offset++, bloodPressureVo.getSystolicBloodPressureValue() );
			pstmt.setString( offset++, bloodPressureVo.getDiastolicBloodPressureUnit() );
			pstmt.setInt( offset++, bloodPressureVo.getDiastolicBloodPressureValue() );
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
	 * @param bloodPressureVo the blood pressure vo
	 * @throws Exception the exception
	 */
	public static void deleteByPk( MasterConfig masterConfig, BloodPressureVo bloodPressureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodPressureVo.getBloodPressureUuid() );
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
	 * @param bloodPressureVo the blood pressure vo
	 * @throws Exception the exception
	 */
	public static void deleteBatchMode( Connection con, BloodPressureVo bloodPressureVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodPressureVo.getBloodPressureUuid() );
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
	 * @param bloodPressureVo the blood pressure vo
	 * @return the blood pressure vo
	 * @throws Exception the exception
	 */
	public static BloodPressureVo findByPk( MasterConfig masterConfig, BloodPressureVo bloodPressureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, bloodPressureVo.getBloodPressureUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new BloodPressureVo(rs);
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
