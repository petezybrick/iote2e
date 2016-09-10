package com.pzybrick.test.iote2e.ruleproc.svc;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.pzybrick.iote2e.avro.schema.SourceSensorValue;
import com.pzybrick.iote2e.ruleproc.sourcesensor.SourceSensorHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.test.iote2e.ruleproc.sourceresponse.SourceResponseSvcUnitTestImpl;

public class TestSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues;
	protected SourceSensorHandler sourceSensorHandler;
	protected SourceResponseSvcUnitTestImpl sourceResponseSvcUnitTestImpl;
	
	public TestSourceSensorHandlerBase() {
		super();
	}
	
	@Before
	public void before() throws Exception {
		log.info("------------------------------------------------------------------------------------------------------");		
		sourceSensorValues = new ConcurrentLinkedQueue<SourceSensorValue>();
		sourceSensorHandler = new SourceSensorHandler(System.getenv("SOURCE_SENSOR_CONFIG_JSON_FILE"), sourceSensorValues);
		sourceResponseSvcUnitTestImpl = (SourceResponseSvcUnitTestImpl)sourceSensorHandler.getSourceResponseSvc();
		sourceResponseSvcUnitTestImpl.setRuleEvalResults(null);
		sourceSensorHandler.start();
	}	
	
	@After
	public void after() throws Exception {
		while( !sourceSensorValues.isEmpty() ) {
			try{ Thread.sleep(2000L); } catch(Exception e ) {}
		}
		sourceSensorHandler.shutdown();
		sourceSensorHandler.join();
	}
	
	protected void commonRun( String sourceUuid, String sensorUuid, String sensorValue ) {
		log.info("sourceUuid=" + sourceUuid + ", sensorUuid=" + sensorUuid + ", sensorValue=" + sensorValue );
		try {
			SourceSensorValue sourceSensorValue = new SourceSensorValue(sourceUuid, sensorUuid, sensorValue);
			sourceSensorHandler.putSourceSensorValue(sourceSensorValue);

		} catch( Exception e ) {
			log.error(e.getMessage(),e);
		}		
	}
	
	protected List<RuleEvalResult> commonGetRuleEvalResults( long maxWaitMsecs ) {
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while( System.currentTimeMillis() < wakeupAt ) {
			if( sourceResponseSvcUnitTestImpl.getRuleEvalResults() != null )
				return sourceResponseSvcUnitTestImpl.getRuleEvalResults();
			try { Thread.sleep(100); }catch(Exception e){}
		}
		return null;
	}
}
