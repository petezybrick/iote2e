package com.pzybrick.iote2e.ruleproc.sourceresponse;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;

public class SourceResponseSvcIgniteImpl implements SourceResponseSvc {
	private static final Log log = LogFactory.getLog(SourceResponseSvcIgniteImpl.class);
	
	public void processRuleEvalResults( String sourceUuid, String sensorUuid, List<RuleEvalResult> ruleEvalResults ) throws Exception {
		for( RuleEvalResult ruleEvalResult : ruleEvalResults ) {
			log.debug(ruleEvalResult.toString());
		}
	}

}
