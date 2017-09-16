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
package com.pzybrick.learn.avro2kafka;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;

import com.pzybrick.avro.schema.User;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;


/**
 * The Class AvroConsumerThread.
 */
public class AvroConsumerThread implements Runnable {
	
	/** The m stream. */
	private KafkaStream m_stream;
	
	/** The m thread number. */
	private int m_threadNumber;

	/**
	 * Instantiates a new avro consumer thread.
	 *
	 * @param a_stream the a stream
	 * @param a_threadNumber the a thread number
	 */
	public AvroConsumerThread(KafkaStream a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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

	/**
	 * Generic record to user.
	 *
	 * @param genericRecord the generic record
	 * @return the user
	 */
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