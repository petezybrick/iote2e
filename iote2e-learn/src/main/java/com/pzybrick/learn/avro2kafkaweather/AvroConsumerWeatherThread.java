package com.pzybrick.learn.avro2kafkaweather;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.avro.schema.Weather;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class AvroConsumerWeatherThread implements Runnable {
	private KafkaStream<byte[], byte[]> stream;
	private int threadNumber;

	public AvroConsumerWeatherThread(KafkaStream<byte[], byte[]> stream, int threadNumber) {
		this.threadNumber = threadNumber;
		this.stream = stream;
	}

	public void run() {
		try {
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			BinaryDecoder binaryDecoder = null;
			Weather weatherRead = null;
			DatumReader<Weather> datumReaderWeather = new SpecificDatumReader<Weather>(Weather.getClassSchema());

			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
				String key = new String(messageAndMetadata.key());
				binaryDecoder = DecoderFactory.get().binaryDecoder(messageAndMetadata.message(), binaryDecoder);
				weatherRead = datumReaderWeather.read(weatherRead, binaryDecoder);
				// User user = (User)
				// recordInjection.invert(messageAndMetadata.message()).get();
				String summary = "Thread " + threadNumber + ", topic=" + messageAndMetadata.topic() + ", partition="
						+ messageAndMetadata.partition() + ", key=" + key + ", offset="
						+ messageAndMetadata.offset() + ", timestamp=" + messageAndMetadata.timestamp()
						+ ", timestampType=" + messageAndMetadata.timestampType()
						+ ", weatherRead=" + weatherRead.toString();
				System.out.println(summary);
			}
			System.out.println("Shutting down Thread: " + threadNumber);
		} catch (Exception e) {
			System.out.println("Exception in thread "+threadNumber);
			System.out.println(e);
			e.printStackTrace();
		}
	}

}