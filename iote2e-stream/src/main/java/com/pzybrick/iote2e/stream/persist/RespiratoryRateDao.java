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
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.RespiratoryRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;



/**
 * The Class RespiratoryRateDao.
 */
public class RespiratoryRateDao extends OmhDao  {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RespiratoryRateDao.class);
	
	/** The sql delete by pk. */
	private static String sqlDeleteByPk = "DELETE FROM respiratory_rate WHERE respiratory_rate_uuid=?";
	
	/** The sql insert. */
	private static String sqlInsert = "INSERT INTO respiratory_rate (respiratory_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,descriptive_statistic,temporal_relationship_to_physical_activity,respiratory_rate_unit,respiratory_rate_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/** The sql find by pk. */
	private static String sqlFindByPk = "SELECT respiratory_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,descriptive_statistic,temporal_relationship_to_physical_activity,respiratory_rate_unit,respiratory_rate_value,insert_ts FROM respiratory_rate WHERE respiratory_rate_uuid=?";


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
			RespiratoryRate respiratoryRate = objectMapper.readValue(rawJson, RespiratoryRate.class);
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
					new Timestamp(respiratoryRate.getEffectiveTimeFrame().getDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, respiratoryRate.getUserNotes() );
			pstmt.setString( offset++, respiratoryRate.getDescriptiveStatistic().name() );
			
			pstmt.setString( offset++, respiratoryRate.getTemporalRelationshipToPhysicalActivity().name() );
			pstmt.setString( offset++, respiratoryRate.getRespiratoryRate().getUnit() );
			pstmt.setFloat( offset++, respiratoryRate.getRespiratoryRate().getValue().floatValue() );
			
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
	 * @param respiratoryRateVo the respiratory rate vo
	 * @throws Exception the exception
	 */
	public static void insertBatchMode( Connection con, RespiratoryRateVo respiratoryRateVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUuid() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, respiratoryRateVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrUserId() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrModality() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, respiratoryRateVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, respiratoryRateVo.getUserNotes() );
			pstmt.setString( offset++, respiratoryRateVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, respiratoryRateVo.getTemporalRelationshipToPhysicalActivity() );
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUnit() );
			pstmt.setFloat( offset++, respiratoryRateVo.getRespiratoryRateValue() );
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
	 * @param respiratoryRateVo the respiratory rate vo
	 * @throws Exception the exception
	 */
	public static void insert( MasterConfig masterConfig, RespiratoryRateVo respiratoryRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUuid() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, respiratoryRateVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrUserId() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrModality() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, respiratoryRateVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, respiratoryRateVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, respiratoryRateVo.getUserNotes() );
			pstmt.setString( offset++, respiratoryRateVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, respiratoryRateVo.getTemporalRelationshipToPhysicalActivity() );
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUnit() );
			pstmt.setFloat( offset++, respiratoryRateVo.getRespiratoryRateValue() );
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
	 * @param respiratoryRateVo the respiratory rate vo
	 * @throws Exception the exception
	 */
	public static void deleteByPk( MasterConfig masterConfig, RespiratoryRateVo respiratoryRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUuid() );
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
	 * @param respiratoryRateVo the respiratory rate vo
	 * @throws Exception the exception
	 */
	public static void deleteBatchMode( Connection con, RespiratoryRateVo respiratoryRateVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUuid() );
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
	 * @param respiratoryRateVo the respiratory rate vo
	 * @return the respiratory rate vo
	 * @throws Exception the exception
	 */
	public static RespiratoryRateVo findByPk( MasterConfig masterConfig, RespiratoryRateVo respiratoryRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, respiratoryRateVo.getRespiratoryRateUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new RespiratoryRateVo(rs);
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
