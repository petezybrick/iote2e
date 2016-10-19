package com.pzybrick.iote2e.ruleproc.sourcesensor;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class LoginSourceSensorConfig {
	@Expose
	private String ruleSvcClassName;
	@Expose
	private String sourceResponseSvcClassName;
	@Expose
	private String pathNameExtRuleConfigFile;

	public String getRuleSvcClassName() {
		return ruleSvcClassName;
	}

	public String getSourceResponseSvcClassName() {
		return sourceResponseSvcClassName;
	}

	public LoginSourceSensorConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}

	public LoginSourceSensorConfig setSourceResponseSvcClassName(String sourceResponseSvcClassName) {
		this.sourceResponseSvcClassName = sourceResponseSvcClassName;
		return this;
	}

	@Override
	public String toString() {
		return "SourceSensorConfig [ruleSvcClassName=" + ruleSvcClassName + ", sourceResponseSvcClassName="
				+ sourceResponseSvcClassName + ", pathNameExtRuleConfigFile=" + pathNameExtRuleConfigFile + "]";
	}

	public String getPathNameExtRuleConfigFile() {
		return pathNameExtRuleConfigFile;
	}

	public LoginSourceSensorConfig setPathNameExtRuleConfigFile(String pathNameExtRuleConfigFile) {
		this.pathNameExtRuleConfigFile = pathNameExtRuleConfigFile;
		return this;
	}

}
