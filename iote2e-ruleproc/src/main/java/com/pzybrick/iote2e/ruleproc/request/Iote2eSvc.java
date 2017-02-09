package com.pzybrick.iote2e.ruleproc.request;

import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public interface Iote2eSvc {
	public abstract void init(MasterConfig masterConfig) throws Exception;
	public void close() throws Exception;
	public void processRuleEvalResults( Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception;
}
