package com.pzybrick.iote2e.stream.bdbb;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.DataPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.EngineStatusDao;
import com.pzybrick.iote2e.stream.persist.FlightStatusDao;
import com.pzybrick.iote2e.stream.persist.OmhDao;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;

public class BdbbRouterHandlerSparkBatchImpl implements BdbbRouterHandler {
	private static final Logger logger = LogManager.getLogger(BdbbRouterHandlerSparkBatchImpl.class);
	private MasterConfig masterConfig;


	public BdbbRouterHandlerSparkBatchImpl( ) throws Exception {
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
		try {
			if( byteBuffers != null && byteBuffers.size() > 0 ) {
				con = PooledDataSource.getInstance(masterConfig).getConnection();
				insertAllBlocks( byteBuffers, con );
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
			if( con != null ) {
				try {
					con.close();
				} catch(Exception exCon ) {
					logger.warn(exCon.getMessage());
				}
			}
		}
	}
	
	
	private void insertAllBlocks( List<ByteBuffer> byteBuffers, Connection con ) throws Exception {
		Integer insertBlockSize = masterConfig.getJdbcInsertBlockSize();
		for( int i=0 ;; i+=insertBlockSize ) {
			int startNextBlock = i + insertBlockSize;
			if( startNextBlock > byteBuffers.size() ) 
				startNextBlock = byteBuffers.size();
			try {
				insertEachBlock(byteBuffers.subList(i, startNextBlock), con );
			} catch( Exception e ) {
				// At least one failure, reprocess one by one
				for( ByteBuffer byteBuffer : byteBuffers.subList(i, startNextBlock) ) {
					insertEachBlock( Arrays.asList(byteBuffer), con );
				}
				
			}
			if( startNextBlock == byteBuffers.size() ) break;
		}
	}
	
	
	private void insertEachBlock( List<ByteBuffer> byteBuffers, Connection con ) throws Exception {
		FlightStatus flightStatus = null;
		try {
			for( ByteBuffer byteBuffer : byteBuffers) {
				String rawJson = new String( CompressionUtils.decompress(byteBuffer.array()) );
				try {
					flightStatus = Iote2eUtils.getGsonInstance().fromJson(rawJson, FlightStatus.class);
					FlightStatusDao.insertBatchMode(con, flightStatus);
					for( EngineStatus engineStatus : flightStatus.getEngineStatuss() ) {
						EngineStatusDao.insertBatchMode(con, engineStatus);
					}
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
					logger.debug("Skipping duplicate row, flightStatusUuid={}", flightStatus.getFlightStatusUuid() );
				else {
					if( flightStatus != null ) 
						logger.error("Error on insert for flightStatusUuid={}", flightStatus.getFlightStatusUuid() );
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
	
}
		
