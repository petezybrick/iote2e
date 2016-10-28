package com.pzybrick.learn.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import kafka.utils.VerifiableProperties;

public class KafkaPartitionerTest implements Partitioner {
	
	public KafkaPartitionerTest() {
		System.out.println("KafkaPartitionerTest");
	}
	
	public KafkaPartitionerTest(VerifiableProperties props) {

	}

	public int partition(Object key, int a_numPartitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset = stringKey.lastIndexOf('.');
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset + 1)) % a_numPartitions;
		}
		return partition;
	}

	@Override
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		System.out.println("key=" + key + ", value=" + value + ", cluster=" + cluster.toString());
		return 0;
	}

}