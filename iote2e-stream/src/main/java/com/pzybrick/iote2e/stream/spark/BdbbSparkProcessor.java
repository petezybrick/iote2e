package com.pzybrick.iote2e.stream.spark;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.router.RouterBdbb;
import com.pzybrick.iote2e.stream.router.RouterBdbbImpl;

import consumer.kafka.MessageAndMetadata;

public class BdbbSparkProcessor implements Serializable {
	private static final long serialVersionUID = 2989374902129650863L;
	private static final Logger logger = LogManager.getLogger(BdbbSparkProcessor.class);
	private MasterConfig masterConfig;

	
	public BdbbSparkProcessor(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
	}

    public void processBdbbRDD(JavaRDD<MessageAndMetadata> rdd) {
        logger.debug("Processing BdbbRDD {}, rdd={}", Thread.currentThread().getId(), rdd.toString() );
        rdd.foreachPartition(partition -> processPartition(partition));
    }

    public void processPartition(Iterator<MessageAndMetadata> partition) throws Exception {
        logger.debug("Starting to process partition");
        try (RouterBdbb routerBdbb = new RouterBdbbImpl(masterConfig); ) {
            partition.forEachRemaining(messageAndMetadata -> processBdbbRecord(messageAndMetadata, routerBdbb));
        } catch(Exception e ) {
            logger.error(e.getMessage(),e);
        }
        logger.debug("Finished processing partition");
    }

    public void processBdbbRecord(MessageAndMetadata messageAndMetadata, RouterBdbb routerBdbb) {
        try {
        	ByteBuffer byteBuffer = ByteBuffer.wrap(messageAndMetadata.getPayload() );
        	logger.debug("Adding bdbb: len={}", messageAndMetadata.getPayload().length );
            routerBdbb.add(byteBuffer);
        } catch (Exception e) {
            logger.error("Error processing record : " + e.getMessage(), e);
        }
    }


}
