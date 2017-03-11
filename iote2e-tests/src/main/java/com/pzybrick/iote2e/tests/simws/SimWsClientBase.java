package com.pzybrick.iote2e.tests.simws;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.ignite.IgniteGridConnection;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;
import com.pzybrick.iote2e.ws.security.LoginVo;
import com.pzybrick.iote2e.ws.socket.ClientSocketHandler;

public class SimWsClientBase {
	private static final Logger logger = LogManager.getLogger(SimWsClientBase.class);
	protected ClientSocketHandler clientSocketHandler;
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	protected static MasterConfig masterConfig;
	protected ThreadSparkRun threadSparkRun;
	protected Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	protected LoginVo loginVo;
	protected String url;
	protected PollIote2eResultsThread pollIote2eResultsThread;
	
	
	public SimWsClientBase() throws Exception {
		if( SimWsClientBase.masterConfig == null ) 
			SimWsClientBase.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), 
					System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
	}


	public void before( ) {
		try {
			// if Spark not running standalone then start
			if( masterConfig.getSparkMaster().startsWith("local")) {
		    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
		    	threadSparkRun = new ThreadSparkRun( masterConfig, iote2eRequestSparkConsumer);
		    	threadSparkRun.start();
		    	long expiredAt = System.currentTimeMillis() + (10*1000);
		    	while( expiredAt > System.currentTimeMillis() ) {
		    		if( threadSparkRun.isStarted() ) break;
		    		try {
		    			Thread.sleep(250);
		    		} catch( Exception e ) {}
		    	}
		    	if( !threadSparkRun.isStarted() ) throw new Exception("Timeout waiting for Spark to start");
			}

			clientSocketHandler = new ClientSocketHandler()
					.setLoginVo(loginVo).setUrl(url)
					.setQueueIote2eRequests(queueIote2eRequests)
					.setQueueIote2eResults(queueIote2eResults)
					.setPollIote2eResultsThread(pollIote2eResultsThread);
			clientSocketHandler.connect();			

		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void after() throws Exception {
		clientSocketHandler.shutdown();
		pollIote2eResultsThread.shutdown();
		pollIote2eResultsThread.join(5000);
		threadSparkRun.shutdown();
	}
	
}
