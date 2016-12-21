package com.pzybrick.iote2e.ruleproc.request;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.schema.avro.Iote2eResult;

public class Iote2eRequestHandler extends Thread {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestHandler.class);
	private ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests = null;
	private RuleSvc ruleSvc;
	private Iote2eSvc iote2eSvc;
	private boolean shutdown;
	private Iote2eRequestConfig iote2eRequestConfig;
	private RuleConfig ruleConfig;

	public Iote2eRequestHandler(String pathNameExtSourceSensorConfig,
			ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) throws Exception {
		logger.debug("ctor");
		try {
			this.iote2eRequests = iote2eRequests;
			String rawJson = FileUtils.readFileToString(new File(pathNameExtSourceSensorConfig));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			iote2eRequestConfig = gson.fromJson(rawJson, Iote2eRequestConfig.class);
	
			Class cls = Class.forName(iote2eRequestConfig.getRuleSvcClassName());
			ruleSvc = (RuleSvc) cls.newInstance();
			cls = Class.forName(iote2eRequestConfig.getRequestSvcClassName());
	
			rawJson = FileUtils.readFileToString(new File(iote2eRequestConfig.getPathNameExtRuleConfigFile()));
			ruleConfig = gson.fromJson(rawJson, RuleConfig.class);
			// TODO: get via static instanceOf, or just wrap the iote2esvc calls in synchronized
			iote2eSvc = (Iote2eSvc) cls.newInstance();
			
			ruleSvc.init(ruleConfig);
			iote2eSvc.init(ruleConfig);
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

	public Iote2eRequestConfig getIote2eRequestConfig() {
		return iote2eRequestConfig;
	}

	public RuleConfig getRuleConfig() {
		return ruleConfig;
	}

	public ConcurrentLinkedQueue<Iote2eRequest> getIote2eRequests() {
		return iote2eRequests;
	}

	public Iote2eRequestHandler setIote2eRequests(ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) {
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

}
