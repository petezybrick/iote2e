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

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.bdbb.EngineStatus;



/**
 * The Class EngineStatusDao.
 */
public class EngineStatusDao {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(EngineStatusDao.class);
	
	/** The sql delete by pk. */
	private static String sqlDeleteByPk = "DELETE FROM engine_status WHERE engine_status_uuid=?";
	
	/** The sql insert. */
	private static String sqlInsert = "INSERT INTO engine_status (engine_status_uuid,flight_status_uuid,engine_uuid,engine_number,oil_temp_c,oil_pressure,exhaust_gas_temp_c,n1_pct,n2_pct,engine_status_ts) VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/** The sql find by pk. */
	private static String sqlFindByPk = "SELECT engine_status_uuid,flight_status_uuid,engine_uuid,engine_number,oil_temp_c,oil_pressure,exhaust_gas_temp_c,n1_pct,n2_pct,engine_status_ts,insert_ts FROM engine_status WHERE engine_status_uuid=?";

	/**
	 * Insert batch mode.
	 *
	 * @param con the con
	 * @param engineStatus the engine status
	 * @throws Exception the exception
	 */
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

	/**
	 * Insert.
	 *
	 * @param masterConfig the master config
	 * @param engineStatus the engine status
	 * @throws Exception the exception
	 */
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

	/**
	 * Delete by pk.
	 *
	 * @param masterConfig the master config
	 * @param engineStatus the engine status
	 * @throws Exception the exception
	 */
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

	/**
	 * Delete batch mode.
	 *
	 * @param con the con
	 * @param engineStatus the engine status
	 * @throws Exception the exception
	 */
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

	/**
	 * Find by pk.
	 *
	 * @param masterConfig the master config
	 * @param engineStatus the engine status
	 * @return the engine status
	 * @throws Exception the exception
	 */
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