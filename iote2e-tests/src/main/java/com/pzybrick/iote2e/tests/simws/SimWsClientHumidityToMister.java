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
package com.pzybrick.iote2e.tests.simws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.ws.security.LoginVo;


/**
 * The Class SimWsClientHumidityToMister.
 */
public class SimWsClientHumidityToMister extends SimWsClientBase {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(SimWsClientHumidityToMister.class);
	
	/** The Constant HUMIDITY_MIN. */
	private static final double HUMIDITY_MIN = 82.0;
	
	/** The Constant HUMIDITY_MAX. */
	private static final double HUMIDITY_MAX = 93.0;
	
	/** The Constant HUMIDITY_START. */
	private static final double HUMIDITY_START = 90.0;
	
	/** The Constant HUMIDITY_INCR. */
	private static final double HUMIDITY_INCR = 1.0;
	
	/** The Constant HUMIDITY_PUT_FREQ_MS. */
	private static final long HUMIDITY_PUT_FREQ_MS = 4000;
	
	/** The humidity direction increase. */
	private boolean humidityDirectionIncrease = true;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
		SimWsClientHumidityToMister simWsClientHumidityToMister = new SimWsClientHumidityToMister();
		simWsClientHumidityToMister.process(args);
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	
	/**
	 * Instantiates a new sim ws client humidity to mister.
	 *
	 * @throws Exception the exception
	 */
	public SimWsClientHumidityToMister() throws Exception {
		super();
	}

	
	/**
	 * Process.
	 *
	 * @param args the args
	 */
	public void process(String[] args) {
		try {
			url = args[0];
			loginVo = new LoginVo()
					.setLoginName(TestCommonHandler.testHumidityLoginName)
					.setSourceName(TestCommonHandler.testHumiditySourceName)
					.setOptionalFilterSensorName(TestCommonHandler.testHumiditySensorName);
			Runtime.getRuntime().addShutdownHook(new SimWsTempToFanShutdownHook());
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
			pollIote2eResultsThread = new PollIote2eResultsThread(queueIote2eResults) {
				@Override
				public void processIote2eResult( Iote2eResult iote2eResult ) {
					try {
						String actuatorValue = iote2eResult.getPairs().get( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE)).toString();
						logger.info("actuatorValue {}", actuatorValue);
						if( "off".equals(actuatorValue)) humidityDirectionIncrease = false;
						else if( "on".equals(actuatorValue)) humidityDirectionIncrease = true;
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			pollIote2eResultsThread.start();			
			before();
			// give spark and ignite a few seconds to start
			try { Thread.sleep(2500);} catch(InterruptedException e) {}
			double humidityNow = HUMIDITY_START;
			humidityDirectionIncrease = false;
			while( true ) {
				if( humidityDirectionIncrease && humidityNow < HUMIDITY_MAX ) {
					humidityNow += HUMIDITY_INCR;
				} else if( !humidityDirectionIncrease && humidityNow > HUMIDITY_MIN) {
					humidityNow -= HUMIDITY_INCR;
				}
				logger.info( "humidityNow: {}", humidityNow );
				
				Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
				pairs.put(TestCommonHandler.testHumiditySensorName, new Utf8(String.valueOf(humidityNow)));
				
				Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
						.setLoginName(TestCommonHandler.testHumidityLoginName)
						.setSourceName(TestCommonHandler.testHumiditySourceName)
						.setSourceType(TestCommonHandler.testHumiditySourceType)
						.setRequestUuid(UUID.randomUUID().toString())
						.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
						.setOperation(OPERATION.SENSORS_VALUES)
						.setPairs(pairs).build();
				clientSocketHandler.sendIote2eRequest(iote2eRequest);
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
	
	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.tests.simws.SimWsClientBase#after()
	 */
	public void after() throws Exception {
		super.after();
	}

	/**
	 * The Class SimWsTempToFanShutdownHook.
	 */
	private class SimWsTempToFanShutdownHook extends Thread {
		
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
