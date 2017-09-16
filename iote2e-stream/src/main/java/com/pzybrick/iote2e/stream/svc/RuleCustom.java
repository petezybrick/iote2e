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
package com.pzybrick.iote2e.stream.svc;

import java.util.List;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;


/**
 * The Interface RuleCustom.
 */
public interface RuleCustom {
	
	/**
	 * Rule eval.
	 *
	 * @param loginUuid the login uuid
	 * @param sourceUuid the source uuid
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @param ruleEvalResults the rule eval results
	 * @param iote2eRequest the iote 2 e request
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			List<RuleEvalResult> ruleEvalResults, Iote2eRequest iote2eRequest ) throws Exception;
	
	/**
	 * Sets the master config.
	 *
	 * @param masterConfig the new master config
	 */
	public void setMasterConfig( MasterConfig masterConfig );

}
