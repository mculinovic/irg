package labos5;

import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Vertex3D {
	
	//vertex coordinates
	public double x;
	public double y;
	public double z;
	
	public IVector normala;
	
	private List<IVector> normale = new ArrayList<>();
	
	public Vertex3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		DecimalFormat f = new DecimalFormat("0.000");
		return "v " + f.format(x) + " " + f.format(y) + " " + f.format(z);
	}
	
//	static class Normala {
//		public double nx;
//		public double ny;
//		public double nz;
//		
//		public Normala(double nx, double ny, double nz) {
//			super();
//			this.nx = nx;
//			this.ny = ny;
//			this.nz = nz;
//		}		
//	}
	
	public void dodajNormal(double nx, double ny, double nz) {
		normale.add((new Vector(nx, ny, nz)).normalize());
	}
	
	public void izracunajNormaluVrha() {
		
		double a = 0;
		double b = 0;
		double c = 0;
		for (IVector n : normale) {
			a += n.get(0);
			b += n.get(1);
			c += n.get(2);
		}
		
		normala = (new Vector(a, b, c)).normalize();
	}
}
