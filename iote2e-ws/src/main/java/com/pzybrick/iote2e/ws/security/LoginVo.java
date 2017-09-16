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
package com.pzybrick.iote2e.ws.security;

import javax.annotation.Generated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;
import com.pzybrick.iote2e.common.config.MasterConfig;


/**
 * The Class LoginVo.
 */
@Generated("org.jsonschema2pojo")
public class LoginVo {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(MasterConfig.class);

	/** The login name. */
	@Expose
	private String loginName;
	
	/** The password encrypted. */
	@Expose
	private String passwordEncrypted;
	
	/** The source name. */
	@Expose
	private String sourceName;
	
	/** The optional filter sensor name. */
	@Expose
	private String optionalFilterSensorName;
	
	/**
	 * Instantiates a new login vo.
	 */
	public LoginVo() {
		super();
	}

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Gets the login name.
	 *
	 * @return the login name
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Gets the password encrypted.
	 *
	 * @return the password encrypted
	 */
	public String getPasswordEncrypted() {
		return passwordEncrypted;
	}

	/**
	 * Gets the source name.
	 *
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Gets the optional filter sensor name.
	 *
	 * @return the optional filter sensor name
	 */
	public String getOptionalFilterSensorName() {
		return optionalFilterSensorName;
	}

	/**
	 * Sets the login name.
	 *
	 * @param login the login
	 * @return the login vo
	 */
	public LoginVo setLoginName(String login) {
		this.loginName = login;
		return this;
	}

	/**
	 * Sets the password encrypted.
	 *
	 * @param passwordEncrypted the password encrypted
	 * @return the login vo
	 */
	public LoginVo setPasswordEncrypted(String passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
		return this;
	}

	/**
	 * Sets the source name.
	 *
	 * @param sourceName the source name
	 * @return the login vo
	 */
	public LoginVo setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	/**
	 * Sets the optional filter sensor name.
	 *
	 * @param optionalFilterSensorName the optional filter sensor name
	 * @return the login vo
	 */
	public LoginVo setOptionalFilterSensorName(String optionalFilterSensorName) {
		this.optionalFilterSensorName = optionalFilterSensorName;
		return this;
	}
	
}
