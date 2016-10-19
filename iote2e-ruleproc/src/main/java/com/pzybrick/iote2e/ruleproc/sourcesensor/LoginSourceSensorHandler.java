package com.pzybrick.iote2e.ruleproc.sourcesensor;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.sourceresponse.LoginSourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.LoginSourceSensorValue;

public class LoginSourceSensorHandler extends Thread {
	private static final Log log = LogFactory.getLog(LoginSourceSensorHandler.class);
	private ConcurrentLinkedQueue<LoginSourceSensorValue> loginSourceSensorValues = null;
	private RuleSvc ruleSvc;
	private LoginSourceResponseSvc loginSourceResponseSvc;
	private boolean shutdown;
	private LoginSourceSensorConfig loginSourceSensorConfig;
	private RuleConfig ruleConfig;

	public LoginSourceSensorHandler(String pathNameExtSourceSensorConfig,
			ConcurrentLinkedQueue<LoginSourceSensorValue> loginSourceSensorValues) throws Exception {
		log.debug("ctor");
		this.loginSourceSensorValues = loginSourceSensorValues;
		String rawJson = FileUtils.readFileToString(new File(pathNameExtSourceSensorConfig));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		loginSourceSensorConfig = gson.fromJson(rawJson, LoginSourceSensorConfig.class);

		Class cls = Class.forName(loginSourceSensorConfig.getRuleSvcClassName());
		ruleSvc = (RuleSvc) cls.newInstance();
		cls = Class.forName(loginSourceSensorConfig.getSourceResponseSvcClassName());
		loginSourceResponseSvc = (LoginSourceResponseSvc) cls.newInstance();

		rawJson = FileUtils.readFileToString(new File(loginSourceSensorConfig.getPathNameExtRuleConfigFile()));
		ruleConfig = gson.fromJson(rawJson, RuleConfig.class);
		
		ruleSvc.init(ruleConfig);
		loginSourceResponseSvc.init(ruleConfig);
	}

	@Override
	public void run() {
		while (true) {
			try {
				while (!loginSourceSensorValues.isEmpty()) {
					LoginSourceSensorValue loginSourceSensorValue = loginSourceSensorValues.poll();
					if (loginSourceSensorValue != null) {
						log.debug(loginSourceSensorValue.toString());
						List<RuleEvalResult> ruleEvalResults = ruleSvc.process(
								loginSourceSensorValue.getLoginUuid().toString(),
								loginSourceSensorValue.getSourceUuid().toString(),
								loginSourceSensorValue.getSensorUuid().toString(),
								loginSourceSensorValue.getSensorValue().toString());
						if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
							log.debug(ruleEvalResults);
							loginSourceResponseSvc.processRuleEvalResults(
									loginSourceSensorValue.getLoginUuid().toString(), 
									loginSourceSensorValue.getSourceUuid().toString(),
									loginSourceSensorValue.getSensorUuid().toString(), ruleEvalResults);
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

	public void putLoginSourceSensorValue(LoginSourceSensorValue loginSourceSensorValue) {
		loginSourceSensorValues.add(loginSourceSensorValue);
		interrupt();
	}

	public void putLoginSourceSensorValues(List<LoginSourceSensorValue> loginSourceSensorValues) {
		loginSourceSensorValues.addAll(loginSourceSensorValues);
		interrupt();
	}

	public void shutdown() {
		log.info("shutdown initiated");
		this.shutdown = true;
		interrupt();
	}

	public RuleSvc getRuleSvc() {
		return ruleSvc;
	}

	public LoginSourceResponseSvc getLoginSourceResponseSvc() {
		return loginSourceResponseSvc;
	}

	public LoginSourceSensorConfig getLoginSourceSensorConfig() {
		return loginSourceSensorConfig;
	}

	public RuleConfig getRuleConfig() {
		return ruleConfig;
	}

}
