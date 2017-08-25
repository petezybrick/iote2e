package com.pzybrick.iote2e.stream.bdbb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;


public class EngineStatusDao {
	private static final Logger logger = LogManager.getLogger(EngineStatusDao.class);
	private static String sqlDeleteByPk = "DELETE FROM engine_status WHERE engine_status_uuid=?";
	private static String sqlInsert = "INSERT INTO engine_status (engine_status_uuid,flight_status_uuid,engine_uuid,engine_number,oil_temp_c,oil_pressure,exhaust_gas_temp_c,n1_pct,n2_pct,engine_status_ts) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT engine_status_uuid,flight_status_uuid,engine_uuid,engine_number,oil_temp_c,oil_pressure,exhaust_gas_temp_c,n1_pct,n2_pct,engine_status_ts,insert_ts FROM engine_status WHERE engine_status_uuid=?";

	public static void insertBatchMode( Connection con, EngineStatus engineStatus ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, engineStatus.getEngineStatusUuid() );
			pstmt.setString( offset++, engineStatus.getFlightStatusUuid() );
			pstmt.setString( offset++, engineStatus.getEngineUuid() );
			pstmt.setInt( offset++, engineStatus.getEngineNumber() );
			pstmt.setFloat( offset++, engineStatus.getOilTempC() );
			pstmt.setFloat( offset++, engineStatus.getOilPressure() );
			pstmt.setFloat( offset++, engineStatus.getExhaustGasTempC() );
			pstmt.setFloat( offset++, engineStatus.getN1Pct() );
			pstmt.setFloat( offset++, engineStatus.getN2Pct() );
			pstmt.setTimestamp( offset++, new Timestamp(engineStatus.getEngineStatusTs()) );
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

	public static void insert( MasterConfig masterConfig, EngineStatus engineStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, engineStatus.getEngineStatusUuid() );
			pstmt.setString( offset++, engineStatus.getFlightStatusUuid() );
			pstmt.setString( offset++, engineStatus.getEngineUuid() );
			pstmt.setInt( offset++, engineStatus.getEngineNumber() );
			pstmt.setFloat( offset++, engineStatus.getOilTempC() );
			pstmt.setFloat( offset++, engineStatus.getOilPressure() );
			pstmt.setFloat( offset++, engineStatus.getExhaustGasTempC() );
			pstmt.setFloat( offset++, engineStatus.getN1Pct() );
			pstmt.setFloat( offset++, engineStatus.getN2Pct() );
			pstmt.setTimestamp( offset++, new Timestamp(engineStatus.getEngineStatusTs()) );
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

	public static void deleteByPk( MasterConfig masterConfig, EngineStatus engineStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, engineStatus.getEngineStatusUuid() );
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

	public static void deleteBatchMode( Connection con, EngineStatus engineStatus ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, engineStatus.getEngineStatusUuid() );
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

	public static EngineStatus findByPk( MasterConfig masterConfig, EngineStatus engineStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, engineStatus.getEngineStatusUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new EngineStatus(rs);
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