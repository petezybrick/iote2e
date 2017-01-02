package com.pzybrick.iote2e.ruleproc.svc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.ruleproc.persist.ActuatorStateDao;

public class RuleSvcJsonImpl extends RuleSvc {
	private static final Logger logger = LogManager.getLogger(RuleSvcJsonImpl.class);
	private Map<String, Map<String, RuleLoginSourceSensor>> rssByLoginSourceUuid;
	private List<RuleLoginSourceSensor> ruleLoginSourceSensors;
	private Map<String, RuleDefItem> rdiByRuleUuid;
	private List<RuleDefItem> ruleDefItems;
	private String keyspaceName;

	public RuleSvcJsonImpl() {
		this.rssByLoginSourceUuid = new HashMap<String, Map<String, RuleLoginSourceSensor>>();
		this.rdiByRuleUuid = new HashMap<String, RuleDefItem>();
		this.keyspaceName = System.getenv("CASSANDRA_KEYSPACE_NAME");
	}


	public void init(RuleConfig ruleConfig) throws Exception {
		logger.info(ruleConfig.toString());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// If ActuatorState table doesn't exist or force flag is set then drop/create and populate table
		ActuatorStateDao.useKeyspace(keyspaceName);
		if( ruleConfig.isForceRefreshActuatorState() || !ActuatorStateDao.isTableExists(keyspaceName) ) {
			ActuatorStateDao.dropTable();
			ActuatorStateDao.createTable();
			String rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileActuatorState()));
			List<ActuatorState> actuatorStates = gson.fromJson(rawJson,
					new TypeToken<List<ActuatorState>>() {
					}.getType());
			ActuatorStateDao.insertActuatorStateBatch(actuatorStates);
		} else if( ruleConfig.isForceResetActuatorState()) {
			String rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileActuatorState()));
			List<ActuatorState> actuatorStates = gson.fromJson(rawJson,
					new TypeToken<List<ActuatorState>>() {
					}.getType());
			ActuatorStateDao.resetActuatorStateBatch(actuatorStates);
		}

		String rawJson = FileUtils.readFileToString(new File(ruleConfig.getJsonFileRuleLoginSourceSensor()));
		ruleLoginSourceSensors = gson.fromJson(rawJson,
				new TypeToken<List<RuleLoginSourceSensor>>() {
				}.getType());
		for (RuleLoginSourceSensor ruleLoginSourceSensor : ruleLoginSourceSensors) {
			String key = ruleLoginSourceSensor.getLoginName() + "|" + ruleLoginSourceSensor.getSourceName();
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

	protected ActuatorState findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String pk = String.format("%s|%s|%s", loginUuid, sourceUuid,sensorName);
		return ActuatorStateDao.findActuatorState(pk);
	}

	protected RuleDefItem findRuleDefItem(String ruleUuid) throws Exception {
		return rdiByRuleUuid.get(ruleUuid);
	}


	protected void updateActuatorValue(ActuatorState actuatorState) throws Exception {
		ActuatorStateDao.updateActuatorValue(actuatorState.getPk(), actuatorState.getActuatorValue() );
	}

	protected RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String key = loginUuid + "|" + sourceUuid;
		if( rssByLoginSourceUuid.containsKey(key)
				&& rssByLoginSourceUuid.get(key).containsKey(sensorName) )
				return rssByLoginSourceUuid.get(key).get(sensorName);
		else return null;
	}

}
