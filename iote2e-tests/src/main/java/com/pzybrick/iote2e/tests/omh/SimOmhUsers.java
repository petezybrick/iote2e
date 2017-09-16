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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;


/**
 * The Class SimOmhUsers.
 */
public class SimOmhUsers {
	
	/** The sim omh users. */
	private static SimOmhUsers simOmhUsers;
	
	/** The omh users. */
	private List<OmhUser> omhUsers;
	
	
	/**
	 * Instantiates a new sim omh users.
	 *
	 * @param filePath the file path
	 * @throws Exception the exception
	 */
	protected SimOmhUsers( String filePath ) throws Exception {
		this.omhUsers = new ArrayList<OmhUser>();
		List<String> lines = FileUtils.readLines(new File(filePath));
		for (String line : lines) {
			this.omhUsers.add( new OmhUser(line));
		}		
	}
	
	
	/**
	 * Gets the single instance of SimOmhUsers.
	 *
	 * @param filePath the file path
	 * @return single instance of SimOmhUsers
	 * @throws Exception the exception
	 */
	public static SimOmhUsers getInstance( String filePath ) throws Exception {
		if( simOmhUsers != null ) return simOmhUsers;
		simOmhUsers = new SimOmhUsers(filePath);
		return simOmhUsers;
	}


	/**
	 * Gets the omh users.
	 *
	 * @return the omh users
	 */
	public List<OmhUser> getOmhUsers() {
		return omhUsers;
	}

}
