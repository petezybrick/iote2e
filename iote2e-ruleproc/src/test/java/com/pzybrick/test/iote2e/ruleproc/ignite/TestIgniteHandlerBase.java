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
import com.pzybrick.test.iote2e.ruleproc.common.TestRuleProcCommon;
import com.pzybrick.test.iote2e.ruleproc.common.ThreadIgniteSubscribe;

public class TestIgniteHandlerBase implements TestRuleProcCommon {
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
			iote2eRequestHandler = new Iote2eRequestHandler(System.getenv("REQUEST_CONFIG_JSON_FILE_IGNITE"),
					iote2eRequests);
			iote2eSvc = iote2eRequestHandler.getIote2eSvc();
			igniteSingleton = IgniteSingleton.getInstance(iote2eRequestHandler.getRuleConfig());
			logger.info(">>> Cache name: " + iote2eRequestHandler.getRuleConfig().getSourceResponseIgniteCacheName());
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
			threadIgniteSubscribe = ThreadIgniteSubscribe.startThreadSubscribe(iote2eRequestHandler.getRuleConfig(), igniteFilterKey, igniteSingleton, subscribeResults);
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

	protected List<Iote2eResult> commonThreadSubscribeGetIote2eResults(long maxWaitMsecs) throws Exception {
		List<Iote2eResult> iote2eResults = new ArrayList<Iote2eResult>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (subscribeResults.size() > 0) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
				for( byte[] bytes : subscribeResults ) {
					try {
						iote2eResults.add( iote2eResultReuseItem.fromByteArray(bytes) );
					} catch( IOException e ) {
						logger.error(e.getMessage(),e);
						throw e;
					}
				}
				break;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return iote2eResults;
	}



}
