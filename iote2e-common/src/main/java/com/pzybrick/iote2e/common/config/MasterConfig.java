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

@Generated("org.jsonschema2pojo")
public class MasterConfig implements Serializable {
	private static final Logger logger = LogManager.getLogger(MasterConfig.class);
	private static MasterConfig masterConfig;
	private String masterConfigJsonKey;
	private String contactPoint;
	private String keyspaceName;
	
	@Expose
	private String ruleSvcClassName;
	@Expose
	private String requestSvcClassName;
	@Expose 
	private String actuatorStateKey;
	@Expose 
	private String ruleLoginSourceSensorKey;
	@Expose 
	private String ruleDefItemKey;
	@Expose
	private String igniteCacheName;
	@Expose
	private String igniteConfigFile;
	@Expose
	private String igniteConfigName;
	@Expose
	private boolean igniteClientMode;
	@Expose
	private String igniteConfigPath;
	@Expose
	private boolean forceRefreshActuatorState;
	@Expose
	private boolean forceResetActuatorState;
	@Expose
	private String kafkaGroup;
	@Expose
	private String jdbcDriverClassName;
	@Expose
	private Integer jdbcInsertBlockSize;
	@Expose
	private String jdbcLogin;
	@Expose
	private String jdbcPassword;
	@Expose
	private String jdbcUrl;
	@Expose
	private String kafkaTopic;
	@Expose
	private String kafkaBootstrapServers;
	@Expose
	private String kafkaZookeeperHosts;
	@Expose
	private Integer kafkaZookeeperPort;
	@Expose
	private Integer kafkaConsumerNumThreads;
	@Expose
	private String kafkaZookeeperBrokerPath;
	@Expose
	private String kafkaConsumerId;
	@Expose
	private String kafkaZookeeperConsumerConnection;
	@Expose
	private String kafkaZookeeperConsumerPath;
	@Expose
	private String routerIote2eRequestClassName;
	@Expose
	private String sparkAppName;
	@Expose
	private String sparkMaster;
	@Expose
	private Integer sparkStreamDurationMs;
	@Expose
	private String wsRouterImplClassName;
	@Expose
	private Integer wsServerListenPort;
	

	private MasterConfig() {
		
	}

		
	public static synchronized MasterConfig getInstance( String masterConfigJsonKey, String contactPoint, String keyspaceName ) throws Exception {
		if( masterConfig == null ) {
			try {
				logger.info("Instantiating MasterConfig singleton");
				if( keyspaceName == null ) keyspaceName = CassandraBaseDao.DEFAULT_KEYSPACE_NAME;
				ConfigDao.connect(contactPoint, keyspaceName);
				String rawJson = ConfigDao.findConfigJson(masterConfigJsonKey);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				masterConfig = gson.fromJson(rawJson, MasterConfig.class);
				masterConfig.setContactPoint(contactPoint);
				masterConfig.setKeyspaceName(keyspaceName);				
			} catch (Throwable t ) {
				logger.error("Cassandra initialization failure", t);
				throw t;
			}
		}
		return masterConfig;
	}
	
	public String getRuleSvcClassName() {
		return ruleSvcClassName;
	}
	public String getRequestSvcClassName() {
		return requestSvcClassName;
	}
	public String getActuatorStateKey() {
		return actuatorStateKey;
	}
	public String getRuleLoginSourceSensorKey() {
		return ruleLoginSourceSensorKey;
	}
	public String getRuleDefItemKey() {
		return ruleDefItemKey;
	}
	public String getIgniteCacheName() {
		return igniteCacheName;
	}
	public String getIgniteConfigFile() {
		return igniteConfigFile;
	}
	public String getIgniteConfigName() {
		return igniteConfigName;
	}
	public boolean isIgniteClientMode() {
		return igniteClientMode;
	}
	public boolean isForceRefreshActuatorState() {
		return forceRefreshActuatorState;
	}
	public boolean isForceResetActuatorState() {
		return forceResetActuatorState;
	}
	public MasterConfig setRuleSvcClassName(String ruleSvcClassName) {
		this.ruleSvcClassName = ruleSvcClassName;
		return this;
	}
	public MasterConfig setRequestSvcClassName(String requestSvcClassName) {
		this.requestSvcClassName = requestSvcClassName;
		return this;
	}
	public MasterConfig setActuatorStateKey(String actuatorStateKey) {
		this.actuatorStateKey = actuatorStateKey;
		return this;
	}
	public MasterConfig setRuleLoginSourceSensorKey(String ruleLoginSourceSensorKey) {
		this.ruleLoginSourceSensorKey = ruleLoginSourceSensorKey;
		return this;
	}
	public MasterConfig setRuleDefItemKey(String ruleDefItemKey) {
		this.ruleDefItemKey = ruleDefItemKey;
		return this;
	}
	public MasterConfig setIgniteCacheName(String igniteCacheName) {
		this.igniteCacheName = igniteCacheName;
		return this;
	}
	public MasterConfig setIgniteConfigFile(String igniteConfigFile) {
		this.igniteConfigFile = igniteConfigFile;
		return this;
	}
	public MasterConfig setIgniteConfigName(String igniteConfigName) {
		this.igniteConfigName = igniteConfigName;
		return this;
	}
	public MasterConfig setIgniteClientMode(boolean igniteClientMode) {
		this.igniteClientMode = igniteClientMode;
		return this;
	}
	public MasterConfig setForceRefreshActuatorState(boolean forceRefreshActuatorState) {
		this.forceRefreshActuatorState = forceRefreshActuatorState;
		return this;
	}
	public MasterConfig setForceResetActuatorState(boolean forceResetActuatorState) {
		this.forceResetActuatorState = forceResetActuatorState;
		return this;
	}

