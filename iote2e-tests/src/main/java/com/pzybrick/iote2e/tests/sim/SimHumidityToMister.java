package com.pzybrick.iote2e.tests.sim;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;

public class SimHumidityToMister extends SimBase {
	private static final Logger logger = LogManager.getLogger(SimHumidityToMister.class);
	private static final double HUMIDITY_MIN = 82.0;
	private static final double HUMIDITY_MAX = 93.0;
	private static final double HUMIDITY_START = 92.0;
	private static final double HUMIDITY_INCR = .5;
	private static final long HUMIDITY_PUT_FREQ_MS = 2000;
	private boolean humidityDirectionIncrease = true;
	private ThreadPollResult pollResult;

	public static void main(String[] args) {
		SimHumidityToMister simHumidityToMister = new SimHumidityToMister();
		simHumidityToMister.process();
	}

	public void process() {
		try {
			Runtime.getRuntime().addShutdownHook(new SimHumidityToMisterShutdownHook());
			before();
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testHumidityFilterKey, null);
			pollResult = new ThreadPollResult();
			pollResult.start();
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(
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
	
	private class ThreadPollResult extends Thread {
		private boolean shutdown;

		public ThreadPollResult( ) {
			super();
		}
		
		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

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
	
	public void after() throws Exception {
		pollResult.shutdown();
		pollResult.join(5000);
		super.after();
	}
	
	

	private class SimHumidityToMisterShutdownHook extends Thread {
		
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
