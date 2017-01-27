package com.pzybrick.iote2e.tests.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.spark.Iote2eRequestSparkConsumer;

public class ThreadSparkRun extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadSparkRun.class);
	private Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;
	
	public ThreadSparkRun( Iote2eRequestSparkConsumer iote2eRequestSparkConsumer ) {
		this.iote2eRequestSparkConsumer = iote2eRequestSparkConsumer;
	}
	
	public boolean isStarted() {
		return iote2eRequestSparkConsumer.isStarted();
	}
	@Override
	public void run() {
		try {
    		iote2eRequestSparkConsumer.process();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
}

