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
package com.pzybrick.iote2e.tests.validic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;


/**
 * The Class SimValidicUsers.
 */
public class SimValidicUsers {
	
	/** The sim validic users. */
	private static SimValidicUsers simValidicUsers;
	
	/** The validic users. */
	private List<ValidicUser> validicUsers;
	
	
	/**
	 * Instantiates a new sim validic users.
	 *
	 * @param filePath the file path
	 * @throws Exception the exception
	 */
	protected SimValidicUsers( String filePath ) throws Exception {
		this.validicUsers = new ArrayList<ValidicUser>();
		List<String> lines = FileUtils.readLines(new File(filePath));
		for (String line : lines) {
			this.validicUsers.add( new ValidicUser(line));
		}		
	}
	
	
	/**
	 * Gets the single instance of SimValidicUsers.
	 *
	 * @param filePath the file path
	 * @return single instance of SimValidicUsers
	 * @throws Exception the exception
	 */
	public static SimValidicUsers getInstance( String filePath ) throws Exception {
		if( simValidicUsers != null ) return simValidicUsers;
		simValidicUsers = new SimValidicUsers(filePath);
		return simValidicUsers;
	}


	/**
	 * Gets the validic users.
	 *
	 * @return the validic users
	 */
	public List<ValidicUser> getValidicUsers() {
		return validicUsers;
	}

}
