package com.pzybrick.iote2e.stream.svc;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class RuleDefItem implements Serializable {
	private static final long serialVersionUID = -8827304765202247822L;
	@Expose
	private String ruleUuid;
	@Expose
	private String ruleName;
	@Expose
	private String ruleCustomClassName;
	@Expose
	private List<RuleDefCondItem> ruleDefCondItems;
	
	private RuleCustom ruleCustom;
	
	public RuleDefItem() {
		
	}
	

	public List<RuleDefCondItem> getRuleDefCondItems() {
		return ruleDefCondItems;
	}


	public RuleDefItem setRuleDefCondItems(List<RuleDefCondItem> ruleDefCondItems) {
		this.ruleDefCondItems = ruleDefCondItems;
		return this;
	}

	@Override
	public String toString() {
		return "RuleDefItem [ruleUuid=" + ruleUuid + ", ruleName=" + ruleName + ", ruleCustomClassName="
				+ ruleCustomClassName + ", ruleDefCondItems=" + ruleDefCondItems + ", ruleCustomClass="
				+ ruleCustom + "]";
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


	public String getRuleCustomClassName() {
		return ruleCustomClassName;
	}


	public RuleCustom getRuleCustom() {
		return ruleCustom;
	}


	public RuleDefItem setRuleCustomClassName(String ruleCustomClassName) {
		this.ruleCustomClassName = ruleCustomClassName;
		return this;
	}


	public RuleDefItem setRuleCustom(RuleCustom ruleCustom) {
		this.ruleCustom = ruleCustom;
		return this;
	}

}
