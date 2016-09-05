/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pzybrick.iote2e.ruleproc.sourceresponse.ignite;

import java.util.List;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

/**
 * This examples demonstrates continuous query API.
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ignite.{sh|bat} examples/config/example-ignite.xml'}.
 * <p>
 * Alternatively you can run {@link ExampleNodeStartup} in another JVM which will
 * start node with {@code examples/config/example-ignite.xml} configuration.
 */
public class PubContinuousQuery {
    /** Cache name. */
    private static final String CACHE_NAME = "pzcache";

    public static void main(String[] args) throws Exception {
    	PubContinuousQuery pubContinuousQuery = new PubContinuousQuery();
    	pubContinuousQuery.process();
    }

    public PubContinuousQuery() {
    }
    
    public void process() throws Exception {
    	Ignite ignite = null;
        try {
        	System.out.println(CACHE_NAME);
        	IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean("config/example-default.xml", "ignite.cfg");
			Ignition.setClientMode(true);
			ignite = Ignition.start(igniteConfiguration);
			//ignite = Ignition.getOrStart(igniteConfiguration);
        	System.out.println(ignite.toString());
            System.out.println();
            System.out.println(">>> PubContinuousQuery started.");

            // Auto-close cache at the end of the example.
            try {
            	IgniteCache<Integer, String> cache = ignite.getOrCreateCache(CACHE_NAME);
            	new PublishTestMessage( cache ).run();
            	this.wait();
            }
            finally {
                // Distributed cache could be removed from cluster only by #destroyCache() call.
                ignite.destroyCache(CACHE_NAME);
            }
        } catch( Exception e ) {
        	throw e;
        } finally {
        	System.out.println("in finally");
        	System.out.println(ignite.cluster());
        	ignite.close();
        }
    }
    
    private static class PublishTestMessage extends Thread {
    	private IgniteCache<Integer, String> cache;
    	
    	public PublishTestMessage( IgniteCache<Integer, String> cache ) {
    		this.cache = cache;
    	}

		@Override
		public void run() {
			try {
	            for (int i = 0;  ; i++) {
	            	//System.out.println(Integer.toString(i));
	                cache.put(i, Integer.toString(i));
	                sleep( 100 );
	            }
			} catch( Exception e ) {
				System.out.println(e);
			}
		}
    	
    }
}