package com.pzybrick.iote2e.stream.validic;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class ValidicBody {
	
	public abstract String getSchemaName();
	public abstract void setSchemaName( String schemaName );
	
	public static Double roundDouble( Double dbl) {
		if( dbl == null ) return null;
		BigDecimal bd = new BigDecimal(dbl);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	

}
