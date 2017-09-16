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
package com.pzybrick.learn.avro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pzybrick.avro.schema.Weather;


/**
 * The Class GenWeatherData.
 */
public class GenWeatherData {
    
    /** The Constant ISO8601_UTC. */
    private static final SimpleDateFormat ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    /** The Constant ELEMENT_NAMES. */
    private static final String[] ELEMENT_NAMES = { "TMIN", "TMAX", "TOBS", "WDFM", "WSFM", "WSFG" };

	/**
	 * Gen weather data.
	 *
	 * @param numRows the num rows
	 * @return the list
	 * @throws Exception the exception
	 */
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
