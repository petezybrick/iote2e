package com.pzybrick.iote2e.tests.bdbb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ws.bdbb.ThreadEntryPointBdbb;

public class TestBdbbWsServer {
	private static final Logger logger = LogManager.getLogger(TestBdbbWsServer.class);

	public static void main(String[] args) {
		logger.info("Starting");
		try {
			MasterConfig masterConfig = MasterConfig.getInstance( args[0], args[1], args[2] );
			ThreadEntryPointBdbb threadEntryPointBdbb = new ThreadEntryPointBdbb(masterConfig);
			threadEntryPointBdbb.start();			
			threadEntryPointBdbb.join();
			
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
