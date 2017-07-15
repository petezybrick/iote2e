package com.pzybrick.iote2e.stream.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.pzybrick.iote2e.common.config.MasterConfig;


public class BloodPressureDao {
	private static final Logger logger = LogManager.getLogger(BloodPressureDao.class);
	private static String sqlDeleteByPk = "DELETE FROM blood_pressure WHERE blood_pressure_uuid=?";
	private static String sqlInsert = "INSERT INTO blood_pressure (blood_pressure_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,position_during_measurement,systolic_blood_pressure_unit,systolic_blood_pressure_value,diastolic_blood_pressure_unit,diastolic_blood_pressure_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT blood_pressure_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,position_during_measurement,systolic_blood_pressure_unit,systolic_blood_pressure_value,diastolic_blood_pressure_unit,diastolic_blood_pressure_value,insert_ts FROM blood_pressure WHERE blood_pressure_uuid=?";

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
