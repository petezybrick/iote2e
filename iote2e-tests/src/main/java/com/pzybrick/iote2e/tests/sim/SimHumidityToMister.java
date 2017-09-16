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
 * The Class SimHumidityToMister.
 */
public class SimHumidityToMister extends SimBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SimHumidityToMister.class);
	
	/** The Constant HUMIDITY_MIN. */
	private static final double HUMIDITY_MIN = 82.0;
	
	/** The Constant HUMIDITY_MAX. */
	private static final double HUMIDITY_MAX = 93.0;
	
	/** The Constant HUMIDITY_START. */
	private static final double HUMIDITY_START = 92.0;
	
	/** The Constant HUMIDITY_INCR. */
	private static final double HUMIDITY_INCR = .5;
	
	/** The Constant HUMIDITY_PUT_FREQ_MS. */
	private static final long HUMIDITY_PUT_FREQ_MS = 3000;
	
	/** The humidity direction increase. */
	private boolean humidityDirectionIncrease = true;
	
	/** The poll result. */
	private ThreadPollResult pollResult;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			SimHumidityToMister simHumidityToMister = new SimHumidityToMister();
			simHumidityToMister.process();
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Instantiates a new sim humidity to mister.
	 *
	 * @throws Exception the exception
	 */
	public SimHumidityToMister() throws Exception {
		super();
	}

	/**
	 * Process.
	 */
	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimHumidityToMisterShutdownHook());
			before();
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
			pollResult = new ThreadPollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig,
					TestCommonHandler.testHumidityFilterKey, queueIote2eResults, pollResult);
			double humidityNow = HUMIDITY_MAX;
			humidityDirectionIncrease = false;
			while( true ) {
				if( humidityDirectionIncrease && humidityNow < HUMIDITY_MAX ) {
					humidityNow += HUMIDITY_INCR;
				} else if( !humidityDirectionIncrease && humidityNow > HUMIDITY_MIN) {
					humidityNow -= HUMIDITY_INCR;
				}
				logger.info( "humidityNow: {}",humidityNow);
				kafkaSend( TestCommonHandler.testHumidityLoginName, TestCommonHandler.testHumiditySourceName, 
						TestCommonHandler.testHumiditySourceType, TestCommonHandler.testHumiditySensorName,
						String.valueOf(humidityNow));
				try {
					Thread.sleep(HUMIDITY_PUT_FREQ_MS);
				} catch( InterruptedException e) {}	
				// TEST TEST TEST
				//if( tempNow == HUMIDITY_MAX ) tempDirectionIncrease = false;
				//else if( tempNow == HUMIDITY_MIN ) tempDirectionIncrease = true;
				if( humidityNow >= HUMIDITY_MAX || humidityNow <= HUMIDITY_MIN) {
					logger.error("Humidity Exceeded");
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
						if( "off".equals(actuatorValue)) humidityDirectionIncrease = false;
						else if( "on".equals(actuatorValue)) humidityDirectionIncrease = true;
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
	 * The Class SimHumidityToMisterShutdownHook.
	 */
	private class SimHumidityToMisterShutdownHook extends Thread {
		
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
