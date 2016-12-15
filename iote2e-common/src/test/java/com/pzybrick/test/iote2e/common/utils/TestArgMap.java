package com.pzybrick.test.iote2e.common.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.common.utils.ArgMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArgMap {
	private ArgMap argMap;
	
	@Test
	public void testStringExists( ) throws Exception {
		Assert.assertEquals("valueStringExists", argMap.get("nameStringExists"));
	}
	
	@Test
	public void testStringNotExists( ) throws Exception {
		Assert.assertNull(argMap.get("nameStringNull"));
	}
	
	@Test
	public void testStringDefault( ) throws Exception {
		Assert.assertEquals("valueStringDefault", argMap.get("nameStringDefault", "valueStringDefault"));
	}
	
	@Test
	public void testIntegerExists( ) throws Exception {
		Assert.assertEquals( new Integer(111), argMap.getInteger("nameIntegerExists"));
	}
	
	@Test
	public void testIntegerNotExists( ) throws Exception {
		Assert.assertNull(argMap.getInteger("nameIntegerNull"));
	}
	
	@Test
	public void testIntegerDefault( ) throws Exception {
		Assert.assertEquals( new Integer(222), argMap.getInteger("nameIntegerDefault", new Integer(222)));
	}
	
	@Test
	public void testLongExists( ) throws Exception {
		Assert.assertEquals( new Long(333), argMap.getLong("nameLongExists"));
	}
	
	@Test
	public void testLongNotExists( ) throws Exception {
		Assert.assertNull(argMap.getLong("nameLongNull"));
	}
	
	@Test
	public void testLongDefault( ) throws Exception {
		Assert.assertEquals( new Long(444), argMap.getLong("nameLongDefault", new Long(444)));
	}
	
	@Test
	public void testDoubleExists( ) throws Exception {
		Assert.assertEquals( new Double(555.666), argMap.getDouble("nameDoubleExists"));
	}
	
	@Test
	public void testDoubleNotExists( ) throws Exception {
		Assert.assertNull(argMap.getDouble("nameDoubleNull"));
	}
	
	@Test
	public void testDoubleDefault( ) throws Exception {
		Assert.assertEquals( new Double(777.888), argMap.getDouble("nameDoubleDefault", new Double(777.888)));
	}
	
	@Test
	public void testDump( ) throws Exception {
		final String dumpResults = "nameDoubleExists = 555.666\nnameIntegerExists = 111\nnameLongExists = 333\nnameStringExists = valueStringExists";
		Assert.assertEquals( dumpResults, argMap.dump() );
	}

	@Before
	public void setUp() throws Exception {
		final String[] args = {
				"nameStringExists=valueStringExists", 
				"nameIntegerExists=111", 
				"nameLongExists=333", 
				"nameDoubleExists=555.666", 
				};
		this.argMap = new ArgMap(args);
	}

	@After
	public void tearDown() throws Exception {
	}


}
