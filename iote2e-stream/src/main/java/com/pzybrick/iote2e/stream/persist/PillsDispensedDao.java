package com.pzybrick.iote2e.stream.persist;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo.DispenseState;

public class PillsDispensedDao {
	private static final Logger logger = LogManager.getLogger(PillsDispensedDao.class);
	private static String sqlInsertPending = "INSERT INTO pills_dispensed (pills_dispensed_uuid,login_name,actuator_name,dispense_state,num_to_dispense,state_pending_ts,insert_ts) VALUES(?,?,?,?,?,now(),now())";
	private static String sqlUpdatePendingToDispensing = "UPDATE pills_dispensed SET dispense_state=?,state_dispensing_ts=now() WHERE pills_dispensed_uuid=?";
	private static String sqlUpdateDispensingToComplete = "UPDATE pills_dispensed SET dispense_state=?,state_complete_ts=now(),num_dispensed=?,delta=? WHERE pills_dispensed_uuid=?";
	private static String sqlInsertImage = "INSERT INTO pills_dispensed_image (pills_dispensed_uuid,image_png,insert_ts) VALUES(?,?,now())";
	private static String sqlFindByPillsDispensedUuid = "SELECT * FROM pills_dispensed WHERE pills_dispensed_uuid=?";
	
	
	public static void updateDispensingToComplete(MasterConfig masterConfig, String pillsDispensedUuid, Integer numDispensed, Integer delta, byte[] imagePng ) throws Exception {
		Connection con = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsertImage = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmtUpdate = con.prepareStatement(sqlUpdateDispensingToComplete);
			int offset = 1;
			pstmtUpdate.setString(offset++, DispenseState.COMPLETE.toString());
			pstmtUpdate.setInt(offset++, numDispensed);
			pstmtUpdate.setInt(offset++, delta);
			pstmtUpdate.setString(offset++, pillsDispensedUuid);
			pstmtUpdate.executeUpdate();
			
			pstmtInsertImage = con.prepareStatement(sqlInsertImage);
			pstmtInsertImage.setString(1, pillsDispensedUuid);
			ByteArrayInputStream bais = new ByteArrayInputStream(imagePng);
			pstmtInsertImage.setBinaryStream(2, bais);
			
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
				if (pstmtUpdate != null)
					pstmtUpdate.close();
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

	
	public static void updatePendingToInProgress(MasterConfig masterConfig, String pillsDispensedUuid) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlUpdatePendingToDispensing);
			int offset = 1;
			pstmt.setString(offset++, DispenseState.DISPENSING.toString());
			pstmt.setString(offset++, pillsDispensedUuid );
			pstmt.executeUpdate();
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
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}

	
	public static void insertPending(MasterConfig masterConfig, String pillsDispensedUuid, String loginName, String actuatorName, Integer numToDispense ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlInsertPending);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
			pstmt.setString(offset++, loginName);
			pstmt.setString(offset++, actuatorName);
			pstmt.setString(offset++, DispenseState.PENDING.toString());
			pstmt.setInt(offset++, numToDispense);
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
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}
	
	public static PillsDispensedVo findByPillsDispensedUuid(MasterConfig masterConfig, String pillsDispensedUuid ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPillsDispensedUuid);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
			ResultSet rs = pstmt.executeQuery();
			if( !rs.next() ) throw new Exception("Row not found for pillsDispensedUuid=" + pillsDispensedUuid);
			PillsDispensedVo pillsDispensedVo = new PillsDispensedVo(rs);
			rs.close();
			return pillsDispensedVo;
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
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}
	
}
