package com.pzybrick.learn.avro2kafkawave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.learn.utils.LogTool;

public class AvroProducerWaveMaster {
	private static final Log log = LogFactory.getLog(AvroProducerWaveMaster.class);
	private ExecutorService executor;


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