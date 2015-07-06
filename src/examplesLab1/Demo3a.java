package examplesLab1;

import java.util.Scanner;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

public class Demo3a {
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		System.out.println(" -- Unesite o podatke o vrhovima trokuta --");
		System.out.println(" -- Potrebno je unijeti x y z koordinate --");
		
		System.out.print("Koordinate za A:");
		IVector a = Vector.parseSimple(in.nextLine());
		System.out.print("Koordinate za B: ");
		IVector b = Vector.parseSimple(in.nextLine());
		System.out.print("Koordinate za C: ");
		IVector c = Vector.parseSimple(in.nextLine());
		
		System.out.println();
		System.out.println(" -- Unesite koordinate proizvoljne točke --");
		System.out.print("Koordinate za T: ");
		IVector t = Vector.parseSimple(in.nextLine());
		
		Matrix m = new Matrix(3, 3);
		m.set(0, 0, a.get(0));
		m.set(0, 1, a.get(1));
		m.set(0, 2, a.get(2));
		m.set(1, 0, b.get(0));
		m.set(1, 1, b.get(1));
		m.set(1, 2, b.get(2));
		m.set(2, 0, c.get(0));
		m.set(2, 1, c.get(1));
		m.set(2, 2, c.get(2));
		
		IMatrix baricentricne = null;
		try {
			baricentricne = m.nInvert().nMultiply(t.toColumnMatrix(true));
		} catch (IncompatibleOperandException | NoSquareMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println("baricentricne koordinate: ");
		System.out.println(baricentricne);
		
		in.close();
	}

}
