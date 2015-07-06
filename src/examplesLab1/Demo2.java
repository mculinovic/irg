package examplesLab1;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.Matrix;

import java.util.Scanner;

public class Demo2 {
	
	public static void main(String[] args) {
		
		System.out.println("Unesite podatke o sustavu jednadžbi:");
		System.out.println("x   y   z   rez");
		
		Scanner in = new Scanner(System.in);
		
		double[][] values = new double[3][3];
		double[][] res = new double[3][1];
		
		int i = 0;
		while(i < 3) {
			values[i][0] = in.nextDouble();
			values[i][1] = in.nextDouble();
			values[i][2] = in.nextDouble();
			res[i][0] = in.nextDouble();
			i++;
		}
		
		in.close();
		
		IMatrix system = new Matrix(3,3,values,true);
		IMatrix result = new Matrix(3,1,res,true);
		
		try {
			System.out.println();
			System.out.println("[x y z]");
			System.out.println(system.nInvert().nMultiply(result));
		} catch (IncompatibleOperandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSquareMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
