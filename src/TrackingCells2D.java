import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import mcib3d.Jama.Matrix;
import mcib3d.geom.Point3D;

public class TrackingCells2D {
	
	private List<Frame> video;
	private Map<Integer, List<Particle>> particlesMapTracking;
	private Map<Integer, List<Particle>> particlesOutMap;
	private int currentFrame;
	private List<BufferedImage> pathImages;
	
	private static int id = 1;
	
	public TrackingCells2D(List<BufferedImage> images) {
		this.video = new ArrayList<Frame>();
		this.particlesMapTracking = new HashMap<Integer, List<Particle>>();
		this.particlesOutMap = new HashMap<Integer, List<Particle>>();
		this.pathImages = new ArrayList<BufferedImage>();
		this.currentFrame = 0;
		loadVideo(images);
		processCurrentFrame();
	}

	private void processCurrentFrame() {
		Frame frame = video.get(currentFrame);
		List<Particle> particles = frame.getParticles();
		List<Particle> particleMotion = null;
		if(currentFrame == 0) {
			for (Particle particle : particles) {
				particleMotion = new ArrayList<Particle>();
				particleMotion.add(particle);
				particlesMapTracking.put(id, particleMotion);
				id++;
			}
			currentFrame++;
		}
	}

	private void loadVideo(List<BufferedImage> images) {
		for (BufferedImage image : images) {
			video.add(new Frame(image));
		}
	}
	
	public void trackCells() {
		/**
		 * This first version only works for videos with perfect conditions, 
		 * where there are no changing in the number of cells, no overlap, no cell division,
		 * no cell going in or out to the scene. The size changing and acceleration of cells can also make errors.
		 */
		int videoSize = video.size();
		Frame frame = null;
		Frame lastFrame = video.get((currentFrame-1));;
		List<Particle> frameParticles = null;
		int numParticlesLastFrame = lastFrame.getParticles().size();
		int numParticlesCurrentFrame = 0;
		while(currentFrame < videoSize) {
			frame = video.get(currentFrame);
			frameParticles = frame.getParticles();
			numParticlesCurrentFrame = frameParticles.size();
			matchParticles(frameParticles, numParticlesCurrentFrame - numParticlesLastFrame);
			currentFrame++;
		}
	}

	private void matchParticles(List<Particle> frameParticles, int numberMoreParticles) {
		Set<Integer> ids = particlesMapTracking.keySet();
		Particle particle = null;
		List<Particle> particleMotion = null;
		Particle closest = null;
		List<Particle> frameParticlesClone = new ArrayList<Particle>(frameParticles);
		//if same number of cells from frame t to t+1
		for (Integer id : ids) {
			particleMotion = particlesMapTracking.get(id);
			particle = particleMotion.get(currentFrame-1);
			closest = findClosestFromList(particle, frameParticlesClone);
			particleMotion.add(closest);
			particle.addChildren(closest);
			closest.setParent(particle);
		}
		if(frameParticlesClone.size() > 0) { //if the number of cells is bigger in the frame t+1 
			Integer closestId = null;
			Particle closestParent = null;
			Particle closestSibling = null;
			List<Particle> removedList = null;
			List<Particle> newList = null;
			for (int i = 0; i < frameParticlesClone.size(); i++) {
				particle = frameParticlesClone.get(i);
				closestId = findClosestFromMapTracking(particle);
				particleMotion = particlesMapTracking.get(closestId);
				closestSibling = particleMotion.remove(currentFrame);
				closestParent = closest.getParent();
				particle.setParent(closestParent);
				closestParent.addChildren(particle);
				removedList = particlesMapTracking.remove(closestId);
				particlesOutMap.put(closestId, removedList);
				newList = new ArrayList<Particle>(removedList);
				newList.add(closestSibling);
				particlesMapTracking.put(id, newList);
				id++;
				newList = new ArrayList<Particle>(removedList);
				newList.add(particle);
				particlesMapTracking.put(id, newList);
				id++;
			}
		}
	}

	private Particle findClosestFromList(Particle particle, List<Particle> particles) {
		Particle closest = particles.get(0);
		Particle current = null;
		double currentDistance = Double.MAX_VALUE;
		double closestDistance = particle.getParticlePosition().distance(closest.getParticlePosition());
		for (int i = 1; i < particles.size(); i++) {
			current = particles.get(i);
			currentDistance = particle.getParticlePosition().distance(current.getParticlePosition());
			if (currentDistance < closestDistance) {
				closest = current;
				closestDistance = currentDistance;
			}
		}
		
		particles.remove(closest);
		return closest;
	}
	
	private Integer findClosestFromMapTracking(Particle particle) {
		Set<Integer> ids = particlesMapTracking.keySet();
		List<Particle> particleMotion = null;
		Particle closest = null;
		Particle current = null;
		double currentDistance = Double.MAX_VALUE;
		double closestDistance = currentDistance;
		Integer closestId = null;
		for (Integer id : ids) {
			particleMotion = particlesMapTracking.get(id);
			current = particleMotion.get(currentFrame-1);
			currentDistance = particle.getParticlePosition().distance(current.getParticlePosition());
			if (currentDistance < closestDistance) {
				closest = current;
				closestDistance = currentDistance;
				closestId = id;
			}
		}
		
		return closestId;
	}

	public void drawPaths() {
		Set<Integer> ids = particlesMapTracking.keySet();
		Particle particle = null;
		Particle particleLast = null;
		initGraphics();
		List<Particle> particleMotion = null;
		BufferedImage image = null;
		for (Integer id : ids) {
			particleMotion = particlesMapTracking.get(id);
			particleLast = particleMotion.get(0);
			for (int i = 1; i < particleMotion.size(); i++) {
				particle = particleMotion.get(i);
				for (int j = 1; j < pathImages.size(); j++) {
					image = pathImages.get(j);
					drawLine(particleLast.getParticlePosition(), particle.getParticlePosition(), image);
					if (j == i) {
						particle.getRoi().draw(image.getGraphics());
					}
				}
				particleLast = particle;
			}
		}
		saveImages();
	}
	
	private void saveImages() {
		int counterImage = 0;
		for (BufferedImage image : pathImages) {
			try {
				ImageIO.write(image, "BMP", new File("./Tests/testDraw"+counterImage+".bmp"));
				counterImage++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initGraphics() {
		BufferedImage image = null;
		for (Frame frame : video) {
//			image = new BufferedImage(frame.getImage().getWidth(), frame.getImage().getHeight(), frame.getImage().getType());
			image = frame.getImage();
			Graphics2D g2d = image.createGraphics();
			g2d.setBackground(Color.WHITE);
//			g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
			pathImages.add(image);
		}
	}

	private void drawLine(Point3D point1, Point3D point2, BufferedImage bufferedImage) {
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
	    g2d.setColor(Color.BLACK);
	    BasicStroke bs = new BasicStroke(2);
	    g2d.setStroke(bs);
	    g2d.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
	}

	public void drawContours() {
		Set<Integer> ids = particlesMapTracking.keySet();
		Particle particle = null;
		initGraphics();
		List<Particle> particleMotion = null;
		BufferedImage image = null;
		for (Integer id : ids) {
			particleMotion = particlesMapTracking.get(id);
			for (int i = 1; i < particleMotion.size(); i++) {
				particle = particleMotion.get(i);
				for (int j = 1; j < pathImages.size(); j++) {
					image = pathImages.get(j);
					if (j == i) {
						particle.getRoi().draw(image.getGraphics());
					}
				}
			}
		}
		saveImages();
	}
	
}
