package com.pzybrick.iote2e.tests.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;

public abstract class TestCommonHandler {
	private static final Logger logger = LogManager.getLogger(TestCommonHandler.class);

	public static final String testHumidityLoginName = "lo1";
	public static final String testHumiditySourceName = "lo1so1";
	public static final String testHumiditySourceType = "humidity";
	public static final String testHumiditySensorName = "humidity1";
	public static final String testHumidityFilterKey = testHumidityLoginName + "|" + testHumiditySourceName + "|" + testHumiditySensorName + "|";

	public static final String testLedLoginName = "lo1";
	public static final String testLedSourceName = "lo1so2";
	public static final String testLedSourceType = "switch";
	public static final String testLedSensorNameGreen = "switch0";
	public static final String testLedGreenFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen + "|";
	public static final String testLedSensorNameRed = "switch1";
	public static final String testLedRedFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed + "|";
	public static final String testLedSensorNameYellow = "switch2";
	public static final String testLedYellowFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow + "|";
	
	public static final String testTempToFanLoginName = "lo1";
	public static final String testTempToFanSourceName = "lo1so1";
	public static final String testTempToFanSourceType = "temp";
	public static final String testTempToFanSensorName = "temp1";
	public static final String testTempToFanFilterKey = testTempToFanLoginName + "|" + testTempToFanSourceName + "|" + testTempToFanSensorName + "|";
	
	public static List<Iote2eResult> commonThreadSubscribeGetIote2eResults(long maxWaitMsecs, ConcurrentLinkedQueue<byte[]> subscribeResults,
			Iote2eResultReuseItem iote2eResultReuseItem) throws Exception {
		List<Iote2eResult> iote2eResults = new ArrayList<Iote2eResult>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (subscribeResults.size() > 0) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				logger.debug("subscribeResults.size() {}", subscribeResults.size());
				while( true ) {
					byte[] bytes = subscribeResults.poll();
					if( bytes == null ) {
						logger.debug("subscribeResults poll empty");
						break;					
					}
					logger.debug("add to iote2eResults from subscribeResults");
					try {
						iote2eResults.add( iote2eResultReuseItem.fromByteArray(bytes) );
					} catch( IOException e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
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
