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
	private Map<String, Map<String, SourceSensorActuator>> ssaBySourceUuid;
	private List<SourceSensorActuator> sourceSensorActuators;	
	private Map<String, Map<String, RuleSourceSensor>> rssBySourceUuid;
	private List<RuleSourceSensor> ruleSourceSensors;
	private Map<String, RuleDefItem> rdiByRuleUuid;
	private List<RuleDefItem> ruleDefItems;

	public RuleSvcJsonImpl() {
		this.ssaBySourceUuid = new HashMap<String, Map<String, SourceSensorActuator>>();
		this.rssBySourceUuid = new HashMap<String, Map<String, RuleSourceSensor>>();
		this.rdiByRuleUuid = new HashMap<String, RuleDefItem>();
	}


	public void init(RuleConfig ruleConfig) throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileSourceSensorActuator()));
		sourceSensorActuators = gson.fromJson(rawJson,
				new TypeToken<List<SourceSensorActuator>>() {
				}.getType());
		for (SourceSensorActuator sourceSensorActuator : sourceSensorActuators) {
			Map<String, SourceSensorActuator> mapBySensorUuid = ssaBySourceUuid
					.get(sourceSensorActuator.getSourceUuid());
			if (mapBySensorUuid == null) {
				mapBySensorUuid = new HashMap<String, SourceSensorActuator>();
				ssaBySourceUuid.put(sourceSensorActuator.getSourceUuid(), mapBySensorUuid);
			}
			mapBySensorUuid.put(sourceSensorActuator.getSensorUuid(), sourceSensorActuator);
		}
		
		rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileRuleSourceSensor()));
		ruleSourceSensors = gson.fromJson(rawJson,
				new TypeToken<List<RuleSourceSensor>>() {
				}.getType());
		for (RuleSourceSensor ruleSourceSensor : ruleSourceSensors) {
			Map<String, RuleSourceSensor> mapBySensorUuid = rssBySourceUuid
					.get(ruleSourceSensor.getSourceUuid());
			if (mapBySensorUuid == null) {
				mapBySensorUuid = new HashMap<String, RuleSourceSensor>();
				rssBySourceUuid.put(ruleSourceSensor.getSourceUuid(), mapBySensorUuid);
			}
			mapBySensorUuid.put(ruleSourceSensor.getSensorUuid(), ruleSourceSensor);
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

	protected SourceSensorActuator findSourceSensorActuator(String sourceUuid, String sensorUuid) throws Exception {
		if( ssaBySourceUuid.containsKey(sourceUuid)
				&& ssaBySourceUuid.get(sourceUuid).containsKey(sensorUuid) )
				return ssaBySourceUuid.get(sourceUuid).get(sensorUuid);
		else return null;
	}

	protected RuleDefItem findRuleDefItem(String ruleUuid) throws Exception {
		return rdiByRuleUuid.get(ruleUuid);
	}


	protected void updateActuatorValue(SourceSensorActuator sourceSensorActuator) throws Exception {
		// TODO phase 1- show value, phase 2 write value to ignite
		
	}

	protected RuleSourceSensor findRuleSourceSensor(String sourceUuid, String sensorUuid) throws Exception {
		if( rssBySourceUuid.containsKey(sourceUuid)
				&& rssBySourceUuid.get(sourceUuid).containsKey(sensorUuid) )
				return rssBySourceUuid.get(sourceUuid).get(sensorUuid);
		else return null;
	}


}
