package com.pzybrick.learn.avro;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.avro.schema.User;
import com.pzybrick.avro.schema.Wave;

public class AvroByteArrayGenSpec {

	public static void main(String[] args) {
		try {
			AvroByteArrayGenSpec avroByteArray = new AvroByteArrayGenSpec();
			avroByteArray.processSingle();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	public void processSingle() throws Exception {
		int base = (int) System.currentTimeMillis();
		Map<CharSequence, CharSequence> myMap = new HashMap<CharSequence, CharSequence>();
		User user = User.newBuilder().setName("name" + base).setFavoriteColor("color" + base).setFavoriteNumber(base)
				.setMymap(myMap).build();
		Wave wave = Wave.newBuilder().setSourceUuid("aa-aa-aa-aa").setTsEpoch(System.currentTimeMillis())
				.setWaveType("Gravy").setWaveValue(12.34).build();
		DatumWriter<User> datumWriterUser = new GenericDatumWriter<User>(User.getClassSchema());
		DatumWriter<Wave> datumWriterWave = new GenericDatumWriter<Wave>(Wave.getClassSchema());
		List<byte[]> byteDatas = new ArrayList<byte[]>();
		BinaryEncoder binaryEncoder = null;

		for (int i = 1; i < 3; i++) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
				byte[] byteData = null;
				user.getMymap().put("testname", "testvalue" + i);
				datumWriterUser.write(user, binaryEncoder);
				binaryEncoder.flush();
				byteData = baos.toByteArray();
				byteDatas.add(byteData);
				baos.close();

				baos = new ByteArrayOutputStream();
				binaryEncoder = EncoderFactory.get().binaryEncoder(baos, binaryEncoder);
				byteData = null;
				wave.setWaveValue(new Double(i));
				datumWriterWave.write(wave, binaryEncoder);
				binaryEncoder.flush();
				byteDatas.add(byteData);
				baos.close();
			} finally {

			}
		}
		System.out.println(byteDatas.size());

		DatumReader<Wave> datumReaderWave = new SpecificDatumReader<Wave>(Wave.getClassSchema());
		DatumReader<User> datumReaderUser = new SpecificDatumReader<User>(User.getClassSchema());
		BinaryDecoder binaryDecoder = null;

		for (byte[] byteData : byteDatas) {
		    binaryDecoder = DecoderFactory.get().binaryDecoder(byteData, null);
		    GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();
		    reader.setSchema(Wave.getClassSchema());
		    GenericRecord genericRecord = reader.read(null,  binaryDecoder);
		    
		    System.out.println(genericRecord);
			

//			try {
//				binaryDecoder = DecoderFactory.get().binaryDecoder(byteData, binaryDecoder);
//				Wave waveRead = datumReaderWave.read(null, binaryDecoder);
//				System.out.println(waveRead);
//				
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//
//			try {
//				binaryDecoder = DecoderFactory.get().binaryDecoder(byteData, binaryDecoder);
//				User userRead = datumReaderUser.read(null, binaryDecoder);
//				System.out.println(userRead);
//				
//			} catch (Exception e) {
//				System.out.println(e);
//			}

		}

		// System.out.println(userRead.getFavoriteColor());
		// System.out.println(userRead.getMymap());
		// System.out.println(userRead.getMymap().keySet());
		// myMap = userRead.getMymap();
		// System.out.println(myMap.get(new Utf8("Mike")));

	}

	public void processSinglex() throws Exception {
		int base = (int) System.currentTimeMillis();
		User user = User.newBuilder().setName("name" + base).setFavoriteColor("color" + base).setFavoriteNumber(base)
				.build();
		DatumWriter<GenericRecord> datumWriterUser = new GenericDatumWriter<GenericRecord>(User.getClassSchema());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] byteData = null;
		try {
			BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(baos, null);
			datumWriterUser.write(user, binaryEncoder);
			binaryEncoder.flush();
			byteData = baos.toByteArray();
		} finally {
			baos.close();
		}
		System.out.println(byteData.length);

		DatumReader<GenericRecord> datumReaderUser = new GenericDatumReader<GenericRecord>(User.getClassSchema());
		GenericRecord genericRecord = datumReaderUser.read(null, DecoderFactory.get().binaryDecoder(byteData, null));
		System.out.println(genericRecord);
		System.out.println(genericRecord.get("name"));
	}

	public void processList() throws Exception {
		List<User> users = new ArrayList<User>();
		int base = (int) System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			users.add(User.newBuilder().setName("name" + base).setFavoriteColor("color" + base).setFavoriteNumber(base)
					.build());
			base++;
		}
		DatumWriter<User> datumWriterUser = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriterUser = new DataFileWriter<User>(datumWriterUser);
		dataFileWriterUser.create(users.get(0).getSchema(), new File("/Users/pzybrick/temp/avrofirst.ser"));
		for (User user : users)
			dataFileWriterUser.append(user);
		dataFileWriterUser.close();

		DatumReader<User> datumReaderUser = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReaderUser = new DataFileReader<User>(
				new File("/Users/pzybrick/temp/avrofirst.ser"), datumReaderUser);
		User user = null;
		while (dataFileReaderUser.hasNext()) {
			user = dataFileReaderUser.next(user);
			System.out.println(user);
		}
		dataFileReaderUser.close();
	}

}
