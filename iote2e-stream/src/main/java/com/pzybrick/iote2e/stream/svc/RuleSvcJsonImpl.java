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


/**
 * The Class RuleSvcJsonImpl.
 */
public class RuleSvcJsonImpl extends RuleSvc {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RuleSvcJsonImpl.class);
	
	/** The rss by login source uuid. */
	private Map<String, Map<String, RuleLoginSourceSensor>> rssByLoginSourceUuid;
	
	/** The rule login source sensors. */
	private List<RuleLoginSourceSensor> ruleLoginSourceSensors;
	
	/** The rdi by rule uuid. */
	private Map<String, RuleDefItem> rdiByRuleUuid;
	
	/** The rule def items. */
	private List<RuleDefItem> ruleDefItems;

	/**
	 * Instantiates a new rule svc json impl.
	 */
	public RuleSvcJsonImpl() {
		this.rssByLoginSourceUuid = new HashMap<String, Map<String, RuleLoginSourceSensor>>();
		this.rdiByRuleUuid = new HashMap<String, RuleDefItem>();
	}


	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleSvc#init(com.pzybrick.iote2e.common.config.MasterConfig)
	 */
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
		// same rule def can apply to multiple logins and/or sourcenames, i.e. pzybrick1, joe2, rpi-001, rpi-002, etc.
		// If login and/or source name are pipe delimited, then create a separate source for each distinct login sourcename
		expandLoginsSourceNames();
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
				ruleCustom.setMasterConfig(masterConfig);
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
	
	/**
	 * Expand logins source names.
	 *
	 * @throws Exception the exception
	 */
	protected void expandLoginsSourceNames() throws Exception {
		ListIterator<RuleLoginSourceSensor> lit = ruleLoginSourceSensors.listIterator();
		while( lit.hasNext() ) {
			RuleLoginSourceSensor ruleLoginSourceSensor = lit.next();
			if(ruleLoginSourceSensor.getLoginName().indexOf("|") > -1 || ruleLoginSourceSensor.getSourceName().indexOf("|") > -1 ) {
				lit.remove();
				List<String> loginNames = Arrays.asList( ruleLoginSourceSensor.getLoginName().split("[|]"));
				List<String> sourceNames = Arrays.asList( ruleLoginSourceSensor.getSourceName().split("[|]"));
				for( String loginName : loginNames ) {
					for( String sourceName : sourceNames ) {
						RuleLoginSourceSensor clone = ruleLoginSourceSensor.clone();
						clone.setLoginName(loginName);
						clone.setSourceName(sourceName);
						lit.add(clone);
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleSvc#findSourceSensorActuator(java.lang.String, java.lang.String, java.lang.String)
	 */
	protected ActuatorState findSourceSensorActuator(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String pk = String.format("%s|%s|%s|", loginUuid, sourceUuid,sensorName);
		return ActuatorStateDao.findActuatorState(pk);
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleSvc#findRuleDefItem(java.lang.String)
	 */
	protected RuleDefItem findRuleDefItem(String ruleUuid) throws Exception {
		return rdiByRuleUuid.get(ruleUuid);
	}


	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleSvc#updateActuatorValue(com.pzybrick.iote2e.stream.svc.ActuatorState)
	 */
	protected void updateActuatorValue(ActuatorState actuatorState) throws Exception {
		ActuatorStateDao.updateActuatorValue(actuatorState.getPk(), actuatorState.getActuatorValue() );
	}

	/* (non-Javadoc)
	 * @see com.pzybrick.iote2e.stream.svc.RuleSvc#findRuleLoginSourceSensor(java.lang.String, java.lang.String, java.lang.String)
	 */
	protected RuleLoginSourceSensor findRuleLoginSourceSensor(String loginUuid, String sourceUuid, String sensorName) throws Exception {
		String key = loginUuid + "|" + sourceUuid;
		if( rssByLoginSourceUuid.containsKey(key)
				&& rssByLoginSourceUuid.get(key).containsKey(sensorName) )
				return rssByLoginSourceUuid.get(key).get(sensorName);
		else return null;
	}

}
