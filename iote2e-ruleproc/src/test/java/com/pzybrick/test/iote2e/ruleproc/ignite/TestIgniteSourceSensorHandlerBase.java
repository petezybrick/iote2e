package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.pzybrick.avro.schema.SourceSensorValue;
import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.sourceresponse.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.sourcesensor.SourceSensorHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;

public class TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues;
	protected SourceSensorHandler sourceSensorHandler;
	protected SourceResponseSvc sourceResponseSvc;
	protected ThreadSubscribe threadSubscribe;
	protected boolean subscribeUp;

	public TestIgniteSourceSensorHandlerBase() {
		super();
	}

	@Before
	public void before() throws Exception {
		log.info( "------------------------------------------------------------------------------------------------------");
		sourceSensorValues = new ConcurrentLinkedQueue<SourceSensorValue>();
		sourceSensorHandler = new SourceSensorHandler(System.getenv("SOURCE_SENSOR_CONFIG_JSON_FILE"),
				sourceSensorValues);
		sourceResponseSvc = sourceSensorHandler.getSourceResponseSvc();
		log.info(">>> Cache name: " + sourceSensorHandler.getRuleConfig().getSourceResponseIgniteCacheName());
		startThreadSubscribe(sourceSensorHandler.getRuleConfig());
		sourceSensorHandler.start();
	}

	private void startThreadSubscribe(RuleConfig ruleConfig) throws Exception {
		threadSubscribe = new ThreadSubscribe(ruleConfig);
		threadSubscribe.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !subscribeUp) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if( !subscribeUp ) throw new Exception( "Timeout starting ThreadSubscribe");
	}


	@After
	public void after() throws Exception {
		while (!sourceSensorValues.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		sourceSensorHandler.shutdown();
		sourceSensorHandler.join();
		threadSubscribe.shutdown();
		threadSubscribe.join();
	}

	protected void commonRun(String sourceUuid, String sensorUuid, String sensorValue) {
		log.info("sourceUuid=" + sourceUuid + ", sensorUuid=" + sensorUuid + ", sensorValue=" + sensorValue);
		try {
			SourceSensorValue sourceSensorValue = new SourceSensorValue(sourceUuid, sensorUuid, sensorValue);
			sourceSensorHandler.putSourceSensorValue(sourceSensorValue);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	// TODO: read cache results from string as Avro
	protected List<String> commonReadCacheResults(long maxWaitMsecs) {
		List<String> results = new ArrayList<String>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			// if( sourceResponseSvcUnitTestImpl.getRuleEvalResults() != null )
			// return sourceResponseSvcUnitTestImpl.getRuleEvalResults();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return results;
	}

	private class ThreadSubscribe extends Thread {
		private RuleConfig ruleConfig;
		private boolean shutdown;

		public ThreadSubscribe(RuleConfig ruleConfig) {
			this.ruleConfig = ruleConfig;
		}

		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			IgniteSingleton igniteSingleton = null;
			try {
				igniteSingleton = IgniteSingleton.getInstance( ruleConfig );

				// Create new continuous query.
				ContinuousQuery<Integer, String> qry = new ContinuousQuery<>();

				// Callback that is called locally when update notifications are
				// received.
				qry.setLocalListener(new CacheEntryUpdatedListener<Integer, String>() {
					@Override
					public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends String>> evts) {
						for (CacheEntryEvent<? extends Integer, ? extends String> e : evts)
							log.info("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
					}
				});
				qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<Integer, String>>() {
					@Override
					public CacheEntryEventFilter<Integer, String> create() {
						return new CacheEntryEventFilter<Integer, String>() {
							@Override
							public boolean evaluate(CacheEntryEvent<? extends Integer, ? extends String> e) {
								return true;
							}
						};
					}
				});
				QueryCursor<Cache.Entry<Integer, String>> cur = igniteSingleton.getCache().query(qry);


			} catch (Exception e) {
				System.out.println(e);
				return;

			}
			subscribeUp = true;
			while (true) {
				if (shutdown)
					break;
				try {
					sleep(60*60*5);
				} catch (InterruptedException e) {
				} 
			}
		}
	}
}
