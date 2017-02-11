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

public class SimWsClientLedGreen extends SimWsClientBase {
	private static final Logger logger = LogManager.getLogger(SimWsClientLedGreen.class);
	private static final long LEDGREEN_PUT_FREQ_MS = 3000;
	private String ledGreenState = "0";


	public static void main(String[] args) {
		SimWsClientLedGreen simWsClientLedGreen = new SimWsClientLedGreen();
		simWsClientLedGreen.process(args);
	}

	public void process(String[] args) {
		try {
			url = args[0];
			loginVo = new LoginVo()
					.setLogin(TestCommonHandler.testLedLoginName)
					.setSourceName(TestCommonHandler.testLedSourceName)
					.setOptionalFilterSensorName(TestCommonHandler.testLedSensorNameGreen);
			masterConfig = MasterConfig.getInstance();
			Runtime.getRuntime().addShutdownHook(new SimWsTempToFanShutdownHook());
			ActuatorStateDao.updateActuatorValue(TestCommonHandler.testLedGreenFilterKey, null);
			pollIote2eResultsThread = new PollIote2eResultsThread(queueIote2eResults) {
				@Override
				public void processIote2eResult( Iote2eResult iote2eResult ) {
					try {
						String actuatorValue = iote2eResult.getPairs().get( new Utf8(Iote2eSchemaConstants.PAIRNAME_ACTUATOR_VALUE)).toString();
						logger.info("actuatorValue {}", actuatorValue);
						if( "off".equals(actuatorValue)) setLedGreenState("1");
						else if( "green".equals(actuatorValue)) setLedGreenState("0");
					} catch(Exception e ) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			pollIote2eResultsThread.start();			
			before();
			// give spark and ignite a few seconds to start
			try { Thread.sleep(2500);} catch(InterruptedException e) {}
			while( true ) {
				logger.info( "ledGreenState: {}", getLedGreenState());
				Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
				pairs.put(TestCommonHandler.testLedSensorNameGreen, new Utf8(String.valueOf(getLedGreenState())));				
				Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
						.setLoginName(TestCommonHandler.testLedLoginName)
						.setSourceName(TestCommonHandler.testLedSourceName)
						.setSourceType(TestCommonHandler.testLedSourceType)
						.setRequestUuid(UUID.randomUUID().toString())
						.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601())
						.setOperation(OPERATION.SENSORS_VALUES)
						.setPairs(pairs).build();
				clientSocketHandler.sendIote2eRequest(iote2eRequest);
				try {
					Thread.sleep(LEDGREEN_PUT_FREQ_MS);
				} catch( InterruptedException e) {}	
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

	public String getLedGreenState() {
		synchronized(ledGreenState ) {
			return ledGreenState;
		}
	}

	public SimWsClientLedGreen setLedGreenState(String ledGreenState) {
		synchronized(ledGreenState ) {
			this.ledGreenState = ledGreenState;
			return this;
		}
	}
}
