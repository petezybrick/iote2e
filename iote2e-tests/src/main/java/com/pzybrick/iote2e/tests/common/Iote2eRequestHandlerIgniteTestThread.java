package com.pzybrick.iote2e.tests.common;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eRequestHandlerIgniteTestThread extends Thread {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestHandlerIgniteTestThread.class);
	private ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests = null;
	private RuleSvc ruleSvc;
	private Iote2eSvc iote2eSvc;
	private boolean shutdown;
//	private Iote2eRequestConfig iote2eRequestConfig;
//	private RuleConfig ruleConfig;
	private MasterConfig masterConfig;
	private String keyspaceName;

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


	public void shutdown() {
		logger.info("shutdown initiated");
		this.shutdown = true;
		interrupt();
	}

	public RuleSvc getRuleSvc() {
		return ruleSvc;
	}

	public Iote2eSvc getIote2eSvc() {
		return iote2eSvc;
	}

	public ConcurrentLinkedQueue<Iote2eRequest> getIote2eRequests() {
		return iote2eRequests;
	}

	public Iote2eRequestHandlerIgniteTestThread setIote2eRequests(ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) {
		this.iote2eRequests = iote2eRequests;
		return this;
	}
	

	public void addIote2eRequest(Iote2eRequest iote2eRequest) {
		iote2eRequests.add(iote2eRequest);
		interrupt();
	}

	public void addIote2eRequest(List<Iote2eResult> iote2eRequests) {
		iote2eRequests.addAll(iote2eRequests);
		interrupt();
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public MasterConfig getMasterConfig() {
		return masterConfig;
	}

	public String getKeyspaceName() {
		return keyspaceName;
	}

	public Iote2eRequestHandlerIgniteTestThread setRuleSvc(RuleSvc ruleSvc) {
		this.ruleSvc = ruleSvc;
		return this;
	}

	public Iote2eRequestHandlerIgniteTestThread setIote2eSvc(Iote2eSvc iote2eSvc) {
		this.iote2eSvc = iote2eSvc;
		return this;
	}

	public Iote2eRequestHandlerIgniteTestThread setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
		return this;
	}

	public Iote2eRequestHandlerIgniteTestThread setMasterConfig(MasterConfig masterConfig) {
		this.masterConfig = masterConfig;
		return this;
	}

	public Iote2eRequestHandlerIgniteTestThread setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
		return this;
	}

}
