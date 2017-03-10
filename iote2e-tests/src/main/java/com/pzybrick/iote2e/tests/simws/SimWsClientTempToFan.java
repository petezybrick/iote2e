package com.pzybrick.iote2e.tests.simws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.ws.security.LoginVo;

public class SimWsClientTempToFan extends SimWsClientBase {
	private static final Logger logger = LogManager.getLogger(SimWsClientTempToFan.class);
	private static final double TEMP_MIN = 74.0;
	private static final double TEMP_MAX = 83.0;
	private static final double TEMP_START = 77.0;
	private static final double TEMP_INCR = 1.0;
	private static final long TEMP_PUT_FREQ_MS = 4000;
	private boolean tempDirectionIncrease = true;

	
	public static void main(String[] args) {
		try {
			SimWsClientTempToFan simWsClientTempToFan = new SimWsClientTempToFan();
			simWsClientTempToFan.process(args);
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}

	
	public SimWsClientTempToFan() throws Exception {
		super();
	}


	public void process(String[] args) {
		try {
			url = args[0];
			loginVo = new LoginVo()
					.setLoginName(TestCommonHandler.testTempToFanLoginName)
					.setSourceName(TestCommonHandler.testTempToFanSourceName)
					.setOptionalFilterSensorName(TestCommonHandler.testTempToFanSensorName);
			Runtime.getRuntime().addShutdownHook(new SimWsTempToFanShutdownHook());
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
			pollIote2eResultsThread = new PollIote2eResultsThread(queueIote2eResults) {
				@Override
				public void processIote2eResult( Iote2eResult iote2eResult ) {
					try {
						String actuatorValue = iote2eResult.getPairs().get( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE)).toString();
						logger.info("actuatorValue {}", actuatorValue);
						if( "off".equals(actuatorValue)) tempDirectionIncrease = true;
						else if( "on".equals(actuatorValue)) tempDirectionIncrease = false;
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			pollIote2eResultsThread.start();			
			before();
			// give spark and ignite a few seconds to start
			try { Thread.sleep(2500);} catch(InterruptedException e) {}
			double tempNow = TEMP_START;
			tempDirectionIncrease = true;
			while( true ) {
				if( tempDirectionIncrease && tempNow < TEMP_MAX ) {
					tempNow += TEMP_INCR;
				} else if( !tempDirectionIncrease && tempNow > TEMP_MIN) {
					tempNow -= TEMP_INCR;
				}
				logger.info( "tempNow: {}",tempNow);
				
				Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
				pairs.put(TestCommonHandler.testTempToFanSensorName, new Utf8(String.valueOf(tempNow)));
				
				Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
						.setLoginName(TestCommonHandler.testTempToFanLoginName)
						.setSourceName(TestCommonHandler.testTempToFanSourceName)
						.setSourceType(TestCommonHandler.testTempToFanSourceType)
						.setRequestUuid(UUID.randomUUID().toString())
						.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
						.setOperation(OPERATION.SENSORS_VALUES)
						.setPairs(pairs).build();
				clientSocketHandler.sendIote2eRequest(iote2eRequest);
				try {
					Thread.sleep(TEMP_PUT_FREQ_MS);
				} catch( InterruptedException e) {}	
				if( tempNow >= TEMP_MAX || tempNow <= TEMP_MIN) {
					logger.error("Temperature Exceeded");
					after();
					break;
				}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		
	}
	

	
	public void after() throws Exception {
		super.after();
	}
	
	

	private class SimWsTempToFanShutdownHook extends Thread {
		
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
