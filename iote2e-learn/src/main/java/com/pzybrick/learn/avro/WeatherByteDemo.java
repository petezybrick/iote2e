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

public class WeatherByteDemo {

	public static void main(String[] args) {
		try {
			List<Weather> weathers = GenWeatherData.genWeatherData(10000);
//			for (Weather weather : weathers)
//				System.out.println(weather);
			WeatherByteDemo weatherByteDemo = new WeatherByteDemo();
			weatherByteDemo.processList(weathers);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	public void processList(List<Weather> weathers) throws Exception {
		long before = System.currentTimeMillis();
		BinaryEncoder binaryEncoder = null;
		BinaryDecoder binaryDecoder = null;
		Weather weatherRead = null;
		for (Weather weather : weathers) {
			DatumWriter<Weather> datumWriterWeather = new SpecificDatumWriter<Weather>(Weather.getClassSchema());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] byteData = null;
			try {
				binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
				datumWriterWeather.write(weather, binaryEncoder);
				binaryEncoder.flush();
				byteData = baos.toByteArray();
			} finally {
				baos.close();
			}

			DatumReader<Weather> datumReaderWeather = new SpecificDatumReader<Weather>(Weather.getClassSchema());
			binaryDecoder = DecoderFactory.get().binaryDecoder(byteData, binaryDecoder);
			weatherRead = datumReaderWeather.read(weatherRead, binaryDecoder);
			// System.out.println("After Binary Read: " + weatherRead.toString());
		}
		System.out.println("size=" + weathers.size() + ", elapsed: " + (System.currentTimeMillis()-before));
	}

}
