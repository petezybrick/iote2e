package com.pzybrick.test.iote2e.ruleproc.svc;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.pzybrick.iote2e.ruleproc.sourcesensor.LoginSourceSensorHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.test.iote2e.ruleproc.sourceresponse.RequestSvcUnitTestImpl;

public class TestLoginSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestLoginSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<LoginSourceSensorValue> loginSourceSensorValues;
	protected LoginSourceSensorHandler loginSourceSensorHandler;
	protected RequestSvcUnitTestImpl loginSourceResponseSvcUnitTestImpl;
	
	public TestLoginSourceSensorHandlerBase() {
		super();
	}
	
	@Before
	public void before() throws Exception {
		log.info("------------------------------------------------------------------------------------------------------");		
		loginSourceSensorValues = new ConcurrentLinkedQueue<LoginSourceSensorValue>();
		loginSourceSensorHandler = new LoginSourceSensorHandler(System.getenv("LOGIN_SOURCE_SENSOR_CONFIG_JSON_FILE"), loginSourceSensorValues);
		loginSourceResponseSvcUnitTestImpl = (RequestSvcUnitTestImpl)loginSourceSensorHandler.getLoginSourceResponseSvc();
		loginSourceResponseSvcUnitTestImpl.setRuleEvalResults(null);
		loginSourceSensorHandler.start();
	}	
	
	@After
	public void after() throws Exception {
		while( !loginSourceSensorValues.isEmpty() ) {
			try{ Thread.sleep(2000L); } catch(Exception e ) {}
		}
		loginSourceSensorHandler.shutdown();
		loginSourceSensorHandler.join();
	}
	
	protected void commonRun( String loginUuid, String sourceUuid, String sensorName, String sensorValue ) {
		log.info( String.format("loginUuid=%s, sourceUuid=%s, sensorName=%s, sensorValue=%s", loginUuid, sourceUuid, sensorName, sensorValue ));
		try {
			LoginSourceSensorValue loginSourceSensorValue = new LoginSourceSensorValue(loginUuid,sourceUuid, sensorName, sensorValue);
			loginSourceSensorHandler.putLoginSourceSensorValue(loginSourceSensorValue);

		} catch( Exception e ) {
			log.error(e.getMessage(),e);
		}		
	}
	
	protected List<RuleEvalResult> commonGetRuleEvalResults( long maxWaitMsecs ) {
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while( System.currentTimeMillis() < wakeupAt ) {
			if( loginSourceResponseSvcUnitTestImpl.getRuleEvalResults() != null )
				return loginSourceResponseSvcUnitTestImpl.getRuleEvalResults();
			try { Thread.sleep(100); }catch(Exception e){}
		}
		return null;
	}
}
