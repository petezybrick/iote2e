package com.pzybrick.iote2e.tests.validic;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pzybrick.iote2e.common.utils.Iote2eUtils;
import com.pzybrick.iote2e.stream.bdbb.SimSequenceDouble;
import com.pzybrick.iote2e.stream.validic.Biometric;
import com.pzybrick.iote2e.stream.validic.Diabete;
import com.pzybrick.iote2e.stream.validic.ValidicBody;
import com.pzybrick.iote2e.stream.validic.ValidicBodyDeserializer;
import com.pzybrick.iote2e.stream.validic.ValidicHeader;
import com.pzybrick.iote2e.stream.validic.ValidicMessage;
import com.pzybrick.iote2e.tests.omh.ClientSocketOmhHandler;

public class TestValidicMessage {
	private static final Logger logger = LogManager.getLogger(TestValidicMessage.class);
	public static int MIN_PCT_EXCEEDED = 2;
	public static int BLOCK_SIZE = 5;
	private ObjectMapper objectMapper;

	
	public static void main(String[] args) {
		try {
			TestValidicMessage testValidicMessage = new TestValidicMessage();
			testValidicMessage.process();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public TestValidicMessage() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ValidicBody.class, new ValidicBodyDeserializer(objectMapper));
		objectMapper.registerModule(module);
	}

	
	public void process() throws Exception {
		ClientSocketOmhHandler clientSocketOmhHandler = null;
		List<String> userIds = Arrays.asList("pzybrick", "jdoe", "sjones");
		int numBlocks = 4;
		userIds.forEach( u-> {
			Runnable task = () -> {createMessageBlock( u, numBlocks, clientSocketOmhHandler);  };
			new Thread(task).start();			
		} );
//		Runnable task = () -> {createMessageBlock( userId, numBlocks, clientSocketOmhHandler);  };
//		new Thread(task).start();
		
//		for( int i=0 ; i<3 ; i++ ) {
//			ValidicMessage before = createMessageBlock();
//			String rawJson = objectMapper.writeValueAsString(before);
//			System.out.println(rawJson);
//			ValidicMessage after = objectMapper.readValue(rawJson, ValidicMessage.class);
//			
//			System.out.println(after.getHeader());
//			after.getBodies().forEach(b->System.out.println("\t" + b.toString()));
//		}
	}
	
	
	public void createMessageBlock(String userId, int numBlocks, ClientSocketOmhHandler clientSocketOmhHandler ) {
		try {
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
					
					if( ++cntLoop == BLOCK_SIZE ) break;
					Iote2eUtils.sleepMillis(1000L);
					bodyDateTime = OffsetDateTime.now();
				}
				header.setEndDateTime(bodyDateTime);
				header.setCreationDateTime(OffsetDateTime.now());
				
				ValidicMessage validicMessage = new ValidicMessage().setHeader(header).setBodies(bodies);
				StringBuilder sb = new StringBuilder();
				sb.append(validicMessage.getHeader()).append("\n");
				validicMessage.getBodies().forEach(b->sb.append("\t").append( b.toString()).append("\n"));
				System.out.println(sb.toString());
			}
		} catch( Exception e ) {
			logger.error(e.getMessage(), e );
		}
	}
}
