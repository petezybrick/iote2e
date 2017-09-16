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

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class RuleDefItem.
 */
@Generated("org.jsonschema2pojo")
public class RuleDefItem implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8827304765202247822L;
	
	/** The rule uuid. */
	@Expose
	private String ruleUuid;
	
	/** The rule name. */
	@Expose
	private String ruleName;
	
	/** The rule custom class name. */
	@Expose
	private String ruleCustomClassName;
	
	/** The rule def cond items. */
	@Expose
	private List<RuleDefCondItem> ruleDefCondItems;
	
	/** The rule custom. */
	private RuleCustom ruleCustom;
	
	/**
	 * Instantiates a new rule def item.
	 */
	public RuleDefItem() {
		
	}
	

	/**
	 * Gets the rule def cond items.
	 *
	 * @return the rule def cond items
	 */
	public List<RuleDefCondItem> getRuleDefCondItems() {
		return ruleDefCondItems;
	}


	/**
	 * Sets the rule def cond items.
	 *
	 * @param ruleDefCondItems the rule def cond items
	 * @return the rule def item
	 */
	public RuleDefItem setRuleDefCondItems(List<RuleDefCondItem> ruleDefCondItems) {
		this.ruleDefCondItems = ruleDefCondItems;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleDefItem [ruleUuid=" + ruleUuid + ", ruleName=" + ruleName + ", ruleCustomClassName="
				+ ruleCustomClassName + ", ruleDefCondItems=" + ruleDefCondItems + ", ruleCustomClass="
				+ ruleCustom + "]";
	}

	/**
	 * Gets the rule uuid.
	 *
	 * @return the rule uuid
	 */
	public String getRuleUuid() {
		if( this.ruleUuid == null ) this.ruleUuid = UUID.randomUUID().toString();
		return ruleUuid;
	}

	/**
	 * Sets the rule uuid.
	 *
	 * @param ruleUuid the rule uuid
	 * @return the rule def item
	 */
	public RuleDefItem setRuleUuid(String ruleUuid) {
		if( ruleUuid == null ) ruleUuid = UUID.randomUUID().toString();
		this.ruleUuid = ruleUuid;
		return this;
	}

	/**
	 * Gets the rule name.
	 *
	 * @return the rule name
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * Sets the rule name.
	 *
	 * @param ruleName the rule name
	 * @return the rule def item
	 */
	public RuleDefItem setRuleName(String ruleName) {
		this.ruleName = ruleName;
		return this;
	}


	/**
	 * Gets the rule custom class name.
	 *
	 * @return the rule custom class name
	 */
	public String getRuleCustomClassName() {
		return ruleCustomClassName;
	}


	/**
	 * Gets the rule custom.
	 *
	 * @return the rule custom
	 */
	public RuleCustom getRuleCustom() {
		return ruleCustom;
	}


	/**
	 * Sets the rule custom class name.
	 *
	 * @param ruleCustomClassName the rule custom class name
	 * @return the rule def item
	 */
	public RuleDefItem setRuleCustomClassName(String ruleCustomClassName) {
		this.ruleCustomClassName = ruleCustomClassName;
		return this;
	}


	/**
	 * Sets the rule custom.
	 *
	 * @param ruleCustom the rule custom
	 * @return the rule def item
	 */
	public RuleDefItem setRuleCustom(RuleCustom ruleCustom) {
		this.ruleCustom = ruleCustom;
		return this;
	}

}
