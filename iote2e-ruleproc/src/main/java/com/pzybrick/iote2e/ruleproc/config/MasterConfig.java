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
	private String sourceResponseIgniteCacheName;
	@Expose
	private String sourceResponseIgniteConfigFile;
	@Expose
	private String sourceResponseIgniteConfigName;
	@Expose
	private boolean igniteClientMode;
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
	private String kafkaZookeeper;
	@Expose
	private Integer kafkaStreamConsumerNumThreads;
	@Expose
	private String igniteConfigPath;
	

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
	public String getSourceResponseIgniteCacheName() {
		return sourceResponseIgniteCacheName;
	}
	public String getSourceResponseIgniteConfigFile() {
		return sourceResponseIgniteConfigFile;
	}
	public String getSourceResponseIgniteConfigName() {
		return sourceResponseIgniteConfigName;
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
	public MasterConfig setSourceResponseIgniteCacheName(String sourceResponseIgniteCacheName) {
		this.sourceResponseIgniteCacheName = sourceResponseIgniteCacheName;
		return this;
	}
	public MasterConfig setSourceResponseIgniteConfigFile(String sourceResponseIgniteConfigFile) {
		this.sourceResponseIgniteConfigFile = sourceResponseIgniteConfigFile;
		return this;
	}
	public MasterConfig setSourceResponseIgniteConfigName(String sourceResponseIgniteConfigName) {
		this.sourceResponseIgniteConfigName = sourceResponseIgniteConfigName;
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

	public String getKafkaZookeeper() {
		return kafkaZookeeper;
	}

	public Integer getKafkaStreamConsumerNumThreads() {
		return kafkaStreamConsumerNumThreads;
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

	public MasterConfig setKafkaZookeeper(String kafkaZookeeper) {
		this.kafkaZookeeper = kafkaZookeeper;
		return this;
	}

	public MasterConfig setKafkaStreamConsumerNumThreads(Integer kafkaStreamConsumerNumThreads) {
		this.kafkaStreamConsumerNumThreads = kafkaStreamConsumerNumThreads;
		return this;
	}

	public MasterConfig setIgniteConfigPath(String igniteConfigPath) {
		this.igniteConfigPath = igniteConfigPath;
		return this;
	}

	@Override
	public String toString() {
		return "MasterConfig [ruleSvcClassName=" + ruleSvcClassName + ", requestSvcClassName=" + requestSvcClassName
				+ ", actuatorStateKey=" + actuatorStateKey + ", ruleLoginSourceSensorKey=" + ruleLoginSourceSensorKey
				+ ", ruleDefItemKey=" + ruleDefItemKey + ", sourceResponseIgniteCacheName="
				+ sourceResponseIgniteCacheName + ", sourceResponseIgniteConfigFile=" + sourceResponseIgniteConfigFile
				+ ", sourceResponseIgniteConfigName=" + sourceResponseIgniteConfigName + ", igniteClientMode="
				+ igniteClientMode + ", forceRefreshActuatorState=" + forceRefreshActuatorState
				+ ", forceResetActuatorState=" + forceResetActuatorState + ", kafkaGroup=" + kafkaGroup
				+ ", kafkaTopic=" + kafkaTopic + ", kafkaBootstrapServers=" + kafkaBootstrapServers
				+ ", kafkaZookeeper=" + kafkaZookeeper + ", kafkaStreamConsumerNumThreads="
				+ kafkaStreamConsumerNumThreads + ", igniteConfigPath=" + igniteConfigPath + "]";
	}

}
