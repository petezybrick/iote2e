package com.pzybrick.iote2e.ruleproc.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.config.MasterConfig;

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
				long expiresAt = System.currentTimeMillis() + (10*1000);
				while( expiresAt > System.currentTimeMillis()) {
					if( igniteSingleton.getCache().isClosed() ) break;
					Thread.sleep(250);
				}
				if( !igniteSingleton.getCache().isClosed() ) throw new Exception("Failed to close Ignite cache");
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
		
	public static synchronized IgniteSingleton getInstance( MasterConfig masterConfig ) throws Exception {
		if( igniteSingleton == null ) {
			try {
				String igniteConfigPath = masterConfig.getIgniteConfigPath();
				if( igniteConfigPath == null ) throw new Exception("Required MasterConfig value igniteConfigPath is not set, try setting to location of ignite-iote2e.xml");
				if( !igniteConfigPath.endsWith("/") ) igniteConfigPath = igniteConfigPath + "/";
				String igniteConfigPathNameExt = igniteConfigPath + masterConfig.getIgniteConfigFile();
				logger.info("Initializing Ignite, config file=" + igniteConfigPathNameExt + ", config name=" +  masterConfig.getIgniteConfigName());
				IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean(
						igniteConfigPathNameExt, masterConfig.getIgniteConfigName());
				Ignition.setClientMode(masterConfig.isIgniteClientMode());
				Ignite ignite = Ignition.start(igniteConfiguration);
				if (logger.isDebugEnabled()) logger.debug(ignite.toString());
				IgniteCache<String, byte[]> cache = ignite.getOrCreateCache(masterConfig.getIgniteCacheName());
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
