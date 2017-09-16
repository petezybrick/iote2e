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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.Utf8;

import com.pzybrick.avro.schema.User;


/**
 * The Class AvroByteArray.
 */
public class AvroByteArray {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			AvroByteArray avroByteArray = new AvroByteArray();
			avroByteArray.processSingle();

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
	
	/**
	 * Process single.
	 *
	 * @throws Exception the exception
	 */
	public void processSingle() throws Exception {
		int base = (int) System.currentTimeMillis();
		Map<CharSequence,CharSequence> myMap = new HashMap<CharSequence,CharSequence>();
		myMap.put("Mike", "one");
		myMap.put("Chris", "two");
		myMap.put("Rob", "three");
		myMap.put("Madeline", "four");
		User user = User.newBuilder().setName("name" + base).setFavoriteColor("color" + base).setFavoriteNumber(base)
				.setMymap(myMap)
				.build();
		DatumWriter<User> datumWriterUser = new SpecificDatumWriter<User>(User.getClassSchema());
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

		DatumReader<User> datumReaderUser = new SpecificDatumReader<User>( User.getClassSchema());
		User userRead = datumReaderUser.read(null, DecoderFactory.get().binaryDecoder(byteData, null) );
		System.out.println(userRead.getFavoriteColor());
		System.out.println(userRead.getMymap());
		System.out.println(userRead.getMymap().keySet());
		myMap = userRead.getMymap();
		System.out.println(myMap.get( new Utf8("Mike")));

	}
	
	/**
	 * Process singlex.
	 *
	 * @throws Exception the exception
	 */
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
		
		DatumReader<GenericRecord> datumReaderUser = new GenericDatumReader<GenericRecord>( User.getClassSchema());
		GenericRecord genericRecord = datumReaderUser.read(null, DecoderFactory.get().binaryDecoder(byteData, null) );
		System.out.println(genericRecord);
		System.out.println( genericRecord.get("name"));
	}

	/**
	 * Process list.
	 *
	 * @throws Exception the exception
	 */
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
