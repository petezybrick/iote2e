package com.pzybrick.iote2e.ruleproc.spark;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;

import com.pzybrick.iote2e.ruleproc.router.Router;
import com.pzybrick.iote2e.ruleproc.router.RouterIote2eRequestImpl;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestSparkProcessor implements Serializable {
	private static final Log log = LogFactory.getLog(Iote2eRequestSparkProcessor.class);

    public void processIote2eRequestRDD(JavaRDD<Iote2eRequest> rdd) {
        log.debug("Processing Iote2eRequestRDD " + Thread.currentThread().getId() );
        rdd.foreachPartition(partition -> processPartition(partition));
    }

    public void processPartition(Iterator<Iote2eRequest> partition) throws Exception {
        log.debug("Starting to process partition");
        try (Router router = new RouterIote2eRequestImpl(); ) {
            partition.forEachRemaining(iote2eRequest -> processIote2eRequestRecord(iote2eRequest, router));
        } catch(Exception e ) {
            log.error(e.getMessage(),e);
        }
        log.debug("Finished processing partition");
    }

    public void processIote2eRequestRecord(Iote2eRequest iote2eRequest, Router router) {
        try {
            router.add(iote2eRequest);
        } catch (Exception e) {
            log.error("Error processing record : " + e.getMessage(), e);
        }
    }


}
