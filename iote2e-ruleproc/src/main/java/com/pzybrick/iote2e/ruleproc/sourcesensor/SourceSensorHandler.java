package com.pzybrick.iote2e.ruleproc.sourcesensor;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.avro.schema.SourceSensorValue;
import com.pzybrick.iote2e.ruleproc.sourceresponse.SourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;

public class SourceSensorHandler extends Thread {
	private static final Log log = LogFactory.getLog(SourceSensorHandler.class);
	private ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues = null;
	private RuleSvc ruleSvc;
	private SourceResponseSvc sourceResponseSvc;
	private boolean shutdown;
	private SourceSensorConfig sourceSensorConfig;
	private RuleConfig ruleConfig;

	public SourceSensorHandler(String pathNameExtSourceSensorConfig,
			ConcurrentLinkedQueue<SourceSensorValue> sourceSensorValues) throws Exception {
		log.debug("ctor");
		this.sourceSensorValues = sourceSensorValues;
		String rawJson = FileUtils.readFileToString(new File(pathNameExtSourceSensorConfig));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		sourceSensorConfig = gson.fromJson(rawJson, SourceSensorConfig.class);

		Class cls = Class.forName(sourceSensorConfig.getRuleSvcClassName());
		ruleSvc = (RuleSvc) cls.newInstance();
		cls = Class.forName(sourceSensorConfig.getSourceResponseSvcClassName());
		sourceResponseSvc = (SourceResponseSvc) cls.newInstance();

		rawJson = FileUtils.readFileToString(new File(sourceSensorConfig.getPathNameExtRuleConfigFile()));
		ruleConfig = gson.fromJson(rawJson, RuleConfig.class);
		
		ruleSvc.init(ruleConfig);
		sourceResponseSvc.init(ruleConfig);
	}

	@Override
	public void run() {
		while (true) {
			try {
				while (!sourceSensorValues.isEmpty()) {
					SourceSensorValue sourceSensorValue = sourceSensorValues.poll();
					if (sourceSensorValue != null) {
						log.debug(sourceSensorValue.toString());
						List<RuleEvalResult> ruleEvalResults = ruleSvc.process(
								sourceSensorValue.getSourceUuid().toString(),
								sourceSensorValue.getSensorUuid().toString(),
								sourceSensorValue.getSensorValue().toString());
						if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
							log.debug(ruleEvalResults);
							sourceResponseSvc.processRuleEvalResults(sourceSensorValue.getSourceUuid().toString(),
									sourceSensorValue.getSensorUuid().toString(), ruleEvalResults);
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

	public void putSourceSensorValue(SourceSensorValue sourceSensorValue) {
		sourceSensorValues.add(sourceSensorValue);
		interrupt();
	}

	public void putSourceSensorValues(List<SourceSensorValue> sourceSensorValues) {
		sourceSensorValues.addAll(sourceSensorValues);
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

	public SourceResponseSvc getSourceResponseSvc() {
		return sourceResponseSvc;
	}

	public SourceSensorConfig getSourceSensorConfig() {
		return sourceSensorConfig;
	}

	public RuleConfig getRuleConfig() {
		return ruleConfig;
	}

}
