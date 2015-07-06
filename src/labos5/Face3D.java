package labos5;

import hr.fer.zemris.linearna.IVector;

public class Face3D {
	
	public int[] indexes;
	
	private double A;
	private double B;
	private double C;
	private double D;
	
	boolean visible;
	
	public Face3D(int v1, int v2, int v3) {
		indexes = new int[3];
		indexes[0] = v1;
		indexes[1] = v2;
		indexes[2] = v3;
	}
	
	@Override
	public String toString() {
		return "f " + (1 + indexes[0]) + " " + (1 + indexes[1]) + " " + (1 + indexes[2]);
	}

	public double getA() {
		return A;
	}

	public void setA(double a) {
		A = a;
	}

	public double getB() {
		return B;
	}

	public void setB(double b) {
		B = b;
	}

	public double getC() {
		return C;
	}

	public void setC(double c) {
		C = c;
	}

	public double getD() {
		return D;
	}

	public void setD(double d) {
		D = d;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
