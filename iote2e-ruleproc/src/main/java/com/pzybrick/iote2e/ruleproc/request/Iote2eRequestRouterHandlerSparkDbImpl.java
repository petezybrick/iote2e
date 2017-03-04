package com.pzybrick.iote2e.ruleproc.request;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestRouterHandlerSparkDbImpl implements Iote2eRequestRouterHandler {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestRouterHandlerSparkDbImpl.class);


	public Iote2eRequestRouterHandlerSparkDbImpl( ) throws Exception {

	}
	
	
	public void init(MasterConfig masterConfig) throws Exception {
		try {
			logger.info("+++ TODO +++ read database config info from cassandra");
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public void processRequests( List<Iote2eRequest> iote2eRequests ) throws Exception {
		try {
			for( Iote2eRequest iote2eRequest : iote2eRequests ) {
				if (iote2eRequest != null) {
					logger.info("+++ TODO +++ Insert into database, table={}", iote2eRequest.getSourceType().toString() );
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

}
