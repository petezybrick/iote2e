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
package com.pzybrick.iote2e.stream.request;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;


/**
 * The Class Iote2eRequestConfig.
 */
@Generated("org.jsonschema2pojo")
public class Iote2eRequestConfig {
	
	/** The rule svc class name. */
	@Expose
	private String ruleSvcClassName;
	
	/** The request svc class name. */
	@Expose
	private String requestSvcClassName;
	
	/** The rule config key. */
	@Expose
	private String ruleConfigKey;

	/**
	 * Gets the rule svc class name.
	 *
	 * @return the rule svc class name
	 */
	public String getRuleSvcClassName() {
		return ruleSvcClassName;
	}

	/**
	 * Gets the request svc class name.
	 *
	 * @return the request svc class name
	 */
	public String getRequestSvcClassName() {
		return requestSvcClassName;
	}

	/**
	 * Sets the rule svc class name.
	 *
	 * @param ruleSvcClassName the rule svc class name
	 * @return the iote 2 e request config
	 */
	public Iote2eRequestConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}

	/**
	 * Sets the request svc class name.
	 *
	 * @param sourceResponseSvcClassName the source response svc class name
	 * @return the iote 2 e request config
	 */
	public Iote2eRequestConfig setRequestSvcClassName(String sourceResponseSvcClassName) {
		this.requestSvcClassName = sourceResponseSvcClassName;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Iote2eRequestConfig [ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName="
				+ requestSvcClassName + ", ruleConfigKey=" + ruleConfigKey + "]";
	}

	/**
	 * Gets the rule config key.
	 *
	 * @return the rule config key
	 */
	public String getRuleConfigKey() {
		return ruleConfigKey;
	}

	/**
	 * Sets the rule config key.
	 *
	 * @param ruleConfigKey the rule config key
	 * @return the iote 2 e request config
	 */
	public Iote2eRequestConfig setRuleConfigKey(String ruleConfigKey) {
		this.ruleConfigKey = ruleConfigKey;
		return this;
	}

}
