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

public class TestLocalHandlerBase extends TestCommonHandler {
	private static final Logger logger = LogManager.getLogger(TestLocalHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> queueIote2eRequests;
	protected Iote2eRequestHandlerIgniteTestThread iote2eRequestHandler;
	protected Iote2eSvcLocalTestImpl iote2eSvc;

	public TestLocalHandlerBase() throws Exception {
		super();
	}
	
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		TestCommonHandler.beforeClass();
	}

	
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

	protected void commonRun(String loginName, String sourceName, String sourceType, String sensorName,
			String sensorValue ) throws Exception {
		commonRun( loginName, sourceName, sourceType, sensorName, sensorValue, (Map<CharSequence, CharSequence>)null );
	}

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
