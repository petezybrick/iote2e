package com.pzybrick.iote2e.tests.simws;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public abstract class PollIote2eResultsThread extends Thread {
	private static final Logger logger = LogManager.getLogger(PollIote2eResultsThread.class);
	private ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	private boolean shutdown;

	public PollIote2eResultsThread( ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults) {
		super();
		this.queueIote2eResults = queueIote2eResults;
	}
	
	public abstract void processIote2eResult( Iote2eResult iote2eResult );
	
	public void shutdown() {
		this.shutdown = true;
		interrupt();
	}

	@Override
	public void run() {
		while( true ) {
			while( !queueIote2eResults.isEmpty() ) {
				Iote2eResult iote2eResult = queueIote2eResults.poll();
				if( iote2eResult != null ) {
					processIote2eResult( iote2eResult );
				}
			}
			try {
				sleep(1000);
			} catch( InterruptedException e ) {}
			if( this.shutdown ) break;
		}
	}
}