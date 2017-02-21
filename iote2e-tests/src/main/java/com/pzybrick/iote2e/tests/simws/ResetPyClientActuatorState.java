package com.pzybrick.iote2e.tests.simws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
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
			logger.info("Resetting Actuator table for: {}",args[0]);
			if("temp".equalsIgnoreCase(args[0]))
				ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
			else if("humidity".equalsIgnoreCase(args[0]))
				ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
			else if("ledgreen".equalsIgnoreCase(args[0]))
				ActuatorStateDao.updateActuatorValue(TestCommonHandler.testLedGreenFilterKey, null);
			else throw new Exception("Invalid request, must be temp|humidity|ledgreen, was: " + args[0]);
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		} finally {
			ActuatorStateDao.disconnect();
		}
		
	}

}
