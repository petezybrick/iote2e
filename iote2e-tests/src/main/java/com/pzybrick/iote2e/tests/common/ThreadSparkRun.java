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
package com.pzybrick.iote2e.tests.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer;


/**
 * The Class ThreadSparkRun.
 */
public class ThreadSparkRun extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ThreadSparkRun.class);
	
	/** The iote 2 e request spark consumer. */
	private Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	
	/** The master config. */
	private MasterConfig masterConfig;
	
	/**
	 * Instantiates a new thread spark run.
	 *
	 * @param masterConfig the master config
	 * @param iote2eRequestSparkConsumer the iote 2 e request spark consumer
	 */
	public ThreadSparkRun( MasterConfig masterConfig, Iote2eRequestSparkConsumer iote2eRequestSparkConsumer ) {
		this.masterConfig = masterConfig;
		this.iote2eRequestSparkConsumer = iote2eRequestSparkConsumer;
	}
	
	/**
	 * Shutdown.
	 */
	public void shutdown() {
		try {
		iote2eRequestSparkConsumer.stop();
		} catch( Exception e ) {
			logger.warn(e.getMessage());
		}
	}
	
	/**
	 * Checks if is started.
	 *
	 * @return true, if is started
	 */
	public boolean isStarted() {
		return iote2eRequestSparkConsumer.isStarted();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
    		iote2eRequestSparkConsumer.process(masterConfig);
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
}

