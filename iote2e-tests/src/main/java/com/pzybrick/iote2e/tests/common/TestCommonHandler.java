/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.tests.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;


/**
 * The Class TestCommonHandler.
 */
public abstract class TestCommonHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestCommonHandler.class);
	
	/** The master config. */
	protected static MasterConfig masterConfig;

	/** The Constant testHumidityLoginName. */
	public static final String testHumidityLoginName = "pzybrick1";
	
	/** The Constant testHumiditySourceName. */
	public static final String testHumiditySourceName = "rpi-999";
	
	/** The Constant testHumiditySourceType. */
	public static final String testHumiditySourceType = "humidity";
	
	/** The Constant testHumiditySensorName. */
	public static final String testHumiditySensorName = "humidity1";
	
	/** The Constant testHumidityFilterKey. */
	public static final String testHumidityFilterKey = testHumidityLoginName + "|" + testHumiditySourceName + "|" + testHumiditySensorName + "|";

	/** The Constant testLedLoginName. */
	public static final String testLedLoginName = "pzybrick1";
	
	/** The Constant testLedSourceName. */
	public static final String testLedSourceName = "rpi-999";
	
	/** The Constant testLedSourceType. */
	public static final String testLedSourceType = "switch";
	
	/** The Constant testLedSensorNameGreen. */
	public static final String testLedSensorNameGreen = "switch0";
	
	/** The Constant testLedGreenFilterKey. */
	public static final String testLedGreenFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameGreen + "|";
	
	/** The Constant testLedSensorNameRed. */
	public static final String testLedSensorNameRed = "switch1";
	
	/** The Constant testLedRedFilterKey. */
	public static final String testLedRedFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameRed + "|";
	
	/** The Constant testLedSensorNameYellow. */
	public static final String testLedSensorNameYellow = "switch2";
	
	/** The Constant testLedYellowFilterKey. */
	public static final String testLedYellowFilterKey = testLedLoginName + "|" + testLedSourceName + "|" + testLedSensorNameYellow + "|";
	
	/** The Constant testTempToFanLoginName. */
	public static final String testTempToFanLoginName = "pzybrick1";
	
	/** The Constant testTempToFanSourceName. */
	public static final String testTempToFanSourceName = "rpi-999";
	
	/** The Constant testTempToFanSourceType. */
	public static final String testTempToFanSourceType = "temperature";
	
	/** The Constant testTempToFanSensorName. */
	public static final String testTempToFanSensorName = "temp1";
	
	/** The Constant testTempToFanFilterKey. */
	public static final String testTempToFanFilterKey = testTempToFanLoginName + "|" + testTempToFanSourceName + "|" + testTempToFanSensorName + "|";
	
	/** The Constant testPillDispenseImageLoginName. */
	public static final String testPillDispenseImageLoginName = "pzybrick1";
	
	/** The Constant testPillDispenseImageSourceName. */
	public static final String testPillDispenseImageSourceName = "rpi-999";
	
	/** The Constant testPillDispenseImageSourceType. */
	public static final String testPillDispenseImageSourceType = "pill_dispenser";
	
	/** The Constant testPillDispenseImageSensorName. */
	public static final String testPillDispenseImageSensorName = "pilldisp1";
	
	/** The Constant testPillDispenseImageFilterKey. */
	public static final String testPillDispenseImageFilterKey = testPillDispenseImageLoginName + "|" + testPillDispenseImageSourceName + "|" + testPillDispenseImageSensorName + "|";
	
	/**
	 * Instantiates a new test common handler.
	 *
	 * @throws Exception the exception
	 */
	public TestCommonHandler() throws Exception {
	}
	
	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestCommonHandler.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"), System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME") );
	}

	
	/**
	 * Common thread subscribe get iote 2 e results.
	 *
	 * @param maxWaitMsecs the max wait msecs
	 * @param queueIote2eResults the queue iote 2 e results
	 * @return the list
	 * @throws Exception the exception
	 */
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

	
	/**
	 * File to byte array.
	 *
	 * @param path the path
	 * @return the byte[]
	 * @throws Exception the exception
	 */
	public static byte[] fileToByteArray( String path ) throws Exception {
		File file = new File(path);
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(bytes); //read file into bytes[]
		fis.close();
		return bytes;
	}
	
}
