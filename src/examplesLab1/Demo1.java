package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

public class Demo1 {
	
	public static void main(String[] args) {
		
		IVector p1 = Vector.parseSimple("2 3 -4");
		IVector p2 = Vector.parseSimple("-1 4 -3");
		
		IVector v1 = null;
		double s = 0.0;
		IVector v2 = null;
		try {
			v1 = p1.nAdd(p2);
			s = v1.scalarProduct(Vector.parseSimple("-1 4 -3"));
			v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));
		} catch (IncompatibleOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IVector v3 = v2.nNormalize();
		IVector v4 = v2.nScalarMultiply(-1);
		
		System.out.println("Vektor v1: " + v1);
		System.out.println("Skalarni produkt: " + s);
		System.out.println("Vektor v2: " + v2);
		System.out.println("Vektor v3: " +v3);
		System.out.println("Vektor v4: " + v4);
		
		System.out.println();
		
		IMatrix m1 = null;
		IMatrix m2 = null;
		IMatrix m3 = null;
		
		try {
		m1 = Matrix.parseSimple("1 2 3|2 1 3|4 5 1");
		m1 = m1.add(Matrix.parseSimple("-1 2 -3|5 -2 7|-4 -1 3"));
		m2 = Matrix.parseSimple("1 2 3|2 1 3|4 5 1");
		m2 = m2.nMultiply(Matrix.parseSimple("-1 2 -3|5 -2 7|-4 -1 3").nTranspose(true));
		m3 = Matrix.parseSimple("-24 18 5|20 -15 -4|-5 4 1");
		m3 = m3.nInvert().nMultiply(Matrix.parseSimple("1 2 3|0 1 4|5 6 0").nInvert());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Matrica m1:");
		System.out.println(m1);
		System.out.println("Matrica m2:");
		System.out.println(m2);
		System.out.println("Matrica m3:");
		System.out.println(m3);
		
		
	}

}
