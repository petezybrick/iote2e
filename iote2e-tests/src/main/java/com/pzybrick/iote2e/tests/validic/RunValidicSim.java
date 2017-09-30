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
package com.pzybrick.iote2e.tests.validic;

import java.nio.ByteBuffer;
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
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.utils.CompressionUtils;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.bdbb.SimSequenceDouble;
import com.pzybrick.iote2e.stream.validic.Biometric;
import com.pzybrick.iote2e.stream.validic.Diabete;
import com.pzybrick.iote2e.stream.validic.ValidicBody;
import com.pzybrick.iote2e.stream.validic.ValidicBodyDeserializer;
import com.pzybrick.iote2e.stream.validic.ValidicHeader;
import com.pzybrick.iote2e.stream.validic.ValidicMessage;
import com.pzybrick.iote2e.tests.omh.ClientSocketOmhHandler;
import com.pzybrick.iote2e.tests.omh.OmhUser;
import com.pzybrick.iote2e.tests.omh.SimOmhUsers;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodGlucoseImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBloodPressureImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaBodyTempImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHeartRateImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaHkWorkoutImpl;
import com.pzybrick.iote2e.tests.omh.SimSchemaImpl.SimSchemaRespiratoryRateImpl;


/**
 * The Class RunOmhSim.
 */
public class RunValidicSim {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(RunValidicSim.class);
	
	public static int MIN_PCT_EXCEEDED = 2;
	
	/** The sim users file path. */
	private String simUsersFilePath;
	
	/** The sim users offset. */
	private Integer simUsersOffset;
	
	/** The sim users num users. */
	private Integer simUsersNumUsers;
	
	/** The ws endpoint. */
	private String wsEndpoint;
	
	/** The max loops. */
	private Integer maxLoops;
	
	/** The subset omh users. */
	private List<OmhUser> subsetOmhUsers;
	
