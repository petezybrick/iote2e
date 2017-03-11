package com.pzybrick.iote2e.tests.local;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eSvcLocalTestImpl implements Iote2eSvc {
	private static final Logger logger = LogManager.getLogger(Iote2eSvcLocalTestImpl.class);
	private List<RuleEvalResult> ruleEvalResults;

	@Override
	public void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		if (logger.isDebugEnabled())
			logger.debug( String.format("iote2eRequest=%s, ruleEvalResults=%s", iote2eRequest.toString(), ruleEvalResults));
		this.ruleEvalResults = ruleEvalResults;		
	}
	

	@Override
	public void close() throws Exception {
	
	}

	public List<RuleEvalResult> getRuleEvalResults() {
		return ruleEvalResults;
	}

	public Iote2eSvcLocalTestImpl setRuleEvalResults(List<RuleEvalResult> ruleEvalResults) {
		this.ruleEvalResults = ruleEvalResults;
		return this;
	}

	@Override
	public void init(MasterConfig masterConfig) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
