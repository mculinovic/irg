package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

public class Test4 {
	
	public static void main(String[] args) {
		
		IVector n = Vector.parseSimple("3 3");
		IVector m = Vector.parseSimple("2 3");
		
		
		try {
			IVector r = n.nScalarMultiply(1/(n.norm() * n.norm()));
			r = r.nScalarMultiply(2);
			r = r.nScalarMultiply(n.scalarProduct(m));
			r = r.sub(m);
			System.out.println(r);
		} catch (IncompatibleOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
