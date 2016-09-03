package com.pzybrick.iote2e.ruleproc.svc;

import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleDefItem {
	@Expose
	private String ruleUuid;
	@Expose
	private String ruleName;
	@Expose
	private String customPkgClass;
	@Expose
	private List<RuleDefCondItem> ruleDefCondItems;
	
	public RuleDefItem() {
		
	}
	
	public String getCustomPkgClass() {
		return customPkgClass;
	}
	public List<RuleDefCondItem> getRuleDefCondItems() {
		return ruleDefCondItems;
	}

	public RuleDefItem setCustomPkgClass(String customPkgClass) {
		this.customPkgClass = customPkgClass;
		return this;
	}
	public RuleDefItem setRuleDefCondItems(List<RuleDefCondItem> ruleDefCondItems) {
		this.ruleDefCondItems = ruleDefCondItems;
		return this;
	}

	@Override
	public String toString() {
		return "RuleDefItem [ruleUuid=" + ruleUuid + ", ruleName=" + ruleName + ", customPkgClass=" + customPkgClass
				+ ", ruleDefCondItems=" + ruleDefCondItems + "]";
	}

	public String getRuleUuid() {
		if( this.ruleUuid == null ) this.ruleUuid = UUID.randomUUID().toString();
		return ruleUuid;
	}

	public RuleDefItem setRuleUuid(String ruleUuid) {
		if( ruleUuid == null ) ruleUuid = UUID.randomUUID().toString();
		this.ruleUuid = ruleUuid;
		return this;
	}

	public String getRuleName() {
		return ruleName;
	}

	public RuleDefItem setRuleName(String ruleName) {
		this.ruleName = ruleName;
		return this;
	}

}
