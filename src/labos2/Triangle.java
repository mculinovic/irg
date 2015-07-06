package labos2;

public class Triangle {

	private int[] x;
	private int[] y;
	private int triangleColor;
	private int pointCounter;
	
	public Triangle(int currentColor) {
		this.triangleColor = currentColor;
		x = new int[3];
		y = new int[3];
	}
	
	public void addPoint(int x, int y) {
		this.x[pointCounter] = x;
		this.y[pointCounter] = y;
		pointCounter++;
	}
	
	public int getColor() {
		return triangleColor;
	}
	
	public int getPointCounter() {
		return pointCounter;
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}
	
	
	
}
