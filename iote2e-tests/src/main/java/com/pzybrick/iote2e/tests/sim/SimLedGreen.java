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
 * The Class SimLedGreen.
 */
public class SimLedGreen extends SimBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SimLedGreen.class);
	
	/** The Constant LEDGREEN_PUT_FREQ_MS. */
	private static final long LEDGREEN_PUT_FREQ_MS = 3000;
	
	/** The poll result. */
	private ThreadPollResult pollResult;
	
	/** The led green state. */
	private String ledGreenState = "0";

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			SimLedGreen simLedGreen = new SimLedGreen();
			simLedGreen.process();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Instantiates a new sim led green.
	 *
	 * @throws Exception the exception
	 */
	public SimLedGreen() throws Exception {
		super();
	}
	

	/**
	 * Process.
	 */
	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimLedGreenShutdownHook());
			before();
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testLedGreenFilterKey, null);
			pollResult = new ThreadPollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig,
					TestCommonHandler.testLedGreenFilterKey, queueIote2eResults, pollResult);

			while( true ) {
				logger.info( "ledGreenState: {}", getLedGreenState());
				kafkaSend( TestCommonHandler.testLedLoginName, TestCommonHandler.testLedSourceName, 
						TestCommonHandler.testLedSourceType, TestCommonHandler.testLedSensorNameGreen,
						String.valueOf( getLedGreenState() ));
				try {
					Thread.sleep(LEDGREEN_PUT_FREQ_MS);
				} catch( InterruptedException e) {}	
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
						if( "off".equals(actuatorValue)) setLedGreenState("1");
						else if( "green".equals(actuatorValue)) setLedGreenState("0");
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
	 * The Class SimLedGreenShutdownHook.
	 */
	private class SimLedGreenShutdownHook extends Thread {
		
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

	/**
	 * Gets the led green state.
	 *
	 * @return the led green state
	 */
	public String getLedGreenState() {
		synchronized(ledGreenState ) {
			return ledGreenState;
		}
	}

	/**
	 * Sets the led green state.
	 *
	 * @param ledGreenState the led green state
	 * @return the sim led green
	 */
	public SimLedGreen setLedGreenState(String ledGreenState) {
		synchronized(ledGreenState ) {
			this.ledGreenState = ledGreenState;
			return this;
		}
	}

}
