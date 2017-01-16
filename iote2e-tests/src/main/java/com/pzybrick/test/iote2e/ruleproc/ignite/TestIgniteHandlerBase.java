package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;
import com.pzybrick.test.iote2e.ruleproc.common.TestCommonHandler;
import com.pzybrick.test.iote2e.ruleproc.common.ThreadIgniteSubscribe;

public class TestIgniteHandlerBase extends TestCommonHandler {
	private static final Logger logger = LogManager.getLogger(TestIgniteHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected ConcurrentLinkedQueue<byte[]> subscribeResults;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected Iote2eSvc iote2eSvc;
	protected ThreadIgniteSubscribe threadIgniteSubscribe;
	protected IgniteSingleton igniteSingleton = null;
	protected Gson gson;
	protected Iote2eResultReuseItem iote2eResultReuseItem;
	
	public TestIgniteHandlerBase() {
		super();
		gson = new GsonBuilder().create();
	}

	@Before
	public void before() throws Exception {
		try {			
			logger.info(
					"------------------------------------------------------------------------------------------------------");
			iote2eResultReuseItem = new Iote2eResultReuseItem();
			subscribeResults = new ConcurrentLinkedQueue<byte[]>();
			iote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
			iote2eRequestHandler = new Iote2eRequestHandler(System.getenv("MASTER_CONFIG_JSON_KEY"),
					iote2eRequests);
			iote2eSvc = iote2eRequestHandler.getIote2eSvc();
			igniteSingleton = IgniteSingleton.getInstance(iote2eRequestHandler.getMasterConfig());
			logger.info("Cache name: " + iote2eRequestHandler.getMasterConfig().getSourceResponseIgniteCacheName());
			iote2eRequestHandler.start();
		} catch (Exception e) {
			logger.error("Exception in before, " + e.getMessage(), e);
		}
	}

	@After
	public void after() throws Exception {
		while (!iote2eRequests.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		iote2eRequestHandler.shutdown();
		iote2eRequestHandler.join();
		threadIgniteSubscribe.shutdown();
		threadIgniteSubscribe.join();
		IgniteSingleton.reset();
	}

	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue, String igniteFilterKey) throws Exception {
		logger.info(String.format("loginName=%s, sourceName=%s, sourceType=%s, sensorName=%s, sensorValue=%s", loginName,
				sourceName, sourceType, sensorName, sensorValue));
		try {
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(iote2eRequestHandler.getMasterConfig(), igniteFilterKey, igniteSingleton, subscribeResults);
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
