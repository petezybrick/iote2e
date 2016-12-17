package com.pzybrick.iote2e.ruleproc.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;

public class IgniteSingleton {
	private static final Logger logger = LogManager.getLogger(IgniteSingleton.class);
	private static IgniteSingleton igniteSingleton;
	private Ignite ignite;
	private IgniteCache<String, byte[]> cache;
	
	private IgniteSingleton(Ignite ignite, IgniteCache<String, byte[]> cache) {
		this.ignite = ignite;
		this.cache = cache;
	}
	
	public static synchronized void reset( ) throws Exception {
		if( igniteSingleton != null ) {
			try {
				igniteSingleton.getCache().close();
			} catch( Exception e ) {
				logger.warn(e.getMessage(),e);
			}			
			try {
				igniteSingleton.getIgnite().close();
			} catch( Exception e ) {
				logger.warn(e.getMessage(),e);
			}
			igniteSingleton = null;
		}
		
	}
		
	public static synchronized IgniteSingleton getInstance( RuleConfig ruleConfig ) throws Exception {
		if( igniteSingleton == null ) {
			try {
				logger.info("Initializing Ignite, config file=" + ruleConfig.getSourceResponseIgniteConfigFile() + ", config name=" +  ruleConfig.getSourceResponseIgniteConfigName());
				IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
						ruleConfig.getSourceResponseIgniteConfigFile(), ruleConfig.getSourceResponseIgniteConfigName());
				Ignition.setClientMode(ruleConfig.isIgniteClientMode());
				Ignite ignite = Ignition.start(igniteConfiguration);
				if (logger.isDebugEnabled()) logger.debug(ignite.toString());
				IgniteCache<String, byte[]> cache = ignite.getOrCreateCache(ruleConfig.getSourceResponseIgniteCacheName());
				igniteSingleton = new IgniteSingleton( ignite, cache);
			} catch (Throwable t ) {
				logger.error("Ignite initialization failure", t);
				throw t;
			}
		}
		return igniteSingleton;
	}

	public Ignite getIgnite() {
		return ignite;
	}

	public IgniteCache<String, byte[]> getCache() {
		return cache;
	}
	
}
