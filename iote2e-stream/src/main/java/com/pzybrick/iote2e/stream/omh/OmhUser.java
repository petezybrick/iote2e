package com.pzybrick.iote2e.stream.omh;

public class OmhUser {
	private String firstName;
	private String lastName;
	private String email;
	
	public OmhUser( String csvFirstLastEmail ) {
		String[] tokens = csvFirstLastEmail.split("[\t]");
		this.firstName = tokens[0];
		this.lastName = tokens[1];
		this.email = tokens[2];
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public OmhUser setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	public OmhUser setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	public OmhUser setEmail(String email) {
		this.email = email;
		return this;
	}
	
}
