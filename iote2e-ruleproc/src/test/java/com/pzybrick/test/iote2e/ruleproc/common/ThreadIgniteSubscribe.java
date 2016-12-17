package com.pzybrick.test.iote2e.ruleproc.common;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.ignite.IgniteSingleton;
import com.pzybrick.iote2e.ruleproc.ignite.Iote2eIgniteCacheEntryEventFilter;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;


public class ThreadIgniteSubscribe extends Thread {
	private static final Logger logger = LogManager.getLogger(ThreadIgniteSubscribe.class);
	private String igniteFilterKey;
	private IgniteSingleton igniteSingleton;
	private boolean shutdown;
	private boolean subscribeUp;
	private ConcurrentLinkedQueue<byte[]> subscribeResults;

	public ThreadIgniteSubscribe() {
	}

	public static ThreadIgniteSubscribe startThreadSubscribe(RuleConfig ruleConfig, String igniteFilterKey,
			IgniteSingleton igniteSingleton, ConcurrentLinkedQueue<byte[]> subscribeResults ) throws Exception {
		ThreadIgniteSubscribe threadIgniteSubscribe = new ThreadIgniteSubscribe().setIgniteFilterKey(igniteFilterKey).setIgniteSingleton(igniteSingleton).setSubscribeResults(subscribeResults) ;
		threadIgniteSubscribe.start();
		long timeoutAt = System.currentTimeMillis() + 10000L;
		while (System.currentTimeMillis() < timeoutAt && !threadIgniteSubscribe.isSubscribeUp() ) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		if (!threadIgniteSubscribe.isSubscribeUp())
			throw new Exception("Timeout starting ThreadSubscribe");
		return threadIgniteSubscribe;
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
						logger.info("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
						subscribeResults.add(e.getValue());
					}
				}
			});

			Iote2eIgniteCacheEntryEventFilter<String,byte[]> sourceSensorCacheEntryEventFilter = 
					new Iote2eIgniteCacheEntryEventFilter<String, byte[]>(igniteFilterKey);
			qry.setRemoteFilterFactory(new Factory<Iote2eIgniteCacheEntryEventFilter<String, byte[]>>() {
				@Override
				public Iote2eIgniteCacheEntryEventFilter<String, byte[]> create() {
					return sourceSensorCacheEntryEventFilter;
					//return new SourceSensorCacheEntryEventFilter<String, String>(remoteFilterKey);
				}
			});
			QueryCursor<Cache.Entry<String, byte[]>> cur = igniteSingleton.getCache().query(qry);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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

	public void shutdown() {
		this.shutdown = true;
		interrupt();
	}

	public String getIgniteFilterKey() {
		return igniteFilterKey;
	}

	public IgniteSingleton getIgniteSingleton() {
		return igniteSingleton;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public boolean isSubscribeUp() {
		return subscribeUp;
	}

	public ConcurrentLinkedQueue<byte[]> getSubscribeResults() {
		return subscribeResults;
	}

	public ThreadIgniteSubscribe setIgniteFilterKey(String igniteFilterKey) {
		this.igniteFilterKey = igniteFilterKey;
		return this;
	}

	public ThreadIgniteSubscribe setIgniteSingleton(IgniteSingleton igniteSingleton) {
		this.igniteSingleton = igniteSingleton;
		return this;
	}

	public ThreadIgniteSubscribe setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
		return this;
	}

	public ThreadIgniteSubscribe setSubscribeUp(boolean subscribeUp) {
		this.subscribeUp = subscribeUp;
		return this;
	}

	public ThreadIgniteSubscribe setSubscribeResults(ConcurrentLinkedQueue<byte[]> subscribeResults) {
		this.subscribeResults = subscribeResults;
		return this;
	}
}
