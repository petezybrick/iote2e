package com.pzybrick.iote2e.common.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

public class Iote2eUtils {

	public static String getDateNowUtc8601() {
		return ISODateTimeFormat.dateTime().print(new DateTime().toDateTime(DateTimeZone.UTC));
	}
}
