package fraktali;

import java.util.ArrayList;
import java.util.List;

public class ComplexRootedPolynomial {
	
	private static final double CONVERGENCE_THRESHOLD = 0.002;

	/**
	 * polynomial complex roots
	 */
	List<Complex> roots = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param roots
	 *            complex polynomial roots; variable argument
	 */
	public ComplexRootedPolynomial(Complex... roots) {

		for (Complex root : roots) {
			this.roots.add(root);
		}
	}

	/**
	 * Calculates value of polynomial in complex number z
	 * 
	 * @param z
	 *            complex number for applying at polynomial
	 * @return polynomial value at given point z
	 */
	public Complex apply(Complex z) {

		Complex result = Complex.ONE;
		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		return result;
	}

	/**
	 * Converts this representation to {@link ComplexPolynomial}
	 * 
	 * @return {@link ComplexPolynomial} representation of
	 *         {@link ComplexRootedPolynomial}
	 */
	public ComplexPolynomial toComplexPolynom() {

		ComplexPolynomial result = new ComplexPolynomial(Complex.ONE);

		for (Complex root : roots) {
			result = result.multiply(new ComplexPolynomial(Complex.ONE, root
					.negate()));
		}

		return result;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for (Complex root : roots) {
			sb.append("[z-(").append(root).append(")]");
		}
		sb.append(" = 0");

		return sb.toString();
	}

	/**
	 * Finds index of closest root for given complex number z that is within
	 * treshold. If there is no such root, returns -1
	 * 
	 * @param z
	 *            complex number z for calculating
	 * @return index of closest root; -1 if there is no such root
	 */
	public int indexOfClosestRootFor(Complex z) {
		int index = -1;
		double minDistance = -1;
		for (int i = roots.size() - 1; i >= 0; i--) {
			double currentDistance = z.sub(roots.get(i)).module();
			if (currentDistance < CONVERGENCE_THRESHOLD) {
				if (minDistance < 0 || currentDistance < minDistance) {
					minDistance = currentDistance;
					index = i;
				}
			}
		}

		return index;
	}

	/**
	 * Rooted polynomial order
	 * 
	 * @return polnomial order
	 */
	public int order() {
		return roots.size();
	}

}