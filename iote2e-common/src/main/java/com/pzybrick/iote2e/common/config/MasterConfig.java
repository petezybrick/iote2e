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
package com.pzybrick.iote2e.common.config;

import java.io.Serializable;

import javax.annotation.Generated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.pzybrick.iote2e.common.persist.CassandraBaseDao;
import com.pzybrick.iote2e.common.persist.ConfigDao;


/**
 * The Class MasterConfig.
 */
@Generated("org.jsonschema2pojo")
public class MasterConfig implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2980557216488140670L;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(MasterConfig.class);
	
	/** The master config json key. */
	private String masterConfigJsonKey;
	
	/** The contact point. */
	private String contactPoint;
	
	/** The keyspace name. */
	private String keyspaceName;
	
	/** The master config. */
	private static MasterConfig masterConfig;
	
	/** The rule svc class name. */
	@Expose
	private String ruleSvcClassName;
	
	/** The request svc class name. */
	@Expose
	private String requestSvcClassName;
	
	/** The actuator state key. */
	@Expose 
	private String actuatorStateKey;
	
	/** The rule login source sensor key. */
	@Expose 
	private String ruleLoginSourceSensorKey;
	
	/** The rule def item key. */
	@Expose 
	private String ruleDefItemKey;
	
	/** The ignite cache name. */
	@Expose
	private String igniteCacheName;
	
	/** The ignite config file. */
	@Expose
	private String igniteConfigFile;
	
	/** The ignite config name. */
	@Expose
	private String igniteConfigName;
	
	/** The ignite client mode. */
	@Expose
	private boolean igniteClientMode;
	
	/** The ignite config path. */
	@Expose
	private String igniteConfigPath;
	
	/** The force refresh actuator state. */
	@Expose
	private boolean forceRefreshActuatorState;
	
	/** The force reset actuator state. */
	@Expose
	private boolean forceResetActuatorState;
	
	/** The jdbc driver class name. */
	@Expose
	private String jdbcDriverClassName;
	
	/** The jdbc insert block size. */
	@Expose
	private Integer jdbcInsertBlockSize;
	
	/** The jdbc login. */
	@Expose
	private String jdbcLogin;
	
	/** The jdbc password. */
	@Expose
	private String jdbcPassword;
	
	/** The jdbc url. */
	@Expose
	private String jdbcUrl;
	
	/** The kafka group. */
	@Expose
	private String kafkaGroup;
	
	/** The kafka group omh. */
	@Expose
	private String kafkaGroupOmh;
	
	/** The kafka group bdbb. */
	@Expose
	private String kafkaGroupBdbb;
	
	/** The kafka topic. */
	@Expose
	private String kafkaTopic;
	
	/** The kafka topic omh. */
	@Expose
	private String kafkaTopicOmh;
	
	/** The kafka topic bdbb. */
	@Expose
	private String kafkaTopicBdbb;
	
	/** The kafka bootstrap servers. */
	@Expose
	private String kafkaBootstrapServers;
	
	/** The kafka zookeeper hosts. */
	@Expose
	private String kafkaZookeeperHosts;
	
	/** The kafka zookeeper port. */
	@Expose
	private Integer kafkaZookeeperPort;
	
	/** The kafka consumer num threads. */
	@Expose
	private Integer kafkaConsumerNumThreads;
	
	/** The kafka zookeeper broker path. */
	@Expose
	private String kafkaZookeeperBrokerPath;
	
	/** The kafka consumer id. */
	@Expose
	private String kafkaConsumerId;
	
	/** The kafka zookeeper consumer connection. */
	@Expose
	private String kafkaZookeeperConsumerConnection;
	
	/** The kafka zookeeper consumer path. */
	@Expose
	private String kafkaZookeeperConsumerPath;
	
	/** The router iote 2 e request class name. */
	@Expose
	private String routerIote2eRequestClassName;
	
	/** The router omh class name. */
	@Expose
	private String routerOmhClassName;
	
	/** The router bdbb class name. */
	@Expose
	private String routerBdbbClassName;
	
	/** The spark app name. */
	@Expose
	private String sparkAppName;
	
	/** The spark app name bdbb. */
	@Expose
	private String sparkAppNameBdbb;
	
	/** The spark app name omh. */
	@Expose
	private String sparkAppNameOmh;
	
	/** The spark master. */
	@Expose
	private String sparkMaster;
	
	/** The smtp email omh. */
	@Expose
	private String smtpEmailOmh;
	
	/** The smtp login omh. */
	@Expose
	private String smtpLoginOmh;
	
	/** The smtp password omh. */
	@Expose
	private String smtpPasswordOmh;
	
	/** The smtp email bdbb. */
	@Expose
	private String smtpEmailBdbb;
	
	/** The smtp login bdbb. */
	@Expose
	private String smtpLoginBdbb;
	
	/** The smtp password bdbb. */
	@Expose
	private String smtpPasswordBdbb;
	
	/** The spark stream duration ms. */
	@Expose
	private Integer sparkStreamDurationMs;
	
	/** The web server port. */
	@Expose
	private Integer webServerPort;
	
	/** The web server content path. */
	@Expose
	private String webServerContentPath;
	
	/** The web server context path. */
	@Expose
	private String webServerContextPath;
	
	/** The ws bdbb server listen port. */
	@Expose
	private Integer wsBdbbServerListenPort;
	
	/** The ws nrt server listen port. */
	@Expose
	private Integer wsNrtServerListenPort;
	
	/** The ws omh server listen port. */
	@Expose
	private Integer wsOmhServerListenPort;
	
	/** The ws router impl class name. */
	@Expose
	private String wsRouterImplClassName;
	
	/** The ws bdbb router impl class name. */
	@Expose
	private String wsBdbbRouterImplClassName;
	
	/** The ws omh router impl class name. */
	@Expose
	private String wsOmhRouterImplClassName;
	
	/** The ws server listen port. */
	@Expose
	private Integer wsServerListenPort;
	

	/**
	 * Instantiates a new master config.
	 */
	private MasterConfig() {
		
	}

	
	/**
	 * Gets the single instance of MasterConfig.
	 *
	 * @return single instance of MasterConfig
	 * @throws Exception the exception
	 */
	public static MasterConfig getInstance( ) throws Exception {
		if( MasterConfig.masterConfig == null ) throw new Exception("MasterConfig was never initialized");
			return MasterConfig.masterConfig;
	}

		
	/**
	 * Gets the single instance of MasterConfig.
	 *
	 * @param masterConfigJsonKey the master config json key
	 * @param contactPoint the contact point
	 * @param keyspaceName the keyspace name
	 * @return single instance of MasterConfig
	 * @throws Exception the exception
	 */
	public static MasterConfig getInstance( String masterConfigJsonKey, String contactPoint, String keyspaceName ) throws Exception {
		if( MasterConfig.masterConfig != null ) return MasterConfig.masterConfig;
		MasterConfig masterConfigNew = null;
		final int RETRY_MINUTES = 10;
		long maxWait = System.currentTimeMillis() + (RETRY_MINUTES * 60 * 1000);
		Exception exception = null;
		logger.info("Instantiating MasterConfig");
		if( keyspaceName == null ) keyspaceName = CassandraBaseDao.DEFAULT_KEYSPACE_NAME;
		
		while( true ) {
			try {
				ConfigDao.connect(contactPoint, keyspaceName);
				String rawJson = ConfigDao.findConfigJson(masterConfigJsonKey);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				masterConfigNew = gson.fromJson(rawJson, MasterConfig.class);
				masterConfigNew.setContactPoint(contactPoint);
				masterConfigNew.setKeyspaceName(keyspaceName);
				MasterConfig.masterConfig = masterConfigNew;
				return masterConfigNew;
			} catch(Exception e ) {
				exception = e;
				ConfigDao.disconnect();
			}
			if( System.currentTimeMillis() > maxWait ) break;
			logger.debug("retrying Cassandra connection");
			try { Thread.sleep(5000); } catch(Exception e) {}
		}
		throw exception;
	}
	
	
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
	 * Gets the actuator state key.
	 *
	 * @return the actuator state key
	 */
	public String getActuatorStateKey() {
		return actuatorStateKey;
	}
	
	/**
	 * Gets the rule login source sensor key.
	 *
	 * @return the rule login source sensor key
	 */
	public String getRuleLoginSourceSensorKey() {
		return ruleLoginSourceSensorKey;
	}
	
	/**
	 * Gets the rule def item key.
	 *
	 * @return the rule def item key
	 */
	public String getRuleDefItemKey() {
		return ruleDefItemKey;
	}
	
	/**
	 * Gets the ignite cache name.
	 *
	 * @return the ignite cache name
	 */
	public String getIgniteCacheName() {
		return igniteCacheName;
	}
	
	/**
	 * Gets the ignite config file.
	 *
	 * @return the ignite config file
	 */
	public String getIgniteConfigFile() {
		return igniteConfigFile;
	}
	
	/**
	 * Gets the ignite config name.
	 *
	 * @return the ignite config name
	 */
	public String getIgniteConfigName() {
		return igniteConfigName;
	}
	
	/**
	 * Checks if is ignite client mode.
	 *
	 * @return true, if is ignite client mode
	 */
	public boolean isIgniteClientMode() {
		return igniteClientMode;
	}
	
	/**
	 * Checks if is force refresh actuator state.
	 *
	 * @return true, if is force refresh actuator state
	 */
	public boolean isForceRefreshActuatorState() {
		return forceRefreshActuatorState;
	}
	
	/**
	 * Checks if is force reset actuator state.
	 *
	 * @return true, if is force reset actuator state
	 */
	public boolean isForceResetActuatorState() {
		return forceResetActuatorState;
	}
	
	/**
	 * Sets the rule svc class name.
	 *
	 * @param ruleSvcClassName the rule svc class name
	 * @return the master config
	 */
	public MasterConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}
	
	/**
	 * Sets the request svc class name.
	 *
	 * @param requestSvcClassName the request svc class name
	 * @return the master config
	 */
	public MasterConfig setRequestSvcClassName(String requestSvcClassName) {
		this.requestSvcClassName = requestSvcClassName;
		return this;
	}
	
	/**
	 * Sets the actuator state key.
	 *
	 * @param actuatorStateKey the actuator state key
	 * @return the master config
	 */
	public MasterConfig setActuatorStateKey(String actuatorStateKey) {
		this.actuatorStateKey = actuatorStateKey;
		return this;
	}
	
	/**
	 * Sets the rule login source sensor key.
	 *
	 * @param ruleLoginSourceSensorKey the rule login source sensor key
	 * @return the master config
	 */
	public MasterConfig setRuleLoginSourceSensorKey(String ruleLoginSourceSensorKey) {
		this.ruleLoginSourceSensorKey = ruleLoginSourceSensorKey;
		return this;
	}
	
	/**
	 * Sets the rule def item key.
	 *
	 * @param ruleDefItemKey the rule def item key
	 * @return the master config
	 */
	public MasterConfig setRuleDefItemKey(String ruleDefItemKey) {
		this.ruleDefItemKey = ruleDefItemKey;
		return this;
	}
	
	/**
	 * Sets the ignite cache name.
	 *
	 * @param igniteCacheName the ignite cache name
	 * @return the master config
	 */
	public MasterConfig setIgniteCacheName(String igniteCacheName) {
		this.igniteCacheName = igniteCacheName;
		return this;
	}
	
	/**
	 * Sets the ignite config file.
	 *
	 * @param igniteConfigFile the ignite config file
	 * @return the master config
	 */
	public MasterConfig setIgniteConfigFile(String igniteConfigFile) {
		this.igniteConfigFile = igniteConfigFile;
		return this;
	}
	
	/**
	 * Sets the ignite config name.
	 *
	 * @param igniteConfigName the ignite config name
	 * @return the master config
	 */
	public MasterConfig setIgniteConfigName(String igniteConfigName) {
		this.igniteConfigName = igniteConfigName;
		return this;
	}
	
	/**
	 * Sets the ignite client mode.
	 *
	 * @param igniteClientMode the ignite client mode
	 * @return the master config
	 */
	public MasterConfig setIgniteClientMode(boolean igniteClientMode) {
		this.igniteClientMode = igniteClientMode;
		return this;
	}
	
	/**
	 * Sets the force refresh actuator state.
	 *
	 * @param forceRefreshActuatorState the force refresh actuator state
	 * @return the master config
	 */
	public MasterConfig setForceRefreshActuatorState(boolean forceRefreshActuatorState) {
		this.forceRefreshActuatorState = forceRefreshActuatorState;
		return this;
	}
	
	/**
	 * Sets the force reset actuator state.
	 *
	 * @param forceResetActuatorState the force reset actuator state
	 * @return the master config
	 */
	public MasterConfig setForceResetActuatorState(boolean forceResetActuatorState) {
		this.forceResetActuatorState = forceResetActuatorState;
		return this;
	}

	/**
	 * Gets the kafka group.
	 *
	 * @return the kafka group
	 */
	public String getKafkaGroup() {
		return kafkaGroup;
	}

	/**
	 * Gets the kafka topic.
	 *
	 * @return the kafka topic
	 */
	public String getKafkaTopic() {
		return kafkaTopic;
	}

	/**
	 * Gets the kafka bootstrap servers.
	 *
	 * @return the kafka bootstrap servers
	 */
	public String getKafkaBootstrapServers() {
		return kafkaBootstrapServers;
	}

	/**
	 * Gets the kafka zookeeper hosts.
	 *
	 * @return the kafka zookeeper hosts
	 */
	public String getKafkaZookeeperHosts() {
		return kafkaZookeeperHosts;
	}

	/**
	 * Gets the kafka zookeeper port.
	 *
	 * @return the kafka zookeeper port
	 */
	public Integer getKafkaZookeeperPort() {
		return kafkaZookeeperPort;
	}

	/**
	 * Gets the kafka consumer num threads.
	 *
	 * @return the kafka consumer num threads
	 */
	public Integer getKafkaConsumerNumThreads() {
		return kafkaConsumerNumThreads;
	}

	/**
	 * Gets the ignite config path.
	 *
	 * @return the ignite config path
	 */
	public String getIgniteConfigPath() {
		return igniteConfigPath;
	}

	/**
	 * Sets the kafka group.
	 *
	 * @param kafkaGroup the kafka group
	 * @return the master config
	 */
	public MasterConfig setKafkaGroup(String kafkaGroup) {
		this.kafkaGroup = kafkaGroup;
		return this;
	}

	/**
	 * Sets the kafka topic.
	 *
	 * @param kafkaTopic the kafka topic
	 * @return the master config
	 */
	public MasterConfig setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
		return this;
	}

	/**
	 * Sets the kafka bootstrap servers.
	 *
	 * @param kafkaBootstrapServers the kafka bootstrap servers
	 * @return the master config
	 */
	public MasterConfig setKafkaBootstrapServers(String kafkaBootstrapServers) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
		return this;
	}

	/**
	 * Sets the kafka zookeeper hosts.
	 *
	 * @param kafkaZookeeperHosts the kafka zookeeper hosts
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeperHosts(String kafkaZookeeperHosts) {
		this.kafkaZookeeperHosts = kafkaZookeeperHosts;
		return this;
	}

	/**
	 * Sets the kafka zookeeper.
	 *
	 * @param kafkaZookeeperPort the kafka zookeeper port
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeper(Integer kafkaZookeeperPort) {
		this.kafkaZookeeperPort = kafkaZookeeperPort;
		return this;
	}

	/**
	 * Sets the kafka consumer num threads.
	 *
	 * @param kafkaConsumerNumThreads the kafka consumer num threads
	 * @return the master config
	 */
	public MasterConfig setKafkaConsumerNumThreads(Integer kafkaConsumerNumThreads) {
		this.kafkaConsumerNumThreads = kafkaConsumerNumThreads;
		return this;
	}

	/**
	 * Sets the ignite config path.
	 *
	 * @param igniteConfigPath the ignite config path
	 * @return the master config
	 */
	public MasterConfig setIgniteConfigPath(String igniteConfigPath) {
		this.igniteConfigPath = igniteConfigPath;
		return this;
	}
	
	/**
	 * Creates the kafka zookeeper host port pairs.
	 *
	 * @return the string
	 */
	public String createKafkaZookeeperHostPortPairs( ) {
		StringBuilder sb = new StringBuilder();
		String[] hosts = kafkaZookeeperHosts.split("[,]");
		for( String host : hosts ) {
			if( sb.length() > 0 ) sb.append(",");
			sb.append(host).append(":").append(kafkaZookeeperPort);
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MasterConfig [masterConfigJsonKey=" + masterConfigJsonKey + ", contactPoint=" + contactPoint
				+ ", keyspaceName=" + keyspaceName + ", ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName="
				+ requestSvcClassName + ", actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey="
				+ ruleLoginSourceSensorKey + ", ruleDefItemKey=" + ruleDefItemKey + ", igniteCacheName="
				+ igniteCacheName + ", igniteConfigFile=" + igniteConfigFile + ", igniteConfigName=" + igniteConfigName
				+ ", igniteClientMode=" + igniteClientMode + ", igniteConfigPath=" + igniteConfigPath
				+ ", forceRefreshActuatorState=" + forceRefreshActuatorState + ", forceResetActuatorState="
				+ forceResetActuatorState + ", jdbcDriverClassName=" + jdbcDriverClassName + ", jdbcInsertBlockSize="
				+ jdbcInsertBlockSize + ", jdbcLogin=" + jdbcLogin + ", jdbcPassword=" + jdbcPassword + ", jdbcUrl="
				+ jdbcUrl + ", kafkaGroup=" + kafkaGroup + ", kafkaGroupOmh=" + kafkaGroupOmh + ", kafkaGroupBdbb="
				+ kafkaGroupBdbb + ", kafkaTopic=" + kafkaTopic + ", kafkaTopicOmh=" + kafkaTopicOmh
				+ ", kafkaTopicBdbb=" + kafkaTopicBdbb + ", kafkaBootstrapServers=" + kafkaBootstrapServers
				+ ", kafkaZookeeperHosts=" + kafkaZookeeperHosts + ", kafkaZookeeperPort=" + kafkaZookeeperPort
				+ ", kafkaConsumerNumThreads=" + kafkaConsumerNumThreads + ", kafkaZookeeperBrokerPath="
				+ kafkaZookeeperBrokerPath + ", kafkaConsumerId=" + kafkaConsumerId
				+ ", kafkaZookeeperConsumerConnection=" + kafkaZookeeperConsumerConnection
				+ ", kafkaZookeeperConsumerPath=" + kafkaZookeeperConsumerPath + ", routerIote2eRequestClassName="
				+ routerIote2eRequestClassName + ", routerOmhClassName=" + routerOmhClassName + ", routerBdbbClassName="
				+ routerBdbbClassName + ", sparkAppName=" + sparkAppName + ", sparkAppNameBdbb=" + sparkAppNameBdbb
				+ ", sparkAppNameOmh=" + sparkAppNameOmh + ", sparkMaster=" + sparkMaster + ", smtpEmailOmh="
				+ smtpEmailOmh + ", smtpLoginOmh=" + smtpLoginOmh + ", smtpPasswordOmh=" + smtpPasswordOmh
				+ ", smtpEmailBdbb=" + smtpEmailBdbb + ", smtpLoginBdbb=" + smtpLoginBdbb + ", smtpPasswordBdbb="
				+ smtpPasswordBdbb + ", sparkStreamDurationMs=" + sparkStreamDurationMs + ", webServerPort="
				+ webServerPort + ", webServerContentPath=" + webServerContentPath + ", webServerContextPath="
				+ webServerContextPath + ", wsBdbbServerListenPort=" + wsBdbbServerListenPort
				+ ", wsNrtServerListenPort=" + wsNrtServerListenPort + ", wsOmhServerListenPort="
				+ wsOmhServerListenPort + ", wsRouterImplClassName=" + wsRouterImplClassName
				+ ", wsBdbbRouterImplClassName=" + wsBdbbRouterImplClassName + ", wsOmhRouterImplClassName="
				+ wsOmhRouterImplClassName + ", wsServerListenPort=" + wsServerListenPort + "]";
	}

	/**
	 * Gets the kafka zookeeper broker path.
	 *
	 * @return the kafka zookeeper broker path
	 */
	public String getKafkaZookeeperBrokerPath() {
		return kafkaZookeeperBrokerPath;
	}

	/**
	 * Gets the kafka consumer id.
	 *
	 * @return the kafka consumer id
	 */
	public String getKafkaConsumerId() {
		return kafkaConsumerId;
	}

	/**
	 * Gets the kafka zookeeper consumer connection.
	 *
	 * @return the kafka zookeeper consumer connection
	 */
	public String getKafkaZookeeperConsumerConnection() {
		return kafkaZookeeperConsumerConnection;
	}

	/**
	 * Gets the kafka zookeeper consumer path.
	 *
	 * @return the kafka zookeeper consumer path
	 */
	public String getKafkaZookeeperConsumerPath() {
		return kafkaZookeeperConsumerPath;
	}

	/**
	 * Gets the spark app name.
	 *
	 * @return the spark app name
	 */
	public String getSparkAppName() {
		return sparkAppName;
	}

	/**
	 * Gets the spark master.
	 *
	 * @return the spark master
	 */
	public String getSparkMaster() {
		return sparkMaster;
	}

	/**
	 * Gets the spark stream duration ms.
	 *
	 * @return the spark stream duration ms
	 */
	public Integer getSparkStreamDurationMs() {
		return sparkStreamDurationMs;
	}

	/**
	 * Sets the kafka zookeeper port.
	 *
	 * @param kafkaZookeeperPort the kafka zookeeper port
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeperPort(Integer kafkaZookeeperPort) {
		this.kafkaZookeeperPort = kafkaZookeeperPort;
		return this;
	}

	/**
	 * Sets the kafka zookeeper broker path.
	 *
	 * @param kafkaZookeeperBrokerPath the kafka zookeeper broker path
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeperBrokerPath(String kafkaZookeeperBrokerPath) {
		this.kafkaZookeeperBrokerPath = kafkaZookeeperBrokerPath;
		return this;
	}

	/**
	 * Sets the kafka consumer id.
	 *
	 * @param kafkaConsumerId the kafka consumer id
	 * @return the master config
	 */
	public MasterConfig setKafkaConsumerId(String kafkaConsumerId) {
		this.kafkaConsumerId = kafkaConsumerId;
		return this;
	}

	/**
	 * Sets the kafka zookeeper consumer connection.
	 *
	 * @param kafkaZookeeperConsumerConnection the kafka zookeeper consumer connection
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeperConsumerConnection(String kafkaZookeeperConsumerConnection) {
		this.kafkaZookeeperConsumerConnection = kafkaZookeeperConsumerConnection;
		return this;
	}

	/**
	 * Sets the kafka zookeeper consumer path.
	 *
	 * @param kafkaZookeeperConsumerPath the kafka zookeeper consumer path
	 * @return the master config
	 */
	public MasterConfig setKafkaZookeeperConsumerPath(String kafkaZookeeperConsumerPath) {
		this.kafkaZookeeperConsumerPath = kafkaZookeeperConsumerPath;
		return this;
	}

	/**
	 * Sets the spark app name.
	 *
	 * @param sparkAppName the spark app name
	 * @return the master config
	 */
	public MasterConfig setSparkAppName(String sparkAppName) {
		this.sparkAppName = sparkAppName;
		return this;
	}

	/**
	 * Sets the spark master.
	 *
	 * @param sparkMaster the spark master
	 * @return the master config
	 */
	public MasterConfig setSparkMaster(String sparkMaster) {
		this.sparkMaster = sparkMaster;
		return this;
	}

	/**
	 * Sets the spark stream duration ms.
	 *
	 * @param sparkStreamDurationMs the spark stream duration ms
	 * @return the master config
	 */
	public MasterConfig setSparkStreamDurationMs(Integer sparkStreamDurationMs) {
		this.sparkStreamDurationMs = sparkStreamDurationMs;
		return this;
	}

	/**
	 * Gets the ws server listen port.
	 *
	 * @return the ws server listen port
	 */
	public Integer getWsServerListenPort() {
		return wsServerListenPort;
	}

	/**
	 * Sets the ws server listen port.
	 *
	 * @param wsServerListenPort the ws server listen port
	 * @return the master config
	 */
	public MasterConfig setWsServerListenPort(Integer wsServerListenPort) {
		this.wsServerListenPort = wsServerListenPort;
		return this;
	}

	/**
	 * Gets the ws router impl class name.
	 *
	 * @return the ws router impl class name
	 */
	public String getWsRouterImplClassName() {
		return wsRouterImplClassName;
	}

	/**
	 * Sets the ws router impl class name.
	 *
	 * @param wsRouterImplClassName the ws router impl class name
	 * @return the master config
	 */
	public MasterConfig setWsRouterImplClassName(String wsRouterImplClassName) {
		this.wsRouterImplClassName = wsRouterImplClassName;
		return this;
	}

	/**
	 * Gets the keyspace name.
	 *
	 * @return the keyspace name
	 */
	public String getKeyspaceName() {
		return keyspaceName;
	}

	/**
	 * Sets the keyspace name.
	 *
	 * @param keyspacename the keyspacename
	 * @return the master config
	 */
	public MasterConfig setKeyspaceName(String keyspacename) {
		this.keyspaceName = keyspacename;
		return this;
	}

	/**
	 * Gets the master config json key.
	 *
	 * @return the master config json key
	 */
	public String getMasterConfigJsonKey() {
		return masterConfigJsonKey;
	}

	/**
	 * Sets the master config json key.
	 *
	 * @param masterConfigJsonKey the master config json key
	 * @return the master config
	 */
	public MasterConfig setMasterConfigJsonKey(String masterConfigJsonKey) {
		this.masterConfigJsonKey = masterConfigJsonKey;
		return this;
	}

	/**
	 * Gets the contact point.
	 *
	 * @return the contact point
	 */
	public String getContactPoint() {
		return contactPoint;
	}

	/**
	 * Sets the contact point.
	 *
	 * @param contactPoint the contact point
	 * @return the master config
	 */
	public MasterConfig setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
		return this;
	}


	/**
	 * Gets the router iote 2 e request class name.
	 *
	 * @return the router iote 2 e request class name
	 */
	public String getRouterIote2eRequestClassName() {
		return routerIote2eRequestClassName;
	}


	/**
	 * Sets the router iote 2 e request class name.
	 *
	 * @param routerIote2eRequestClassName the router iote 2 e request class name
	 * @return the master config
	 */
	public MasterConfig setRouterIote2eRequestClassName(String routerIote2eRequestClassName) {
		this.routerIote2eRequestClassName = routerIote2eRequestClassName;
		return this;
	}


	/**
	 * Gets the jdbc driver class name.
	 *
	 * @return the jdbc driver class name
	 */
	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}


	/**
	 * Gets the jdbc login.
	 *
	 * @return the jdbc login
	 */
	public String getJdbcLogin() {
		return jdbcLogin;
	}


	/**
	 * Gets the jdbc password.
	 *
	 * @return the jdbc password
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}


	/**
	 * Gets the jdbc url.
	 *
	 * @return the jdbc url
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}


	/**
	 * Sets the jdbc driver class name.
	 *
	 * @param jdbcDriverClassName the jdbc driver class name
	 * @return the master config
	 */
	public MasterConfig setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
		return this;
	}


	/**
	 * Sets the jdbc login.
	 *
	 * @param jdbcLogin the jdbc login
	 * @return the master config
	 */
	public MasterConfig setJdbcLogin(String jdbcLogin) {
		this.jdbcLogin = jdbcLogin;
		return this;
	}


	/**
	 * Sets the jdbc password.
	 *
	 * @param jdbcPassword the jdbc password
	 * @return the master config
	 */
	public MasterConfig setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
		return this;
	}


	/**
	 * Sets the jdbc url.
	 *
	 * @param jdbcUrl the jdbc url
	 * @return the master config
	 */
	public MasterConfig setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
		return this;
	}


	/**
	 * Gets the jdbc insert block size.
	 *
	 * @return the jdbc insert block size
	 */
	public Integer getJdbcInsertBlockSize() {
		return jdbcInsertBlockSize;
	}


	/**
	 * Sets the jdbc insert block size.
	 *
	 * @param jdbcInsertBlockSize the jdbc insert block size
	 * @return the master config
	 */
	public MasterConfig setJdbcInsertBlockSize(Integer jdbcInsertBlockSize) {
		this.jdbcInsertBlockSize = jdbcInsertBlockSize;
		return this;
	}


	/**
	 * Gets the ws nrt server listen port.
	 *
	 * @return the ws nrt server listen port
	 */
	public Integer getWsNrtServerListenPort() {
		return wsNrtServerListenPort;
	}


	/**
	 * Sets the ws nrt server listen port.
	 *
	 * @param wsNrtServerListenPort the ws nrt server listen port
	 * @return the master config
	 */
	public MasterConfig setWsNrtServerListenPort(Integer wsNrtServerListenPort) {
		this.wsNrtServerListenPort = wsNrtServerListenPort;
		return this;
	}


	/**
	 * Gets the kafka topic omh.
	 *
	 * @return the kafka topic omh
	 */
	public String getKafkaTopicOmh() {
		return kafkaTopicOmh;
	}


	/**
	 * Sets the kafka topic omh.
	 *
	 * @param kafkaTopicOmh the kafka topic omh
	 * @return the master config
	 */
	public MasterConfig setKafkaTopicOmh(String kafkaTopicOmh) {
		this.kafkaTopicOmh = kafkaTopicOmh;
		return this;
	}


	/**
	 * Gets the kafka group omh.
	 *
	 * @return the kafka group omh
	 */
	public String getKafkaGroupOmh() {
		return kafkaGroupOmh;
	}


	/**
	 * Sets the kafka group omh.
	 *
	 * @param kafkaGroupOmh the kafka group omh
	 * @return the master config
	 */
	public MasterConfig setKafkaGroupOmh(String kafkaGroupOmh) {
		this.kafkaGroupOmh = kafkaGroupOmh;
		return this;
	}


	/**
	 * Gets the ws omh router impl class name.
	 *
	 * @return the ws omh router impl class name
	 */
	public String getWsOmhRouterImplClassName() {
		return wsOmhRouterImplClassName;
	}


	/**
	 * Sets the ws omh router impl class name.
	 *
	 * @param wsOmhRouterImplClassName the ws omh router impl class name
	 * @return the master config
	 */
	public MasterConfig setWsOmhRouterImplClassName(String wsOmhRouterImplClassName) {
		this.wsOmhRouterImplClassName = wsOmhRouterImplClassName;
		return this;
	}


	/**
	 * Gets the ws omh server listen port.
	 *
	 * @return the ws omh server listen port
	 */
	public Integer getWsOmhServerListenPort() {
		return wsOmhServerListenPort;
	}


	/**
	 * Sets the ws omh server listen port.
	 *
	 * @param wsOmhServerListenPort the ws omh server listen port
	 * @return the master config
	 */
	public MasterConfig setWsOmhServerListenPort(Integer wsOmhServerListenPort) {
		this.wsOmhServerListenPort = wsOmhServerListenPort;
		return this;
	}


	/**
	 * Gets the spark app name omh.
	 *
	 * @return the spark app name omh
	 */
	public String getSparkAppNameOmh() {
		return sparkAppNameOmh;
	}


	/**
	 * Sets the spark app name omh.
	 *
	 * @param sparkAppNameOmh the spark app name omh
	 * @return the master config
	 */
	public MasterConfig setSparkAppNameOmh(String sparkAppNameOmh) {
		this.sparkAppNameOmh = sparkAppNameOmh;
		return this;
	}


	/**
	 * Gets the router omh class name.
	 *
	 * @return the router omh class name
	 */
	public String getRouterOmhClassName() {
		return routerOmhClassName;
	}


	/**
	 * Sets the router omh class name.
	 *
	 * @param routerOmhClassName the router omh class name
	 * @return the master config
	 */
	public MasterConfig setRouterOmhClassName(String routerOmhClassName) {
		this.routerOmhClassName = routerOmhClassName;
		return this;
	}


	/**
	 * Gets the smtp email omh.
	 *
	 * @return the smtp email omh
	 */
	public String getSmtpEmailOmh() {
		return smtpEmailOmh;
	}


	/**
	 * Gets the smtp login omh.
	 *
	 * @return the smtp login omh
	 */
	public String getSmtpLoginOmh() {
		return smtpLoginOmh;
	}


	/**
	 * Gets the smtp password omh.
	 *
	 * @return the smtp password omh
	 */
	public String getSmtpPasswordOmh() {
		return smtpPasswordOmh;
	}


	/**
	 * Sets the smtp email omh.
	 *
	 * @param smtpEmail the smtp email
	 * @return the master config
	 */
	public MasterConfig setSmtpEmailOmh(String smtpEmail) {
		this.smtpEmailOmh = smtpEmail;
		return this;
	}


	/**
	 * Sets the smtp login omh.
	 *
	 * @param smtpLogin the smtp login
	 * @return the master config
	 */
	public MasterConfig setSmtpLoginOmh(String smtpLogin) {
		this.smtpLoginOmh = smtpLogin;
		return this;
	}


	/**
	 * Sets the smtp password omh.
	 *
	 * @param smtpPassword the smtp password
	 * @return the master config
	 */
	public MasterConfig setSmtpPasswordOmh(String smtpPassword) {
		this.smtpPasswordOmh = smtpPassword;
		return this;
	}


	/**
	 * Gets the web server port.
	 *
	 * @return the web server port
	 */
	public Integer getWebServerPort() {
		return webServerPort;
	}


	/**
	 * Gets the web server context path.
	 *
	 * @return the web server context path
	 */
	public String getWebServerContextPath() {
		return webServerContextPath;
	}


	/**
	 * Sets the web server port.
	 *
	 * @param webServerPort the web server port
	 * @return the master config
	 */
	public MasterConfig setWebServerPort(Integer webServerPort) {
		this.webServerPort = webServerPort;
		return this;
	}


	/**
	 * Sets the web server context path.
	 *
	 * @param webServerContextPath the web server context path
	 * @return the master config
	 */
	public MasterConfig setWebServerContextPath(String webServerContextPath) {
		this.webServerContextPath = webServerContextPath;
		return this;
	}


	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * Gets the web server content path.
	 *
	 * @return the web server content path
	 */
	public String getWebServerContentPath() {
		return webServerContentPath;
	}


	/**
	 * Sets the web server content path.
	 *
	 * @param webServerContentPath the web server content path
	 * @return the master config
	 */
	public MasterConfig setWebServerContentPath(String webServerContentPath) {
		this.webServerContentPath = webServerContentPath;
		return this;
	}


	/**
	 * Gets the kafka group bdbb.
	 *
	 * @return the kafka group bdbb
	 */
	public String getKafkaGroupBdbb() {
		return kafkaGroupBdbb;
	}


	/**
	 * Gets the kafka topic bdbb.
	 *
	 * @return the kafka topic bdbb
	 */
	public String getKafkaTopicBdbb() {
		return kafkaTopicBdbb;
	}


	/**
	 * Sets the kafka group bdbb.
	 *
	 * @param kafkaGroupBdbb the kafka group bdbb
	 * @return the master config
	 */
	public MasterConfig setKafkaGroupBdbb(String kafkaGroupBdbb) {
		this.kafkaGroupBdbb = kafkaGroupBdbb;
		return this;
	}


	/**
	 * Sets the kafka topic bdbb.
	 *
	 * @param kafkaTopicBdbb the kafka topic bdbb
	 * @return the master config
	 */
	public MasterConfig setKafkaTopicBdbb(String kafkaTopicBdbb) {
		this.kafkaTopicBdbb = kafkaTopicBdbb;
		return this;
	}


	/**
	 * Gets the ws bdbb server listen port.
	 *
	 * @return the ws bdbb server listen port
	 */
	public Integer getWsBdbbServerListenPort() {
		return wsBdbbServerListenPort;
	}


	/**
	 * Gets the ws bdbb router impl class name.
	 *
	 * @return the ws bdbb router impl class name
	 */
	public String getWsBdbbRouterImplClassName() {
		return wsBdbbRouterImplClassName;
	}


	/**
	 * Sets the ws bdbb server listen port.
	 *
	 * @param wsBdbbServerListenPort the ws bdbb server listen port
	 * @return the master config
	 */
	public MasterConfig setWsBdbbServerListenPort(Integer wsBdbbServerListenPort) {
		this.wsBdbbServerListenPort = wsBdbbServerListenPort;
		return this;
	}


	/**
	 * Sets the ws bdbb router impl class name.
	 *
	 * @param wsBdbbRouterImplClassName the ws bdbb router impl class name
	 * @return the master config
	 */
	public MasterConfig setWsBdbbRouterImplClassName(String wsBdbbRouterImplClassName) {
		this.wsBdbbRouterImplClassName = wsBdbbRouterImplClassName;
		return this;
	}


	/**
	 * Gets the router bdbb class name.
	 *
	 * @return the router bdbb class name
	 */
	public String getRouterBdbbClassName() {
		return routerBdbbClassName;
	}


	/**
	 * Sets the router bdbb class name.
	 *
	 * @param routerBdbbClassName the router bdbb class name
	 * @return the master config
	 */
	public MasterConfig setRouterBdbbClassName(String routerBdbbClassName) {
		this.routerBdbbClassName = routerBdbbClassName;
		return this;
	}


	/**
	 * Gets the spark app name bdbb.
	 *
	 * @return the spark app name bdbb
	 */
	public String getSparkAppNameBdbb() {
		return sparkAppNameBdbb;
	}


	/**
	 * Sets the spark app name bdbb.
	 *
	 * @param sparkAppNameBdbb the spark app name bdbb
	 * @return the master config
	 */
	public MasterConfig setSparkAppNameBdbb(String sparkAppNameBdbb) {
		this.sparkAppNameBdbb = sparkAppNameBdbb;
		return this;
	}


	/**
	 * Gets the smtp email bdbb.
	 *
	 * @return the smtp email bdbb
	 */
	public String getSmtpEmailBdbb() {
		return smtpEmailBdbb;
	}


	/**
	 * Gets the smtp login bdbb.
	 *
	 * @return the smtp login bdbb
	 */
	public String getSmtpLoginBdbb() {
		return smtpLoginBdbb;
	}


	/**
	 * Gets the smtp password bdbb.
	 *
	 * @return the smtp password bdbb
	 */
	public String getSmtpPasswordBdbb() {
		return smtpPasswordBdbb;
	}


	/**
	 * Sets the smtp email bdbb.
	 *
	 * @param smtpEmailBdbb the smtp email bdbb
	 * @return the master config
	 */
	public MasterConfig setSmtpEmailBdbb(String smtpEmailBdbb) {
		this.smtpEmailBdbb = smtpEmailBdbb;
		return this;
	}


	/**
	 * Sets the smtp login bdbb.
	 *
	 * @param smtpLoginBdbb the smtp login bdbb
	 * @return the master config
	 */
	public MasterConfig setSmtpLoginBdbb(String smtpLoginBdbb) {
		this.smtpLoginBdbb = smtpLoginBdbb;
		return this;
	}


	/**
	 * Sets the smtp password bdbb.
	 *
	 * @param smtpPasswordBdbb the smtp password bdbb
	 * @return the master config
	 */
	public MasterConfig setSmtpPasswordBdbb(String smtpPasswordBdbb) {
		this.smtpPasswordBdbb = smtpPasswordBdbb;
		return this;
	}

}
