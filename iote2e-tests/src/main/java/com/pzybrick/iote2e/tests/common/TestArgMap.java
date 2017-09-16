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
package com.pzybrick.iote2e.tests.common;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.pzybrick.iote2e.common.utils.ArgMap;


/**
 * The Class TestArgMap.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArgMap {
	
	/** The arg map. */
	private ArgMap argMap;
	
	/**
	 * Test string exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStringExists( ) throws Exception {
		Assert.assertEquals("valueStringExists", argMap.get("nameStringExists"));
	}
	
	/**
	 * Test string not exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStringNotExists( ) throws Exception {
		Assert.assertNull(argMap.get("nameStringNull"));
	}
	
	/**
	 * Test string default.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStringDefault( ) throws Exception {
		Assert.assertEquals("valueStringDefault", argMap.get("nameStringDefault", "valueStringDefault"));
	}
	
	/**
	 * Test integer exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIntegerExists( ) throws Exception {
		Assert.assertEquals( new Integer(111), argMap.getInteger("nameIntegerExists"));
	}
	
	/**
	 * Test integer not exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIntegerNotExists( ) throws Exception {
		Assert.assertNull(argMap.getInteger("nameIntegerNull"));
	}
	
	/**
	 * Test integer default.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIntegerDefault( ) throws Exception {
		Assert.assertEquals( new Integer(222), argMap.getInteger("nameIntegerDefault", new Integer(222)));
	}
	
	/**
	 * Test long exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testLongExists( ) throws Exception {
		Assert.assertEquals( new Long(333), argMap.getLong("nameLongExists"));
	}
	
	/**
	 * Test long not exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testLongNotExists( ) throws Exception {
		Assert.assertNull(argMap.getLong("nameLongNull"));
	}
	
	/**
	 * Test long default.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testLongDefault( ) throws Exception {
		Assert.assertEquals( new Long(444), argMap.getLong("nameLongDefault", new Long(444)));
	}
	
	/**
	 * Test double exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDoubleExists( ) throws Exception {
		Assert.assertEquals( new Double(555.666), argMap.getDouble("nameDoubleExists"));
	}
	
	/**
	 * Test double not exists.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDoubleNotExists( ) throws Exception {
		Assert.assertNull(argMap.getDouble("nameDoubleNull"));
	}
	
	/**
	 * Test double default.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDoubleDefault( ) throws Exception {
		Assert.assertEquals( new Double(777.888), argMap.getDouble("nameDoubleDefault", new Double(777.888)));
	}
	
	/**
	 * Test dump.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDump( ) throws Exception {
		final String dumpResults = "nameDoubleExists = 555.666\nnameIntegerExists = 111\nnameLongExists = 333\nnameStringExists = valueStringExists";
		Assert.assertEquals( dumpResults, argMap.dump() );
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
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

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}


}
