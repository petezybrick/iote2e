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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class ResetPyClientActuatorState.
 */
/*
 * Python code is running outside the docker network, so can't reset from the python script
 * Must reset the appropriate actuator before running pyclient ProcessSim...
 */
public class ResetPyClientActuatorState {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(ResetPyClientActuatorState.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ResetPyClientActuatorState resetPyClientActuatorState = new ResetPyClientActuatorState();
		resetPyClientActuatorState.process(args);
	}

	/**
	 * Process.
	 *
	 * @param args the args
	 */
	public void process(String[] args) {
		try {
			logger.info("Resetting Actuator table for: {}",args[2]);
			ActuatorStateDao.connect(args[0], args[1]);
			if("temp".equalsIgnoreCase(args[2]) ) {
				resetTemp();
			} else if("humidity".equalsIgnoreCase(args[2]) ) {
				resetHumidity();
			} else if("ledgreen".equalsIgnoreCase(args[2]) ) {
				resetLedGreen();
			} else if( "all".equalsIgnoreCase(args[2]) ) {
				resetTemp();
				resetHumidity();
				resetLedGreen();
			} else throw new Exception("Invalid request, must be temp|humidity|ledgreen, was: " + args[2]);
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		} finally {
			ActuatorStateDao.disconnect();
		}
		
	}
	
	/**
	 * Reset humidity.
	 *
	 * @throws Exception the exception
	 */
	// Hacks
	private void resetHumidity() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testHumidityLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testHumiditySensorName + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}
	
	/**
	 * Reset temp.
	 *
	 * @throws Exception the exception
	 */
	// Hacks
	private void resetTemp() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testTempToFanLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testTempToFanSensorName + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}
	
	/**
	 * Reset led green.
	 *
	 * @throws Exception the exception
	 */
	// Hacks
	private void resetLedGreen() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testLedGreenFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testLedLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testLedSensorNameGreen + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}

}
