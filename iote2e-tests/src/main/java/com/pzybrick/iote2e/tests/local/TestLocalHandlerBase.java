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
package com.pzybrick.iote2e.tests.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.tests.common.Iote2eRequestHandlerIgniteTestThread;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class TestLocalHandlerBase.
 */
public class TestLocalHandlerBase extends TestCommonHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerBase.class);
	
	/** The queue iote 2 e requests. */
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	
	/** The iote 2 e request handler. */
	protected Iote2eRequestHandlerIgniteTestThread iote2eRequestHandler;
	
	/** The iote 2 e svc. */
	protected Iote2eSvcLocalTestImpl iote2eSvc;

	/**
	 * Instantiates a new test local handler base.
	 *
	 * @throws Exception the exception
	 */
	public TestLocalHandlerBase() throws Exception {
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
		logger.info(
				"------------------------------------------------------------------------------------------------------");
		queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		iote2eRequestHandler = new Iote2eRequestHandlerIgniteTestThread( masterConfig, queueIote2eRequests );
		iote2eSvc = (Iote2eSvcLocalTestImpl) iote2eRequestHandler.getIote2eSvc();
		iote2eSvc.setRuleEvalResults(null);
		iote2eRequestHandler.start();
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
		// if Before failed, the iote2eRequestHandler might not exist
		if( iote2eRequestHandler != null ) {
			iote2eRequestHandler.shutdown();
			iote2eRequestHandler.join();
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
	 * @throws Exception the exception
	 */
	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue ) throws Exception {
		commonRun( loginName, sourceName, sourceType, sensorName, sensorValue, (Map<CharSequence, CharSequence>)null );
	}

	/**
	 * Common run.
	 *
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param sourceType the source type
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @param metadata the metadata
	 * @throws Exception the exception
	 */
	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, Map<CharSequence, CharSequence> metadata ) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			Map<CharSequence, CharSequence> pairs = new HashMap<CharSequence, CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder().setLoginName(loginName).setSourceName(sourceName)
					.setSourceType(sourceType).setRequestUuid(UUID.randomUUID().toString())
					.setRequestTimestamp(Iote2eUtils.getDateNowUtc8601()).setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs).setMetadata(metadata).build();
			iote2eRequestHandler.addIote2eRequest(iote2eRequest);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Common get rule eval results.
	 *
	 * @param maxWaitMsecs the max wait msecs
	 * @return the list
	 */
	protected List<RuleEvalResult> commonGetRuleEvalResults(long maxWaitMsecs) {
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (iote2eSvc.getRuleEvalResults() != null)
				return iote2eSvc.getRuleEvalResults();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return null;
	}
}
