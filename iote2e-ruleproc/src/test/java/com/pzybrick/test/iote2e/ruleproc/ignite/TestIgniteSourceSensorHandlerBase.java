package com.pzybrick.test.iote2e.ruleproc.ignite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.junit.After;
import org.junit.Before;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.sourceresponse.LoginSourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.sourceresponse.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.sourceresponse.ignite.LoginSourceSensorCacheEntryEventFilter;
import com.pzybrick.iote2e.ruleproc.sourcesensor.LoginSourceSensorHandler;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.schema.avro.ActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginActuatorResponse;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;
import com.pzybrick.iote2e.schema.util.AvroSchemaUtils;
import com.pzybrick.iote2e.schema.util.LoginActuatorResponseFromByteArrayReuseItem;

public class TestIgniteSourceSensorHandlerBase {
	private static final Log log = LogFactory.getLog(TestIgniteSourceSensorHandlerBase.class);
	protected ConcurrentLinkedQueue<LoginSourceSensorValue> loginSourceSensorValues;
	protected ConcurrentLinkedQueue<byte[]> subscribeResults;
	protected LoginSourceSensorHandler loginSourceSensorHandler;
	protected LoginSourceResponseSvc loginSourceResponseSvc;
	protected ThreadSubscribe threadSubscribe;
	protected boolean subscribeUp;
	protected IgniteSingleton igniteSingleton = null;
	protected Gson gson;
	protected LoginActuatorResponseFromByteArrayReuseItem loginActuatorResponseFromByteArrayReuseItem;
//	protected BinaryDecoder binaryDecoder = null;
//	private DatumReader<ActuatorResponse> datumReaderActuatorResponse = null;

	@Before
	public void before() throws Exception {
		try {
			gson = new GsonBuilder().create();
			loginActuatorResponseFromByteArrayReuseItem = new LoginActuatorResponseFromByteArrayReuseItem();
			subscribeResults = new ConcurrentLinkedQueue<byte[]>();
			loginSourceSensorValues = new ConcurrentLinkedQueue<LoginSourceSensorValue>();
			loginSourceSensorHandler = new LoginSourceSensorHandler(System.getenv("LOGIN_SOURCE_SENSOR_CONFIG_JSON_FILE"),
					loginSourceSensorValues);
			loginSourceResponseSvc = loginSourceSensorHandler.getLoginSourceResponseSvc();
			igniteSingleton = IgniteSingleton.getInstance(loginSourceSensorHandler.getRuleConfig());
			log.info(
					"------------------------------------------------------------------------------------------------------");
			log.info(">>> Cache name: " + loginSourceSensorHandler.getRuleConfig().getSourceResponseIgniteCacheName());
			loginSourceSensorHandler.start();
		} catch (Exception e) {
			log.error("Exception in before, " + e.getMessage(), e);
		}
	}

	@After
	public void after() throws Exception {
		while (!loginSourceSensorValues.isEmpty()) {
			try {
				Thread.sleep(2000L);
			} catch (Exception e) {
			}
		}
		loginSourceSensorHandler.shutdown();
		loginSourceSensorHandler.join();
		threadSubscribe.shutdown();
		threadSubscribe.join();
		IgniteSingleton.reset();
	}

	protected void commonRun(String loginUuid, String sourceUuid, String sensorName, String sensorValue, String igniteFilterKey) {
		log.info( String.format("loginUuid=%s, sourceUuid=%s, sensorName=%s, sensorValue=%s", loginUuid, sourceUuid, sensorName, sensorValue));
		try {
			startThreadSubscribe(loginSourceSensorHandler.getRuleConfig(), igniteFilterKey);
			LoginSourceSensorValue loginSourceSensorValue = new LoginSourceSensorValue(loginUuid, sourceUuid, sensorName, sensorValue);
			loginSourceSensorHandler.putLoginSourceSensorValue(loginSourceSensorValue);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	protected List<LoginActuatorResponse> commonThreadSubscribeGetLoginActuatorResponses(long maxWaitMsecs) throws Exception {
		List<LoginActuatorResponse> loginActuatorResponses = new ArrayList<LoginActuatorResponse>();
		long wakeupAt = System.currentTimeMillis() + maxWaitMsecs;
		while (System.currentTimeMillis() < wakeupAt) {
			if (subscribeResults.size() > 0) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
				for( byte[] bytes : subscribeResults ) {
					try {
						AvroSchemaUtils.loginActuatorResponseFromByteArray(loginActuatorResponseFromByteArrayReuseItem, bytes);
						loginActuatorResponses.add( loginActuatorResponseFromByteArrayReuseItem.getLoginActuatorResponse() );
					} catch( IOException e ) {
						log.error(e.getMessage(),e);
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
		return loginActuatorResponses;
	}

	private void startThreadSubscribe(RuleConfig ruleConfig, String igniteFilterKey) throws Exception {
		threadSubscribe = new ThreadSubscribe(ruleConfig, igniteFilterKey);
		threadSubscribe.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !subscribeUp) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (!subscribeUp)
			throw new Exception("Timeout starting ThreadSubscribe");
	}

	private class ThreadSubscribe extends Thread {
		private RuleConfig ruleConfig;
		private String remoteFilterKey;
		private boolean shutdown;

		public ThreadSubscribe(RuleConfig ruleConfig, String remoteFilterKey) {
			this.ruleConfig = ruleConfig;
			this.remoteFilterKey = remoteFilterKey;
		}

		public void shutdown() {
			this.shutdown = true;
			interrupt();
		}

		@Override
		public void run() {
			try {
				// Create new continuous query.
				ContinuousQuery<String,  byte[]> qry = new ContinuousQuery<>();

				// Callback that is called locally when update notifications are
				// received.
				qry.setLocalListener(new CacheEntryUpdatedListener<String,  byte[]>() {
					@Override
					public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends  byte[]>> evts) {
						for (CacheEntryEvent<? extends String, ? extends  byte[]> e : evts) {
							log.info("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
							subscribeResults.add(e.getValue());
						}
					}
				});

				LoginSourceSensorCacheEntryEventFilter<String,byte[]> sourceSensorCacheEntryEventFilter = 
						new LoginSourceSensorCacheEntryEventFilter<String, byte[]>(remoteFilterKey);
				qry.setRemoteFilterFactory(new Factory<LoginSourceSensorCacheEntryEventFilter<String, byte[]>>() {
					@Override
					public LoginSourceSensorCacheEntryEventFilter<String, byte[]> create() {
						return sourceSensorCacheEntryEventFilter;
						//return new SourceSensorCacheEntryEventFilter<String, String>(remoteFilterKey);
					}
				});

//				qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<String, String>>() {
//					@Override
//					public CacheEntryEventFilter<String, String> create() {
//						return new CacheEntryEventFilter<String, String>() {
//							@Override
//							public boolean evaluate(CacheEntryEvent<? extends String, ? extends String> e) {
//								final String remoteFilter = "lo1so1|lo1so1se1|";
//								if (e.getKey().startsWith(remoteFilter))
//									return true;
//								else
//									return false;
//								// return false;
//							}
//						};
//					}
//				});
				
				QueryCursor<Cache.Entry<String, byte[]>> cur = igniteSingleton.getCache().query(qry);

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return;

			}
			subscribeUp = true;
			while (true) {
				if (shutdown)
					break;
				try {
					sleep(60 * 60 * 5);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
