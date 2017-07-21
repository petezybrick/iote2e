package com.pzybrick.iote2e.tests.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BloodGlucose;
import org.openmhealth.schema.domain.omh.BloodPressure;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.openmhealth.schema.domain.omh.DataPointAcquisitionProvenance;
import org.openmhealth.schema.domain.omh.DataPointHeader;
import org.openmhealth.schema.domain.omh.DataPointModality;
import org.openmhealth.schema.domain.omh.HeartRate;
import org.openmhealth.schema.domain.omh.PhysicalActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.config.MasterConfig;
import com.pzybrick.iote2e.stream.persist.OmhDao;
import com.pzybrick.iote2e.stream.persist.PooledDataSource;
import com.pzybrick.iote2e.tests.omh.OmhUser;
import com.pzybrick.iote2e.tests.omh.SimOmhUsers;
import com.pzybrick.iote2e.tests.omh.SimSchema;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodGlucoseImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodPressureImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBodyTempImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHeartRateImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHkWorkoutImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaRespiratoryRateImpl;

public class TestOmhBatchInsert {
	private static final Logger logger = LogManager.getLogger(TestOmhBatchInsert.class);
	private static long SLEEP_INTERVAL_MS = 1000L;
	private Map<String, SimSchema> simSchemasByName;
	private List<String> sortedSimSchemaNames;
	private Map<String, Object> prevBodiesByNameLogin;
	private String simUsersFilePath;
	private Integer simUsersOffset;
	private Integer simUsersNumUsers;
	private List<OmhUser> subsetOmhUsers;
	private ObjectMapper objectMapper;
	private MasterConfig masterConfig;

	public static void main(String[] args) {
		try {
			// Args: simUsersFilePath; simUsersOffset; simUsersNumUsers
			// Args: "iote2e-shared/data/simOmhUsers.csv" 1 5
			TestOmhBatchInsert testOmhSingleInsert = new TestOmhBatchInsert().setSimUsersFilePath(args[0])
					.setSimUsersOffset(Integer.parseInt(args[1])).setSimUsersNumUsers(Integer.parseInt(args[2]));
			testOmhSingleInsert.process();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public TestOmhBatchInsert() throws Exception {
		this.masterConfig = MasterConfig.getInstance(System.getenv("MASTER_CONFIG_JSON_KEY"),
				System.getenv("CASSANDRA_CONTACT_POINT"), System.getenv("CASSANDRA_KEYSPACE_NAME"));
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.prevBodiesByNameLogin = new HashMap<String, Object>();
		this.simSchemasByName = new HashMap<String, SimSchema>();
		this.simSchemasByName.put(BloodGlucose.SCHEMA_ID.getName(), new SimSchemaBloodGlucoseImpl());
		this.simSchemasByName.put(BloodPressure.SCHEMA_ID.getName(), new SimSchemaBloodPressureImpl());
		// for some reason AmbientTemperature SCHEMA_ID is private
		this.simSchemasByName.put("body-temperature", new SimSchemaBodyTempImpl());
		this.simSchemasByName.put(HeartRate.SCHEMA_ID.getName(), new SimSchemaHeartRateImpl());
		this.simSchemasByName.put(PhysicalActivity.SCHEMA_ID.getName(), new SimSchemaHkWorkoutImpl());
		// for some reason RespiratoryRate SCHEMA_ID is private
		this.simSchemasByName.put("respiratory-rate", new SimSchemaRespiratoryRateImpl());
		this.sortedSimSchemaNames = new ArrayList<String>(this.simSchemasByName.keySet());
		Collections.sort(this.sortedSimSchemaNames);
	}

	public void process() throws Exception {
		this.subsetOmhUsers = SimOmhUsers.getInstance(simUsersFilePath).getOmhUsers().subList(simUsersOffset,
				simUsersOffset + simUsersNumUsers);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);

			while (true) {
				long wakeupAt = System.currentTimeMillis() + SLEEP_INTERVAL_MS;
				for (OmhUser omhUser : subsetOmhUsers) {
					OffsetDateTime now = OffsetDateTime.now();
					for (String schemaName : sortedSimSchemaNames) {
						SimSchema simSchema = simSchemasByName.get(schemaName);
						String id = UUID.randomUUID().toString();
						DataPointHeader header = new DataPointHeader.Builder(id, simSchema.getSchemaId(), now)
								.setUserId(omhUser.getEmail())
								.setAcquisitionProvenance(new DataPointAcquisitionProvenance.Builder("RunKeeper")
										.setSourceCreationDateTime(now).setModality(DataPointModality.SENSED).build())
								.build();

						String keyPrevBody = schemaName + "|" + omhUser.getEmail();
						Object prevBody = prevBodiesByNameLogin.get(keyPrevBody);
						Object body = simSchema.createBody(now, prevBody);
						prevBodiesByNameLogin.put(keyPrevBody, body);

						DataPoint dataPoint = new DataPoint(header, body);
						OmhDao.insertBatch( con, dataPoint);
						// System.out.println("length before: " +
						// rawJson.length() + ", after: " + compressed.length);
						System.out.println("inserting: " + dataPoint.getHeader().getBodySchemaId().getName());
					}
				}
				con.commit();
				try {
					long msSleep = wakeupAt - System.currentTimeMillis();
					System.out.println(msSleep);
					Thread.sleep(msSleep);
				} catch (Exception e) {
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (con != null) {
				try {
					con.rollback();
				} catch (Exception erb) {
					logger.warn(e.getMessage(), e);
				}
			}
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}


	public Map<String, SimSchema> getSimSchemasByName() {
		return simSchemasByName;
	}

	public List<String> getSortedSimSchemaNames() {
		return sortedSimSchemaNames;
	}

	public Map<String, Object> getPrevBodiesByNameLogin() {
		return prevBodiesByNameLogin;
	}

	public String getSimUsersFilePath() {
		return simUsersFilePath;
	}

	public Integer getSimUsersOffset() {
		return simUsersOffset;
	}

	public Integer getSimUsersNumUsers() {
		return simUsersNumUsers;
	}

	public List<OmhUser> getSubsetOmhUsers() {
		return subsetOmhUsers;
	}

	public TestOmhBatchInsert setSimSchemasByName(Map<String, SimSchema> simSchemasByName) {
		this.simSchemasByName = simSchemasByName;
		return this;
	}

	public TestOmhBatchInsert setSortedSimSchemaNames(List<String> sortedSimSchemaNames) {
		this.sortedSimSchemaNames = sortedSimSchemaNames;
		return this;
	}

	public TestOmhBatchInsert setPrevBodiesByNameLogin(Map<String, Object> prevBodiesByNameLogin) {
		this.prevBodiesByNameLogin = prevBodiesByNameLogin;
		return this;
	}

	public TestOmhBatchInsert setSimUsersFilePath(String simUsersFilePath) {
		this.simUsersFilePath = simUsersFilePath;
		return this;
	}

	public TestOmhBatchInsert setSimUsersOffset(Integer simUsersOffset) {
		this.simUsersOffset = simUsersOffset;
		return this;
	}

	public TestOmhBatchInsert setSimUsersNumUsers(Integer simUsersNumUsers) {
		this.simUsersNumUsers = simUsersNumUsers;
		return this;
	}

	public TestOmhBatchInsert setSubsetOmhUsers(List<OmhUser> subsetOmhUsers) {
		this.subsetOmhUsers = subsetOmhUsers;
		return this;
	}

}
