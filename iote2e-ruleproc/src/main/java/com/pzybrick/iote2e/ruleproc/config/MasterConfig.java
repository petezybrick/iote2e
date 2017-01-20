package com.pzybrick.iote2e.ruleproc.config;

import javax.annotation.Generated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.pzybrick.iote2e.ruleproc.persist.ConfigDao;

@Generated("org.jsonschema2pojo")
public class MasterConfig {
	private static final Logger logger = LogManager.getLogger(MasterConfig.class);
	private static MasterConfig masterConfig;
	
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
	private String sparkAppName;
	@Expose
	private String sparkMaster;
	@Expose
	private Integer sparkStreamDurationMs;
	

	private MasterConfig() {
		
	}
	
	public static synchronized MasterConfig getInstance( ) throws Exception {
		if( masterConfig == null ) {
			try {
				logger.info("Instantiating MasterConfig singleton");
				String keyspaceName = System.getenv("CASSANDRA_KEYSPACE_NAME");
				ConfigDao.useKeyspace(keyspaceName);
				String masterConfigJsonKey = System.getenv("MASTER_CONFIG_JSON_KEY");
				String rawJson = ConfigDao.findConfigJson(masterConfigJsonKey);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				masterConfig = gson.fromJson(rawJson, MasterConfig.class);
				
			} catch (Throwable t ) {
				logger.error("Ignite initialization failure", t);
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
		return "MasterConfig [ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName=" + requestSvcClassName
				+ ", actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey=" + ruleLoginSourceSensorKey
				+ ", ruleDefItemKey=" + ruleDefItemKey + ", igniteCacheName=" + igniteCacheName + ", igniteConfigFile="
				+ igniteConfigFile + ", igniteConfigName=" + igniteConfigName + ", igniteClientMode=" + igniteClientMode
				+ ", igniteConfigPath=" + igniteConfigPath + ", forceRefreshActuatorState=" + forceRefreshActuatorState
				+ ", forceResetActuatorState=" + forceResetActuatorState + ", kafkaGroup=" + kafkaGroup
				+ ", kafkaTopic=" + kafkaTopic + ", kafkaBootstrapServers=" + kafkaBootstrapServers
				+ ", kafkaZookeeperHosts=" + kafkaZookeeperHosts + ", kafkaZookeeperPort=" + kafkaZookeeperPort
				+ ", kafkaConsumerNumThreads=" + kafkaConsumerNumThreads + ", kafkaZookeeperBrokerPath="
				+ kafkaZookeeperBrokerPath + ", kafkaConsumerId=" + kafkaConsumerId
				+ ", kafkaZookeeperConsumerConnection=" + kafkaZookeeperConsumerConnection
				+ ", kafkaZookeeperConsumerPath=" + kafkaZookeeperConsumerPath + ", sparkAppName=" + sparkAppName
				+ ", sparkMaster=" + sparkMaster + ", sparkStreamDurationMs=" + sparkStreamDurationMs + "]";
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

}
