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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PillsDispensedVo.DispenseState;


/**
 * The Class PillsDispensedDao.
 */
public class PillsDispensedDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(PillsDispensedDao.class);
	
	/** The sql insert pending. */
	private static String sqlInsertPending = "INSERT INTO pills_dispensed (pills_dispensed_uuid,login_name,source_name,actuator_name,dispense_state,num_to_dispense,state_pending_ts,insert_ts) VALUES(?,?,?,?,?,?,now(),now())";
	
	/** The sql update pending to dispensing. */
	private static String sqlUpdatePendingToDispensing = "UPDATE pills_dispensed SET dispense_state=?,state_dispensing_ts=now() WHERE pills_dispensed_uuid=?";
	
	/** The sql update dispensing to dispensed. */
	private static String sqlUpdateDispensingToDispensed = "UPDATE pills_dispensed SET dispense_state=?,state_dispensed_ts=now(),num_dispensed=?,delta=? WHERE pills_dispensed_uuid=?";
	
	/** The sql update dispensed to confirmed. */
	private static String sqlUpdateDispensedToConfirmed = "UPDATE pills_dispensed SET dispense_state=?,state_confirmed_ts=now() WHERE pills_dispensed_uuid=?";
	
	/** The sql insert image. */
	private static String sqlInsertImage = "INSERT INTO pills_dispensed_image (pills_dispensed_uuid,image_png,insert_ts) VALUES(?,?,now())";
	
	/** The sql find all pills dispensed uuids. */
	private static String sqlFindAllPillsDispensedUuids = "SELECT pills_dispensed_uuid FROM pills_dispensed";
	
	/** The sql find by pills dispensed uuid. */
	private static String sqlFindByPillsDispensedUuid = "SELECT * FROM pills_dispensed WHERE pills_dispensed_uuid=?";
	
	/** The sql find by dispense state. */
	private static String sqlFindByDispenseState = "SELECT * FROM pills_dispensed WHERE dispense_state=?";
	
	/** The sql find image bytes by pills dispensed uuid. */
	private static String sqlFindImageBytesByPillsDispensedUuid = "SELECT image_png FROM pills_dispensed_image WHERE pills_dispensed_uuid=?";
	
	/** The sql delete pills dispensed by pills dispensed uuid. */
	private static String sqlDeletePillsDispensedByPillsDispensedUuid = "DELETE FROM pills_dispensed WHERE pills_dispensed_uuid=?";
	
	/** The sql delete image bytes by pills dispensed uuid. */
	private static String sqlDeleteImageBytesByPillsDispensedUuid = "DELETE FROM pills_dispensed_image WHERE pills_dispensed_uuid=?";
	
	
	/**
	 * Update dispensed to confirmed.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @throws Exception the exception
	 */
	public static void updateDispensedToConfirmed(MasterConfig masterConfig, String pillsDispensedUuid ) throws Exception {
		Connection con = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsertImage = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmtUpdate = con.prepareStatement(sqlUpdateDispensedToConfirmed);
			int offset = 1;
			pstmtUpdate.setString(offset++, DispenseState.CONFIRMED.toString());
			pstmtUpdate.setString(offset++, pillsDispensedUuid);
			pstmtUpdate.executeUpdate();			
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
	
	
	/**
	 * Update dispensing to dispensed.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @param numDispensed the num dispensed
	 * @param delta the delta
	 * @param imagePng the image png
	 * @throws Exception the exception
	 */
	public static void updateDispensingToDispensed(MasterConfig masterConfig, String pillsDispensedUuid, Integer numDispensed, Integer delta, byte[] imagePng ) throws Exception {
		Connection con = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsertImage = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmtUpdate = con.prepareStatement(sqlUpdateDispensingToDispensed);
			int offset = 1;
			pstmtUpdate.setString(offset++, DispenseState.DISPENSED.toString());
			pstmtUpdate.setInt(offset++, numDispensed);
			pstmtUpdate.setInt(offset++, delta);
			pstmtUpdate.setString(offset++, pillsDispensedUuid);
			pstmtUpdate.executeUpdate();
			
			pstmtInsertImage = con.prepareStatement(sqlInsertImage);
			pstmtInsertImage.setString(1, pillsDispensedUuid);
			ByteArrayInputStream bais = new ByteArrayInputStream(imagePng);
			pstmtInsertImage.setBinaryStream(2, bais);
			pstmtInsertImage.execute();
			
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

	
	/**
	 * Update pending to dispensing.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @throws Exception the exception
	 */
	public static void updatePendingToDispensing(MasterConfig masterConfig, String pillsDispensedUuid) throws Exception {
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

	
	/**
	 * Insert pending.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param actuatorName the actuator name
	 * @param numToDispense the num to dispense
	 * @throws Exception the exception
	 */
	public static void insertPending(MasterConfig masterConfig, String pillsDispensedUuid, String loginName, String sourceName, String actuatorName, Integer numToDispense ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlInsertPending);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
			pstmt.setString(offset++, loginName);
			pstmt.setString(offset++, sourceName);
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

	
	/**
	 * Sql find by dispense state.
	 *
	 * @param masterConfig the master config
	 * @param dispenseState the dispense state
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<PillsDispensedVo> sqlFindByDispenseState(MasterConfig masterConfig, DispenseState dispenseState ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			List<PillsDispensedVo> pillsDispensedVos = new ArrayList<PillsDispensedVo>();
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByDispenseState);
			int offset = 1;
			pstmt.setString(offset++, dispenseState.toString());
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() ) {
				pillsDispensedVos.add(new PillsDispensedVo(rs));
			}
			rs.close();
			return pillsDispensedVos;
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
	
	/**
	 * Find by pills dispensed uuid.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @return the pills dispensed vo
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Find image bytes by pills dispensed uuid.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @return the byte[]
	 * @throws Exception the exception
	 */
	public static byte[] findImageBytesByPillsDispensedUuid(MasterConfig masterConfig, String pillsDispensedUuid ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindImageBytesByPillsDispensedUuid);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
			ResultSet rs = pstmt.executeQuery();
			if( !rs.next() ) throw new Exception("Row not found for pillsDispensedUuid=" + pillsDispensedUuid);
			InputStream is = rs.getBinaryStream("image_png");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int buffByte;
			while( ( buffByte = is.read()) != -1 ) baos.write(buffByte);
			byte[] imageBytes = baos.toByteArray();
			baos.close();
			rs.close();
			return imageBytes;
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
	
	
	/**
	 * Find all pills dispensed uuids.
	 *
	 * @param masterConfig the master config
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<String> findAllPillsDispensedUuids( MasterConfig masterConfig ) throws Exception {
		List<String> allUuids = new ArrayList<String>();
		Connection con = null;
		Statement stmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlFindAllPillsDispensedUuids);
			while( rs.next() ) {
				allUuids.add( rs.getString(1));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
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
		return allUuids;
	}
	
	
	/**
	 * Delete pills dispensed by pills dispensed uuid.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @throws Exception the exception
	 */
	public static void deletePillsDispensedByPillsDispensedUuid(MasterConfig masterConfig, String pillsDispensedUuid ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeletePillsDispensedByPillsDispensedUuid);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
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
	
	
	/**
	 * Delete image bytes by pills dispensed uuid.
	 *
	 * @param masterConfig the master config
	 * @param pillsDispensedUuid the pills dispensed uuid
	 * @throws Exception the exception
	 */
	public static void deleteImageBytesByPillsDispensedUuid(MasterConfig masterConfig, String pillsDispensedUuid ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteImageBytesByPillsDispensedUuid);
			int offset = 1;
			pstmt.setString(offset++, pillsDispensedUuid);
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
	
}
