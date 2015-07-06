package labos6;

public class Eye {
	
	private double x;
	private double z;
	
	private double angle;
	private double r;
	private double increment;
	
	private double startX;
	private double startZ;
	private double startAngle;
	
	public Eye(double x, double z, double increment) {
		this.x = x;
		this.z = z;
		this.startX = x;
		this.startZ = z;
		this.increment = increment;
		this.angle = Math.toDegrees(Math.atan(z / x));
		startAngle = angle;
		this.r = 1 / Math.sin(Math.toRadians(angle));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public void increment() {
		angle += increment;
		x = r * Math.cos(Math.toRadians(angle));
		z = r * Math.sin(Math.toRadians(angle));
	}
	
	public void decrement() {
		angle -= increment;
		x = r * Math.cos(Math.toRadians(angle));
		z = r * Math.sin(Math.toRadians(angle));
	}
	
	public void reset() {
		x = startX;
		z = startZ;
		angle = startAngle;
	}

}
