package com.pzybrick.iote2e.ruleproc.request;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestHandler extends Thread {
	private static final Log log = LogFactory.getLog(Iote2eRequestHandler.class);
	private ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests = null;
	private RuleSvc ruleSvc;
	private Iote2eSvc iote2eRequestSvc;
	private boolean shutdown;
	private Iote2eRequestConfig iote2eRequestConfig;
	private RuleConfig ruleConfig;

	public Iote2eRequestHandler(String pathNameExtSourceSensorConfig,
			ConcurrentLinkedQueue<Iote2eRequest> iote2eRequests) throws Exception {
		log.debug("ctor");
		try {
		this.iote2eRequests = iote2eRequests;
		String rawJson = FileUtils.readFileToString(new File(pathNameExtSourceSensorConfig));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		iote2eRequestConfig = gson.fromJson(rawJson, Iote2eRequestConfig.class);

		Class cls = Class.forName(iote2eRequestConfig.getRuleSvcClassName());
		ruleSvc = (RuleSvc) cls.newInstance();
		cls = Class.forName(iote2eRequestConfig.getRequestSvcClassName());
		iote2eRequestSvc = (Iote2eSvc) cls.newInstance();

		rawJson = FileUtils.readFileToString(new File(iote2eRequestConfig.getPathNameExtRuleConfigFile()));
		ruleConfig = gson.fromJson(rawJson, RuleConfig.class);
		
		ruleSvc.init(ruleConfig);
		iote2eRequestSvc.init(ruleConfig);
		} catch( Exception e ) {
			log.error(e.getMessage(),e);
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
						log.debug(iote2eRequest.toString());
						List<RuleEvalResult> ruleEvalResults = ruleSvc.process( iote2eRequest);
						if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
							log.debug(ruleEvalResults);
							iote2eRequestSvc.processRuleEvalResults( iote2eRequest, ruleEvalResults);
						}
					}
				}
				sleep(5 * 60 * 1000l);

			} catch (InterruptedException e1) {

			} catch (Exception e) {
				log.error("Exception in run()", e);
			}
			if (shutdown)
				break;
		}
		if( shutdown ) log.info("shutdown complete");
	}


	public void shutdown() {
		log.info("shutdown initiated");
		this.shutdown = true;
		interrupt();
	}

	public RuleSvc getRuleSvc() {
		return ruleSvc;
	}

	public Iote2eSvc getIote2eRequestSvc() {
		return iote2eRequestSvc;
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

	public void addIote2eRequest(List<Iote2eRequest> iote2eRequests) {
		iote2eRequests.addAll(iote2eRequests);
		interrupt();
	}

}
