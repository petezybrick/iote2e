package com.pzybrick.iote2e.stream.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.util.Utf8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public class RuleCustomPillImageImpl implements RuleCustom {
	private static final Logger logger = LogManager.getLogger(RuleCustomPillImageImpl.class);

	public List<RuleEvalResult> ruleEval(String loginUuid, String sourceUuid, String sensorName, String sensorValue,
			List<RuleEvalResult> ruleEvalResults, Iote2eRequest iote2eRequest ) throws Exception {
		//Convert Byte64 string to image, then process the image to count the circles, return number of circles as actuator value
		// TODO: update database, but need to add a uuid to the inbound Iote2eRequest pairs, uuid must have been generated when the pill 
		//			dispense Iote2eResult was sent down to the dispenser, so we can use it to update the num_actual_dispensed based
		//			on the image processing.  Use the metadata in the schema to contain the name/value pair for the pill_dispensed_uuid
		int numPillsToDispense = -1;
		CharSequence keyNumPillsoDispense = new Utf8("num_pills_to_dispense");
		if( iote2eRequest.getMetadata().containsKey(keyNumPillsoDispense) ){
			numPillsToDispense = Integer.parseInt(iote2eRequest.getMetadata().get(keyNumPillsoDispense).toString() );
		}
		int numPillsActualDispense = 3;	// TODO: call boofcv here
		// If correct number of pills dispensed, then show greeen led on pill dispenser, else red
		String actuatorTargetValue = ( numPillsToDispense == numPillsActualDispense ) ? "green" : "red";
		Map<String,String> metadata = new HashMap<String,String>();
		RuleEvalResult ruleEvalResult = new RuleEvalResult(sensorName, actuatorTargetValue, metadata);
		ruleEvalResults.add(ruleEvalResult);

		logger.debug(ruleEvalResults);
		return ruleEvalResults;
	}

}
