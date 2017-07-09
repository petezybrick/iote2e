package com.pzybrick.iote2e.stream.omh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class SimOmhUsers {
	private static SimOmhUsers simOmhUsers;
	private List<OmhUser> omhUsers;
	
	
	protected SimOmhUsers( String filePath ) throws Exception {
		this.omhUsers = new ArrayList<OmhUser>();
		List<String> lines = FileUtils.readLines(new File(filePath));
		for (String line : lines) {
			this.omhUsers.add( new OmhUser(line));
		}		
	}
	
	
	public static SimOmhUsers getInstance( String filePath ) throws Exception {
		if( simOmhUsers != null ) return simOmhUsers;
		simOmhUsers = new SimOmhUsers(filePath);
		return simOmhUsers;
	}


	public List<OmhUser> getOmhUsers() {
		return omhUsers;
	}

}
