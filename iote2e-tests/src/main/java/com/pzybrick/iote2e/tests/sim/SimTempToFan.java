package com.pzybrick.iote2e.tests.sim;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadIgniteSubscribe;

public class SimTempToFan extends SimBase {
	private static final Logger logger = LogManager.getLogger(SimTempToFan.class);
	private static final double TEMP_MIN = 74.0;
	private static final double TEMP_MAX = 83.0;
	private static final double TEMP_START = 79.0;
	private static final double TEMP_INCR = .5;
	private static final long TEMP_PUT_FREQ_MS = 2000;
	private boolean tempDirectionIncrease = true;
	private PollResult pollResult;

	public static void main(String[] args) {
		SimTempToFan simTempToFan = new SimTempToFan();
		simTempToFan.process();
	}

	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimTempToFanShutdownHook());
			before();
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
			pollResult = new PollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(iote2eRequestHandler.getMasterConfig(),
					TestCommonHandler.testTempToFanFilterKey, igniteSingleton, iote2eResultsBytes, pollResult);
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
	
	private class PollResult extends Thread {
		private boolean shutdown;

		public PollResult( ) {
			super();
		}
		
		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			while( true ) {
				byte[] iote2eResultsByte = iote2eResultsBytes.poll();
				if( iote2eResultsByte != null ) {
					try {
						Iote2eResult iote2eResult = iote2eResultReuseItem.fromByteArray(iote2eResultsByte);
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
	
	public void after() throws Exception {
		pollResult.shutdown();
		pollResult.join(5000);
		super.after();
	}
	
	

	private class SimTempToFanShutdownHook extends Thread {
		
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
