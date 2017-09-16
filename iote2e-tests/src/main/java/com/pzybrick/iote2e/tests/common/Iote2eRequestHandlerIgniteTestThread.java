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
package com.pzybrick.iote2e.tests.common;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;
import com.pzybrick.iote2e.stream.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;


/**
 * The Class Iote2eRequestHandlerIgniteTestThread.
 */
public class Iote2eRequestHandlerIgniteTestThread extends Thread {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eRequestHandlerIgniteTestThread.class);
	
	/** The iote 2 e requests. */
	private ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests = null;
	
	/** The rule svc. */
	private RuleSvc ruleSvc;
	
	/** The iote 2 e svc. */
	private Iote2eSvc iote2eSvc;
	
	/** The shutdown. */
	private boolean shutdown;
//	private Iote2eRequestConfig iote2eRequestConfig;
/** The master config. */
//	private RuleConfig ruleConfig;
	private MasterConfig masterConfig;
	
	/** The keyspace name. */
	private String keyspaceName;

	/**
	 * Instantiates a new iote 2 e request handler ignite test thread.
	 *
	 * @param masterConfig the master config
	 * @param iote2eRequests the iote 2 e requests
	 * @throws Exception the exception
	 */
	public Iote2eRequestHandlerIgniteTestThread(MasterConfig masterConfig, ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) throws Exception {
		logger.debug("ctor");
		try {
			this.masterConfig = masterConfig;
			this.iote2eRequests = iote2eRequests;
			Class cls = Class.forName(masterConfig.getRuleSvcClassName());
			ruleSvc = (RuleSvc) cls.newInstance();
			cls = Class.forName(masterConfig.getRequestSvcClassName());
			iote2eSvc = (Iote2eSvc) cls.newInstance();
			
			ruleSvc.init(masterConfig);
			iote2eSvc.init(masterConfig);
		} catch( Exception e ) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				while (!iote2eRequests.isEmpty()) {
					Iote2eRequest iote2eRequest = iote2eRequests.poll();
					if (iote2eRequest != null) {
						logger.debug(iote2eRequest.toString());
						List<RuleEvalResult> ruleEvalResults = ruleSvc.process( iote2eRequest);
						if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
							logger.debug(ruleEvalResults);
							iote2eSvc.processRuleEvalResults( iote2eRequest, ruleEvalResults);
						}
					}
				}
				sleep(5 * 60 * 1000l);

			} catch (InterruptedException e1) {

			} catch (Exception e) {
				logger.error("Exception in run()", e);
			}
			if (shutdown)
				break;
		}
		if( shutdown ) logger.info("shutdown complete");
	}


	/**
	 * Shutdown.
	 */
	public void shutdown() {
		logger.info("shutdown initiated");
		this.shutdown = true;
		interrupt();
	}

	/**
	 * Gets the rule svc.
	 *
	 * @return the rule svc
	 */
	public RuleSvc getRuleSvc() {
		return ruleSvc;
	}

	/**
	 * Gets the iote 2 e svc.
	 *
	 * @return the iote 2 e svc
	 */
	public Iote2eSvc getIote2eSvc() {
		return iote2eSvc;
	}

	/**
	 * Gets the iote 2 e requests.
	 *
	 * @return the iote 2 e requests
	 */
	public ConcurrentLinkedQueue<Iote2eRequest> getIote2eRequests() {
		return iote2eRequests;
	}

	/**
	 * Sets the iote 2 e requests.
	 *
	 * @param iote2eRequests the iote 2 e requests
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setIote2eRequests(ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) {
		this.iote2eRequests = iote2eRequests;
		return this;
	}
	

	/**
	 * Adds the iote 2 e request.
	 *
	 * @param iote2eRequest the iote 2 e request
	 */
	public void addIote2eRequest(Iote2eRequest iote2eRequest) {
		iote2eRequests.add(iote2eRequest);
		interrupt();
	}

	/**
	 * Adds the iote 2 e request.
	 *
	 * @param iote2eRequests the iote 2 e requests
	 */
	public void addIote2eRequest(List<Iote2eResult> iote2eRequests) {
		iote2eRequests.addAll(iote2eRequests);
		interrupt();
	}

	/**
	 * Checks if is shutdown.
	 *
	 * @return true, if is shutdown
	 */
	public boolean isShutdown() {
		return shutdown;
	}

	/**
	 * Gets the master config.
	 *
	 * @return the master config
	 */
	public MasterConfig getMasterConfig() {
		return masterConfig;
	}

	/**
	 * Gets the keyspace name.
	 *
	 * @return the keyspace name
	 */
	public String getKeyspaceName() {
		return keyspaceName;
	}

	/**
	 * Sets the rule svc.
	 *
	 * @param ruleSvc the rule svc
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setRuleSvc(RuleSvc ruleSvc) {
		this.ruleSvc = ruleSvc;
		return this;
	}

	/**
	 * Sets the iote 2 e svc.
	 *
	 * @param iote2eSvc the iote 2 e svc
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setIote2eSvc(Iote2eSvc iote2eSvc) {
		this.iote2eSvc = iote2eSvc;
		return this;
	}

	/**
	 * Sets the shutdown.
	 *
	 * @param shutdown the shutdown
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
		return this;
	}

	/**
	 * Sets the master config.
	 *
	 * @param masterConfig the master config
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setMasterConfig(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
		return this;
	}

	/**
	 * Sets the keyspace name.
	 *
	 * @param keyspaceName the keyspace name
	 * @return the iote 2 e request handler ignite test thread
	 */
	public Iote2eRequestHandlerIgniteTestThread setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
		return this;
	}

}
