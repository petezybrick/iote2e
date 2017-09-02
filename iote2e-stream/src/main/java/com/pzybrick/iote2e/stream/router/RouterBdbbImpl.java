package com.pzybrick.iote2e.stream.router;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.bdbb.BdbbRouterHandler;
import com.pzybrick.iote2e.stream.omh.OmhRouterHandler;


public class RouterBdbbImpl implements RouterBdbb {
	private static final long serialVersionUID = -1713587615093625333L;
	private static final Logger logger = LogManager.getLogger(RouterBdbbImpl.class);
    public static final int DEFAULT_BATCH_SIZE = 1000;
    private int batchSize;
    private List<ByteBuffer> byteBuffers;
    private static BdbbRouterHandler bdbbRouterHandler;


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
    

    @Override
    public void add(ByteBuffer byteBuffer) throws Exception {
    	byteBuffers.add( byteBuffer );
    }
    

    public void flush() throws Exception {
    	if( byteBuffers.size() > 0 ) {
	        logger.info("Flush Start iote2eRequests.size()={}", byteBuffers.size() );
	        // evaluate each rule and if it hits, then push the Iote2eResult back out to the originator via Ignite
	        bdbbRouterHandler.processRequests(byteBuffers);
	        logger.info("Flush End");
    	}
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
