package com.pzybrick.iote2e.ruleproc.request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.persist.PooledDataSource;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestRouterHandlerSparkDbImpl implements Iote2eRequestRouterHandler {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestRouterHandlerSparkDbImpl.class);
	private MasterConfig masterConfig;


	public Iote2eRequestRouterHandlerSparkDbImpl( ) throws Exception {

	}
	
	
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public void processRequests( List<Iote2eRequest> iote2eRequests ) throws Exception {
		Connection con = null;
		Map<String,PreparedStatement> cachePrepStmtsByTableName = new HashMap<String,PreparedStatement>();
		try {
			if( iote2eRequests != null && iote2eRequests.size() > 0 ) {
				con = PooledDataSource.getInstance(masterConfig).getConnection();
				insertAllBlocks( iote2eRequests, con, cachePrepStmtsByTableName );
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			try {
				if( con != null ) 
					con.rollback();
			} catch( Exception exRoll ) {
				logger.warn(exRoll.getMessage());
			}
		} finally {
			for( PreparedStatement pstmt : cachePrepStmtsByTableName.values() ) {
				try {
					pstmt.close();
				} catch( Exception e ) {
					logger.warn(e);
				}
			}
			if( con != null ) {
				try {
					con.close();
				} catch(Exception exCon ) {
					logger.warn(exCon.getMessage());
				}
			}
		}
	}
	
	
	private void insertAllBlocks( List<Iote2eRequest> iote2eRequests, Connection con, Map<String,PreparedStatement> cachePrepStmtsByTableName ) throws Exception {
		Integer insertBlockSize = masterConfig.getJdbcInsertBlockSize();
		for( int i=0 ;; i+=insertBlockSize ) {
			int startNextBlock = i + insertBlockSize;
			if( startNextBlock > iote2eRequests.size() ) 
				startNextBlock = iote2eRequests.size();
			try {
				insertEachBlock(iote2eRequests.subList(i, startNextBlock), con, cachePrepStmtsByTableName);
			} catch( Exception e ) {
				// At least one failure, reprocess one by one
				for( Iote2eRequest iote2eRequest : iote2eRequests.subList(i, startNextBlock) ) {
					insertEachBlock( Arrays.asList(iote2eRequest), con, cachePrepStmtsByTableName);
				}
				
			}
			if( startNextBlock == iote2eRequests.size() ) break;
		}
	}
	
	
	private void insertEachBlock( List<Iote2eRequest> iote2eRequests, Connection con, Map<String,PreparedStatement> cachePrepStmtsByTableName ) throws Exception {
		final DateTimeFormatter dtfmt = ISODateTimeFormat.basicDateTime();
		String tableName = null;
		String request_uuid = null;
		PreparedStatement pstmt = null;
		try {
			for( Iote2eRequest iote2eRequest : iote2eRequests) {
				tableName = iote2eRequest.getSourceType().toString();
				request_uuid = iote2eRequest.getRequestUuid().toString();
				pstmt = getPreparedStatement( tableName, con, cachePrepStmtsByTableName );
				int offset = 1;
				// First set of values are the same on every table
				pstmt.setString(offset++, request_uuid );
				pstmt.setString(offset++, iote2eRequest.getLoginName().toString());
				pstmt.setString(offset++, iote2eRequest.getSourceName().toString());
				Timestamp timestamp = new Timestamp(dtfmt.parseDateTime(iote2eRequest.getRequestTimestamp().toString()).getMillis());
				pstmt.setTimestamp(offset++, timestamp);
				// Next value(s)/types are specific to the table
				// For this simple example, assume one value passed as string
				String value = iote2eRequest.getPairs().values().iterator().next().toString();
				if( "temperature".compareToIgnoreCase(tableName) == 0) {
					// temp_f
					pstmt.setFloat(offset++, new Float(value));
				}else if( "humidity".compareToIgnoreCase(tableName) == 0) {
					// pct_humidity
					pstmt.setFloat(offset++, new Float(value));
				}else if( "switch".compareToIgnoreCase(tableName) == 0) {
					// switch_state
					pstmt.setInt(offset++, Integer.parseInt(value));
				}else if( "heartbeat".compareToIgnoreCase(tableName) == 0) {
					// heartbeat_state
					pstmt.setInt(offset++, Integer.parseInt(value));
				}
				pstmt.execute();
			}
			con.commit();
		} catch( SQLException sqlEx ) {
			con.rollback();
			// Suppress duplicate rows, assume the are the same and were sent over Kafka > 1 time
			if( iote2eRequests.size() == 1 ) {
				if( sqlEx.getSQLState() != null && sqlEx.getSQLState().startsWith("23") )
					logger.debug("Skipping duplicate row, table={}, request_uuid={}", tableName, request_uuid);
				else {
					throw sqlEx;
				}
			} else {
				throw sqlEx;
			}
		} catch( Exception e2 ) {
			con.rollback();
			throw e2;
		}
	}
	
	
	/*
	 * A bit of a hack
	 */
	private PreparedStatement getPreparedStatement( String tableName, Connection con, Map<String,PreparedStatement> cachePrepStmtsByTableName ) throws Exception {
		if( cachePrepStmtsByTableName.containsKey(tableName) ) return cachePrepStmtsByTableName.get(tableName);
		final String sqlTemperature = "INSERT INTO temperature (request_uuid,login_name,source_name,request_timestamp,degrees_f) VALUES (?,?,?,?,?)";
		final String sqlHumidity = "INSERT INTO humidity (request_uuid,login_name,source_name,request_timestamp,pct_humidity) VALUES (?,?,?,?,?)";
		final String sqlSwitch = "INSERT INTO temperature (request_uuid,login_name,source_name,request_timestamp,switch_state) VALUES (?,?,?,?,?)";
		final String sqlHeartbeat = "INSERT INTO temperature (request_uuid,login_name,source_name,request_timestamp,heartbeat_state) VALUES (?,?,?,?,?)";
		
		String sql = null;
		if( "temperature".compareToIgnoreCase(tableName) == 0) sql = sqlTemperature;
		else if( "humidity".compareToIgnoreCase(tableName) == 0) sql = sqlHumidity;
		else if( "switch".compareToIgnoreCase(tableName) == 0) sql = sqlSwitch;
		else if( "heartbeat".compareToIgnoreCase(tableName) == 0) sql = sqlHeartbeat;

		PreparedStatement pstmt = con.prepareStatement(sql);
		cachePrepStmtsByTableName.put(tableName, pstmt);
		return pstmt;
	}

}
		
