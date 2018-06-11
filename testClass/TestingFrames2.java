import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;

public class TestingFrames2 {

	public static void main(String[] args) {
		File directory = new File("./slices/");
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for (int i = 1; i <= directory.listFiles().length; i++) {
			try {
				images.add(ImageIO.read(new File(directory.getAbsolutePath()+"/seg-1_"+i+".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedImage image = images.get(7);
//		int height = image.getHeight();
//		int width = image.getWidth();
//		int color = 0;
//		int count = 0;
//		for (int i = 0; i < height; i++) {
//			for (int j = 0; j < width; j++) {
//				//x is column and y is row
//				//first pass by all columns in the current line
//				color = image.getRGB(j, i);
//				if(color == Color.WHITE.getRGB() || color == Color.BLACK.getRGB()) {
//					count++;
//				}
////				else {
////					image.setRGB(j, i, Color.RED.getRGB());
////				}
//			}
//		}
//		
//		System.out.println(count +" "+height*width);
//		
////		// The colors of segmented cells are not white
////		try {
////			ImageIO.write(image, "BMP", new File(directory.getAbsolutePath()+"/test.bmp"));
////		} catch (IOException e) {
////			e.printStackTrace();
//		}
		
		ImagePlus imp = new ImagePlus("Teste", image);
//		WindowManager.setCurrentWindow(new ImageWindow(imp));
		ParticleAnalyzer pa = new ParticleAnalyzer();
		PlugInFilterRunner pfr = new PlugInFilterRunner(pa, "Analyze Particles...", "", imp);
		
//		ImageStack stack = imp.getStack();
//		ImageProcessor ip = stack.getProcessor(1);
		
	}

}
