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

public class TestingNumberOfCellsPerFrame {

	public static void main(String[] args) {
		File directory = new File("./examples/video1/");
		List<BufferedImage> movie = new ArrayList<BufferedImage>();
		for (int i = 1; i <= directory.listFiles().length; i++) {
			try {
				movie.add(ImageIO.read(new File(directory.getAbsolutePath()+"/seg-1_"+i+".bmp")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedImage frame = null;
		ImagePlus imp = null;
		ImageStack stack = null;
		ImageProcessor ip = null;
		ParticleAnalyzer pa = null;
		for (int i = 0; i < movie.size(); i++) {
			frame = movie.get(i);
			imp = new ImagePlus("Teste", frame);
			stack = imp.getStack();
			ip = stack.getProcessor(1);
			pa = new ParticleAnalyzer();
			pa.setup("", imp);
			pa.run(ip);
			System.out.println("Frame "+i+" "+pa.getParticles().size()+" particles");
		}
		
	}

}
