package com.pzybrick.iote2e.ruleproc.sourceresponse;

import java.util.List;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

public interface SourceResponseSvc {
	public void processRuleEvalResults(String sourceUuid, String sensorUuid, List<RuleEvalResult> ruleEvalResults)
			throws Exception;

}
