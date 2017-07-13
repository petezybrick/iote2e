package com.pzybrick.iote2e.tests.omh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ws.omh.ThreadEntryPointOmh;

public class TestOmhWsServer {
	private static final Logger logger = LogManager.getLogger(TestOmhWsServer.class);

	public static void main(String[] args) {
		logger.info("Starting");
		try {
			MasterConfig masterConfig = MasterConfig.getInstance( args[0], args[1], args[2] );
			ThreadEntryPointOmh threadEntryPointOmh = new ThreadEntryPointOmh(masterConfig);
			threadEntryPointOmh.start();			
			threadEntryPointOmh.join();
			
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}
	
	private class ThreadInjectTestData extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
		}
		
	}
}
