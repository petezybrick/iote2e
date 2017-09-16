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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.bdbb.BdbbRouterHandler;
import com.pzybrick.iote2e.stream.omh.OmhRouterHandler;



/**
 * The Class RouterBdbbImpl.
 */
public class RouterBdbbImpl implements RouterBdbb {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1713587615093625333L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RouterBdbbImpl.class);
    
    /** The Constant DEFAULT_BATCH_SIZE. */
    public static final int DEFAULT_BATCH_SIZE = 1000;
    
    /** The batch size. */
    private int batchSize;
    
    /** The byte buffers. */
    private List<ByteBuffer> byteBuffers;
    
    /** The bdbb router handler. */
    private static BdbbRouterHandler bdbbRouterHandler;


    /**
     * Instantiates a new router bdbb impl.
     *
     * @param masterConfig the master config
     * @throws Exception the exception
     */
    public RouterBdbbImpl( MasterConfig masterConfig ) throws Exception {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.byteBuffers = new ArrayList<ByteBuffer>();
        if( bdbbRouterHandler == null ) {
			Class cls = Class.forName(masterConfig.getRouterBdbbClassName());
			BdbbRouterHandler handler = (BdbbRouterHandler)cls.newInstance();
			handler.init(masterConfig);
			RouterBdbbImpl.bdbbRouterHandler = handler;
        }
    }
    

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.RouterBdbb#add(java.nio.ByteBuffer)
     */
    @Override
    public void add(ByteBuffer byteBuffer) throws Exception {
    	byteBuffers.add( byteBuffer );
    }
    

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.RouterBdbb#flush()
     */
    public void flush() throws Exception {
    	if( byteBuffers.size() > 0 ) {
	        logger.info("Flush Start iote2eRequests.size()={}", byteBuffers.size() );
	        // evaluate each rule and if it hits, then push the Iote2eResult back out to the originator via Ignite
	        bdbbRouterHandler.processRequests(byteBuffers);
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
     * @see com.pzybrick.iote2e.stream.router.RouterBdbb#getBatchSize()
     */
    @Override
    public int getBatchSize() {
        return batchSize;
    }

    /* (non-Javadoc)
     * @see com.pzybrick.iote2e.stream.router.RouterBdbb#setBatchSize(int)
     */
    @Override
   public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
