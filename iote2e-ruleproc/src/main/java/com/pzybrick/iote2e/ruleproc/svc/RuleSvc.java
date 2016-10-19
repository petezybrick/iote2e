package com.pzybrick.iote2e.ruleproc.svc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pzybrick.iote2e.ruleproc.svc.RuleDefCondItem.RuleComparator;

public abstract class RuleSvc {
	private static final Log log = LogFactory.getLog(RuleSvc.class);

	public abstract void init(RuleConfig ruleConfig) throws Exception;

	protected abstract RuleDefItem findRuleDefItem(String ruleUuid) throws Exception;

	protected abstract LoginSourceSensorActuator findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorUuid)
			throws Exception;

	protected abstract void updateActuatorValue(LoginSourceSensorActuator loginSourceSensorActuator) throws Exception;

	protected abstract RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorUuid) throws Exception;


	public List<RuleEvalResult> process(String loginUuid, String sourceUuid, String sensorUuid, String sensorValue) throws Exception {
		List<RuleEvalResult> ruleEvalResults = null;
		RuleLoginSourceSensor ruleSourceSensor = findRuleLoginSourceSensor( loginUuid, sourceUuid, sensorUuid);
		if( ruleSourceSensor != null ) {
			log.debug(ruleSourceSensor);
			RuleDefItem ruleDefItem = findRuleDefItem( ruleSourceSensor.getRuleUuid());
			if( ruleDefItem == null ) throw new Exception ("Missing RuleDefItem for ruleUuid=" + ruleSourceSensor.getRuleUuid());
			log.debug(ruleDefItem);
			ruleEvalResults = ruleEval( loginUuid, sourceUuid, sensorUuid, sensorValue, ruleDefItem);
		} else {
			if( log.isDebugEnabled()) log.debug("ruleSourceSensor doesn't exist for sourceUuid=" + sourceUuid + ", sensorUuid=" + sensorUuid );
		}
		return ruleEvalResults;
	}
	
	protected List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorUuid, String sensorValue,
			RuleDefItem ruleDefItem) throws Exception {
		List<RuleEvalResult> ruleEvalResults = new ArrayList<RuleEvalResult>();
		for (RuleDefCondItem ruleDefCondItem : ruleDefItem.getRuleDefCondItems()) {
			log.debug(ruleDefCondItem);
			// two part rule evaluation: current sensor value and current
			// actuator, i.e. don't turn actuator on if it is already on
			// evaluate sensor value
			boolean isSensorRuleHit = evalRuleSensor(sensorValue, ruleDefCondItem, ruleDefItem);
			if( log.isDebugEnabled()) log.debug("isSensorRuleHit=" + isSensorRuleHit);
			// only evaluate Actuator rule if the Sensor rule has hit
			if (isSensorRuleHit) {
				RuleEvalResult ruleEvalResult = ruleEvalActuator(loginUuid, sourceUuid, sensorUuid, ruleDefCondItem, ruleDefItem);
				log.debug(ruleEvalResult);
				ruleEvalResults.add(ruleEvalResult);
				if (ruleEvalResult.isRuleActuatorHit()) {
					ruleEvalResult.setActuatorTargetValue(ruleDefCondItem.getActuatorTargetValue());
					if (ruleDefCondItem.getStopEvalOnMatch())
						break;
				}
			}
		}
		log.debug(ruleEvalResults);
		return ruleEvalResults;
	}

	private boolean evalRuleSensor(String sensorValue, RuleDefCondItem ruleDefCondItem, RuleDefItem ruleDefItem)
			throws Exception {
		return ruleEvalCommon(sensorValue, ruleDefCondItem.getSensorTypeValue(),
				ruleDefCondItem.getSensorCompareValue(), ruleDefCondItem.getIntSensorCompareValue(),
				ruleDefCondItem.getDblSensorCompareValue(), ruleDefCondItem.getRuleComparatorSensor(), ruleDefItem);
	}

	private RuleEvalResult ruleEvalActuator(String loginUuid, String sourceUuid, String sensorUuid, RuleDefCondItem ruleDefCondItem,
			RuleDefItem ruleDefItem) throws Exception {
		LoginSourceSensorActuator sourceSensorActuator = findSourceSensorActuator(loginUuid, sourceUuid, sensorUuid);
		if (sourceSensorActuator == null)
			throw new Exception( String.format(
					"Missing SourceSensorActuator, loginUuid=%s, sourceUuid=%s, sensorUuid=%s", loginUuid, sourceUuid, sensorUuid) );
		log.debug(sourceSensorActuator);

		boolean ruleActuatorHit = false;
		if( sourceSensorActuator.getActuatorValue() != null ) {
			ruleActuatorHit = ruleEvalCommon(sourceSensorActuator.getActuatorValue(),
					ruleDefCondItem.getActuatorTypeValue(), ruleDefCondItem.getActuatorCompareValue(),
					ruleDefCondItem.getIntActuatorCompareValue(), ruleDefCondItem.getDblActuatorCompareValue(),
					ruleDefCondItem.getRuleComparatorActuator(), ruleDefItem);
			
		} else {
			// If the ActuatorValue is null it means it hasn't been initialized yet, so force the rule to fire
			// so that an initial state will be set for the actuator
			log.debug("actuator not initialized yet - value is null");
			ruleActuatorHit = true;
		}
		RuleEvalResult ruleEvalResult = new RuleEvalResult(ruleActuatorHit, sourceSensorActuator);
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
