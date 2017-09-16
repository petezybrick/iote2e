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
package com.pzybrick.iote2e.stream.router;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.stream.request.Iote2eRequestRouterHandler;



/**
 * The Class RouterIote2eRequestImpl.
 */
public class RouterIote2eRequestImpl implements Router {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4169393694317086396L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RouterIote2eRequestImpl.class);
    
    /** The Constant DEFAULT_BATCH_SIZE. */
    public static final int DEFAULT_BATCH_SIZE = 1000;
    
    /** The batch size. */
    private int batchSize;
    
    /** The iote 2 e requests. */
    private List<Iote2eRequest> iote2eRequests;
    
    /** The iote 2 e request router handler. */
    private static Iote2eRequestRouterHandler iote2eRequestRouterHandler;


    /**
     * Instantiates a new router iote 2 e request impl.
     *
     * @param masterConfig the master config
     * @throws Exception the exception
     */
    public RouterIote2eRequestImpl( MasterConfig masterConfig ) throws Exception {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.iote2eRequests = new ArrayList<Iote2eRequest>();
        if( iote2eRequestRouterHandler == null ) {
			Class cls = Class.forName(masterConfig.getRouterIote2eRequestClassName());
			Iote2eRequestRouterHandler handler = (Iote2eRequestRouterHandler)cls.newInstance();
			handler.init(masterConfig);
			RouterIote2eRequestImpl.iote2eRequestRouterHandler = handler;
        }
    }
    

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.Router#add(com.pzybrick.iote2e.schema.avro.Iote2eRequest)
     */
    @Override
    public void add(Iote2eRequest iote2eRequest) throws Exception {
    	iote2eRequests.add( iote2eRequest );
    }
    

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.Router#flush()
     */
    public void flush() throws Exception {
    	if( iote2eRequests.size() > 0 ) {
	        logger.info("Flush Start iote2eRequests.size()={}", iote2eRequests.size() );
	        // evaluate each rule and if it hits, then push the Iote2eResult back out to the originator via Ignite
	        iote2eRequestRouterHandler.processRequests(iote2eRequests);
	        logger.info("Flush End");
    	}
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        logger.debug("Closing");
        flush();
    }

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.Router#getBatchSize()
     */
    @Override
    public int getBatchSize() {
        return batchSize;
    }

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.Router#setBatchSize(int)
     */
    @Override
   public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
