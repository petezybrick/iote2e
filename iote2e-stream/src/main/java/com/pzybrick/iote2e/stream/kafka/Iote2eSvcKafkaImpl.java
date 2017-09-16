/**
 *    Copyright 2016, 2017 Peter Zybrick and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * 
 * @author  Pete Zybrick
 * @version 1.0.0, 2017-09
 * 
 */
package com.pzybrick.iote2e.stream.kafka;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.stream.request.Iote2eSvc;
import com.pzybrick.iote2e.stream.svc.RuleEvalResult;


/**
 * The Class Iote2eSvcKafkaImpl.
 */
public class Iote2eSvcKafkaImpl implements Iote2eSvc {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(Iote2eSvcKafkaImpl.class);
	
	/** The rule eval results. */
	private List<RuleEvalResult> ruleEvalResults;

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#processRuleEvalResults(com.pzybrick.iote2e.schema.avro.Iote2eRequest, java.util.List)
	 */
	@Override
	public void processRuleEvalResults(Iote2eRequest iote2eRequest, List<RuleEvalResult> ruleEvalResults)
			throws Exception {
		logger.info( String.format(">>>>> iote2eRequest=%s, ruleEvalResults=%s", iote2eRequest.toString(), ruleEvalResults));
		this.ruleEvalResults = ruleEvalResults;		
	}
	

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#close()
	 */
	@Override
	public void close() throws Exception {
	
	}

	/**
	 * Gets the rule eval results.
	 *
	 * @return the rule eval results
	 */
	public List<RuleEvalResult> getRuleEvalResults() {
		return ruleEvalResults;
	}

	/**
	 * Sets the rule eval results.
	 *
	 * @param ruleEvalResults the rule eval results
	 * @return the iote 2 e svc kafka impl
	 */
	public Iote2eSvcKafkaImpl setRuleEvalResults(List<RuleEvalResult> ruleEvalResults) {
		this.ruleEvalResults = ruleEvalResults;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.request.Iote2eSvc#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
	@Override
	public void init(MasterConfig masterConfig) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
