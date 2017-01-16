package com.pzybrick.test.iote2e.scratchpad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.svc.ActuatorState;
import com.pzybrick.iote2e.ruleproc.svc.LoginSourceSensorActuator;
import com.pzybrick.iote2e.ruleproc.svc.RuleDefCondItem;
import com.pzybrick.iote2e.ruleproc.svc.RuleDefItem;
import com.pzybrick.iote2e.ruleproc.svc.RuleLoginSourceSensor;

public class CreateRuleDefJsonFile {
	private static final Logger logger = LogManager.getLogger(CreateRuleDefJsonFile.class);

	public static void main(String[] args) {
		try {
			String nameExtRuleDefs = args[0] + "/rule_def_item.json";
			File fileNameExtRuleItems = new File(nameExtRuleDefs);
			fileNameExtRuleItems.delete();
			String nameExtRuleSourceSensors = args[0] + "/rule_source_sensor.json";
			File fileNameExtRuleSourceSensors = new File(nameExtRuleSourceSensors);
			fileNameExtRuleSourceSensors.delete();
			String nameExtActuatorState = args[0] + "/actuator_state.json";
			File fileNameExtActuatorState = new File(nameExtActuatorState);
			fileNameExtActuatorState.delete();
			
			RuleDefItem fanRuleDefItem = createTempRuleDefItem();
			RuleDefItem misterRuleDefItem = createHumidityRuleDefItem();
			RuleDefItem ledGreenRuleDefItem = createLedGreenRuleDefItem();
			RuleDefItem ledRedRuleDefItem = createLedRedRuleDefItem();
			RuleDefItem ledYellowRuleDefItem = createLedYellowRuleDefItem();
			
			List<RuleDefItem> ruleDefItems = new ArrayList<RuleDefItem>();
			ruleDefItems.add(fanRuleDefItem);
			ruleDefItems.add(misterRuleDefItem);
			ruleDefItems.add(ledGreenRuleDefItem);
			ruleDefItems.add(ledRedRuleDefItem);
			ruleDefItems.add(ledYellowRuleDefItem);			

			// source_uuid can have many sensors and actuators, i.e. an RPi with
			// multi sensors/switches and actuators
			// For this example, Fan and Mister will be on one source_uuid with
			// name=greenhouse,
			// with the LEDs on another with name="lights"
			String sourceUuidFanMister = UUID.randomUUID().toString();
			String sensorNameTemp = UUID.randomUUID().toString();
			String sensorNameHumidity = UUID.randomUUID().toString();
			String actuatorNameFan = UUID.randomUUID().toString();
			String actuatorNameMister = UUID.randomUUID().toString();

			String sourceUuidLights = UUID.randomUUID().toString();
			String sensorNameSwitch0 = UUID.randomUUID().toString();
			String sensorNameSwitch1 = UUID.randomUUID().toString();
			String sensorNameSwitch2 = UUID.randomUUID().toString();
			String actuatorNameLedGreen = UUID.randomUUID().toString();
			String actuatorNameLedRed = UUID.randomUUID().toString();
			String actuatorNameLedYellow = UUID.randomUUID().toString();

			List<RuleLoginSourceSensor> ruleSourceSensors = new ArrayList<>();
			ruleSourceSensors.add( new RuleLoginSourceSensor().setSourceName(sourceUuidFanMister).setSensorName(sensorNameTemp)
					.setRuleName(fanRuleDefItem.getRuleName()).setDesc("TempToFan"));
			ruleSourceSensors.add( new RuleLoginSourceSensor().setSourceName(sourceUuidFanMister).setSensorName(sensorNameHumidity)
					.setRuleName(misterRuleDefItem.getRuleName()).setDesc("HumidityToMister"));
			
			ruleSourceSensors.add( new RuleLoginSourceSensor().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch0)
					.setRuleName(ledGreenRuleDefItem.getRuleName()).setDesc("Switch0ToLedGreen"));
			ruleSourceSensors.add( new RuleLoginSourceSensor().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch1)
					.setRuleName(ledRedRuleDefItem.getRuleName()).setDesc("Switch1ToLedRed"));
			ruleSourceSensors.add( new RuleLoginSourceSensor().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch2)
					.setRuleName(ledYellowRuleDefItem.getRuleName()).setDesc("Switch2ToLedYellow"));

			List<ActuatorState> actuatorStates = new ArrayList<ActuatorState>();
			actuatorStates
			.add(new ActuatorState().setSourceName(sourceUuidFanMister).setSensorName(sensorNameTemp)
					.setActuatorName(actuatorNameFan).setActuatorValue(null).setActuatorDesc("TempToFan"));
			actuatorStates
			.add(new ActuatorState().setSourceName(sourceUuidFanMister).setSensorName(sensorNameHumidity)
					.setActuatorName(actuatorNameMister).setActuatorValue(null).setActuatorDesc("HumidityToMister"));
			actuatorStates
			.add(new ActuatorState().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch0)
					.setActuatorName(actuatorNameLedGreen).setActuatorValue(null).setActuatorDesc("Switch0ToLedGreen"));
			actuatorStates
			.add(new ActuatorState().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch1)
					.setActuatorName(actuatorNameLedRed).setActuatorValue(null).setActuatorDesc("Switch1ToLedRed"));
			actuatorStates
			.add(new ActuatorState().setSourceName(sourceUuidLights).setSensorName(sensorNameSwitch2)
					.setActuatorName(actuatorNameLedYellow).setActuatorValue(null).setActuatorDesc("Switch2ToLedYellow"));

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileUtils.writeStringToFile(fileNameExtRuleItems, gson.toJson(ruleDefItems));
			FileUtils.writeStringToFile(fileNameExtRuleSourceSensors, gson.toJson(ruleSourceSensors));
			FileUtils.writeStringToFile(fileNameExtActuatorState, gson.toJson(actuatorStates));

		} catch (Exception e) {
			logger.error(e);
		}

	}

	public static RuleDefItem createTempRuleDefItem() {
		List<RuleDefCondItem> ruleDefCondItems = new ArrayList<RuleDefCondItem>();
		// If temp >= 80 and fan is off then turn fan on
		RuleDefCondItem ruleDefCondItem = new RuleDefCondItem().setSensorComparator("GE").setSensorCompareValue("80")
				.setSensorTypeValue("dbl").setActuatorComparator("EQ").setActuatorCompareValue("off")
				.setActuatorTypeValue("str").setActuatorTargetCmd("fan").setActuatorTargetValue("on")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		// Turn off fan when temp <= 77
		ruleDefCondItem = new RuleDefCondItem().setSensorComparator("LE").setSensorCompareValue("77")
				.setSensorTypeValue("dbl").setActuatorComparator("EQ").setActuatorCompareValue("on")
				.setActuatorTypeValue("str").setActuatorTargetCmd("fan").setActuatorTargetValue("off")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleName(null).setRuleName("TempFan")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

	public static RuleDefItem createHumidityRuleDefItem() {
		List<RuleDefCondItem> ruleDefCondItems = new ArrayList<RuleDefCondItem>();
		// If rel humidity < 85% and mister off then turn mister on
		RuleDefCondItem ruleDefCondItem = new RuleDefCondItem().setSensorComparator("LE").setSensorCompareValue("85")
				.setSensorTypeValue("dbl").setActuatorComparator("EQ").setActuatorCompareValue("off")
				.setActuatorTypeValue("str").setActuatorTargetCmd("mister").setActuatorTargetValue("on")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		// If rel humidity > 90% and mister on the turn mister off
		ruleDefCondItem = new RuleDefCondItem().setSensorComparator("GE").setSensorCompareValue("90")
				.setSensorTypeValue("dbl").setActuatorComparator("EQ").setActuatorCompareValue("on")
				.setActuatorTypeValue("str").setActuatorTargetCmd("mister").setActuatorTargetValue("off")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleName(null).setRuleName("HumidityMister")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

	public static RuleDefItem createLedGreenRuleDefItem() {
		List<RuleDefCondItem> ruleDefCondItems = new ArrayList<RuleDefCondItem>();
		// If switch is on and LED is not green then turn on LED green
		RuleDefCondItem ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("1")
				.setSensorTypeValue("int").setActuatorComparator("NE").setActuatorCompareValue("green")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("green")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		// If switch is off and LED is green then turn LED off
		ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("0")
				.setSensorTypeValue("int").setActuatorComparator("EQ").setActuatorCompareValue("green")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("off")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleName(null).setRuleName("LedGreenOnOff")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

	public static RuleDefItem createLedRedRuleDefItem() {
		List<RuleDefCondItem> ruleDefCondItems = new ArrayList<RuleDefCondItem>();
		// If switch is on and LED is not red then turn on LED red
		RuleDefCondItem ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("1")
				.setSensorTypeValue("int").setActuatorComparator("NE").setActuatorCompareValue("red")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("red")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		// If switch is off and LED is red then turn LED off
		ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("0")
				.setSensorTypeValue("int").setActuatorComparator("EQ").setActuatorCompareValue("red")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("off")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleName(null).setRuleName("LedGreenOnOff")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

	public static RuleDefItem createLedYellowRuleDefItem() {
		List<RuleDefCondItem> ruleDefCondItems = new ArrayList<RuleDefCondItem>();
		// If switch is on and LED is not yellow then turn on LED yellow
		RuleDefCondItem ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("1")
				.setSensorTypeValue("int").setActuatorComparator("NE").setActuatorCompareValue("yellow")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("yellow")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		// If switch is off and LED is green then turn LED off
		ruleDefCondItem = new RuleDefCondItem().setSensorComparator("EQ").setSensorCompareValue("0")
				.setSensorTypeValue("int").setActuatorComparator("EQ").setActuatorCompareValue("green")
				.setActuatorTypeValue("str").setActuatorTargetCmd("led_color").setActuatorTargetValue("off")
				.setStopEvalOnMatch(true);
		ruleDefCondItems.add(ruleDefCondItem);

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleName(null).setRuleName("LedGreenOnOff")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

}
