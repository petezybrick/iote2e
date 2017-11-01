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
package com.pzybrick.iote2e.stream.validic;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;
import com.pzybrick.iote2e.stream.persist.ValidicDao;


/**
 * The Class ValidicRouterHandlerSparkBatchImpl.
 */
public class ValidicRouterHandlerSparkBatchImpl implements ValidicRouterHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ValidicRouterHandlerSparkBatchImpl.class);
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/** The object mapper. */
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;


	/**
	 * Instantiates a new validic router handler spark batch impl.
	 *
	 * @throws Exception the exception
	 */
	public ValidicRouterHandlerSparkBatchImpl( ) throws Exception {
        this.objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ValidicBody.class, new ValidicBodyDeserializer(objectMapper));
		this.objectMapper.registerModule(module);
        this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.validic.ValidicRouterHandler#init(com.pzybrick.iote2e.common.config.MasterConfig)
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
	 * @see com.pzybrick.iote2e.stream.validic.ValidicRouterHandler#processRequests(java.util.List)
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
		ValidicMessage validicMessage = null;
		try {
			for( ByteBuffer byteBuffer : byteBuffers) {
				// Decompress the JSON string
				String rawJson = new String( CompressionUtils.decompress(byteBuffer.array()) );
				// JSON into ValidicMessage
				try {
			        validicMessage = objectMapper.readValue(rawJson, ValidicMessage.class);
			        logger.debug( "ValidicMessage Header: {}, Number of Bodies: {}, userId={}, uuid={}", validicMessage.getHeader().toString(),
			        		validicMessage.getBodies().size() );
			        ValidicDao.insertBatch( con, validicMessage );
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
					logger.debug("Skipping duplicate row, Header: {}", validicMessage.getHeader().toString() );
				else {
					if( validicMessage != null ) 
						logger.error("Error on insert for Header: {}", validicMessage.getHeader().toString() );
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
		
