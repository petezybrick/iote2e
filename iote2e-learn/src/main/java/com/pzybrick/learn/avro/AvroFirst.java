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

public class AvroFirst {

	public static void main(String[] args) {
		try {
			AvroFirst avroFirst = new AvroFirst();
			avroFirst.process();
			
		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}

	}
	
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
