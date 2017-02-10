package com.pzybrick.iote2e.tests.simws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.spark.Iote2eRequestSparkConsumer;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eSchemaConstants;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;
import com.pzybrick.iote2e.tests.common.ThreadSparkRun;
import com.pzybrick.iote2e.ws.security.LoginVo;
import com.pzybrick.iote2e.ws.socket.ClientSocketHandler;

public class SimWsTempToFan {
	private static final Logger logger = LogManager.getLogger(SimWsTempToFan.class);
	private static final double TEMP_MIN = 74.0;
	private static final double TEMP_MAX = 83.0;
	private static final double TEMP_START = 79.0;
	private static final double TEMP_INCR = .5;
	private static final long TEMP_PUT_FREQ_MS = 2000;
	private boolean tempDirectionIncrease = true;
	private ClientSocketHandler clientSocketHandler;
	private PollIote2eResultsThread pollIote2eResultsThread;
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	protected MasterConfig masterConfig;
	protected ThreadSparkRun threadSparkRun;
	protected Iote2eRequestSparkConsumer iote2eRequestSparkConsumer;


	public static void main(String[] args) {
		SimWsTempToFan simWsTempToFan = new SimWsTempToFan();
		simWsTempToFan.process(args);
	}

	public void process(String[] args) {
		try {
			masterConfig = MasterConfig.getInstance();
			Runtime.getRuntime().addShutdownHook(new SimWsTempToFanShutdownHook());
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testTempToFanFilterKey, null);
			// if Spark not running standalone then start
			if( masterConfig.getSparkMaster().startsWith("local")) {
		    	iote2eRequestSparkConsumer = new Iote2eRequestSparkConsumer();
		    	threadSparkRun = new ThreadSparkRun( iote2eRequestSparkConsumer);
		    	threadSparkRun.start();
		    	long expiredAt = System.currentTimeMillis() + (10*1000);
		    	while( expiredAt > System.currentTimeMillis() ) {
		    		if( threadSparkRun.isStarted() ) break;
		    		try {
		    			Thread.sleep(250);
		    		} catch( Exception e ) {}
		    	}
		    	if( !threadSparkRun.isStarted() ) throw new Exception("Timeout waiting for Spark to start");
			}

			pollIote2eResultsThread = new PollIote2eResultsThread();
			pollIote2eResultsThread.start();
			LoginVo loginVo = new LoginVo()
					.setLogin(TestCommonHandler.testTempToFanLoginName)
					.setSourceName(TestCommonHandler.testTempToFanSourceName)
					.setOptionalFilterSensorName(TestCommonHandler.testTempToFanSensorName);
			clientSocketHandler = new ClientSocketHandler()
					.setLoginVo(loginVo).setUrl(args[0])
					.setQueueIote2eRequests(queueIote2eRequests)
					.setQueueIote2eResults(queueIote2eResults)
					.setPollIote2eResultsThread(pollIote2eResultsThread);
			clientSocketHandler.connect();			
			

			double tempNow = 78.5;
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
					shutdown();
					break;
				}
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	private class PollIote2eResultsThread extends Thread {
		private boolean shutdown;

		public PollIote2eResultsThread( ) {
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
						if( "off".equals(actuatorValue)) tempDirectionIncrease = true;
						else if( "on".equals(actuatorValue)) tempDirectionIncrease = false;
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
				try {
					sleep(100);
				} catch( InterruptedException e ) {}
				if( this.shutdown ) break;
			}
		}
	}
	
	public void shutdown() throws Exception {
		clientSocketHandler.shutdown();
		pollIote2eResultsThread.shutdown();
		pollIote2eResultsThread.join(5000);
		threadSparkRun.shutdown();
	}
	
	

	private class SimWsTempToFanShutdownHook extends Thread {
		
		@Override
		public void run() {
			try {
				logger.info("Shutdownhook - Start");
				shutdown();
				logger.info("Shutdownhook - Complete");
			} catch( Exception e ) {
				logger.error(e.getMessage(), e );
			}
		}
	}

}
