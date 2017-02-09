package com.pzybrick.iote2e.common.persist;

public class ConfigVo {
	private String configName;
	private String configJson;
	
	public ConfigVo(String configName, String configJson) {
		super();
		this.configName = configName;
		this.configJson = configJson;
	}
	
	public String getConfigName() {
		return configName;
	}
	public String getConfigJson() {
		return configJson;
	}
	public ConfigVo setConfigName(String configName) {
		this.configName = configName;
		return this;
	}
	public ConfigVo setConfigJson(String configJson) {
		this.configJson = configJson;
		return this;
	}

	@Override
	public String toString() {
		return "ConfigVo [configName=" + configName + ", configJson=" + configJson + "]";
	}
	
	

}
