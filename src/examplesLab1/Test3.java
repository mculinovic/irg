package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

public class Test3 {
	
	public static void main(String[] args) {
		
		IMatrix a = Matrix.parseSimple("1 5 3 | 0 0 8 | 1 1 1");
		IMatrix b = Matrix.parseSimple("3 | 4 | 1");
		IMatrix c = Matrix.parseSimple("1 2 1 | 3 4 5 | 7 6 8");
		IMatrix d = Matrix.parseSimple("3 1 2 | 4 5 3 | 1 2 1");
		
		IMatrix t = null;
		IMatrix rez = null;
		try {
			t = a.nInvert().nMultiply(b);
			rez = c.nMultiply(d);
		} catch (IncompatibleOperandException | NoSquareMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(t);
		System.out.println("Matrica:");
		System.out.println(rez.toString());
	}

}
