package com.pzybrick.learn.avro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pzybrick.avro.schema.Weather;

public class GenWeatherData {
    private static final SimpleDateFormat ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final String[] ELEMENT_NAMES = { "TMIN", "TMAX", "TOBS", "WDFM", "WSFM", "WSFG" };

	public static List<Weather> genWeatherData(int numRows) throws Exception {		
		List<Weather> weathers = new ArrayList<Weather>();
		Long base = (System.currentTimeMillis() % 1000L) + 1;
		long nextTime = System.currentTimeMillis();

		for (int i = 0; i < numRows; i++) {
			Double baseDouble = new Double(base);
			String startTime = ISO8601_UTC.format(nextTime);
			String endTime = ISO8601_UTC.format(nextTime+2000L);
			Map<CharSequence, Long> elements = new HashMap<CharSequence, Long>();
			long elementValue = i;
			for (String elementName : ELEMENT_NAMES)
				elements.put(elementName, new Long(elementValue++));
			Weather weather = Weather.newBuilder()
					.setStationId("stationId_" + base)
					.setStationLat(baseDouble/10)
					.setStationLon(baseDouble/100)
					.setStationAlt(base+10)
					.setTsStartUtc(startTime)
					.setTsEndUtc(endTime)
					.setElements(elements)
					.build();
			weathers.add(weather);
			nextTime += 5000L;
			base++;
		}	
		
		return weathers;
	}

}
