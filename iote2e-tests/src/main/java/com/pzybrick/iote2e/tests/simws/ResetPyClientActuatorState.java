package com.pzybrick.iote2e.tests.simws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;

/*
 * Python code is running outside the docker network, so can't reset from the python script
 * Must reset the appropriate actuator before running pyclient ProcessSim...
 */
public class ResetPyClientActuatorState {
	private static final Logger logger = LogManager.getLogger(ResetPyClientActuatorState.class);

	public static void main(String[] args) {
		ResetPyClientActuatorState resetPyClientActuatorState = new ResetPyClientActuatorState();
		resetPyClientActuatorState.process(args);
	}

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
	
	// Hacks
	private void resetHumidity() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testHumidityLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testHumiditySensorName + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}
	
	// Hacks
	private void resetTemp() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testTempToFanLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testTempToFanSensorName + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}
	
	// Hacks
	private void resetLedGreen() throws Exception {
		ActuatorStateDao.updateActuatorValue(TestCommonHandler.testLedGreenFilterKey, null);
		for( int i=1 ; i<4 ; i++ ) {
			String key = TestCommonHandler.testLedLoginName + "|rpi-00" + i + "|" + TestCommonHandler.testLedSensorNameGreen + "|";
			ActuatorStateDao.updateActuatorValue(key, null);
		}
	}

}
