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

public class TestPillDispenser {
	public static String PATH_TEST_PNG = "/home/pete/development/gitrepo/iote2e/iote2e-tests/images/iote2e-test.png";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

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
