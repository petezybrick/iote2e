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
package com.pzybrick.iote2e.tests.sim;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class SimTempToFan.
 */
public class SimTempToFan extends SimBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SimTempToFan.class);
	
	/** The Constant TEMP_MIN. */
	private static final double TEMP_MIN = 74.0;
	
	/** The Constant TEMP_MAX. */
	private static final double TEMP_MAX = 83.0;
	
	/** The Constant TEMP_START. */
	private static final double TEMP_START = 79.0;
	
	/** The Constant TEMP_INCR. */
	private static final double TEMP_INCR = 1.0;
	
	/** The Constant TEMP_PUT_FREQ_MS. */
	private static final long TEMP_PUT_FREQ_MS = 5000;
	
	/** The temp direction increase. */
	private boolean tempDirectionIncrease = true;
	
	/** The poll result. */
	private ThreadPollResult pollResult;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			SimTempToFan simTempToFan = new SimTempToFan();
			simTempToFan.process();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Instantiates a new sim temp to fan.
	 *
	 * @throws Exception the exception
	 */
	public SimTempToFan() throws Exception {
		super();
	}
	
	
	/**
	 * Process.
	 */
	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimTempToFanShutdownHook());
			before();
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
			pollResult = new ThreadPollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig,
					TestCommonHandler.testTempToFanFilterKey, queueIote2eResults, pollResult);
			double tempNow = TEMP_MIN;
			tempDirectionIncrease = true;
			while( true ) {
				if( tempDirectionIncrease && tempNow < TEMP_MAX ) {
					tempNow += TEMP_INCR;
				} else if( !tempDirectionIncrease && tempNow > TEMP_MIN) {
					tempNow -= TEMP_INCR;
				}
				logger.info( "tempNow: {}",tempNow);
				kafkaSend( TestCommonHandler.testTempToFanLoginName, TestCommonHandler.testTempToFanSourceName, 
						TestCommonHandler.testTempToFanSourceType, TestCommonHandler.testTempToFanSensorName,
						String.valueOf(tempNow));
				try {
					Thread.sleep(TEMP_PUT_FREQ_MS);
				} catch( InterruptedException e) {}	
				if( tempNow >= 83.0 || tempNow <= 74.0) {
					logger.error("Temperature Exceeded");
					after();
					break;
				}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	/**
	 * The Class ThreadPollResult.
	 */
	private class ThreadPollResult extends Thread {
		
		/** The shutdown. */
		private boolean shutdown;

		/**
		 * Instantiates a new thread poll result.
		 */
		public ThreadPollResult( ) {
			super();
		}
		
		/**
		 * Shutdown.
		 */
		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while( true ) {
				Iote2eResult iote2eResult = queueIote2eResults.poll();
				if( iote2eResult != null ) {
					try {
						String actuatorValue = iote2eResult.getPairs().get( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE)).toString();
						logger.info("actuatorValue {}", actuatorValue);
						if( "off".equals(actuatorValue)) tempDirectionIncrease = true;
						else if( "on".equals(actuatorValue)) tempDirectionIncrease = false;
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
				try {
					sleep(5000);
				} catch( InterruptedException e ) {}
				if( this.shutdown ) break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.tests.sim.SimBase#after()
	 */
	public void after() throws Exception {
		pollResult.shutdown();
		pollResult.join(5000);
		super.after();
	}
	
	

	/**
	 * The Class SimTempToFanShutdownHook.
	 */
	private class SimTempToFanShutdownHook extends Thread {
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				logger.info("Shutdownhook - Start");
				after();
				logger.info("Shutdownhook - Complete");
			} catch( Exception e ) {
				logger.error(e.getMessage(), e );
			}
		}
	}

}
