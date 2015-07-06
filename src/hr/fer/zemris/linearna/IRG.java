package hr.fer.zemris.linearna;

import hr.fer.zemris.exceptions.IncompatibleOperandException;

public class IRG {
	
	/**
	 * Metoda vraca matricu koja odgovara operatoru translacije uz konvenciju
	 * množenja točke s matricom
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public static IMatrix translate3D(float dx, float dy, float dz) {

		double[][] values = new double[4][4];
		values[0][0] = 1;
		values[1][1] = 1;
		values[2][2] = 1;
		values[3][0] = dx;
		values[3][1] = dy;
		values[3][2] = dz;
		values[3][3] = 1.0;
		IMatrix translationMatrix = new Matrix(4, 4, values, true);
		
		return translationMatrix;
	}
	
	/**
	 * Metoda vraca matricu koja odgovara operatoru skaliranja uz konvenciju
	 * množenja točke s matricom
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public static IMatrix scale3D(float sx, float sy, float sz) {
		
		double[][] values = new double[4][4];
		values[0][0] = sx;
		values[1][1] = sy;
		values[2][2] = sz;
		values[3][3] = 1.0;
		IMatrix scaleMatrix= new Matrix(4, 4, values, true);
		return scaleMatrix;
	}
	
	/**
	 * Metoda vraca matricu koja odgovara transformaciji pogleda koja je zadana
	 * očištem, pravcem očište-centar te view-up vektorom, uz konvenciju množenja
	 * točke s matricom
	 * @param eye
	 * @param center
	 * @param viewUp
	 * @return
	 * @throws IncompatibleOperandException 
	 */
	public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) throws IncompatibleOperandException {
		
		IVector F = center.sub(eye);
		
		IVector f = F.nNormalize();
		IVector UP = viewUp.nNormalize();
		
		IVector s = f.nVectorProduct(UP).normalize();
		IVector u = s.nVectorProduct(f).normalize();
		
		double[][] values = new double[4][4];
		
		values[0][0] = s.get(0);
		values[0][1] = s.get(1);
		values[0][2] = s.get(2);
		
		values[1][0] = u.get(0);
		values[1][1] = u.get(1);
		values[1][2] = u.get(2);
		
		values[2][0] = (-1) * f.get(0);
		values[2][1] = (-1) * f.get(1);
		values[2][2] = (-1) * f.get(2);
		
		values[3][3] = 1;
	
		
		IMatrix M = new Matrix(4, 4, values, true);
		
		
		IMatrix lookAtMatrix = translate3D((-1) * (float)eye.get(0), (-1) * (float)eye.get(1), (-1) * (float)eye.get(2)).nMultiply(M.nTranspose(false));
	
		return lookAtMatrix;
	}
	
	public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) {
		
		double[][] values = new double[4][4];
		
		values[0][0] = 2 * n / (r - l);
		values[1][1] = 2 * n / (t - b);
		values[2][0] = (r + l) / (r - l);
		values[2][1] = (t + b) / (t - b);
		values[2][2] = (-1) * (f + n) / (f - n);
		values[2][3] = -1;
		values[3][2] = (-2) * f * n / (f - n);
		
		IMatrix frustumMatrix = new Matrix(4, 4, values, true);
		return frustumMatrix;
	}
	
	
	public static boolean isAntiClockwise(IVector v0, IVector v1, IVector v2) {
		
		double dx1 = v1.get(0) - v0.get(0);
		double dy1 = v1.get(1) - v0.get(1);
		
		double dx2 = v2.get(0) - v0.get(0);
		double dy2 = v2.get(1) - v0.get(1);
		
		if (dx1 * dy2 > dy1 * dx2) {
			return true;
		}		
		return false;
	}
	
	
	

}
