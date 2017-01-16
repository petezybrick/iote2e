package com.pzybrick.iote2e.ruleproc.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.ruleproc.config.MasterConfig;
import com.pzybrick.iote2e.ruleproc.svc.RuleDefCondItem.RuleComparator;
import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public abstract class RuleSvc {
	private static final Logger logger = LogManager.getLogger(RuleSvc.class);

	public abstract void init(MasterConfig masterConfig) throws Exception;

	protected abstract RuleDefItem findRuleDefItem(String ruleUuid) throws Exception;

	protected abstract ActuatorState findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName)
			throws Exception;

	protected abstract void updateActuatorValue(ActuatorState loginSourceSensorActuator) throws Exception;

	protected abstract RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorName) throws Exception;

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
				ruleEvalResults = ruleEval( loginName, sourceName, sensorName, sensorValue, ruleDefItem, ruleEvalResults);
			} else {
				if( logger.isDebugEnabled()) logger.debug("ruleSourceSensor doesn't exist for sourceName=" + sourceName + ", sensorName=" + sensorName );
			}
		}
		return ruleEvalResults;
	}
	
	protected List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			RuleDefItem ruleDefItem, List<RuleEvalResult> ruleEvalResults) throws Exception {
		for (RuleDefCondItem ruleDefCondItem : ruleDefItem.getRuleDefCondItems()) {
			logger.debug(ruleDefCondItem);
			// two part rule evaluation: current sensor value and current
			// actuator, i.e. don't turn actuator on if it is already on
			// evaluate sensor value
			boolean isSensorRuleHit = evalRuleSensor(sensorValue, ruleDefCondItem, ruleDefItem);
			if( logger.isDebugEnabled()) logger.debug("isSensorRuleHit=" + isSensorRuleHit);
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
		return ruleEvalResults;
	}

	private boolean evalRuleSensor(String sensorValue, RuleDefCondItem ruleDefCondItem, RuleDefItem ruleDefItem)
			throws Exception {
		return ruleEvalCommon(sensorValue, ruleDefCondItem.getSensorTypeValue(),
				ruleDefCondItem.getSensorCompareValue(), ruleDefCondItem.getIntSensorCompareValue(),
				ruleDefCondItem.getDblSensorCompareValue(), ruleDefCondItem.getRuleComparatorSensor(), ruleDefItem);
	}

	private RuleEvalResult ruleEvalActuator(String loginName, String sourceName, String sensorName, RuleDefCondItem ruleDefCondItem,
			RuleDefItem ruleDefItem) throws Exception {
		ActuatorState actuatorState = findSourceSensorActuator(loginName, sourceName, sensorName);
		if (actuatorState == null)
			throw new Exception( String.format(
					"Missing SourceSensorActuator, loginUuid=%s, sourceUuid=%s, sensorName=%s", loginName, sourceName, sensorName) );
		logger.debug(actuatorState);

		boolean ruleActuatorHit = false;
		if( actuatorState.getActuatorValue() != null ) {
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

	private boolean ruleEvalCommon(String currentValue, String typeValue, String strCompareValue,
			Integer intCompareValue, Double dblCompareValue, RuleComparator ruleComparator, RuleDefItem ruleDefItem)
			throws Exception {
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
