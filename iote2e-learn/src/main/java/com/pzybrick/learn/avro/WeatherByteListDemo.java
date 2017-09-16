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

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.avro.schema.Weather;


/**
 * The Class WeatherByteListDemo.
 */
public class WeatherByteListDemo {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			List<Weather> weathers = GenWeatherData.genWeatherData(3);
			// for (Weather weather : weathers)
			// System.out.println(weather);
			WeatherByteListDemo weatherByteDemo = new WeatherByteListDemo();
			weatherByteDemo.processList(weathers);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	/**
	 * Process list.
	 *
	 * @param weathers the weathers
	 * @throws Exception the exception
	 */
	public void processList(List<Weather> weathers) throws Exception {
		long before = System.currentTimeMillis();
		BinaryEncoder binaryEncoder = null;
		BinaryDecoder binaryDecoder = null;
		Weather weatherRead = null;
		DatumWriter<Weather> datumWriterWeather = new SpecificDatumWriter<Weather>(
				Weather.getClassSchema());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] byteData = null;
		try {
			binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
			for( Weather weather : weathers ) {
				datumWriterWeather.write(weather, binaryEncoder);
			}
			binaryEncoder.flush();
			byteData = baos.toByteArray();
		} catch(Exception e )  {
			System.out.println(e);
			e.printStackTrace();
		} finally {
				baos.close();
		}

		DatumReader<Weather> datumReaderWeather = new SpecificDatumReader<Weather>(Weather.getClassSchema());
		binaryDecoder = DecoderFactory.get().binaryDecoder(byteData, binaryDecoder);
		while( true ) {
			try {
				weatherRead = datumReaderWeather.read(weatherRead, binaryDecoder);
			} catch( java.io.EOFException e ) {
				break;
			}
			System.out.println(weatherRead);
		}

		System.out.println("size=" + weathers.size() + ", elapsed: " + (System.currentTimeMillis() - before));
	}

}
