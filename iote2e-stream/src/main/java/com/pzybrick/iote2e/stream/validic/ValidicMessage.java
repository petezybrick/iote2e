package com.pzybrick.iote2e.stream.validic;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidicMessage {

	@JsonProperty("header")
	private ValidicHeader header;
	
	@JsonProperty("bodies")
	private List<ValidicBody> bodies = null;

	public ValidicHeader getHeader() {
		return header;
	}

	public List<ValidicBody> getBodies() {
		return bodies;
	}

	public ValidicMessage setHeader(ValidicHeader header) {
		this.header = header;
		return this;
	}

	public ValidicMessage setBodies(List<ValidicBody> bodies) {
		this.bodies = bodies;
		return this;
	}

	@Override
	public String toString() {
		return "ValidicMessage [header=" + header + ", bodies=" + bodies + "]";
	}

}