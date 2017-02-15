package com.pzybrick.iote2e.tests.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public abstract class TestCommonHandler {
	private static final Logger logger = LogManager.getLogger(TestCommonHandler.class);

	public static final String testHumidityLoginName = "pzybrick1";
	public static final String testHumiditySourceName = "rpi_999";
	public static final String testHumiditySourceType = "humidity";
	public static final String testHumiditySensorName = "humidity1";
	public static final String testHumidityFilterKey = testHumidityLoginName + "|" + testHumiditySourceName + "|" + testHumiditySensorName + "|";

	public static final String testLedLoginName = "pzybrick1";
	public static final String testLedSourceName = "rpi_999";
	public static final String testLedSourceType = "switch";
	public static final String testLedSensorNameGreen = "switch0";
	public static final String testLedGreenFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen + "|";
	public static final String testLedSensorNameRed = "switch1";
	public static final String testLedRedFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed + "|";
	public static final String testLedSensorNameYellow = "switch2";
	public static final String testLedYellowFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow + "|";
	
	public static final String testTempToFanLoginName = "pzybrick1";
	public static final String testTempToFanSourceName = "rpi_999";
	public static final String testTempToFanSourceType = "temp";
	public static final String testTempToFanSensorName = "temp1";
	public static final String testTempToFanFilterKey = testTempToFanLoginName + "|" + testTempToFanSourceName + "|" + testTempToFanSensorName + "|";
	
	public static List<Iote2eResult> commonThreadSubscribeGetIote2eResults(long maxWaitMsecs, ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults ) throws Exception {
		List<Iote2eResult> iote2eResults = new ArrayList<Iote2eResult>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (!queueIote2eResults.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				logger.debug("queueIote2eResults.size() {}", queueIote2eResults.size());
				while( true ) {
					Iote2eResult iote2eResult = queueIote2eResults.poll();
					if( iote2eResult == null ) {
						logger.debug("queueIote2eResults poll empty");
						break;					
					}
					logger.debug("add to iote2eResults from subscribeResults");
					iote2eResults.add( iote2eResult );
				}
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return iote2eResults;
	}


}
