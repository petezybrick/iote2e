package com.pzybrick.learn.avro2kafka;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;

import com.pzybrick.avro.schema.User;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class AvroConsumerThread implements Runnable {
	private KafkaStream m_stream;
	private int m_threadNumber;

	public AvroConsumerThread(KafkaStream a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	public void run() {
		try {
			ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
			Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(User.getClassSchema());

			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
				String key = new String(messageAndMetadata.key());
				User user = genericRecordToUser(recordInjection.invert(messageAndMetadata.message()).get());
				// User user = (User)
				// recordInjection.invert(messageAndMetadata.message()).get();
				String summary = "Thread " + m_threadNumber + ", topic=" + messageAndMetadata.topic() + ", partition="
						+ messageAndMetadata.partition() + ", key=" + key + ", user=" + user.toString() + ", offset="
						+ messageAndMetadata.offset() + ", timestamp=" + messageAndMetadata.timestamp()
						+ ", timestampType=" + messageAndMetadata.timestampType();
				System.out.println(summary);
			}
			System.out.println("Shutting down Thread: " + m_threadNumber);
		} catch (Exception e) {
			System.out.println("Exception in thread "+m_threadNumber);
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static User genericRecordToUser(GenericRecord genericRecord) {
		User user = new User();
//		Utf8 favoriteColor = (genericRecord.get("favoriteColor") != null ) ? (String) genericRecord.get("favoriteColor") : null;
//		int favoriteNumber = (genericRecord.get("favoriteNumber") != null ) ? (int) genericRecord.get("favoriteNumber") : null;
//		String name = (genericRecord.get("name") != null ) ? (String) genericRecord.get("name") : null;
		user.setFavoriteColor((Utf8) genericRecord.get("favorite_color"));
		user.setFavoriteNumber((Integer) genericRecord.get("favorite_number"));
		user.setName((Utf8) genericRecord.get("name"));
		return user;
	}
}