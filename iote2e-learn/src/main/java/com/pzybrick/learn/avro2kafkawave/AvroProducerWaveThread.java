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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.UUID;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.avro.schema.Wave;


/**
 * The Class AvroProducerWaveThread.
 */
public class AvroProducerWaveThread extends Thread {
    
    /** The Constant ISO8601_UTC. */
    private static final SimpleDateFormat ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    /** The thread number. */
    private int threadNumber;

	/** The run secs. */
	private int runSecs;
    
    /** The sleep msecs. */
    private int sleepMsecs;
    
    /** The source uuid. */
    private String sourceUuid;
    
    /** The run exception. */
    private Exception runException;
    
    /**
     * Instantiates a new avro producer wave thread.
     *
     * @param threadNumber the thread number
     * @param runSecs the run secs
     * @param sleepMsecs the sleep msecs
     */
    public AvroProducerWaveThread( int threadNumber, int runSecs, int sleepMsecs ) {
    	this.threadNumber = threadNumber;
    	this.runSecs = runSecs;
    	this.sourceUuid = UUID.randomUUID().toString();
    	this.sleepMsecs = sleepMsecs;
    }


	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			Properties props = new Properties();
			props.put("bootstrap.servers", "hp-lt-ubuntu-1:9092");
			props.put("producer.type", "sync");
			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
			props.put("request.required.acks", "1");
			props.put("group.id", "group1");
			props.put("zookeeper.connect", "hp-lt-ubuntu-1:2181");

			KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
			DatumWriter<Wave> datumWriterWave = new SpecificDatumWriter<Wave>(Wave.getClassSchema());

			BinaryEncoder binaryEncoder = null;
			long currTime = System.currentTimeMillis();
			long endTime = currTime + (runSecs*1000L);
			boolean isUp = true;
			double waveValue = roundDblTwoDec(-1.0);
			while( currTime <= endTime ) {
				Wave wave = Wave.newBuilder()
						.setSourceUuid(sourceUuid)
						.setWaveType("triangle")
						.setTsEpoch(currTime)
						.setWaveValue(waveValue)
						.build();
				// System.out.println(">>> PRODUCER: " + wave.toString());
				String key = String.valueOf(currTime);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] bytes = null;
				try {
					binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
					datumWriterWave.write(wave, binaryEncoder);
					binaryEncoder.flush();
					bytes = baos.toByteArray();
					ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>("pzrepltopic1", key, bytes);
					producer.send(data);
				} finally {
					baos.close();
				}
				sleep( sleepMsecs );
				currTime = System.currentTimeMillis();
				//System.out.println(currTime + ", " + endTime );
				if( isUp ) {
					waveValue = roundDblTwoDec(waveValue + .1);
					if(waveValue == 1.0 ) isUp = false;
				} else {
					waveValue = roundDblTwoDec(waveValue - .1);
					if(waveValue == -1.0 ) isUp = true;
				}
			}

			producer.close();
			
			
		} catch( Exception e ) {
			this.runException = e;
		} finally {
			
		}
	}
	
	/**
	 * Round dbl two dec.
	 *
	 * @param dbl the dbl
	 * @return the double
	 */
	public static double roundDblTwoDec( double dbl ) {
		dbl = dbl*100;
		dbl = Math.round(dbl);
		dbl = dbl /100;
		return dbl;
	}


	/**
	 * Gets the run exception.
	 *
	 * @return the run exception
	 */
	public Exception getRunException() {
		return runException;
	}


	/**
	 * Sets the run exception.
	 *
	 * @param runException the new run exception
	 */
	public void setRunException(Exception runException) {
		this.runException = runException;
	}

    /**
     * Gets the thread number.
     *
     * @return the thread number
     */
    public int getThreadNumber() {
		return threadNumber;
	}


	/**
	 * Sets the thread number.
	 *
	 * @param threadNumber the new thread number
	 */
	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}


}
