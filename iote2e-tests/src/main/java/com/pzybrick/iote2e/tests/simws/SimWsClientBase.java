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
package com.pzybrick.iote2e.tests.simws;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;
import com.pzybrick.iote2e.ws.security.LoginVo;


/**
 * The Class SimWsClientBase.
 */
public class SimWsClientBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SimWsClientBase.class);
	
	/** The client socket handler. */
	protected ClientSocketHandler clientSocketHandler;
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	
	/** The master config. */
	protected static MasterConfig masterConfig;
	
	/** The thread spark run. */
	protected ThreadSparkRun threadSparkRun;
	
	/** The iote 2 e request spark consumer. */
	protected Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	
	/** The login vo. */
	protected LoginVo loginVo;
	
	/** The url. */
	protected String url;
	
	/** The poll iote 2 e results thread. */
	protected PollIote2eResultsThread pollIote2eResultsThread;
	
	
	/**
	 * Instantiates a new sim ws client base.
	 *
	 * @throws Exception the exception
	 */
	public SimWsClientBase() throws Exception {
		if( SimWsClientBase.masterConfig == null ) 
			SimWsClientBase.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), 
					System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
	}


	/**
	 * Before.
	 */
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
	
	/**
	 * After.
	 *
	 * @throws Exception the exception
	 */
	public void after() throws Exception {
		clientSocketHandler.shutdown();
		pollIote2eResultsThread.shutdown();
		pollIote2eResultsThread.join(5000);
		threadSparkRun.shutdown();
	}
	
}
