package com.pzybrick.iote2e.ruleproc.router;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestRouterHandler;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


public class RouterIote2eRequestImpl implements Router {
	private static final long serialVersionUID = -4169393694317086396L;
	private static final Logger logger = LogManager.getLogger(RouterIote2eRequestImpl.class);
    public static final int DEFAULT_BATCH_SIZE = 1000;
    private int batchSize;
    private List<Iote2eRequest> iote2eRequests;
    private static Iote2eRequestRouterHandler iote2eRequestRouterHandler;


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
    

    @Override
    public void add(Iote2eRequest iote2eRequest) throws Exception {
    	iote2eRequests.add( iote2eRequest );
    }
    

    public void flush() throws Exception {
    	if( iote2eRequests.size() > 0 ) {
	        logger.info("Flush Start iote2eRequests.size()={}", iote2eRequests.size() );
	        // evaluate each rule and if it hits, then push the Iote2eResult back out to the originator via Ignite
	        iote2eRequestRouterHandler.processRequests(iote2eRequests);
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
