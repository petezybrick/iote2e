package com.pzybrick.iote2e.ruleproc.kafka;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ruleproc.request.Iote2eSvc;
import com.pzybrick.iote2e.ruleproc.svc.RuleConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleEvalResult;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class Iote2eSvcKafkaImpl implements Iote2eSvc {
	private static final Log log = LogFactory.getLog(Iote2eSvcKafkaImpl.class);
	private List<RuleEvalResult> ruleEvalResults;

	@Override
	public void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		log.info( String.format(">>>>> iote2eRequest=%s, ruleEvalResults=%s", iote2eRequest.toString(), ruleEvalResults));
		this.ruleEvalResults = ruleEvalResults;		
	}
	

	@Override
	public void close() throws Exception {
	
	}

	public List<RuleEvalResult> getRuleEvalResults() {
		return ruleEvalResults;
	}

	public Iote2eSvcKafkaImpl setRuleEvalResults(List<RuleEvalResult> ruleEvalResults) {
		this.ruleEvalResults = ruleEvalResults;
		return this;
	}

	@Override
	public void init(RuleConfig ruleConfig) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
