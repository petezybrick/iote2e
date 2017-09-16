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
package com.pzybrick.iote2e.stream.spark;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.router.RouterOmh;
import com.pzybrick.iote2e.stream.router.RouterOmhImpl;

import consumer.kafka.MessageAndMetadata;


/**
 * The Class OmhSparkProcessor.
 */
public class OmhSparkProcessor implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2989374902129650863L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(OmhSparkProcessor.class);
	
	/** The master config. */
	private MasterConfig masterConfig;

	
	/**
	 * Instantiates a new omh spark processor.
	 *
	 * @param masterConfig the master config
	 */
	public OmhSparkProcessor(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
	}

    /**
     * Process omh RDD.
     *
     * @param rdd the rdd
     */
    public void processOmhRDD(JavaRDD<MessageAndMetadata> rdd) {
        logger.debug("Processing OmhRDD {}, rdd={}", Thread.currentThread().getId(), rdd.toString() );
        rdd.foreachPartition(partition -> processPartition(partition));
    }

    /**
     * Process partition.
     *
     * @param partition the partition
     * @throws Exception the exception
     */
    public void processPartition(Iterator<MessageAndMetadata> partition) throws Exception {
        logger.debug("Starting to process partition");
        try (RouterOmh routerOmh = new RouterOmhImpl(masterConfig); ) {
            partition.forEachRemaining(messageAndMetadata -> processOmhRecord(messageAndMetadata, routerOmh));
        } catch(Exception e ) {
            logger.error(e.getMessage(),e);
        }
        logger.debug("Finished processing partition");
    }

    /**
     * Process omh record.
     *
     * @param messageAndMetadata the message and metadata
     * @param routerOmh the router omh
     */
    public void processOmhRecord(MessageAndMetadata messageAndMetadata, RouterOmh routerOmh) {
        try {
        	ByteBuffer byteBuffer = ByteBuffer.wrap(messageAndMetadata.getPayload() );
        	logger.debug("Adding omh: len={}", messageAndMetadata.getPayload().length );
            routerOmh.add(byteBuffer);
        } catch (Exception e) {
            logger.error("Error processing record : " + e.getMessage(), e);
        }
    }


}
