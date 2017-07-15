package com.pzybrick.iote2e.stream.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pzybrick.iote2e.common.config.MasterConfig;


public class RespiratoryRateDao {
	private static final Logger logger = LogManager.getLogger(RespiratoryRateDao.class);
	private static String sqlDeleteByPk = "DELETE FROM respiratory_rate WHERE respiratory_rate_uuid=?";
	private static String sqlInsert = "INSERT INTO respiratory_rate (respiratory_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,descriptive_statistic,temporal_relationship_to_physical_activity,respiratory_rate_unit,respiratory_rate_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT respiratory_rate_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,user_notes,descriptive_statistic,temporal_relationship_to_physical_activity,respiratory_rate_unit,respiratory_rate_value,insert_ts FROM respiratory_rate WHERE respiratory_rate_uuid=?";

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
