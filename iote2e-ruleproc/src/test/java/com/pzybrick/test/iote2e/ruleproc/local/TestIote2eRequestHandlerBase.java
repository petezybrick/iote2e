package com.pzybrick.test.iote2e.ruleproc.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.pzybrick.iote2e.common.utils.IotE2eUtils;
import com.pzybrick.iote2e.ruleproc.request.Iote2eRequestHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.OPERATION;
import com.pzybrick.test.iote2e.ruleproc.sourceresponse.RequestSvcUnitTestImpl;

public class TestIote2eRequestHandlerBase {
	private static final Log log = LogFactory.getLog(TestIote2eRequestHandlerBase.class);
	protected ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests;
	protected Iote2eRequestHandler iote2eRequestHandler;
	protected RequestSvcUnitTestImpl requestSvcUnitTestImpl;
	
	public TestIote2eRequestHandlerBase() {
		super();
	}
	
	@Before
	public void before() throws Exception {
		log.info("------------------------------------------------------------------------------------------------------");		
		iote2eRequests = new ConcurrentLinkedQueue<Iote2eRequest>();
		iote2eRequestHandler = new Iote2eRequestHandler(System.getenv("LOGIN_SOURCE_SENSOR_CONFIG_JSON_FILE"), iote2eRequests);
		requestSvcUnitTestImpl = (RequestSvcUnitTestImpl)iote2eRequestHandler.getIote2eRequestSvc();
		requestSvcUnitTestImpl.setRuleEvalResults(null);
		iote2eRequestHandler.start();
	}	
	
	@After
	public void after() throws Exception {
		while( !iote2eRequests.isEmpty() ) {
			try{ Thread.sleep(2000L); } catch(Exception e ) {}
		}
		iote2eRequestHandler.shutdown();
		iote2eRequestHandler.join();
	}
	
	protected void commonRun( String loginName, String sourceName, String sourceType, String sensorName, String sensorValue ) {
		log.info( String.format("loginUuid=%s, sourceUuid=%s, sensorName=%s, sensorValue=%s", loginName, sourceName, sensorName, sensorValue ));
		try {
			String requestUuid = UUID.randomUUID().toString();
			Map<CharSequence,CharSequence> pairs = new HashMap<CharSequence,CharSequence>();
			pairs.put(sensorName, sensorValue);
			Iote2eRequest iote2eRequest = Iote2eRequest.newBuilder()
					.setLoginName(loginName)
					.setSourceName(sourceName)
					.setSourceType(sourceType)
					.setRequestUuid(requestUuid)
					.setTimestamp(IotE2eUtils.getDateNowUtc8601())
					.setOperation(OPERATION.SENSORS_VALUES)
					.setPairs(pairs)
					.build();

			iote2eRequestHandler.getIote2eRequests().add( iote2eRequest );

		} catch( Exception e ) {
			log.error(e.getMessage(),e);
		}		
	}
	
	protected List<RuleEvalResult> commonGetRuleEvalResults( long maxWaitMsecs ) {
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while( System.currentTimeMillis() < wakeupAt ) {
			if( requestSvcUnitTestImpl.getRuleEvalResults() != null )
				return requestSvcUnitTestImpl.getRuleEvalResults();
			try { Thread.sleep(100); }catch(Exception e){}
		}
		return null;
	}
}
