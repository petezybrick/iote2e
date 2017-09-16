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
package com.pzybrick.iote2e.tests.pilldisp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pzybrick.iote2e.stream.pilldisp.PillDispenser;
import com.pzybrick.iote2e.tests.common.TestCommonHandler;


/**
 * The Class TestPillDispenser.
 */
public class TestPillDispenser {
	
	/** The path test png. */
	//public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/images/iote2e-test1.png";
	public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/images/726cf518-62b0-11e7-b07a-0242c0a8153c.png";
	//public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/images/726cf3ea-62b0-11e7-b07a-0242c0a8153c.png";
	//public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/iote2e-shared/images/726cf1f2-62b0-11e7-b07a-0242c0a8153c.png";
	//public static String PATH_TEST_PNG = "/tmp/d48421c2-5e98-11e7-b5bb-0242c0a8153c.png"; 
	//public static String PATH_TEST_PNG = "/tmp/d4841ff8-5e98-11e7-b5bb-0242c0a8153c.png";
	//public static String PATH_TEST_PNG = "/tmp/d48422c3-5e98-11e7-b5bb-0242c0a8153c.png";
	// /tmp/d48421c2-5e98-11e7-b5bb-0242c0a8153c.png

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test count pills.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCountPills() throws Exception {
		// Read file into byte array
		byte[] imageBytes = TestCommonHandler.fileToByteArray(PATH_TEST_PNG);
		// Base64 encode the byte array - this is how the image will come in Iote2eRequest
		String imageB64 = Base64.getEncoder().encodeToString(imageBytes);
		// Base64 decode into byte array
		byte[] imageBytesFromB64 = Base64.getDecoder().decode(imageB64);
		// Create BufferedImage from byte array
		InputStream in = new ByteArrayInputStream(imageBytesFromB64);
		BufferedImage bufferedImage = ImageIO.read(in);
		// Count the number of pills
		int numPillsInImage = PillDispenser.countPills( bufferedImage );
		System.out.println("numPillsInImage="+numPillsInImage);
	}

}
