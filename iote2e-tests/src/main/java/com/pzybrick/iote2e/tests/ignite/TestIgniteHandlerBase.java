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

import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.common.ignite.ThreadIgniteSubscribe;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.ActuatorState;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;

public class TestIgniteHandlerBase extends TestCommonHandler {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	protected ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvc iote2eSvc;
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	protected Iote2eResultReuseItem iote2eResultReuseItem;
	
	public TestIgniteHandlerBase() {
		super();
	}

	@Before
	public void before() throws Exception {
		try {			
			logger.info(
					"------------------------------------------------------------------------------------------------------");
			iote2eResultReuseItem = new Iote2eResultReuseItem();
			queueIote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
			queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
			iote2eRequestHandler = new Iote2eRequestHandler(queueIote2eRequests);
			iote2eSvc = iote2eRequestHandler.getIote2eSvc();
			logger.info("Cache name: " + iote2eRequestHandler.getMasterConfig().getIgniteCacheName());
			// reset to same default ActuatorState=null every time
			if( iote2eRequestHandler.getMasterConfig().isForceResetActuatorState()) {
				String rawJson = ConfigDao.findConfigJson(iote2eRequestHandler.getMasterConfig().getActuatorStateKey());
				List<ActuatorState> actuatorStates = Iote2eUtils.getGsonInstance().fromJson(rawJson,
						new TypeToken<List<ActuatorState>>() {
						}.getType());
				ActuatorStateDao.resetActuatorStateBatch(actuatorStates);
			}
			iote2eRequestHandler.start();
		} catch (Exception e) {
			logger.error("Exception in before, " + e.getMessage(), e);
		}
	}

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
	
	@AfterClass
	public static void afterClass() throws Exception {
		try {
		} finally {
			ConfigDao.disconnect();
			ActuatorStateDao.disconnect();
		}
	}

	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, String igniteFilterKey) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe( 
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
