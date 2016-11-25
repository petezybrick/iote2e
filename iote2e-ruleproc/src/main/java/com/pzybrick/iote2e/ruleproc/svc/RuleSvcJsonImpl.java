package com.pzybrick.iote2e.ruleproc.svc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RuleSvcJsonImpl extends RuleSvc {
	private static final Log log = LogFactory.getLog(RuleSvcJsonImpl.class);
	private Map<String, Map<String, LoginSourceSensorActuator>> ssaByLoginSourceUuid;
	private List<LoginSourceSensorActuator> loginSourceSensorActuators;	
	private Map<String, Map<String, RuleLoginSourceSensor>> rssByLoginSourceUuid;
	private List<RuleLoginSourceSensor> ruleLoginSourceSensors;
	private Map<String, RuleDefItem> rdiByRuleUuid;
	private List<RuleDefItem> ruleDefItems;

	public RuleSvcJsonImpl() {
		this.ssaByLoginSourceUuid = new HashMap<String, Map<String, LoginSourceSensorActuator>>();
		this.rssByLoginSourceUuid = new HashMap<String, Map<String, RuleLoginSourceSensor>>();
		this.rdiByRuleUuid = new HashMap<String, RuleDefItem>();
	}


	public void init(RuleConfig ruleConfig) throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileLoginSourceSensorActuator()));
		loginSourceSensorActuators = gson.fromJson(rawJson,
				new TypeToken<List<LoginSourceSensorActuator>>() {
				}.getType());
		for (LoginSourceSensorActuator loginSourceSensorActuator : loginSourceSensorActuators) {
			String key = loginSourceSensorActuator.getLoginUuid() + "|" + loginSourceSensorActuator.getSourceUuid();
			Map<String, LoginSourceSensorActuator> mapByLoginSensorName = ssaByLoginSourceUuid
					.get(key);
			if (mapByLoginSensorName == null) {
				mapByLoginSensorName = new HashMap<String, LoginSourceSensorActuator>();
				ssaByLoginSourceUuid.put(key, mapByLoginSensorName);
			}
			mapByLoginSensorName.put(loginSourceSensorActuator.getSensorName(), loginSourceSensorActuator);
		}
		
		rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileRuleLoginSourceSensor()));
		ruleLoginSourceSensors = gson.fromJson(rawJson,
				new TypeToken<List<RuleLoginSourceSensor>>() {
				}.getType());
		for (RuleLoginSourceSensor ruleLoginSourceSensor : ruleLoginSourceSensors) {
			String key = ruleLoginSourceSensor.getLoginUuid() + "|" + ruleLoginSourceSensor.getSourceUuid();
			Map<String, RuleLoginSourceSensor> mapBySensorName = rssByLoginSourceUuid
					.get(key);
			if (mapBySensorName == null) {
				mapBySensorName = new HashMap<String, RuleLoginSourceSensor>();
				rssByLoginSourceUuid.put(key, mapBySensorName);
			}
			mapBySensorName.put(ruleLoginSourceSensor.getSensorName(), ruleLoginSourceSensor);
		}
		
		rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileRuleDefItem()));
		ruleDefItems = gson.fromJson(rawJson,
				new TypeToken<List<RuleDefItem>>() {
				}.getType());
		for( RuleDefItem ruleDefItem : ruleDefItems ) {
			for( RuleDefCondItem ruleDefCondItem : ruleDefItem.getRuleDefCondItems() ) {
				// convert the compare and actuator values once to improve rule evaluation performance
				// sensor
				if( "dbl".equals(ruleDefCondItem.getSensorTypeValue())) {
					ruleDefCondItem.setDblSensorCompareValue( Double.parseDouble(ruleDefCondItem.getSensorCompareValue()));
					
				} else if( "int".equals(ruleDefCondItem.getSensorTypeValue())) {
					ruleDefCondItem.setIntSensorCompareValue( Integer.parseInt(ruleDefCondItem.getSensorCompareValue()));
				}
				// actuator
				if( "dbl".equals(ruleDefCondItem.getActuatorTypeValue())) {
					ruleDefCondItem.setDblActuatorCompareValue( Double.parseDouble(ruleDefCondItem.getActuatorCompareValue()));
					
				} else if( "int".equals(ruleDefCondItem.getActuatorTypeValue())) {
					ruleDefCondItem.setIntActuatorCompareValue( Integer.parseInt(ruleDefCondItem.getActuatorCompareValue()));
				}
				
				ruleDefCondItem.setRuleComparatorSensor(ruleDefCondItem.ruleComparatorFromString(ruleDefCondItem.getSensorComparator())); 
				ruleDefCondItem.setRuleComparatorActuator(ruleDefCondItem.ruleComparatorFromString(ruleDefCondItem.getActuatorComparator())); 
			}
			rdiByRuleUuid.put(ruleDefItem.getRuleUuid(), ruleDefItem );
		}
	}

	protected LoginSourceSensorActuator findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String key = loginUuid + "|" + sourceUuid;
		if( ssaByLoginSourceUuid.containsKey(key)
				&& ssaByLoginSourceUuid.get(key).containsKey(sensorName) )
				return ssaByLoginSourceUuid.get(key).get(sensorName);
		else return null;
	}

	protected RuleDefItem findRuleDefItem(String ruleUuid) throws Exception {
		return rdiByRuleUuid.get(ruleUuid);
	}


	protected void updateActuatorValue(LoginSourceSensorActuator sourceSensorActuator) throws Exception {
		// TODO phase 1- show value, phase 2 write value to ignite
		
	}

	protected RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String key = loginUuid + "|" + sourceUuid;
		if( rssByLoginSourceUuid.containsKey(key)
				&& rssByLoginSourceUuid.get(key).containsKey(sensorName) )
				return rssByLoginSourceUuid.get(key).get(sensorName);
		else return null;
	}

}
