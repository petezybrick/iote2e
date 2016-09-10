package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;

public class IgniteSingleton {
	private static final Log log = LogFactory.getLog(IgniteSingleton.class);
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
				log.warn(e.getMessage(),e);
			}			
			try {
				igniteSingleton.getIgnite().close();
			} catch( Exception e ) {
				log.warn(e.getMessage(),e);
			}
			igniteSingleton = null;
		}
		
	}
		
	public static synchronized IgniteSingleton getInstance( RuleConfig ruleConfig ) throws Exception {
		if( igniteSingleton == null ) {
			try {
				log.info("Initializing Ignite, config file=" + ruleConfig.getSourceResponseIgniteConfigFile() + ", config name=" +  ruleConfig.getSourceResponseIgniteConfigName());
				IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
						ruleConfig.getSourceResponseIgniteConfigFile(), ruleConfig.getSourceResponseIgniteConfigName());
				Ignition.setClientMode(true);
				Ignite ignite = Ignition.start(igniteConfiguration);
				if (log.isDebugEnabled()) log.debug(ignite.toString());
				IgniteCache<String, byte[]> cache = ignite.getOrCreateCache(ruleConfig.getSourceResponseIgniteCacheName());
				igniteSingleton = new IgniteSingleton( ignite, cache);
			} catch (Exception e) {
				log.error("Ignite initialization failure", e);
				throw e;
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
