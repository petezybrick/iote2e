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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import com.pzybrick.avro.schema.User;


/**
 * The Class AvroFirst.
 */
public class AvroFirst {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			AvroFirst avroFirst = new AvroFirst();
			avroFirst.process();
			
		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
	
	/**
	 * Process.
	 *
	 * @throws Exception the exception
	 */
	public void process() throws Exception {
		List<User> users = new ArrayList<User>();
		int base = (int)System.currentTimeMillis();
		for( int i=0; i<10 ; i++ ) {
			users.add( User.newBuilder().setName("name"+base).setFavoriteColor("color"+base).setFavoriteNumber(base).build());
			base++;
		}
		DatumWriter<User> datumWriterUser = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriterUser = new DataFileWriter<User>(datumWriterUser);
		dataFileWriterUser.create(users.get(0).getSchema(), new File("/Users/pzybrick/temp/avrofirst.ser"));
		for( User user : users ) dataFileWriterUser.append(user);
		dataFileWriterUser.close();
		
		DatumReader<User> datumReaderUser = new SpecificDatumReader<User>(User.class);
		DataFileReader<User> dataFileReaderUser = new DataFileReader<User>(new File("/Users/pzybrick/temp/avrofirst.ser"), datumReaderUser);
		User user = null;
		while( dataFileReaderUser.hasNext() ) {
			user = dataFileReaderUser.next(user);
			System.out.println(user);
		}
		dataFileReaderUser.close();
	}

}
