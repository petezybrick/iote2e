package com.pzybrick.iote2e.ws.security;

import javax.annotation.Generated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;
import com.pzybrick.iote2e.common.config.MasterConfig;

@Generated("org.jsonschema2pojo")
public class LoginVo {
	private static final Logger logger = LogManager.getLogger(MasterConfig.class);

	@Expose
	private String loginName;
	@Expose
	private String passwordEncrypted;
	@Expose
	private String sourceName;
	@Expose
	private String optionalFilterSensorName;
	
	public LoginVo() {
		super();
	}

	public static Logger getLogger() {
		return logger;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getPasswordEncrypted() {
		return passwordEncrypted;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getOptionalFilterSensorName() {
		return optionalFilterSensorName;
	}

	public LoginVo setLoginName(String login) {
		this.loginName = login;
		return this;
	}

	public LoginVo setPasswordEncrypted(String passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
		return this;
	}

	public LoginVo setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public LoginVo setOptionalFilterSensorName(String optionalFilterSensorName) {
		this.optionalFilterSensorName = optionalFilterSensorName;
		return this;
	}
	
}
