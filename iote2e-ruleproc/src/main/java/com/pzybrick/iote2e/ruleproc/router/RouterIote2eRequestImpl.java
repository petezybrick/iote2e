/*******************************************************************************
 * Copyright 2016, Medidata Solutions, Inc., All Rights Reserved. This is
 * PROPRIETARY SOURCE CODE of Medidata Solutions Inc. The contents of this file
 * may not be disclosed to third parties, copied or duplicated in any form, in
 * whole or in part, without the prior written permission of Medidata Solutions,
 * Inc. Author: Bikram Kashyap, bkashyap@mdsol.com
 ******************************************************************************/

package com.pzybrick.iote2e.ruleproc.router;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestSparkHandler;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


public class RouterIote2eRequestImpl implements Router {
	private static final long serialVersionUID = -4169393694317086396L;
	private static final Logger logger = LogManager.getLogger(RouterIote2eRequestImpl.class);
    public static final int DEFAULT_BATCH_SIZE = 1000;
    private int batchSize;
    private List<Iote2eRequest> iote2eRequests;
    private static Iote2eRequestSparkHandler iote2eRequestSparkHandler;
    
    static {
    	try {
    		iote2eRequestSparkHandler = new Iote2eRequestSparkHandler();
    	} catch( Exception e ) {
    		logger.error(e.getMessage(), e);
    	}
    }

    public RouterIote2eRequestImpl( ) {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.iote2eRequests = new ArrayList<Iote2eRequest>();
    }

    @Override
    public void add(Iote2eRequest iote2eRequest) throws Exception {
    	iote2eRequests.add( iote2eRequest );
    }

    public void flush() throws Exception {
    	// CRITICAL: need to get key/value for LoginSensorActuator - can i do this in Ignite, or need to install something like Mongo or Cassandra
        logger.info("------------------- Flush Start");
        // evaluate each rule and if it hits, then push the Iote2eResult back out to the originator via Ignite
    	iote2eRequestSparkHandler.processRequests(iote2eRequests);
        logger.info("------------------- Flush End");
    }

    @Override
    public void close() throws Exception {
        logger.debug("Closing");
        flush();
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
   public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}