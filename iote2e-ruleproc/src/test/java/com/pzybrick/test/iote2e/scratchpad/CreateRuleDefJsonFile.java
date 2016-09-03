package com.pzybrick.test.iote2e.scratchpad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.ruleproc.svc.RuleDefCondItem;
import com.pzybrick.iote2e.ruleproc.svc.RuleDefItem;
import com.pzybrick.iote2e.ruleproc.svc.RuleSourceSensor;
import com.pzybrick.iote2e.ruleproc.svc.SourceSensorActuator;

public class CreateRuleDefJsonFile {
	private static final Log log = LogFactory.getLog(CreateRuleDefJsonFile.class);

	public static void main(String[] args) {
		try {
			String nameExtRuleDefs = args[0] + "/rule_def_item.json";
			File fileNameExtRuleItems = new File(nameExtRuleDefs);
			fileNameExtRuleItems.delete();
			String nameExtRuleSourceSensors = args[0] + "/rule_source_sensor.json";
			File fileNameExtRuleSourceSensors = new File(nameExtRuleSourceSensors);
			fileNameExtRuleSourceSensors.delete();
			String nameExtSourceSensorActuator = args[0] + "/source_sensor_actuator.json";
			File fileNameExtSourceSensorActuator = new File(nameExtSourceSensorActuator);
			fileNameExtSourceSensorActuator.delete();
			
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
			String sensorUuidTemp = UUID.randomUUID().toString();
			String sensorUuidHumidity = UUID.randomUUID().toString();
			String actuatorUuidFan = UUID.randomUUID().toString();
			String actuatorUuidMister = UUID.randomUUID().toString();

			String sourceUuidLights = UUID.randomUUID().toString();
			String sensorUuidSwitch0 = UUID.randomUUID().toString();
			String sensorUuidSwitch1 = UUID.randomUUID().toString();
			String sensorUuidSwitch2 = UUID.randomUUID().toString();
			String actuatorUuidLedGreen = UUID.randomUUID().toString();
			String actuatorUuidLedRed = UUID.randomUUID().toString();
			String actuatorUuidLedYellow = UUID.randomUUID().toString();

			List<RuleSourceSensor> ruleSourceSensors = new ArrayList<>();
			ruleSourceSensors.add( new RuleSourceSensor().setSourceUuid(sourceUuidFanMister).setSensorUuid(sensorUuidTemp)
					.setRuleUuid(fanRuleDefItem.getRuleUuid()).setDesc("TempToFan"));
			ruleSourceSensors.add( new RuleSourceSensor().setSourceUuid(sourceUuidFanMister).setSensorUuid(sensorUuidHumidity)
					.setRuleUuid(misterRuleDefItem.getRuleUuid()).setDesc("HumidityToMister"));
			
			ruleSourceSensors.add( new RuleSourceSensor().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch0)
					.setRuleUuid(ledGreenRuleDefItem.getRuleUuid()).setDesc("Switch0ToLedGreen"));
			ruleSourceSensors.add( new RuleSourceSensor().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch1)
					.setRuleUuid(ledRedRuleDefItem.getRuleUuid()).setDesc("Switch1ToLedRed"));
			ruleSourceSensors.add( new RuleSourceSensor().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch2)
					.setRuleUuid(ledYellowRuleDefItem.getRuleUuid()).setDesc("Switch2ToLedYellow"));

			List<SourceSensorActuator> sourceSensorActuators = new ArrayList<SourceSensorActuator>();
			sourceSensorActuators
			.add(new SourceSensorActuator().setSourceUuid(sourceUuidFanMister).setSensorUuid(sensorUuidTemp)
					.setActuatorUuid(actuatorUuidFan).setActuatorValue(null).setDesc("TempToFan"));
			sourceSensorActuators
			.add(new SourceSensorActuator().setSourceUuid(sourceUuidFanMister).setSensorUuid(sensorUuidHumidity)
					.setActuatorUuid(actuatorUuidMister).setActuatorValue(null).setDesc("HumidityToMister"));
			sourceSensorActuators
			.add(new SourceSensorActuator().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch0)
					.setActuatorUuid(actuatorUuidLedGreen).setActuatorValue(null).setDesc("Switch0ToLedGreen"));
			sourceSensorActuators
			.add(new SourceSensorActuator().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch1)
					.setActuatorUuid(actuatorUuidLedRed).setActuatorValue(null).setDesc("Switch1ToLedRed"));
			sourceSensorActuators
			.add(new SourceSensorActuator().setSourceUuid(sourceUuidLights).setSensorUuid(sensorUuidSwitch2)
					.setActuatorUuid(actuatorUuidLedYellow).setActuatorValue(null).setDesc("Switch2ToLedYellow"));

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileUtils.writeStringToFile(fileNameExtRuleItems, gson.toJson(ruleDefItems));
			FileUtils.writeStringToFile(fileNameExtRuleSourceSensors, gson.toJson(ruleSourceSensors));
			FileUtils.writeStringToFile(fileNameExtSourceSensorActuator, gson.toJson(sourceSensorActuators));

		} catch (Exception e) {
			log.error(e);
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

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleUuid(null).setRuleName("TempFan")
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

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleUuid(null).setRuleName("HumidityMister")
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

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleUuid(null).setRuleName("LedGreenOnOff")
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

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleUuid(null).setRuleName("LedGreenOnOff")
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

		RuleDefItem ruleDefItem = new RuleDefItem().setRuleUuid(null).setRuleName("LedGreenOnOff")
				.setRuleDefCondItems(ruleDefCondItems);
		return ruleDefItem;
	}

}
