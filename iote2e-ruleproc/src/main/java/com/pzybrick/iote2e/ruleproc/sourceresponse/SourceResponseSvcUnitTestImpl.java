package com.pzybrick.iote2e.ruleproc.sourceresponse;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

public class SourceResponseSvcUnitTestImpl implements SourceResponseSvc {
	private static final Log log = LogFactory.getLog(SourceResponseSvcUnitTestImpl.class);
	private List<RuleEvalResult> ruleEvalResults;
	
	public void processRuleEvalResults( String sourceUuid, String sensorUuid, List<RuleEvalResult> ruleEvalResults ) throws Exception {
		if( log.isDebugEnabled() ) log.debug(  "sourceUuid=" + sourceUuid +  ",  sensorUuid=" + sensorUuid + ", ruleEvalResults=" + ruleEvalResults);
		this.ruleEvalResults = ruleEvalResults;
	}

	public List<RuleEvalResult> getRuleEvalResults() {
		return ruleEvalResults;
	}

	public SourceResponseSvcUnitTestImpl setRuleEvalResults(List<RuleEvalResult> ruleEvalResults) {
		this.ruleEvalResults = ruleEvalResults;
		return this;
	}

}
