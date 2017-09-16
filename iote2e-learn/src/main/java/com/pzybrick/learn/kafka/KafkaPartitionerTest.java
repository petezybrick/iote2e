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
package com.pzybrick.learn.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import kafka.utils.VerifiableProperties;


/**
 * The Class KafkaPartitionerTest.
 */
public class KafkaPartitionerTest implements Partitioner {
	
	/**
	 * Instantiates a new kafka partitioner test.
	 */
	public KafkaPartitionerTest() {
		System.out.println("KafkaPartitionerTest");
	}
	
	/**
	 * Instantiates a new kafka partitioner test.
	 *
	 * @param props the props
	 */
	public KafkaPartitionerTest(VerifiableProperties props) {

	}

	/**
	 * Partition.
	 *
	 * @param key the key
	 * @param a_numPartitions the a num partitions
	 * @return the int
	 */
	public int partition(Object key, int a_numPartitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset = stringKey.lastIndexOf('.');
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset + 1)) % a_numPartitions;
		}
		return partition;
	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.common.Configurable#configure(java.util.Map)
	 */
	@Override
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.clients.producer.Partitioner#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.kafka.clients.producer.Partitioner#partition(java.lang.String, java.lang.Object, byte[], java.lang.Object, byte[], org.apache.kafka.common.Cluster)
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		System.out.println("key=" + key + ", value=" + value + ", cluster=" + cluster.toString());
		return 0;
	}

}