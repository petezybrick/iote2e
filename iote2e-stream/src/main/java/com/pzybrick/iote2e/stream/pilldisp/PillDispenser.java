package com.pzybrick.iote2e.stream.pilldisp;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

public class PillDispenser {
	private static float PIXEL_THRESHOLD = 225;
	
	public static int countPills( BufferedImage image ) throws Exception {
		GrayF32 input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);
		GrayU8 binary = new GrayU8(input.width,input.height);
		for( int x = 0 ; x<input.width ; x++ ) {
			for( int y=0 ; y<input.height ; y++ ) {
				int binout = input.get(x, y) < PIXEL_THRESHOLD ? 0 : 1;
				binary.set(x, y, binout );
			}
		}
		List<Contour> contours = BinaryImageOps.contour(binary, ConnectRule.EIGHT,null);
		return contours.size();
	}
}
