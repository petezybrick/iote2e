package com.pzybrick.iote2e.stream.router;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.omh.OmhRouterHandler;
import com.pzybrick.iote2e.stream.request.Iote2eRequestRouterHandler;


public class RouterOmhImpl implements RouterOmh {
	private static final long serialVersionUID = -4169393694317086396L;
	private static final Logger logger = LogManager.getLogger(RouterOmhImpl.class);
    public static final int DEFAULT_BATCH_SIZE = 1000;
    private int batchSize;
    private List<ByteBuffer> byteBuffers;
    private static OmhRouterHandler omhRouterHandler;


    public RouterOmhImpl( MasterConfig masterConfig ) throws Exception {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.byteBuffers = new ArrayList<ByteBuffer>();
        if( omhRouterHandler == null ) {
			Class cls = Class.forName(masterConfig.getRouterOmhClassName());
			OmhRouterHandler handler = (OmhRouterHandler)cls.newInstance();
			handler.init(masterConfig);
			RouterOmhImpl.omhRouterHandler = handler;
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
	        omhRouterHandler.processRequests(byteBuffers);
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
