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
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.util.Iote2eRequestReuseItem;
import com.pzybrick.iote2e.stream.router.Router;
import com.pzybrick.iote2e.stream.router.RouterIote2eRequestImpl;

import consumer.kafka.MessageAndMetadata;


/**
 * The Class Iote2eRequestSparkProcessor.
 */
public class Iote2eRequestSparkProcessor implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2989374902129650863L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eRequestSparkProcessor.class);
	
	/** The iote 2 e request reuse item. */
	private Iote2eRequestReuseItem iote2eRequestReuseItem;
	
	/** The master config. */
	private MasterConfig masterConfig;

	
	/**
	 * Instantiates a new iote 2 e request spark processor.
	 *
	 * @param masterConfig the master config
	 */
	public Iote2eRequestSparkProcessor(MasterConfig masterConfig) {
		this.iote2eRequestReuseItem = new Iote2eRequestReuseItem();
		this.masterConfig = masterConfig;
	}

    /**
     * Process iote 2 e request RDD.
     *
     * @param rdd the rdd
     */
    public void processIote2eRequestRDD(JavaRDD<MessageAndMetadata> rdd) {
        logger.debug("Processing Iote2eRequestRDD {}, rdd={}", Thread.currentThread().getId(), rdd.toString() );
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
        try (Router router = new RouterIote2eRequestImpl(masterConfig); ) {
            partition.forEachRemaining(messageAndMetadata -> processIote2eRequestRecord(messageAndMetadata, router));
        } catch(Exception e ) {
            logger.error(e.getMessage(),e);
        }
        logger.debug("Finished processing partition");
    }

    /**
     * Process iote 2 e request record.
     *
     * @param messageAndMetadata the message and metadata
     * @param router the router
     */
    public void processIote2eRequestRecord(MessageAndMetadata messageAndMetadata, Router router) {
        try {
        	Iote2eRequest iote2eRequest = iote2eRequestReuseItem.fromByteArray(messageAndMetadata.getPayload() );
        	logger.debug("Adding iote2eRequest: {}", iote2eRequest.toString() );
            router.add(iote2eRequest);
        } catch (Exception e) {
            logger.error("Error processing record : " + e.getMessage(), e);
        }
    }


}
