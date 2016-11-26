package com.pzybrick.iote2e.ruleproc.request;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Iote2eRequestConfig {
	@Expose
	private String ruleSvcClassName;
	@Expose
	private String requestSvcClassName;
	@Expose
	private String pathNameExtRuleConfigFile;

	public String getRuleSvcClassName() {
		return ruleSvcClassName;
	}

	public String getRequestSvcClassName() {
		return requestSvcClassName;
	}

	public Iote2eRequestConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}

	public Iote2eRequestConfig setRequestSvcClassName(String sourceResponseSvcClassName) {
		this.requestSvcClassName = sourceResponseSvcClassName;
		return this;
	}

	@Override
	public String toString() {
		return "Iote2eRequestConfig [ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName="
				+ requestSvcClassName + ", pathNameExtRuleConfigFile=" + pathNameExtRuleConfigFile + "]";
	}

	public String getPathNameExtRuleConfigFile() {
		return pathNameExtRuleConfigFile;
	}

	public Iote2eRequestConfig setPathNameExtRuleConfigFile(String pathNameExtRuleConfigFile) {
		this.pathNameExtRuleConfigFile = pathNameExtRuleConfigFile;
		return this;
	}

}
