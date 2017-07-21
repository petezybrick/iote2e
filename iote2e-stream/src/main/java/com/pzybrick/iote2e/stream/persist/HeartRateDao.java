package com.pzybrick.iote2e.stream.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.HeartRate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;


public class HeartRateDao extends OmhDao  {
	private static final Logger logger = LogManager.getLogger(HeartRateDao.class);
	private static String sqlDeleteByPk = "DELETE FROM heart_rate WHERE heart_rate_uuid=?";
	private static String sqlInsert = "INSERT INTO heart_rate (heart_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,temporal_relationship_to_physical_activity,heart_rate_unit,heart_rate_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT heart_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,temporal_relationship_to_physical_activity,heart_rate_unit,heart_rate_value,insert_ts FROM heart_rate WHERE heart_rate_uuid=?";


	public static void insertFromJsonSchema( PreparedStatement pstmt, DataPointHeader dataPointHeader, String rawJson, ObjectMapper objectMapper ) throws Exception {
		try {
			HeartRate heartRate = objectMapper.readValue(rawJson, HeartRate.class);
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
					new Timestamp(heartRate.getEffectiveTimeFrame().getDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, heartRate.getDescriptiveStatistic().name() );
			pstmt.setString( offset++, heartRate.getUserNotes() );
			
			pstmt.setString( offset++, heartRate.getTemporalRelationshipToPhysicalActivity().name() );
			pstmt.setString( offset++, heartRate.getHeartRate().getUnit() );
			pstmt.setInt( offset++, heartRate.getHeartRate().getValue().intValue() );
			
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	
	public static void insertBatchMode( Connection con, HeartRateVo heartRateVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, heartRateVo.getHeartRateUuid() );
			pstmt.setString( offset++, heartRateVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, heartRateVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, heartRateVo.getHdrUserId() );
			pstmt.setString( offset++, heartRateVo.getHdrModality() );
			pstmt.setString( offset++, heartRateVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, heartRateVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, heartRateVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, heartRateVo.getUserNotes() );
			pstmt.setString( offset++, heartRateVo.getTemporalRelationshipToPhysicalActivity() );
			pstmt.setString( offset++, heartRateVo.getHeartRateUnit() );
			pstmt.setInt( offset++, heartRateVo.getHeartRateValue() );
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

	public static void insert( MasterConfig masterConfig, HeartRateVo heartRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, heartRateVo.getHeartRateUuid() );
			pstmt.setString( offset++, heartRateVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, heartRateVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, heartRateVo.getHdrUserId() );
			pstmt.setString( offset++, heartRateVo.getHdrModality() );
			pstmt.setString( offset++, heartRateVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, heartRateVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, heartRateVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, heartRateVo.getUserNotes() );
			pstmt.setString( offset++, heartRateVo.getTemporalRelationshipToPhysicalActivity() );
			pstmt.setString( offset++, heartRateVo.getHeartRateUnit() );
			pstmt.setInt( offset++, heartRateVo.getHeartRateValue() );
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

	public static void deleteByPk( MasterConfig masterConfig, HeartRateVo heartRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, heartRateVo.getHeartRateUuid() );
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

	public static void deleteBatchMode( Connection con, HeartRateVo heartRateVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, heartRateVo.getHeartRateUuid() );
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

	public static HeartRateVo findByPk( MasterConfig masterConfig, HeartRateVo heartRateVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, heartRateVo.getHeartRateUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new HeartRateVo(rs);
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
