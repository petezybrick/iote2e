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
package com.pzybrick.iote2e.tests.ignite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.stream.svc.ActuatorState;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.tests.common.Iote2eRequestHandlerIgniteTestThread;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class TestIgniteHandlerBase.
 */
public class TestIgniteHandlerBase extends TestCommonHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerBase.class);
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	
	/** The queue iote 2 e results. */
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	
	/** The iote 2 e request handler. */
	protected Iote2eRequestHandlerIgniteTestThread iote2eRequestHandler;
	
	/** The iote 2 e svc. */
	protected Iote2eSvc iote2eSvc;
	
	/** The thread ignite subscribe. */
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	
	/** The iote 2 e result reuse item. */
	protected Iote2eResultReuseItem iote2eResultReuseItem;
	
	/**
	 * Instantiates a new test ignite handler base.
	 *
	 * @throws Exception the exception
	 */
	public TestIgniteHandlerBase() throws Exception {
		super();
	}
	
	
	/**
	 * Before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestCommonHandler.beforeClass();
	}
	
	
	/**
	 * Before.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void before() throws Exception {
		try {			
			logger.info(
					"------------------------------------------------------------------------------------------------------");
			iote2eResultReuseItem = new Iote2eResultReuseItem();
			queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
			queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
			iote2eRequestHandler = new Iote2eRequestHandlerIgniteTestThread(masterConfig, queueIote2eRequests);
			iote2eSvc = iote2eRequestHandler.getIote2eSvc();
			logger.info("Cache name: " + masterConfig.getIgniteCacheName());
			// reset to same default ActuatorState=null every time
			if( masterConfig.isForceResetActuatorState()) {
				String rawJson = ConfigDao.findConfigJson(masterConfig.getActuatorStateKey());
				List<ActuatorState> actuatorStates = ActuatorStateDao.createActuatorStatesFromJson(rawJson);
				ActuatorStateDao.resetActuatorStateBatch(actuatorStates);
			}
			iote2eRequestHandler.start();
		} catch (Exception e) {
			logger.error("Exception in before, " + e.getMessage(), e);
		}
	}

	/**
	 * After.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void after() throws Exception {
		while (!queueIote2eRequests.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		try {
			Thread.sleep(2000);
			iote2eRequestHandler.shutdown();
			iote2eRequestHandler.join();
			threadIgniteSubscribe.shutdown();
			threadIgniteSubscribe.join();
		} finally {
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		}
	}
	
	/**
	 * After class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void afterClass() throws Exception {
		try {
		} finally {
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		}
	}

	/**
	 * Common run.
	 *
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param sourceType the source type
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @param igniteFilterKey the ignite filter key
	 * @throws Exception the exception
	 */
	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, String igniteFilterKey) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( masterConfig,
					igniteFilterKey, queueIote2eResults, (Thread)null);
			Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(loginName).setSourceName(sourceName)
					.setSourceType(sourceType).setRequestUuid(UUID.randomUUID().toString())
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs).build();
			iote2eRequestHandler.addIote2eRequest(iote2eRequest);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}




}
