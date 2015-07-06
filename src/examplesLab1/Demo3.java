package examplesLab1;

import java.util.Scanner;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.Vector;
import hr.fer.zemris.linearna.IVector;

public class Demo3 {
	
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
		
		double pov = 0.0;
		double povA = 0.0;
		double povB = 0.0;
		double povC = 0.0;
		
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
		
		System.out.println("Baricentricne koordinate su: [ " + t1 + " " + t2 + " " + t3 + " ]");
		in.close();
		
	}

}
