package com.pzybrick.iote2e.ws.launch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ws.nrt.ThreadEntryPointNearRealTime;
import com.pzybrick.iote2e.ws.omh.ThreadEntryPointOmh;
import com.pzybrick.iote2e.ws.socket.ThreadEntryPointIote2eRequest;

public class ServerLauncher {
	private static final Logger logger = LogManager.getLogger(ThreadEntryPointIote2eRequest.class);

	
	public static void main(String[] args) {
		logger.info("Starting");
		try {
			MasterConfig masterConfig = MasterConfig.getInstance( args[0], args[1], args[2] );
			ThreadEntryPointIote2eRequest threadEntryPointIote2eRequest = new ThreadEntryPointIote2eRequest(masterConfig);
			ThreadEntryPointNearRealTime threadEntryPointNearRealTime = new ThreadEntryPointNearRealTime(masterConfig);
			ThreadEntryPointOmh threadEntryPointOmh = new ThreadEntryPointOmh(masterConfig);

			threadEntryPointIote2eRequest.start();
			threadEntryPointNearRealTime.start();
			threadEntryPointOmh.start();
			
			threadEntryPointIote2eRequest.join();
			threadEntryPointNearRealTime.join();
			threadEntryPointOmh.join();
			
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
		}
	}

}
