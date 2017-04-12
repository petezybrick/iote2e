package com.pzybrick.iote2e.stream.svc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.reflect.TypeToken;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.common.persist.ConfigDao;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.persist.ActuatorStateDao;

public class RuleSvcJsonImpl extends RuleSvc {
	private static final Logger logger = LogManager.getLogger(RuleSvcJsonImpl.class);
	private Map<String, Map<String, RuleLoginSourceSensor>> rssByLoginSourceUuid;
	private List<RuleLoginSourceSensor> ruleLoginSourceSensors;
	private Map<String, RuleDefItem> rdiByRuleUuid;
	private List<RuleDefItem> ruleDefItems;

	public RuleSvcJsonImpl() {
		this.rssByLoginSourceUuid = new HashMap<String, Map<String, RuleLoginSourceSensor>>();
		this.rdiByRuleUuid = new HashMap<String, RuleDefItem>();
	}


	public void init(MasterConfig masterConfig) throws Exception {
		logger.info(masterConfig.toString());
		ConfigDao.connect(masterConfig.getContactPoint(), masterConfig.getKeyspaceName());
		// If ActuatorState table doesn't exist or force flag is set then drop/create and populate table
		ActuatorStateDao.connect(masterConfig.getContactPoint(), masterConfig.getKeyspaceName());
		if( masterConfig.isForceRefreshActuatorState() || !ActuatorStateDao.isTableExists(masterConfig.getKeyspaceName()) ) {
			ActuatorStateDao.dropTable();
			ActuatorStateDao.createTable();
			String rawJson = ConfigDao.findConfigJson(masterConfig.getActuatorStateKey());
			List<ActuatorState> actuatorStates = ActuatorStateDao.createActuatorStatesFromJson(rawJson);
			ActuatorStateDao.insertActuatorStateBatch(actuatorStates);
		} else if( masterConfig.isForceResetActuatorState()) {
			String rawJson = ConfigDao.findConfigJson(masterConfig.getActuatorStateKey());
			List<ActuatorState> actuatorStates = ActuatorStateDao.createActuatorStatesFromJson(rawJson);
			ActuatorStateDao.resetActuatorStateBatch(actuatorStates);
		}

		String rawJson = ConfigDao.findConfigJson(masterConfig.getRuleLoginSourceSensorKey());
		ruleLoginSourceSensors = Iote2eUtils.getGsonInstance().fromJson(rawJson,
				new TypeToken<List<RuleLoginSourceSensor>>() {
				}.getType());
		// same rule def can apply to multiple sourcenames, i.e. rpi-001, rpi-002, etc.
		// If source name is pipe delimited, then create a separate source for each distinct sourcename
		expandSourceNames();
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
		
		rawJson = ConfigDao.findConfigJson(masterConfig.getRuleDefItemKey());
		ruleDefItems = Iote2eUtils.getGsonInstance().fromJson(rawJson,
				new TypeToken<List<RuleDefItem>>() {
				}.getType());
		for( RuleDefItem ruleDefItem : ruleDefItems ) {
			// Cache the custom rule class, if specified
			if( ruleDefItem.getRuleCustomClassName() != null ) {
				Class cls = Class.forName(ruleDefItem.getRuleCustomClassName());
				RuleCustom ruleCustom = (RuleCustom) cls.newInstance();
				ruleDefItem.setRuleCustom(ruleCustom);
			}
			if( ruleDefItem.getRuleDefCondItems() != null ) {
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
			}
			rdiByRuleUuid.put(ruleDefItem.getRuleUuid(), ruleDefItem );
		}
	}
	
	protected void expandSourceNames() throws Exception {
		ListIterator<RuleLoginSourceSensor> lit = ruleLoginSourceSensors.listIterator();
		while( lit.hasNext() ) {
			RuleLoginSourceSensor ruleLoginSourceSensor = lit.next();
			if(ruleLoginSourceSensor.getSourceName().indexOf("|") > -1 ) {
				lit.remove();
				List<String> sourceNames = Arrays.asList( ruleLoginSourceSensor.getSourceName().split("[|]"));
				for( String sourceName : sourceNames ) {
					RuleLoginSourceSensor clone = ruleLoginSourceSensor.clone();
					clone.setSourceName(sourceName);
					lit.add(clone);
				}
			}
		}
	}

	protected ActuatorState findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String pk = String.format("%s|%s|%s|", loginUuid, sourceUuid,sensorName);
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
