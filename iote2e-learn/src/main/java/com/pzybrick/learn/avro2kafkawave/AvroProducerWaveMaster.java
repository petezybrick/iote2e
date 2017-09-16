/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.learn.avro2kafkawave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.learn.utils.LogTool;


/**
 * The Class AvroProducerWaveMaster.
 */
public class AvroProducerWaveMaster {
	
	/** The Constant log. */
	private static final Log log = LogFactory.getLog(AvroProducerWaveMaster.class);
	
	/** The executor. */
	private ExecutorService executor;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			LogTool.initConsole();
			AvroProducerWaveMaster avroProducerWaveMaster = new AvroProducerWaveMaster();
			avroProducerWaveMaster.startThreads(1, 10, 100);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	

	/**
	 * Start threads.
	 *
	 * @param numThreads the num threads
	 * @param runSecs the run secs
	 * @param sleepMsecs the sleep msecs
	 */
	public void startThreads(int numThreads, int runSecs, int sleepMsecs ) {
		executor = Executors.newFixedThreadPool(numThreads);
		for (int threadNumber = 0 ; threadNumber<numThreads ; threadNumber++ ) {
			executor.submit(new AvroProducerWaveThread( threadNumber, runSecs, sleepMsecs));
		}
		try {
			Thread.sleep((runSecs+2)*1000L);
		} catch(Exception e) {}
		
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
				System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted during shutdown, exiting uncleanly");
		}

	}

}