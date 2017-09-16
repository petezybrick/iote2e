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
package com.pzybrick.iote2e.tests.omh;


/**
 * The Class OmhUser.
 */
public class OmhUser {
	
	/** The first name. */
	private String firstName;
	
	/** The last name. */
	private String lastName;
	
	/** The email. */
	private String email;
	
	/**
	 * Instantiates a new omh user.
	 *
	 * @param csvFirstLastEmail the csv first last email
	 */
	public OmhUser( String csvFirstLastEmail ) {
		String[] tokens = csvFirstLastEmail.split("[\t]");
		this.firstName = tokens[0];
		this.lastName = tokens[1];
		this.email = tokens[2];
	}
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the first name.
	 *
	 * @param firstName the first name
	 * @return the omh user
	 */
	public OmhUser setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	/**
	 * Sets the last name.
	 *
	 * @param lastName the last name
	 * @return the omh user
	 */
	public OmhUser setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the email
	 * @return the omh user
	 */
	public OmhUser setEmail(String email) {
		this.email = email;
		return this;
	}
	
}
