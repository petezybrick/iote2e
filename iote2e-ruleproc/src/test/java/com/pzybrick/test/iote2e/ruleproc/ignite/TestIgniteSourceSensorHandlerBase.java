package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.pzybrick.avro.schema.SourceSensorValue;
import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.sourcesensor.SourceSensorHandler;

public class TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues;
	protected SourceSensorHandler sourceSensorHandler;
	protected SourceResponseSvc sourceResponseSvc;
	
	public TestIgniteSourceSensorHandlerBase() {
		super();
	}
	
	@Before
	public void before() throws Exception {
		log.info("------------------------------------------------------------------------------------------------------");		
		sourceSensorValues = new ConcurrentLinkedQueue<SourceSensorValue>();
		sourceSensorHandler = new SourceSensorHandler(System.getenv("SOURCE_SENSOR_CONFIG_JSON_FILE"), sourceSensorValues);
		sourceResponseSvc = sourceSensorHandler.getSourceResponseSvc();
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
	
	// TODO: read cache results from string as Avro
	protected List<String> commonReadCacheResults( long maxWaitMsecs ) {
		List<String> results = new ArrayList<String>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while( System.currentTimeMillis() < wakeupAt ) {
//			if( sourceResponseSvcUnitTestImpl.getRuleEvalResults() != null )
//				return sourceResponseSvcUnitTestImpl.getRuleEvalResults();
			try { Thread.sleep(100); }catch(Exception e){}
		}
		return results;
	}
}
