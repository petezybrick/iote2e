package com.pzybrick.iote2e.tests.spark;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConcurrentLinkedQueuePZ<E> extends ConcurrentLinkedQueue<E> {
	private static final Logger logger = LogManager.getLogger(ConcurrentLinkedQueuePZ.class);

	private static final long serialVersionUID = 4652416336893106685L;	
	
	public boolean add(E e) {
		logger.info("================= add ============== {}", Thread.currentThread().getId() );
		return super.add(e);
	}
}
