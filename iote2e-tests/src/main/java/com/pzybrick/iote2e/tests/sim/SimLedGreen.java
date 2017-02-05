package com.pzybrick.iote2e.tests.sim;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadIgniteSubscribe;

public class SimLedGreen extends SimBase {
	private static final Logger logger = LogManager.getLogger(SimLedGreen.class);
	private static final long LEDGREEN_PUT_FREQ_MS = 3000;
	private PollResult pollResult;
	private String ledGreenState = "0";

	public static void main(String[] args) {
		SimLedGreen simLedGreen = new SimLedGreen();
		simLedGreen.process();
	}

	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimLedGreenShutdownHook());
			before();
			pollResult = new PollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(iote2eRequestHandler.getMasterConfig(),
					TestCommonHandler.testLedGreenFilterKey, igniteSingleton, iote2eResultsBytes, pollResult);

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
	
	public void after() throws Exception {
		pollResult.shutdown();
		pollResult.join(5000);
		super.after();
	}
	
	

	private class SimLedGreenShutdownHook extends Thread {
		
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

	public String getLedGreenState() {
		synchronized(ledGreenState ) {
			return ledGreenState;
		}
	}

	public SimLedGreen setLedGreenState(String ledGreenState) {
		synchronized(ledGreenState ) {
			this.ledGreenState = ledGreenState;
			return this;
		}
	}

}
