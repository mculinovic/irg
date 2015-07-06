package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

public class Test1 {
	
	public static void main(String[] args) {
		IVector a = Vector.parseSimple("1 0 0");
		IVector b = Vector.parseSimple("5 0 0");
		IVector c = Vector.parseSimple("3 8 0");
		
		IVector t = Vector.parseSimple("3 4 0");
		
		double pov = 0;
		double povA = 0;
		double povB = 0;
		double povC = 0;
		try {
			pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
			povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
			povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
			povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
		} catch (IncompatibleOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		double t1 = povA / pov;
		double t2 = povB / pov;
		double t3 = povC / pov;
		
		System.out.println("Baricentricne koordinate su: (" + t1 + ", " + t2 + ", " + t3 + ")");
		
	}

}
