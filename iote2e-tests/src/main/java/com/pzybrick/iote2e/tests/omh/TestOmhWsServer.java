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
package com.pzybrick.iote2e.tests.omh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ws.omh.ThreadEntryPointOmh;


/**
 * The Class TestOmhWsServer.
 */
public class TestOmhWsServer {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestOmhWsServer.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
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
	
	/**
	 * The Class ThreadInjectTestData.
	 */
	private class ThreadInjectTestData extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
		}
		
	}
}
