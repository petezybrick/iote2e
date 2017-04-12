package com.pzybrick.iote2e.stream.svc;

import java.util.List;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public interface RuleCustom {
	
	public List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			List<RuleEvalResult> ruleEvalResults, Iote2eRequest iote2eRequest ) throws Exception;

}
