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

public class AvroProducerWaveThread extends Thread {
    private static final SimpleDateFormat ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private int threadNumber;

	private int runSecs;
    private int sleepMsecs;
    private String sourceUuid;
    private Exception runException;
    
    public AvroProducerWaveThread( int threadNumber, int runSecs, int sleepMsecs ) {
    	this.threadNumber = threadNumber;
    	this.runSecs = runSecs;
    	this.sourceUuid = UUID.randomUUID().toString();
    	this.sleepMsecs = sleepMsecs;
    }


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
	
	public static double roundDblTwoDec( double dbl ) {
		dbl = dbl*100;
		dbl = Math.round(dbl);
		dbl = dbl /100;
		return dbl;
	}


	public Exception getRunException() {
		return runException;
	}


	public void setRunException(Exception runException) {
		this.runException = runException;
	}

    public int getThreadNumber() {
		return threadNumber;
	}


	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}


}
