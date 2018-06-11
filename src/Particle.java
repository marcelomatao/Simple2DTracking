import java.util.ArrayList;
import java.util.List;

import ij.gui.Roi;
import mcib3d.geom.Point3D;

public class Particle {
	
	private Roi roi;
	private ParticleState state;
	private Point3D particlePosition;
	private List<Particle> children;
	private Particle parent;
	
	public Particle(Roi roi) {
		this.roi = roi;
		this.children = new ArrayList<Particle>();
		double centroid[] = roi.getContourCentroid();
		this.particlePosition = new Point3D(centroid[0], centroid[1], 1.0);
		this.state = ParticleState.TRACKING;
	}
	
	public Roi getRoi() {
		return roi;
	}
	
	public Point3D getParticlePosition() {
		return particlePosition;
	}
	
	public void addChildren(Particle particle) {
		this.children.add(particle);
	}
	
	public void setParent(Particle particle) {
		this.parent = particle;
	}
	
	public List<Particle> getChildren() {
		return children;
	}
	
	public Particle getParent() {
		return parent;
	}

}