	public String getKafkaGroup() {
		return kafkaGroup;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public String getKafkaBootstrapServers() {
		return kafkaBootstrapServers;
	}

	public String getKafkaZookeeperHosts() {
		return kafkaZookeeperHosts;
	}

	public Integer getKafkaZookeeperPort() {
		return kafkaZookeeperPort;
	}

	public Integer getKafkaConsumerNumThreads() {
		return kafkaConsumerNumThreads;
	}

	public String getIgniteConfigPath() {
		return igniteConfigPath;
	}

	public MasterConfig setKafkaGroup(String kafkaGroup) {
		this.kafkaGroup = kafkaGroup;
		return this;
	}

	public MasterConfig setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
		return this;
	}

	public MasterConfig setKafkaBootstrapServers(String kafkaBootstrapServers) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
		return this;
	}

	public MasterConfig setKafkaZookeeperHosts(String kafkaZookeeperHosts) {
		this.kafkaZookeeperHosts = kafkaZookeeperHosts;
		return this;
	}

	public MasterConfig setKafkaZookeeper(Integer kafkaZookeeperPort) {
		this.kafkaZookeeperPort = kafkaZookeeperPort;
		return this;
	}

	public MasterConfig setKafkaConsumerNumThreads(Integer kafkaConsumerNumThreads) {
		this.kafkaConsumerNumThreads = kafkaConsumerNumThreads;
		return this;
	}

	public MasterConfig setIgniteConfigPath(String igniteConfigPath) {
		this.igniteConfigPath = igniteConfigPath;
		return this;
	}
	
	public String createKafkaZookeeperHostPortPairs( ) {
		StringBuilder sb = new StringBuilder();
		String[] hosts = kafkaZookeeperHosts.split("[,]");
		for( String host : hosts ) {
			if( sb.length() > 0 ) sb.append(",");
			sb.append(host).append(":").append(kafkaZookeeperPort);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "MasterConfig [masterConfigJsonKey=" + masterConfigJsonKey + ", contactPoint=" + contactPoint
				+ ", keyspaceName=" + keyspaceName + ", ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName="
				+ requestSvcClassName + ", actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey="
				+ ruleLoginSourceSensorKey + ", ruleDefItemKey=" + ruleDefItemKey + ", igniteCacheName="
				+ igniteCacheName + ", igniteConfigFile=" + igniteConfigFile + ", igniteConfigName=" + igniteConfigName
				+ ", igniteClientMode=" + igniteClientMode + ", igniteConfigPath=" + igniteConfigPath
				+ ", forceRefreshActuatorState=" + forceRefreshActuatorState + ", forceResetActuatorState="
				+ forceResetActuatorState + ", kafkaGroup=" + kafkaGroup + ", jdbcDriverClassName="
				+ jdbcDriverClassName + ", jdbcInsertBlockSize=" + jdbcInsertBlockSize + ", jdbcLogin=" + jdbcLogin
				+ ", jdbcPassword=" + jdbcPassword + ", jdbcUrl=" + jdbcUrl + ", kafkaTopic=" + kafkaTopic
				+ ", kafkaBootstrapServers=" + kafkaBootstrapServers + ", kafkaZookeeperHosts=" + kafkaZookeeperHosts
				+ ", kafkaZookeeperPort=" + kafkaZookeeperPort + ", kafkaConsumerNumThreads=" + kafkaConsumerNumThreads
				+ ", kafkaZookeeperBrokerPath=" + kafkaZookeeperBrokerPath + ", kafkaConsumerId=" + kafkaConsumerId
				+ ", kafkaZookeeperConsumerConnection=" + kafkaZookeeperConsumerConnection
				+ ", kafkaZookeeperConsumerPath=" + kafkaZookeeperConsumerPath + ", routerIote2eRequestClassName="
				+ routerIote2eRequestClassName + ", sparkAppName=" + sparkAppName + ", sparkMaster=" + sparkMaster
				+ ", sparkStreamDurationMs=" + sparkStreamDurationMs + ", wsRouterImplClassName="
				+ wsRouterImplClassName + ", wsServerListenPort=" + wsServerListenPort + "]";
	}

	public String getKafkaZookeeperBrokerPath() {
		return kafkaZookeeperBrokerPath;
	}

	public String getKafkaConsumerId() {
		return kafkaConsumerId;
	}

	public String getKafkaZookeeperConsumerConnection() {
		return kafkaZookeeperConsumerConnection;
	}

	public String getKafkaZookeeperConsumerPath() {
		return kafkaZookeeperConsumerPath;
	}

	public String getSparkAppName() {
		return sparkAppName;
	}

	public String getSparkMaster() {
		return sparkMaster;
	}

	public Integer getSparkStreamDurationMs() {
		return sparkStreamDurationMs;
	}

	public MasterConfig setKafkaZookeeperPort(Integer kafkaZookeeperPort) {
		this.kafkaZookeeperPort = kafkaZookeeperPort;
		return this;
	}

	public MasterConfig setKafkaZookeeperBrokerPath(String kafkaZookeeperBrokerPath) {
		this.kafkaZookeeperBrokerPath = kafkaZookeeperBrokerPath;
		return this;
	}

	public MasterConfig setKafkaConsumerId(String kafkaConsumerId) {
		this.kafkaConsumerId = kafkaConsumerId;
		return this;
	}

	public MasterConfig setKafkaZookeeperConsumerConnection(String kafkaZookeeperConsumerConnection) {
		this.kafkaZookeeperConsumerConnection = kafkaZookeeperConsumerConnection;
		return this;
	}

	public MasterConfig setKafkaZookeeperConsumerPath(String kafkaZookeeperConsumerPath) {
		this.kafkaZookeeperConsumerPath = kafkaZookeeperConsumerPath;
		return this;
	}

	public MasterConfig setSparkAppName(String sparkAppName) {
		this.sparkAppName = sparkAppName;
		return this;
	}

	public MasterConfig setSparkMaster(String sparkMaster) {
		this.sparkMaster = sparkMaster;
		return this;
	}

	public MasterConfig setSparkStreamDurationMs(Integer sparkStreamDurationMs) {
		this.sparkStreamDurationMs = sparkStreamDurationMs;
		return this;
	}

	public Integer getWsServerListenPort() {
		return wsServerListenPort;
	}

	public MasterConfig setWsServerListenPort(Integer wsServerListenPort) {
		this.wsServerListenPort = wsServerListenPort;
		return this;
	}

	public String getWsRouterImplClassName() {
		return wsRouterImplClassName;
	}

	public MasterConfig setWsRouterImplClassName(String wsRouterImplClassName) {
		this.wsRouterImplClassName = wsRouterImplClassName;
		return this;
	}

	public String getKeyspaceName() {
		return keyspaceName;
	}

	public MasterConfig setKeyspaceName(String keyspacename) {
		this.keyspaceName = keyspacename;
		return this;
	}

	public String getMasterConfigJsonKey() {
		return masterConfigJsonKey;
	}

	public MasterConfig setMasterConfigJsonKey(String masterConfigJsonKey) {
		this.masterConfigJsonKey = masterConfigJsonKey;
		return this;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public MasterConfig setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
		return this;
	}


	public String getRouterIote2eRequestClassName() {
		return routerIote2eRequestClassName;
	}


	public MasterConfig setRouterIote2eRequestClassName(String routerIote2eRequestClassName) {
		this.routerIote2eRequestClassName = routerIote2eRequestClassName;
		return this;
	}


	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}


	public String getJdbcLogin() {
		return jdbcLogin;
	}


	public String getJdbcPassword() {
		return jdbcPassword;
	}


	public String getJdbcUrl() {
		return jdbcUrl;
	}


	public MasterConfig setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
		return this;
	}


	public MasterConfig setJdbcLogin(String jdbcLogin) {
		this.jdbcLogin = jdbcLogin;
		return this;
	}


	public MasterConfig setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
		return this;
	}


	public MasterConfig setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
		return this;
	}


	public Integer getJdbcInsertBlockSize() {
		return jdbcInsertBlockSize;
	}


	public MasterConfig setJdbcInsertBlockSize(Integer jdbcInsertBlockSize) {
		this.jdbcInsertBlockSize = jdbcInsertBlockSize;
		return this;
	}

}
