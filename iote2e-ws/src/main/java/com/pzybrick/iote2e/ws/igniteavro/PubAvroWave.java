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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.pzybrick.avro.schema.Wave;
import com.pzybrick.iote2e.ws.ignite.ExampleNodeStartup;
import com.pzybrick.learnjetty.utils.EndToEndConstants.WaveType;

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
public class PubAvroWave {
	/** Cache name. */
	public static final String CACHE_NAME = "cache_wave";

	public static void main(String[] args) throws Exception {
		PubAvroWave pubAvroWave = new PubAvroWave();
		pubAvroWave.process();
	}

	public PubAvroWave() {
	}

	public void process() throws Exception {
		Ignite ignite = null;
		try {
			System.out.println(CACHE_NAME);
			IgniteConfiguration igniteConfiguration = Ignition.loadSpringBean("config/example-ignite.xml",
					"ignite.cfg");
			ignite = Ignition.getOrStart(igniteConfiguration);
			System.out.println(ignite.toString());
			System.out.println();
			System.out.println(">>> PubAvroWave started.");

			// Auto-close cache at the end of the example.
			try {
				IgniteCache<Integer, byte[]> cache = ignite.getOrCreateCache(CACHE_NAME);
				new PublishTestMessage(cache).run();
				this.wait();
			} finally {
				// Distributed cache could be removed from cluster only by
				// #destroyCache() call.
				ignite.destroyCache(CACHE_NAME);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			System.out.println("in finally");
			System.out.println(ignite.cluster());
			ignite.close();
		}
	}

	private static class PublishTestMessage extends Thread {
		private WaveType waveType;
		private static final int NUM_TEST_SENSORS = 7;
		private IgniteCache<Integer, byte[]> cache;
		private List<String> testUuids;
		private boolean isUp = true;

		public PublishTestMessage(IgniteCache<Integer, byte[]> cache) {
			this.cache = cache;
			createTestUuids();
			waveType = WaveType.Triangle;
		}

		@Override
		public void run() {
			try {
				DatumWriter<Wave> datumWriterWave = new SpecificDatumWriter<Wave>(Wave.getClassSchema());
				BinaryEncoder binaryEncoder = null;
				long currTime = System.currentTimeMillis();

				double waveValue = roundDblTwoDec(0.0);
				int key = 1;
				while (true) {
					currTime = System.currentTimeMillis();
					for (String testUuid : testUuids) {
						Wave wave = Wave.newBuilder().setSourceUuid(testUuid).setWaveType("triangle")
								.setTsEpoch(currTime).setWaveValue(waveValue).build();
						//System.out.println(">>> Pub: " + wave.toString());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] bytes = null;
						try {
							binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
							datumWriterWave.write(wave, binaryEncoder);
							binaryEncoder.flush();
							bytes = baos.toByteArray();
							cache.put(key++, bytes);
						} catch (Exception e) {
							System.out.println(e);
							e.printStackTrace();
							break;
						} finally {
							baos.close();
						}
					}
					sleep(50L);
					waveValue = nextWaveValue(waveValue);
				}

			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		private double nextWaveValue( double waveValue) {
			final double one = roundDblTwoDec(1);
			if( waveType == WaveType.Triangle ) {
				if( isUp ) {
					waveValue = roundDblTwoDec(waveValue + .1);
					if(waveValue == 2.0 ) isUp = false;
				} else {
					waveValue = roundDblTwoDec(waveValue - .1);
					if(waveValue == 0 ) isUp = true;
				}
			} else if( waveType == WaveType.Heartbeat ) {
				waveValue = one;
			}
			return waveValue;
		}
		
		private void createTestUuids() {
			testUuids = new ArrayList<String>();
			for( int i=0 ; i<NUM_TEST_SENSORS ; i++) {
				// 42f4176d-ac4b-462a-a2f3-50780e0104ea
				testUuids.add( "00000000-0000-0000-" + String.format("%012d", i));
			}
		}

	}

	public static double roundDblTwoDec(double dbl) {
		dbl = dbl * 100;
		dbl = Math.round(dbl);
		dbl = dbl / 100;
		return dbl;
	}
}