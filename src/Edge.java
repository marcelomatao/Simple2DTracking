
public class Edge implements Comparable<Edge> {
	
	private double distance;
	private Particle particle1;
	private Particle particle2;
	
	public Edge(Particle particle1, Particle particle2) {
		this.particle1 = particle1;
		this.particle2 = particle2;
		this.distance = particle1.getParticlePosition().distance(particle2.getParticlePosition());
	}

	@Override
	public int compareTo(Edge e2) {
		if(this.distance < e2.distance) {
			return -1;
		} else if(this.distance > e2.distance) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public double getDistance() {
		return distance;
	}
	
	public Particle getParticle1() {
		return particle1;
	}
	
	public Particle getParticle2() {
		return particle2;
	}
	

}
