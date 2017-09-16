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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;
import com.pzybrick.iote2e.stream.svc.RuleDefCondItem.RuleComparator;


/**
 * The Class RuleSvc.
 */
public abstract class RuleSvc {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RuleSvc.class);

	/**
	 * Inits the.
	 *
	 * @param masterConfig the master config
	 * @throws Exception the exception
	 */
	public abstract void init(MasterConfig masterConfig) throws Exception;

	/**
	 * Find rule def item.
	 *
	 * @param ruleUuid the rule uuid
	 * @return the rule def item
	 * @throws Exception the exception
	 */
	protected abstract RuleDefItem findRuleDefItem(String ruleUuid) throws Exception;

	/**
	 * Find source sensor actuator.
	 *
	 * @param loginUuid the login uuid
	 * @param sourceUuid the source uuid
	 * @param sensorName the sensor name
	 * @return the actuator state
	 * @throws Exception the exception
	 */
	protected abstract ActuatorState findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName)
			throws Exception;

	/**
	 * Update actuator value.
	 *
	 * @param loginSourceSensorActuator the login source sensor actuator
	 * @throws Exception the exception
	 */
	protected abstract void updateActuatorValue(ActuatorState loginSourceSensorActuator) throws Exception;

	/**
	 * Find rule login source sensor.
	 *
	 * @param loginUuid the login uuid
	 * @param sourceUuid the source uuid
	 * @param sensorName the sensor name
	 * @return the rule login source sensor
	 * @throws Exception the exception
	 */
	protected abstract RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorName) throws Exception;

	/**
	 * Process.
	 *
	 * @param iote2eRequest the iote 2 e request
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<RuleEvalResult> process(Iote2eRequest iote2eRequest ) throws Exception {
		List<RuleEvalResult> ruleEvalResults = new ArrayList<RuleEvalResult>();
		for( Map.Entry<CharSequence,CharSequence> entry : iote2eRequest.getPairs().entrySet() ) {
			String loginName = iote2eRequest.getLoginName().toString();
			String sourceName = iote2eRequest.getSourceName().toString();
			String sensorName = entry.getKey().toString();
			RuleLoginSourceSensor ruleSourceSensor = findRuleLoginSourceSensor( loginName, sourceName, sensorName );
			if( ruleSourceSensor != null ) {
				logger.debug(ruleSourceSensor);
				RuleDefItem ruleDefItem = findRuleDefItem( ruleSourceSensor.getRuleName());
				if( ruleDefItem == null ) throw new Exception ("Missing RuleDefItem for ruleUuid=" + ruleSourceSensor.getRuleName());
				logger.debug(ruleDefItem);
				String sensorValue = entry.getValue().toString();
				if( ruleDefItem.getRuleCustom() == null ) {
					ruleEval( loginName, sourceName, sensorName, sensorValue, ruleDefItem, ruleEvalResults);
				} else {
					ruleDefItem.getRuleCustom().ruleEval(loginName, sourceName, sensorName, sensorValue, ruleEvalResults, iote2eRequest);
				}
			} else {
				if( logger.isDebugEnabled()) logger.debug("ruleSourceSensor doesn't exist for sourceName=" + sourceName + ", sensorName=" + sensorName );
			}
		}
		return ruleEvalResults;
	}
	
	/**
	 * Rule eval.
	 *
	 * @param loginUuid the login uuid
	 * @param sourceUuid the source uuid
	 * @param sensorName the sensor name
	 * @param sensorValue the sensor value
	 * @param ruleDefItem the rule def item
	 * @param ruleEvalResults the rule eval results
	 * @throws Exception the exception
	 */
	protected void ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			RuleDefItem ruleDefItem, List<RuleEvalResult> ruleEvalResults) throws Exception {
		for (RuleDefCondItem ruleDefCondItem : ruleDefItem.getRuleDefCondItems()) {
			logger.debug(ruleDefCondItem);
			// two part rule evaluation: current sensor value and current
			// actuator, i.e. don't turn actuator on if it is already on
			// evaluate sensor value
			boolean isSensorRuleHit = evalRuleSensor(sensorValue, ruleDefCondItem, ruleDefItem);
			logger.info("isSensorRuleHit={}", isSensorRuleHit);
			// only evaluate Actuator rule if the Sensor rule has hit
			if (isSensorRuleHit) {
				RuleEvalResult ruleEvalResult = ruleEvalActuator(loginUuid, sourceUuid, sensorName, ruleDefCondItem, ruleDefItem);
				logger.debug(ruleEvalResult);
				ruleEvalResults.add(ruleEvalResult);
				if (ruleEvalResult.isRuleActuatorHit()) {
					ruleEvalResult.setActuatorTargetValue(ruleDefCondItem.getActuatorTargetValue());
					if (ruleDefCondItem.getStopEvalOnMatch())
						break;
				}
			}
		}
		logger.debug(ruleEvalResults);
	}

	/**
	 * Eval rule sensor.
	 *
	 * @param sensorValue the sensor value
	 * @param ruleDefCondItem the rule def cond item
	 * @param ruleDefItem the rule def item
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean evalRuleSensor(String sensorValue, RuleDefCondItem ruleDefCondItem, RuleDefItem ruleDefItem)
			throws Exception {
		return ruleEvalCommon(sensorValue, ruleDefCondItem.getSensorTypeValue(),
				ruleDefCondItem.getSensorCompareValue(), ruleDefCondItem.getIntSensorCompareValue(),
				ruleDefCondItem.getDblSensorCompareValue(), ruleDefCondItem.getRuleComparatorSensor(), ruleDefItem);
	}

	/**
	 * Rule eval actuator.
	 *
	 * @param loginName the login name
	 * @param sourceName the source name
	 * @param sensorName the sensor name
	 * @param ruleDefCondItem the rule def cond item
	 * @param ruleDefItem the rule def item
	 * @return the rule eval result
	 * @throws Exception the exception
	 */
	private RuleEvalResult ruleEvalActuator(String loginName, String sourceName, String sensorName, RuleDefCondItem ruleDefCondItem,
			RuleDefItem ruleDefItem) throws Exception {
		ActuatorState actuatorState = findSourceSensorActuator(loginName, sourceName, sensorName);
		if (actuatorState == null)
			throw new Exception( String.format(
					"Missing SourceSensorActuator, loginUuid=%s, sourceUuid=%s, sensorName=%s", loginName, sourceName, sensorName) );
		logger.debug(actuatorState);

		boolean ruleActuatorHit = false;
		if( actuatorState.getActuatorValue() != null ) {
			logger.debug("actuatorState.getActuatorValue() = " + actuatorState.getActuatorValue());
			ruleActuatorHit = ruleEvalCommon(actuatorState.getActuatorValue(),
					ruleDefCondItem.getActuatorTypeValue(), ruleDefCondItem.getActuatorCompareValue(),
					ruleDefCondItem.getIntActuatorCompareValue(), ruleDefCondItem.getDblActuatorCompareValue(),
					ruleDefCondItem.getRuleComparatorActuator(), ruleDefItem);
			
		} else {
			// If the ActuatorValue is null it means it hasn't been initialized yet, so force the rule to fire
			// so that an initial state will be set for the actuator
			logger.debug("actuator not initialized yet - value is null");
			ruleActuatorHit = true;
		}
		RuleEvalResult ruleEvalResult = new RuleEvalResult(sensorName, ruleActuatorHit, actuatorState);
		return ruleEvalResult;
	}

	/**
	 * Rule eval common.
	 *
	 * @param currentValue the current value
	 * @param typeValue the type value
	 * @param strCompareValue the str compare value
	 * @param intCompareValue the int compare value
	 * @param dblCompareValue the dbl compare value
	 * @param ruleComparator the rule comparator
	 * @param ruleDefItem the rule def item
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean ruleEvalCommon(String currentValue, String typeValue, String strCompareValue,
			Integer intCompareValue, Double dblCompareValue, RuleComparator ruleComparator, RuleDefItem ruleDefItem)
			throws Exception {
		logger.debug("currentValue {}, typeValue {}, strCompareValue {}, intCompareValue {}, dblCompareValue {}", currentValue, typeValue, strCompareValue, intCompareValue, dblCompareValue);
		boolean isRuleHit = false;
		int compareResult = 0;
		if ("dbl".equals(typeValue)) {
			Double dblSensorValue = Double.parseDouble(currentValue);
			compareResult = dblSensorValue.compareTo(dblCompareValue);
		} else if ("int".equals(typeValue)) {
			Integer intSensorValue = Integer.parseInt(currentValue);
			compareResult = intSensorValue.compareTo(intCompareValue);
		} else if ("str".equals(typeValue)) {
			compareResult = currentValue.compareTo(strCompareValue);
		} else
			throw new Exception("Unknown sensor type value: " + typeValue + ", ruleUuid=" + ruleDefItem.getRuleUuid());

		if (ruleComparator == RuleComparator.EQ) {
			if (compareResult == 0)
				isRuleHit = true;
		} else if (ruleComparator == RuleComparator.NE) {
			if (compareResult != 0)
				isRuleHit = true;
		} else if (ruleComparator == RuleComparator.LE) {
			if (compareResult <= 0)
				isRuleHit = true;
		} else if (ruleComparator == RuleComparator.GE) {
			if (compareResult >= 0)
				isRuleHit = true;
		} else if (ruleComparator == RuleComparator.LT) {
			if (compareResult < 0)
				isRuleHit = true;
		} else if (ruleComparator == RuleComparator.GT) {
			if (compareResult > 0)
				isRuleHit = true;
		}
		return isRuleHit;
	}

}
