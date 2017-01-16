package com.pzybrick.iote2e.ruleproc.request;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.persist.ConfigDao;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.ruleproc.svc.RuleSvc;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eRequestSparkHandler {
	private static final Logger logger = LogManager.getLogger(Iote2eRequestSparkHandler.class);
	private RuleSvc ruleSvc;
	private Iote2eSvc iote2eSvc;
	private MasterConfig masterConfig;
	private String keyspaceName;

	public Iote2eRequestSparkHandler() throws Exception {
		try {
			this.keyspaceName = System.getenv("CASSANDRA_KEYSPACE_NAME");
			ConfigDao.useKeyspace(keyspaceName);
			String sourceSensorConfigKey = System.getenv("MASTER_CONFIG_JSON_KEY");
			String rawJson = ConfigDao.findConfigJson(sourceSensorConfigKey);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			masterConfig = gson.fromJson(rawJson, MasterConfig.class);
	
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
	
	public void processRequests( List<Iote2eRequest> iote2eRequests ) throws Exception {
		try {
			for( Iote2eRequest iote2eRequest : iote2eRequests ) {
				if (iote2eRequest != null) {
					logger.debug(iote2eRequest.toString());
					List<RuleEvalResult> ruleEvalResults = ruleSvc.process( iote2eRequest);
					if (ruleEvalResults != null && ruleEvalResults.size() > 0 ) {
						logger.debug(ruleEvalResults);
						iote2eSvc.processRuleEvalResults( iote2eRequest, ruleEvalResults);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

}
