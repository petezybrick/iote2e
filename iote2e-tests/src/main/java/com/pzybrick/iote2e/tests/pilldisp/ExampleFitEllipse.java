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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.alg.shapes.FitData;
import boofcv.alg.shapes.ShapeFittingOps;
import boofcv.gui.feature.VisualizeShapes;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import georegression.struct.shapes.EllipseRotated_F64;


/**
 * The Class ExampleFitEllipse.
 */
public class ExampleFitEllipse {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main( String args[] ) {
		// load and convert the image into a usable format
		BufferedImage image = UtilImageIO.loadImage(UtilIO.pathExample("/home/pete/development/gitrepo/iote2e/iote2e-tests/images/iote2e-test.png"));
		GrayF32 input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32.class);

		GrayU8 binary = new GrayU8(input.width,input.height);
		DecimalFormat fmt = new DecimalFormat("000");
		for( int x = 0 ; x<100 ; x++ ) {
			for( int y=0 ; y<100 ; y++ ) {
				Float out = input.get(x, y) < 225 ? 0f : 1f;
				System.out.print( out);
				int binout = input.get(x, y) < 225 ? 0 : 1;
				binary.set(x, y, binout );
				//System.out.print( fmt.format(input.get(x, y)) + " " );
			}
			System.out.println("");
		}		
		List<Contour> contours = BinaryImageOps.contour(binary, ConnectRule.EIGHT,null);

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

		// Fit an ellipse to each external contour and draw the results
		Graphics2D g2 = image.createGraphics();
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.RED);

		for( Contour c : contours ) {
			FitData<EllipseRotated_F64> ellipse = ShapeFittingOps.fitEllipse_I32(c.external,0,false,null);
			VisualizeShapes.drawEllipse(ellipse.shape, g2);
		}

//		ShowImages.showWindow(VisualizeBinaryData.renderBinary(filtered, false, null),"Binary",true);
		ShowImages.showWindow(image,"Ellipses",true);
	}
}
