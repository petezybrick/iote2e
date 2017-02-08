package com.pzybrick.iote2e.ws.igniteavro;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;
import com.pzybrick.iote2e.schema.util.Iote2eResultReuseItem;

public class CacheEntryUpdatedListenerIote2eResult implements CacheEntryUpdatedListener<Integer, byte[]> {
	private static final Logger logger = LogManager.getLogger(CacheEntryUpdatedListenerIote2eResult.class);
	private ConcurrentLinkedQueue<Iote2eResult> iote2eResults = null;
	private Iote2eResultReuseItem iote2eResultReuseItem = new Iote2eResultReuseItem();


	public CacheEntryUpdatedListenerIote2eResult() {
	}

	public CacheEntryUpdatedListenerIote2eResult(ConcurrentLinkedQueue<Iote2eResult> iote2eResults) {
		this();
		this.iote2eResults = iote2eResults;
	}
	
	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends byte[]>> events) {
		for (CacheEntryEvent<? extends Integer, ? extends byte[]> event : events) {
			try {
				if( iote2eResults != null ) {
					iote2eResults.add( iote2eResultReuseItem.fromByteArray(event.getValue()) );
				}
				
			} catch( Exception e ) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	
	public ConcurrentLinkedQueue<Iote2eResult> getIote2eResults() {
		return iote2eResults;
	}

	public void setIote2eResults(ConcurrentLinkedQueue<Iote2eResult> iote2eResults) {
		this.iote2eResults = iote2eResults;
	}


}
