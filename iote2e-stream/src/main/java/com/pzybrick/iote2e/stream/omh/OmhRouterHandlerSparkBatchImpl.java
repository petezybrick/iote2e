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
package com.pzybrick.iote2e.stream.omh;

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
import com.pzybrick.iote2e.stream.persist.OmhDao;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;


/**
 * The Class OmhRouterHandlerSparkBatchImpl.
 */
public class OmhRouterHandlerSparkBatchImpl implements OmhRouterHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(OmhRouterHandlerSparkBatchImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/** The object mapper. */
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;


	/**
	 * Instantiates a new omh router handler spark batch impl.
	 *
	 * @throws Exception the exception
	 */
	public OmhRouterHandlerSparkBatchImpl( ) throws Exception {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.omh.OmhRouterHandler#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			this.masterConfig = masterConfig;
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.omh.OmhRouterHandler#processRequests(java.util.List)
	 */
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
	
	
	/**
	 * Insert all blocks.
	 *
	 * @param byteBuffers the byte buffers
	 * @param con the con
	 * @throws Exception the exception
	 */
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
	
	
	/**
	 * Insert each block.
	 *
	 * @param byteBuffers the byte buffers
	 * @param con the con
	 * @throws Exception the exception
	 */
	private void insertEachBlock( List<ByteBuffer> byteBuffers, Connection con ) throws Exception {
		DataPoint dataPoint = null;
		try {
			for( ByteBuffer byteBuffer : byteBuffers) {
				// Decompress the JSON string
				String rawJson = new String( CompressionUtils.decompress(byteBuffer.array()) );
				// JSON into Datapoint
				try {
			        dataPoint = objectMapper.readValue(rawJson, DataPoint.class);
			        logger.debug( "OMH Datapoint: {} {}, userId={}, uuid={}", dataPoint.getHeader().getBodySchemaId().getName(),
			        		dataPoint.getHeader().getBodySchemaId().getVersion(), dataPoint.getHeader().getUserId(),
			        		dataPoint.getHeader().getId() );
			        // TODO: for some reason can get the generic on the Body to work on local tests, but not after streaming through kafka, maybe some jackson version issue
			        //		the error: java.util.LinkedHashMap cannot be cast to org.openmhealth.schema.domain.omh.BloodGlucose
			        // For now, this works - turn Body into string, then turn that string into the correct class, seems like a hack that using generics should avoid
			        String rawBody = objectMapper.writeValueAsString(dataPoint.getBody());
			        OmhDao.insertBatch( con, dataPoint, objectMapper, rawBody );
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
	
}
		
