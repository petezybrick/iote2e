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

public class OmhSparkProcessor implements Serializable {
	private static final long serialVersionUID = 2989374902129650863L;
	private static final Logger logger = LogManager.getLogger(OmhSparkProcessor.class);
	private MasterConfig masterConfig;

	
	public OmhSparkProcessor(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
	}

    public void processOmhRDD(JavaRDD<MessageAndMetadata> rdd) {
        logger.debug("Processing OmhRDD {}, rdd={}", Thread.currentThread().getId(), rdd.toString() );
        rdd.foreachPartition(partition -> processPartition(partition));
    }

    public void processPartition(Iterator<MessageAndMetadata> partition) throws Exception {
        logger.debug("Starting to process partition");
        try (RouterOmh routerOmh = new RouterOmhImpl(masterConfig); ) {
            partition.forEachRemaining(messageAndMetadata -> processOmhRecord(messageAndMetadata, routerOmh));
        } catch(Exception e ) {
            logger.error(e.getMessage(),e);
        }
        logger.debug("Finished processing partition");
    }

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
