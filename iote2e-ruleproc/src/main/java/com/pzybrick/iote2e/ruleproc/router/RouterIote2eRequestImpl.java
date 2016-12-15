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

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


public class RouterIote2eRequestImpl implements Router {
	private static final Logger logger = LogManager.getLogger(RouterIote2eRequestImpl.class);
    public static final int DEFAULT_BATCH_SIZE = 1000;
    private int batchSize;
    private List<Iote2eRequest> iote2eRequests;


    public RouterIote2eRequestImpl() {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.iote2eRequests = new ArrayList<Iote2eRequest>();
    }

    @Override
    public void add(Iote2eRequest iote2eRequest) throws Exception {
    	iote2eRequests.add( iote2eRequest );
    }

    public void flush() throws Exception {
        // TODO: Rule processor here, then push to Ignite
    	// CRITICAL: need to get key/value for LoginSensorActuator - can i do this in Ignite, or need to install something like Mongo or Cassandra
        logger.info("------------------- Flush Start");
        for( Iote2eRequest iote2eRequest : iote2eRequests ) {
            logger.info("------------------- iote2eRequest: " + iote2eRequest.toString());        	
        }
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