	private String url;
	
	
	private ObjectMapper objectMapper;


	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			// Args: simUsersFilePath; simUsersOffset; simUsersNumUsers wsEndpoint
			// Args: "iote2e-shared/data/simOmhUsers.csv" 1 5 "ws://localhost:8092/omh/"
			RunValidicSim runOmhSim = new RunValidicSim( ).setSimUsersFilePath(args[0]).setSimUsersOffset( Integer.parseInt(args[1]) )
					.setSimUsersNumUsers( Integer.parseInt(args[2]) ).setMaxLoops(Integer.parseInt(args[3])).setWsEndpoint(args[4])
					.setUrl(args[4]);					
			runOmhSim.process();
		} catch(Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Instantiates a new run omh sim.
	 *
	 * @throws Exception the exception
	 */
	public RunValidicSim() throws Exception {

	}
	
	/**
	 * Process.
	 *
	 * @throws Exception the exception
	 */
	public void process() throws Exception {
		this.subsetOmhUsers = SimOmhUsers.getInstance(simUsersFilePath).getOmhUsers().subList(simUsersOffset, simUsersOffset + simUsersNumUsers );

		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());		
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ValidicBody.class, new ValidicBodyDeserializer(objectMapper));
		objectMapper.registerModule(module);
		
        int numLoops = 0;
        List<Thread> threads = new ArrayList<Thread>();
        subsetOmhUsers.forEach( u-> {
			Runnable task = () -> {createMessageBlock( u.getEmail(), maxLoops );  };
			Thread thread = new Thread(task);
			threads.add( thread );
			thread.start();
		} );
        
		threads.forEach(t -> {
			try { t.join(); } catch (Exception e) {}
		} );
	}

	
	
	public void createMessageBlock(String userId, int numBlocks ) {
		try {
			ClientSocketValidicHandler clientSocketValidicHandler = new ClientSocketValidicHandler().setUrl(url);
			clientSocketValidicHandler.connect();

			for( int i=0 ; i<numBlocks ; i++ ) {
				String msgUuid = UUID.randomUUID().toString();
				OffsetDateTime startDateTime = OffsetDateTime.now();
				Map<String, Object> additionalProperties = new HashMap<>();
				additionalProperties.put("testKey1", "testValue1");
				ValidicHeader header = new ValidicHeader().setUuid(msgUuid).setUserId(msgUuid).setUserId(userId)
						.setStartDateTime(startDateTime).setAdditionalProperties(additionalProperties);
				List<ValidicBody> bodies = new ArrayList<ValidicBody>();			
				OffsetDateTime bodyDateTime = startDateTime;			
				SimSequenceDouble bloodGlucose = new SimSequenceDouble()
						.setExceed(170.0)
						.setIncr(3.0)
						.setMax(130.0)
						.setMid(110.0)
						.setMin(90.0)
						.setMinPctExceeded(MIN_PCT_EXCEEDED);
				SimSequenceDouble restingHeartrate = new SimSequenceDouble()
						.setExceed(100.0)
						.setIncr(3.0)
						.setMax(85.0)
						.setMid(72.0)
						.setMin(65.0)
						.setMinPctExceeded(MIN_PCT_EXCEEDED);
				SimSequenceDouble systolic = new SimSequenceDouble()
						.setExceed(150.0)
						.setIncr(5.0)
						.setMax(130.0)
						.setMid(120.0)
						.setMin(110.0)
						.setMinPctExceeded(MIN_PCT_EXCEEDED);
				SimSequenceDouble diastolic = new SimSequenceDouble()
						.setExceed(105.0)
						.setIncr(5.0)
						.setMax(90.0)
						.setMid(80.0)
						.setMin(70.0)
						.setMinPctExceeded(MIN_PCT_EXCEEDED);
				SimSequenceDouble temperature = new SimSequenceDouble()
						.setExceed(104.0)
						.setIncr(.2)
						.setMax(99.2)
						.setMid(98.6)
						.setMin(98.0)
						.setMinPctExceeded(MIN_PCT_EXCEEDED);
				
				int cntLoop = 0;
				while(true) {
					Diabete diabete = new Diabete()
							.setId(UUID.randomUUID().toString())
							.setBloodGlucose(bloodGlucose.nextDouble())
							.setLastUpdated(bodyDateTime)
							.setTimestamp(bodyDateTime);
					bodies.add(diabete);
					Biometric biometric = new Biometric()
							.setId(UUID.randomUUID().toString())
							.setRestingHeartrate(restingHeartrate.nextDouble())
							.setSystolic(systolic.nextDouble())
							.setDiastolic(diastolic.nextDouble())
							.setTemperature(temperature.nextDouble())
							.setLastUpdated(bodyDateTime)
							.setTimestamp(bodyDateTime);
					bodies.add(biometric);
					
					if( ++cntLoop == 3 ) break;
					Iote2eUtils.sleepMillis(1000L);
					bodyDateTime = OffsetDateTime.now();
				}
				header.setEndDateTime(bodyDateTime);
				header.setCreationDateTime(OffsetDateTime.now());
				
				ValidicMessage validicMessage = new ValidicMessage().setHeader(header).setBodies(bodies);				
		        String rawJson = objectMapper.writeValueAsString(validicMessage);
				byte[] compressed = CompressionUtils.compress(rawJson.getBytes());
				clientSocketValidicHandler.session.getBasicRemote().sendBinary(ByteBuffer.wrap(compressed));
				logger.debug("Raw ValidicMessage for {}", validicMessage.getHeader().getUserId() );

			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e );
		}
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

	public String getWsEndpoint() {
		return wsEndpoint;
	}

	public Integer getMaxLoops() {
		return maxLoops;
	}

	public List<OmhUser> getSubsetOmhUsers() {
		return subsetOmhUsers;
	}


	public RunValidicSim setSimUsersFilePath(String simUsersFilePath) {
		this.simUsersFilePath = simUsersFilePath;
		return this;
	}

	public RunValidicSim setSimUsersOffset(Integer simUsersOffset) {
		this.simUsersOffset = simUsersOffset;
		return this;
	}

	public RunValidicSim setSimUsersNumUsers(Integer simUsersNumUsers) {
		this.simUsersNumUsers = simUsersNumUsers;
		return this;
	}

	public RunValidicSim setWsEndpoint(String wsEndpoint) {
		this.wsEndpoint = wsEndpoint;
		return this;
	}

	public RunValidicSim setMaxLoops(Integer maxLoops) {
		this.maxLoops = maxLoops;
		return this;
	}

	public RunValidicSim setSubsetOmhUsers(List<OmhUser> subsetOmhUsers) {
		this.subsetOmhUsers = subsetOmhUsers;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public RunValidicSim setUrl(String url) {
		this.url = url;
		return this;
	}

}
