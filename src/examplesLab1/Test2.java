package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

public class Test2 {
	
	public static void main(String[] args) {
		
		IMatrix a = Matrix.parseSimple("3 5 | 2 10");
		IMatrix r = Matrix.parseSimple("2 | 8");
		
		
		IMatrix v = null;
		try {
			v = a.nInvert().nMultiply(r);
		} catch (IncompatibleOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSquareMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Rjesenje sustava je: ");
		System.out.println(v);
		
	}

}
