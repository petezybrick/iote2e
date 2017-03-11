package com.pzybrick.iote2e.stream.request;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Iote2eRequestConfig {
	@Expose
	private String ruleSvcClassName;
	@Expose
	private String requestSvcClassName;
	@Expose
	private String ruleConfigKey;

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
				+ requestSvcClassName + ", ruleConfigKey=" + ruleConfigKey + "]";
	}

	public String getRuleConfigKey() {
		return ruleConfigKey;
	}

	public Iote2eRequestConfig setRuleConfigKey(String ruleConfigKey) {
		this.ruleConfigKey = ruleConfigKey;
		return this;
	}

}
