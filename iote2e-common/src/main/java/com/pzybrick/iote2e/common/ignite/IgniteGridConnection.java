package com.pzybrick.iote2e.common.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;

public class IgniteGridConnection {
	private static final Logger logger = LogManager.getLogger(ThreadIgniteSubscribe.class);
	private IgniteCache<String, byte[]> cache = null;
	private Ignite ignite = null;
	
	public IgniteGridConnection connect() throws Exception {
		try {
			MasterConfig masterConfig = MasterConfig.getInstance();
			String igniteConfigPath = masterConfig.getIgniteConfigPath();
			if( igniteConfigPath == null ) throw new Exception("Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
			if( !igniteConfigPath.endsWith("/") ) igniteConfigPath = igniteConfigPath + "/";
			String igniteConfigPathNameExt = igniteConfigPath + masterConfig.getIgniteConfigFile();
			logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" +  masterConfig.getIgniteConfigName());
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
					igniteConfigPathNameExt, masterConfig.getIgniteConfigName());
			Ignition.setClientMode(masterConfig.isIgniteClientMode());
			ignite = Ignition.getOrStart(igniteConfiguration);
			cache = ignite.getOrCreateCache(masterConfig.getIgniteCacheName());	
			logger.debug("***************** create cache: cacheName={}, cache={}", masterConfig.getIgniteCacheName(), cache);
			return this;
		} catch( Exception e ) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public IgniteCache<String, byte[]> getCache() {
		return cache;
	}
	public Ignite getIgnite() {
		return ignite;
	}
	public IgniteGridConnection setCache(IgniteCache<String, byte[]> cache) {
		this.cache = cache;
		return this;
	}
	public IgniteGridConnection setIgnite(Ignite ignite) {
		this.ignite = ignite;
		return this;
	}	
}
