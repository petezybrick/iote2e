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
package com.pzybrick.iote2e.tests.simws;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eResult;


/**
 * The Class PollIote2eResultsThread.
 */
public abstract class PollIote2eResultsThread extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(PollIote2eResultsThread.class);
	
	/** The queue iote 2 e results. */
	private ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults = new ConcurrentLinkedQueue<Iote2eResult>();
	
	/** The shutdown. */
	private boolean shutdown;

	/**
	 * Instantiates a new poll iote 2 e results thread.
	 *
	 * @param queueIote2eResults the queue iote 2 e results
	 */
	public PollIote2eResultsThread( ConcurrentLinkedQueue<Iote2eResult> queueIote2eResults) {
		super();
		this.queueIote2eResults = queueIote2eResults;
	}
	
	/**
	 * Process iote 2 e result.
	 *
	 * @param iote2eResult the iote 2 e result
	 */
	public abstract void processIote2eResult( Iote2eResult iote2eResult );
	
	/**
	 * Shutdown.
	 */
	public void shutdown() {
		this.shutdown = true;
		interrupt();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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