package com.pzybrick.iote2e.stream.bdbb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;


public class FlightStatusDao {
	private static final Logger logger = LogManager.getLogger(FlightStatusDao.class);
	private static String sqlDeleteByPk = "DELETE FROM flight_status WHERE flight_status_uuid=?";
	private static String sqlInsert = "INSERT INTO flight_status (flight_status_uuid,airframe_Uuid,flight_number,from_airport,to_airport,lat,lng,alt,airspeed,heading,flight_status_ts) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT flight_status_uuid,airframe_Uuid,flight_number,from_airport,to_airport,lat,lng,alt,airspeed,heading,flight_status_ts,insert_ts FROM flight_status WHERE flight_status_uuid=?";

	public static void insertBatchMode( Connection con, FlightStatus flightStatus ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, flightStatus.getFlightStatusUuid() );
			pstmt.setString( offset++, flightStatus.getAirframeUuid() );
			pstmt.setString( offset++, flightStatus.getFlightNumber() );
			pstmt.setString( offset++, flightStatus.getFromAirport() );
			pstmt.setString( offset++, flightStatus.getToAirport() );
			pstmt.setDouble( offset++, flightStatus.getLat() );
			pstmt.setDouble( offset++, flightStatus.getLng() );
			pstmt.setFloat( offset++, flightStatus.getAlt() );
			pstmt.setFloat( offset++, flightStatus.getAirspeed() );
			pstmt.setFloat( offset++, flightStatus.getHeading() );
			pstmt.setTimestamp( offset++, new Timestamp(flightStatus.getFlightStatusTs()) );
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

	public static void insert( MasterConfig masterConfig, FlightStatus flightStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, flightStatus.getFlightStatusUuid() );
			pstmt.setString( offset++, flightStatus.getAirframeUuid() );
			pstmt.setString( offset++, flightStatus.getFlightNumber() );
			pstmt.setString( offset++, flightStatus.getFromAirport() );
			pstmt.setString( offset++, flightStatus.getToAirport() );
			pstmt.setDouble( offset++, flightStatus.getLat() );
			pstmt.setDouble( offset++, flightStatus.getLng() );
			pstmt.setFloat( offset++, flightStatus.getAlt() );
			pstmt.setFloat( offset++, flightStatus.getAirspeed() );
			pstmt.setFloat( offset++, flightStatus.getHeading() );
			pstmt.setTimestamp( offset++, new Timestamp(flightStatus.getFlightStatusTs()) );
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

	public static void deleteByPk( MasterConfig masterConfig, FlightStatus flightStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, flightStatus.getFlightStatusUuid() );
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

	public static void deleteBatchMode( Connection con, FlightStatus flightStatus ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, flightStatus.getFlightStatusUuid() );
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

	public static FlightStatus findByPk( MasterConfig masterConfig, FlightStatus flightStatus ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, flightStatus.getFlightStatusUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new FlightStatus(rs);
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