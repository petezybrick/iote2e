package com.pzybrick.iote2e.stream.omh;

import java.nio.ByteBuffer;
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
import org.openmhealth.schema.domain.omh.DataPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.stream.persist.OmhDao;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;

public class OmhRouterHandlerSparkDbImpl implements OmhRouterHandler {
	private static final Logger logger = LogManager.getLogger(OmhRouterHandlerSparkDbImpl.class);
	private MasterConfig masterConfig;
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;


	public OmhRouterHandlerSparkDbImpl( ) throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public void processRequests( List<ByteBuffer> byteBuffers ) throws Exception {
		Connection con = null;
		Map<String,PreparedStatement> cachePrepStmtsByTableName = new HashMap<String,PreparedStatement>();
		try {
			if( byteBuffers != null && byteBuffers.size() > 0 ) {
				con = PooledDataSource.getInstance(masterConfig).getConnection();
				insertAllBlocks( byteBuffers, con, cachePrepStmtsByTableName );
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
	
	
	private void insertAllBlocks( List<ByteBuffer> byteBuffers, Connection con, Map<String,PreparedStatement> cachePrepStmtsByTableName ) throws Exception {
		Integer insertBlockSize = masterConfig.getJdbcInsertBlockSize();
		for( int i=0 ;; i+=insertBlockSize ) {
			int startNextBlock = i + insertBlockSize;
			if( startNextBlock > byteBuffers.size() ) 
				startNextBlock = byteBuffers.size();
			try {
				insertEachBlock(byteBuffers.subList(i, startNextBlock), con, cachePrepStmtsByTableName);
			} catch( Exception e ) {
				// At least one failure, reprocess one by one
				for( ByteBuffer byteBuffer : byteBuffers.subList(i, startNextBlock) ) {
					insertEachBlock( Arrays.asList(byteBuffer), con, cachePrepStmtsByTableName);
				}
				
			}
			if( startNextBlock == byteBuffers.size() ) break;
		}
	}
	
	
	private void insertEachBlock( List<ByteBuffer> byteBuffers, Connection con, Map<String,PreparedStatement> cachePrepStmtsByTableName ) throws Exception {
		final DateTimeFormatter dtfmt = ISODateTimeFormat.dateTime();
		DataPoint dataPoint = null;
		try {
			for( ByteBuffer byteBuffer : byteBuffers) {
				// Decompress the JSON string
				String rawJson = CompressionUtils.decompress(byteBuffer.array()).toString();
				// JSON into Datapoint
				try {
			        dataPoint = objectMapper.readValue(rawJson, DataPoint.class);
			        logger.debug( "OMH Datapoint: {} {}, userId={}, uuid={}", dataPoint.getHeader().getBodySchemaId().getName(),
			        		dataPoint.getHeader().getBodySchemaId().getVersion(), dataPoint.getHeader().getUserId(),
			        		dataPoint.getHeader().getId() );
			        OmhDao.insertBatch( con, dataPoint );
				} catch(Exception e ) {
					logger.error(e.getMessage(), e);
					throw e;
				}
			}
			con.commit();
		} catch( SQLException sqlEx ) {
			con.rollback();
			// Suppress duplicate rows, assume the are the same and were sent over Kafka > 1 time
			if( byteBuffers.size() == 1 ) {
				if( sqlEx.getSQLState() != null && sqlEx.getSQLState().startsWith("23") )
					logger.debug("Skipping duplicate row, schemaName={}, schemaUuid={}", dataPoint.getSchemaId().getName(), dataPoint.getSchemaId().getSchemaId() );
				else {
					if( dataPoint != null ) 
						logger.error("Error on insert for schemaName={}, schemaUuid={}", dataPoint.getSchemaId().getName(), dataPoint.getSchemaId().getSchemaId() );
					else logger.error("Error on insert: {}", sqlEx.getMessage() );
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
		final String sqlTemperature = "INSERT INTO temperature (request_uuid,login_name,source_name,request_timestamp,degrees_c) VALUES (?,?,?,?,?)";
		final String sqlHumidity = "INSERT INTO humidity (request_uuid,login_name,source_name,request_timestamp,pct_humidity) VALUES (?,?,?,?,?)";
		final String sqlSwitch = "INSERT INTO switch (request_uuid,login_name,source_name,request_timestamp,switch_state) VALUES (?,?,?,?,?)";
		final String sqlHeartbeat = "INSERT INTO heartbeat (request_uuid,login_name,source_name,request_timestamp,heartbeat_state) VALUES (?,?,?,?,?)";
		
		String sql = null;
		if( "temperature".compareToIgnoreCase(tableName) == 0) sql = sqlTemperature;
		else if( "humidity".compareToIgnoreCase(tableName) == 0) sql = sqlHumidity;
		else if( "switch".compareToIgnoreCase(tableName) == 0) sql = sqlSwitch;
		else if( "heartbeat".compareToIgnoreCase(tableName) == 0) sql = sqlHeartbeat;
		else if( "pill_dispenser".compareToIgnoreCase(tableName) == 0) sql = null;
		else throw new Exception("Invalid table name: " + tableName);

		PreparedStatement pstmt = null;
		if( sql != null ) { 
			logger.debug("Creating pstmt for {}",  tableName);
			pstmt = con.prepareStatement(sql);
			cachePrepStmtsByTableName.put(tableName, pstmt );
		} else logger.debug("NOT Creating pstmt for {}", tableName);
		
		return pstmt;
	}

}
		
