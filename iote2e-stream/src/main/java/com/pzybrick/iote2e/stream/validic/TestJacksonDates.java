package com.pzybrick.iote2e.stream.validic;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestJacksonDates {

	public static void main(String[] args) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.registerModule(new JavaTimeModule());
	        //objectMapper.sets
			OffsetDateTime testDateTime = OffsetDateTime.now();
	        TestItem before = new TestItem().setTestString("abc").setTestInteger(123).setTestDateTime(testDateTime);
			String rawJson = objectMapper.writeValueAsString(before);
			System.out.println(rawJson);
			TestItem after = objectMapper.readValue(rawJson, TestItem.class);
			System.out.println(after);

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class TestItem {
		@JsonProperty("test_string")
		private String testString;
		@JsonProperty("test_integer")
		private Integer testInteger;
		@JsonProperty("test_date_time")
		private OffsetDateTime testDateTime;
		
		public String getTestString() {
			return testString;
		}
		public Integer getTestInteger() {
			return testInteger;
		}
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		public OffsetDateTime getTestDateTime() {
			return testDateTime;
		}
		public TestItem setTestString(String testString) {
			this.testString = testString;
			return this;
		}
		public TestItem setTestInteger(Integer testInteger) {
			this.testInteger = testInteger;
			return this;
		}
		public TestItem setTestDateTime(OffsetDateTime testDateTime) {
			this.testDateTime = testDateTime;
			return this;
		}
		@Override
		public String toString() {
			return "TestItem [testString=" + testString + ", testInteger=" + testInteger + ", testDateTime="
					+ testDateTime + "]";
		}
		
		
	}

}
