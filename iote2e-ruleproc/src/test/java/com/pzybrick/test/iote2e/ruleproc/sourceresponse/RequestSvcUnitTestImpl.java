package com.pzybrick.test.iote2e.ruleproc.sourceresponse;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ruleproc.sourceresponse.LoginSourceResponseSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

public class RequestSvcUnitTestImpl implements LoginSourceResponseSvc {
	private static final Log log = LogFactory.getLog(RequestSvcUnitTestImpl.class);
	private List<RuleEvalResult> ruleEvalResults;

	@Override
	public void processRuleEvalResults(String loginUuid, String sourceUuid, String sensorName, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		if (log.isDebugEnabled())
			log.debug( String.format("loginUuid=%s, sourceUuid=%s, sensorName=%s, ruleEvalResults=%s", loginUuid, sourceUuid, sensorName, ruleEvalResults));
		this.ruleEvalResults = ruleEvalResults;
	}

	@Override
	public void close() throws Exception {
	
	}

	public List<RuleEvalResult> getRuleEvalResults() {
		return ruleEvalResults;
	}

	public RequestSvcUnitTestImpl setRuleEvalResults(List<RuleEvalResult> ruleEvalResults) {
		this.ruleEvalResults = ruleEvalResults;
		return this;
	}

	@Override
	public void init(RuleConfig ruleConfig) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
