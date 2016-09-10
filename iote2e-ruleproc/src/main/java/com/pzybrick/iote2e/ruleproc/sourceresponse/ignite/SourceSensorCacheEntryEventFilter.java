package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;

public class SourceSensorCacheEntryEventFilter<K extends String, V extends String> implements CacheEntryEventFilter<String, String> {
	private String remoteKey;
	
	public SourceSensorCacheEntryEventFilter( String remoteKey ) {
		this.remoteKey = remoteKey;
	}

	@Override
	public boolean evaluate(CacheEntryEvent<? extends String, ? extends String> event) throws CacheEntryListenerException {
		if( event.getKey().startsWith(remoteKey)) return true; 
		else return false;
	}
}

