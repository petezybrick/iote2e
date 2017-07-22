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

public class OmhRouterHandlerSparkSpeedImpl implements OmhRouterHandler {
	private static final Logger logger = LogManager.getLogger(OmhRouterHandlerSparkSpeedImpl.class);
	private MasterConfig masterConfig;
	// TODO have a cached pool of objectMapper's
	private ObjectMapper objectMapper;


	public OmhRouterHandlerSparkSpeedImpl( ) throws Exception {
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
		try {
			if( byteBuffers != null && byteBuffers.size() > 0 ) {
				DataPoint dataPoint = null;
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
//				        String rawBody = objectMapper.writeValueAsString(dataPoint.getBody());
//				        OmhDao.insertBatch( con, dataPoint, objectMapper, rawBody );
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
						throw e;
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
		}
	}
	
}
		
