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
package com.pzybrick.iote2e.common.persist;


/**
 * The Class ConfigVo.
 */
public class ConfigVo {
	
	/** The config name. */
	private String configName;
	
	/** The config json. */
	private String configJson;
	
	/**
	 * Instantiates a new config vo.
	 *
	 * @param configName the config name
	 * @param configJson the config json
	 */
	public ConfigVo(String configName, String configJson) {
		super();
		this.configName = configName;
		this.configJson = configJson;
	}
	
	/**
	 * Gets the config name.
	 *
	 * @return the config name
	 */
	public String getConfigName() {
		return configName;
	}
	
	/**
	 * Gets the config json.
	 *
	 * @return the config json
	 */
	public String getConfigJson() {
		return configJson;
	}
	
	/**
	 * Sets the config name.
	 *
	 * @param configName the config name
	 * @return the config vo
	 */
	public ConfigVo setConfigName(String configName) {
		this.configName = configName;
		return this;
	}
	
	/**
	 * Sets the config json.
	 *
	 * @param configJson the config json
	 * @return the config vo
	 */
	public ConfigVo setConfigJson(String configJson) {
		this.configJson = configJson;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigVo [configName=" + configName + ", configJson=" + configJson + "]";
	}
	
	

}
