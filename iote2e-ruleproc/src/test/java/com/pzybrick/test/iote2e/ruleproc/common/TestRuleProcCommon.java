package com.pzybrick.test.iote2e.ruleproc.common;

public interface TestRuleProcCommon {
	public static final String testHumidityLoginName = "lo1";
	public static final String testHumiditySourceName = "lo1so1";
	public static final String testHumiditySourceType = "humidity";
	public static final String testHumiditySensorName = "humidity1";
	public static final String testHumidityFilterKey = testHumidityLoginName + "|" + testHumiditySourceName + "|" + testHumiditySensorName + "|";

	public static final String testLedLoginName = "lo1";
	public static final String testLedSourceName = "lo1so2";
	public static final String testLedSourceType = "switch";
	public static final String testLedSensorNameGreen = "switch0";
	public static final String testLedSensorNameRed = "switch1";
	public static final String testLedSensorNameYellow = "switch2";
	
	public static final String testTempToFanLoginName = "lo1";
	public static final String testTempToFanSourceName = "lo1so1";
	public static final String testTempToFanSourceType = "temp";
	public static final String testTempToFanSensorName = "temp1";
	public static final String filterKey = testTempToFanLoginName + "|" + testTempToFanSourceName + "|" + testTempToFanSensorName + "|";

}
