package com.pzybrick.iote2e.tests.boofcv;

import java.awt.image.BufferedImage;
import java.util.List;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;

public class PillDispenser {
	private static float PIXEL_THRESHOLD = 225;

	public static void main( String args[] ) {
		try {
			long before = System.currentTimeMillis();
			System.out.println("countPills=" + PillDispenser.countPills( "/home/pete/development/gitrepo/iote2e/iote2e-tests/images/iote2e-test.png" ));
			System.out.println("elapsed: " + (System.currentTimeMillis()-before));
		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static int countPills( String path ) throws Exception {
		// load and convert the image into a usable format
		BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample(path));
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

//
//		// the mean pixel value is often a reasonable threshold when creating a binary image
//		double mean = ImageStatistics.mean(input);
//
//		// create a binary image by thresholding
//		ThresholdImageOps.threshold(input, binary, (float) mean, true);
//
//		// reduce noise with some filtering
//		GrayU8 filtered = BinaryImageOps.erode8(binary, 1, null);
//		filtered = BinaryImageOps.dilate8(filtered, 1, null);
//		
//		for( int x = 0 ; x<100 ; x++ ) {
//			for( int y=0 ; y<100 ; y++ ) {
//				System.out.print(filtered.get(x, y) );
//			}
//			System.out.println("");
//		}
//		
//
//		// Find the contour around the shapes
//		List<Contour> contours2 = BinaryImageOps.contour(filtered, ConnectRule.EIGHT,null);

//		// Fit an ellipse to each external contour and draw the results
//		Graphics2D g2 = image.createGraphics();
//		g2.setStroke(new BasicStroke(3));
//		g2.setColor(Color.RED);
//
//		for( Contour c : contours ) {
//			FitData<EllipseRotated_F64> ellipse = ShapeFittingOps.fitEllipse_I32(c.external,0,false,null);
//			VisualizeShapes.drawEllipse(ellipse.shape, g2);
//		}
//
////		ShowImages.showWindow(VisualizeBinaryData.renderBinary(filtered, false, null),"Binary",true);
//		ShowImages.showWindow(image,"Ellipses",true);
	}
}
