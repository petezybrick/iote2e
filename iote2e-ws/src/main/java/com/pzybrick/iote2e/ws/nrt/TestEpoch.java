package com.pzybrick.iote2e.ws.nrt;

import java.time.Instant;

public class TestEpoch {

	public static void main(String[] args) {
		long millisFromEpoch = Instant.parse( "2017-07-02T17:37:24.633Z" ).toEpochMilli();
		System.out.println(millisFromEpoch);

	}

}
