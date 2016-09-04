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

package com.pzybrick.iote2e.ws.igniteavro;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.configuration.IgniteConfiguration;


public class SubAvroWaveThread extends Thread {
	private ConcurrentLinkedQueue<String> messagesToSend;
	private Exception exception = null;
	private boolean shutdown;

	public SubAvroWaveThread(ConcurrentLinkedQueue<String> messagesToSend) {
		this.messagesToSend = messagesToSend;
	}

	@Override
	public void run() {
		Ignite ignite = null;
		try {
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean("config/example-ignite.xml",
					"ignite.cfg");
			ignite = Ignition.getOrStart(igniteConfiguration);
			System.out.println(">>> SubAvroWaveThread started " + this.getId());

			IgniteCache<Integer, String> cache = ignite.getOrCreateCache(PubAvroWave.CACHE_NAME);

			// Create new continuous query.
			ContinuousQuery<Integer, byte[]> qry = new ContinuousQuery<>();
			qry.setLocalListener(new CacheEntryUpdatedListenerAvroWave(messagesToSend) );
            qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<Integer, byte[]>>() {
                @Override public CacheEntryEventFilter<Integer, byte[]> create() {
                    return new CacheEntryEventFilter<Integer, byte[]>() {
                        @Override public boolean evaluate(CacheEntryEvent<? extends Integer, ? extends byte[]> e) {
                            return true;
                        }
                    };
                }
            });
            cache.query(qry);
            while(true) sleep(3000L);

		} catch (InterruptedException e) {
			if( !shutdown ) {
				this.exception = e;
				System.out.println(e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			this.exception = e;
			System.out.println(e);
			e.printStackTrace();
		} finally {
			System.out.println(ignite.cluster());
			ignite.close();
		}
	}
	
	public void shutdown() {
		this.shutdown = true;
		interrupt();
	}
	
	
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}