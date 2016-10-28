package com.pzybrick.learn.avro2kafkawave;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import com.pzybrick.avro.schema.Wave;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class AvroConsumerWaveThread implements Runnable {
	private KafkaStream<byte[], byte[]> stream;
	private int threadNumber;

	public AvroConsumerWaveThread(KafkaStream<byte[], byte[]> stream, int threadNumber) {
		this.threadNumber = threadNumber;
		this.stream = stream;
	}

	public void run() {
		try {
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			BinaryDecoder binaryDecoder = null;
			Wave waveRead = null;
			DatumReader<Wave> datumReaderWave = new SpecificDatumReader<Wave>(Wave.getClassSchema());

			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
				String key = new String(messageAndMetadata.key());
				binaryDecoder = DecoderFactory.get().binaryDecoder(messageAndMetadata.message(), binaryDecoder);
				waveRead = datumReaderWave.read(waveRead, binaryDecoder);
				// User user = (User)
				// recordInjection.invert(messageAndMetadata.message()).get();
				String summary = ">>> CONSUMER: Thread " + threadNumber + ", topic=" + messageAndMetadata.topic() + ", partition="
						+ messageAndMetadata.partition() + ", key=" + key + ", offset="
						+ messageAndMetadata.offset() + ", timestamp=" + messageAndMetadata.timestamp()
						+ ", timestampType=" + messageAndMetadata.timestampType()
						+ ", waveRead=" + waveRead.toString();
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