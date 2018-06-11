import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;

public class Frame {
	
	private BufferedImage image;
	private List<Particle> particles;
	
	public Frame(BufferedImage image) {
		this.image = image;
		this.particles = new ArrayList<Particle>();
		findParticles();
	}
	
	public void findParticles() {
		ImagePlus imp = new ImagePlus("Teste", image);
		ImageStack stack = imp.getStack();
		ImageProcessor ip = stack.getProcessor(1);
		ParticleAnalyzer pa = new ParticleAnalyzer();
		pa.setup("", imp);
		pa.run(ip);
		List<Roi> particlesRoi = pa.getParticles();
		for (Roi roi : particlesRoi) {
			particles.add(new Particle(roi));
		}
	}
	
	public List<Particle> getParticles() {
		return particles;
	}
	
	public BufferedImage getImage() {
		return image;
	}

}
