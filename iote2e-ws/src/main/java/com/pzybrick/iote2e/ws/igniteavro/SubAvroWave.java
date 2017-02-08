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

import java.util.List;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

import com.pzybrick.avro.schema.Wave;

/**
 * This examples demonstrates continuous query API.
 * <p>
 * Remote nodes should always be started with special configuration file which
 * enables P2P class loading: {@code 'ignite.{sh|bat}
 * examples/config/example-ignite.xml'}.
 * <p>
 * Alternatively you can run {@link ExampleNodeStartup} in another JVM which
 * will start node with {@code examples/config/example-ignite.xml}
 * configuration.
 */
public class SubAvroWave {

	public static void main(String[] args) throws Exception {
		SubAvroWave subAvroWave = new SubAvroWave();
		subAvroWave.process();
	}

	public void process() throws Exception {
		Ignite ignite = null;
		try {
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean("config/example-ignite.xml",
					"ignite.cfg");
			ignite = Ignition.getOrStart(igniteConfiguration);
			System.out.println(ignite.toString());
			System.out.println();
			System.out.println(">>> SubAvroWave started.");

			// Auto-close cache at the end of the example.
			try {

				IgniteCache<Integer, String> cache = ignite.getOrCreateCache(PubAvroWave.CACHE_NAME);

				// Create new continuous query.
				ContinuousQuery<Integer, byte[]> qry = new ContinuousQuery<>();
				qry.setLocalListener(new CacheEntryUpdatedListenerIote2eResult() );
                qry.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<Integer, byte[]>>() {
                    @Override public CacheEntryEventFilter<Integer, byte[]> create() {
                        return new CacheEntryEventFilter<Integer, byte[]>() {
                            @Override public boolean evaluate(CacheEntryEvent<? extends Integer, ? extends byte[]> e) {
                                return true;
                            }
                        };
                    }
                });
                QueryCursor<Cache.Entry<Integer, byte[]>> cur = cache.query(qry);
				System.out.println("before sleep");
				try { Thread.sleep(600*1000);} catch(Exception e){System.out.println(e);}

			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
				throw e;

			} finally {

			}
			

		} catch (Exception e) {
			throw e;
		} finally {
			System.out.println("in finally");
			System.out.println(ignite.cluster());
			ignite.close();
		}
	}
}