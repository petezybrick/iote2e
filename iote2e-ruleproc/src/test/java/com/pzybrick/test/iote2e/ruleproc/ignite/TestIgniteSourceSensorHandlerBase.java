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
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.junit.After;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.avro.schema.SourceSensorValue;
import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.sourceresponse.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.sourcesensor.SourceSensorHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;

public class TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues;
	protected ConcurrentLinkedQueue<String> subscribeResults;
	protected SourceSensorHandler sourceSensorHandler;
	protected SourceResponseSvc sourceResponseSvc;
	protected ThreadSubscribe threadSubscribe;
	protected boolean subscribeUp;
	protected IgniteSingleton igniteSingleton = null;
	protected Gson gson;

	@Before
	public void before() throws Exception {
		try {
			gson = new GsonBuilder().create();
			subscribeResults = new ConcurrentLinkedQueue<String>();
			sourceSensorValues = new ConcurrentLinkedQueue<SourceSensorValue>();
			sourceSensorHandler = new SourceSensorHandler(System.getenv("SOURCE_SENSOR_CONFIG_JSON_FILE"),
					sourceSensorValues);
			sourceResponseSvc = sourceSensorHandler.getSourceResponseSvc();
			igniteSingleton = IgniteSingleton.getInstance( sourceSensorHandler.getRuleConfig() );
			log.info( "------------------------------------------------------------------------------------------------------");
			log.info(">>> Cache name: " + sourceSensorHandler.getRuleConfig().getSourceResponseIgniteCacheName());
			startThreadSubscribe(sourceSensorHandler.getRuleConfig());
			sourceSensorHandler.start();
		} catch(Exception e ) {
			log.error("Exception in before, " + e.getMessage(), e);
		}
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
		IgniteSingleton.reset();
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
	protected List<String> commonThreadSubscribeResults(long maxWaitMsecs) {
		List<String> results = new ArrayList<String>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if( subscribeResults.size() > 0) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
				results.addAll(subscribeResults);
				break;
			}
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
			try {
				// Create new continuous query.
				ContinuousQuery<String, String> qry = new ContinuousQuery<>();

				// Callback that is called locally when update notifications are
				// received.
				qry.setLocalListener(new CacheEntryUpdatedListener<String, String>() {
					@Override
					public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> evts) {
						for (CacheEntryEvent<? extends String, ? extends String> e : evts) {
							log.info("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
							subscribeResults.add(e.getValue());
						}
					}
				});
				qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<String, String>>() {
					@Override
					public CacheEntryEventFilter<String, String> create() {
						return new CacheEntryEventFilter<String, String>() {
							@Override
							public boolean evaluate(CacheEntryEvent<? extends String, ? extends String> e) {
								return true;
							}
						};
					}
				});
				QueryCursor<Cache.Entry<String, String>> cur = igniteSingleton.getCache().query(qry);


			} catch (Exception e) {
				log.error(e.getMessage(),e);
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
