package com.pzybrick.iote2e.tests.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.spark.Iote2eRequestSparkConsumer;

public class ThreadSparkRun extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadSparkRun.class);
	private Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	private MasterConfig masterConfig;
	
	public ThreadSparkRun( MasterConfig masterConfig, Iote2eRequestSparkConsumer iote2eRequestSparkConsumer ) {
		this.masterConfig = masterConfig;
		this.iote2eRequestSparkConsumer = iote2eRequestSparkConsumer;
	}
	
	public void shutdown() {
		try {
		iote2eRequestSparkConsumer.stop();
		} catch( Exception e ) {
			logger.warn(e.getMessage());
		}
	}
	
	public boolean isStarted() {
		return iote2eRequestSparkConsumer.isStarted();
	}
	@Override
	public void run() {
		try {
    		iote2eRequestSparkConsumer.process(masterConfig);
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
}

