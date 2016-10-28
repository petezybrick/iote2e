package com.pzybrick.learn.avro2kafkaweather;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.pzybrick.avro.schema.Weather;
import com.pzybrick.learn.avro.GenWeatherData;
import com.pzybrick.learn.utils.LogTool;

public class AvroProducerWeather {
	private static final Log log = LogFactory.getLog(AvroProducerWeather.class);

	public static void main(String[] args) {
		try {
			LogTool.initConsole();
			int numRows = Integer.parseInt(args[0]);
			AvroProducerWeather avroProducerWeather = new AvroProducerWeather();
			avroProducerWeather.process(GenWeatherData.genWeatherData(numRows));
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public void process(List<Weather> weathers) throws Exception {
		log.info(">>> Processing weathers, size=" + weathers.size());
		Properties props = new Properties();
		// props.put("metadata.broker.list", "hp-lt-ubuntu-1:9092");
		props.put("bootstrap.servers", "hp-lt-ubuntu-1:9092");
		props.put("producer.type", "sync");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		// props.put("partition.assignment.strategy", "range");
		// props.put("partitioner.class",
		// "com.pzybrick.kafka1.training.KafkaPartitionerTest");
		props.put("request.required.acks", "1");
		props.put("group.id", "group1");
		props.put("zookeeper.connect", "hp-lt-ubuntu-1:2181");


		KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(props);
		long keyNum = System.nanoTime();
		DatumWriter<Weather> datumWriterWeather = new SpecificDatumWriter<Weather>(Weather.getClassSchema());

		BinaryEncoder binaryEncoder = null;
		for (Weather weather : weathers) {
			String key = String.valueOf(keyNum);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] bytes = null;
			try {
				binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
				datumWriterWeather.write(weather, binaryEncoder);
				binaryEncoder.flush();
				bytes = baos.toByteArray();
				ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>("pzmultiptn1", key, bytes);
				producer.send(data);
				keyNum++;
			} finally {
				baos.close();
			}
		}

		producer.close();
	}
}