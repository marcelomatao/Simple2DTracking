import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;

public class TestingFrames3 {

	public static void main(String[] args) {
		File directory = new File("./slices/");
		List<BufferedImage> movie = new ArrayList<BufferedImage>();
		for (int i = 1; i <= directory.listFiles().length; i++) {
			try {
				movie.add(ImageIO.read(new File(directory.getAbsolutePath()+"/seg-1_"+i+".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Video2D video = new Video2D(movie);
		
		
		
//		BufferedImage frame = null;
//		ImagePlus imp = null;
//		ImageStack stack = null;
//		ImageProcessor ip = null;
//		ParticleAnalyzer pa = null;
//		for (int i = 0; i < movie.size(); i++) {
//			frame = movie.get(i);
//			imp = new ImagePlus("Teste", frame);
//			stack = imp.getStack();
//			ip = stack.getProcessor(1);
//			pa = new ParticleAnalyzer();
//			pa.setup("", imp);
//			pa.run(ip);
//			System.out.println("Frame "+i+" "+pa.getParticles().size()+" particles");
//		}
		
//		BufferedImage image = movie.get(7);
//		
//		ImagePlus imp1 = new ImagePlus("Teste", image);
//		ImageStack stack1 = imp.getStack();
//		ImageProcessor ip1 = stack.getProcessor(1);
//		ParticleAnalyzer pa1 = new ParticleAnalyzer();
//		pa1.setup("", imp);
//		pa1.run(ip);
//		List<Roi> particles = pa1.getParticles();
//		Roi r = null;
//		
//		whiteImage(image);
//		double center[] = null;
//		for (int i = 0; i < particles.size(); i++) {
//			r = particles.get(i);
//			center = r.getContourCentroid();
//			image.setRGB((int)center[0], (int)center[1], Color.BLACK.getRGB());
//		}
//		
//		try {
//			ImageIO.write(image, "BMP", new File("./Tests/test.bmp"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	
//		new ImagePlus("Teste", image).show();
		
	}
	
	public static void whiteImage(BufferedImage image) {
		int height = image.getHeight();
		int width = image.getWidth();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				image.setRGB(j, i, Color.WHITE.getRGB());
			}
		}
	}

}
