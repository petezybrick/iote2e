package com.pzybrick.iote2e.common.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Iote2eUtils {
	private static Gson gson;

	public static String getDateNowUtc8601() {
		return ISODateTimeFormat.dateTime().print(new DateTime().toDateTime(DateTimeZone.UTC));
	}
	
	public synchronized static Gson getGsonInstance( ) {
		if( gson == null ) gson = new GsonBuilder().setPrettyPrinting().create();
		return gson;
	}
}
