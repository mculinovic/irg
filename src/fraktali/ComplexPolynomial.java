package fraktali;

import java.util.ArrayList;
import java.util.List;

public class ComplexPolynomial {

	/**
	 * Complex polynomial factors
	 */
	List<Complex> factors = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param factors
	 *            factors of complex polynomial, starting from the highest;
	 *            variable argument
	 */
	public ComplexPolynomial(Complex... factors) {
		for (Complex factor : factors) {
			this.factors.add(factor);
		}
	}

	/**
	 * Returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * 
	 * @return polynom order
	 */
	public short order() {
		return (short) (factors.size() - 1);
	}

	/**
	 * Computes a new polynomial this*p
	 * 
	 * @param p
	 *            complex polynomial
	 * @return new polynomial - result of multiplying
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {

		Complex[] factors = new Complex[this.order() + p.order() + 1];

		for (int i = 0; i < factors.length; i++) {
			factors[i] = Complex.ZERO;
		}

		for (int i = 0; i <= this.order(); i++) {
			for (int j = 0; j <= p.order(); j++) {
				factors[i + j] = factors[i + j].add(this.factors.get(i)
						.multiply(p.factors.get(j)));
			}
		}

		return new ComplexPolynomial(factors);
	}

	/**
	 * Computes first derivative of this polynomial; for example, for
	 * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * 
	 * @return first derivation of this complex polynomial
	 */
	public ComplexPolynomial derive() {

		Complex[] derivedFactors = new Complex[this.order()];
		for (int i = 0; i < this.order(); i++) {
			derivedFactors[i] = factors.get(i).multiply(
					new Complex(this.order() - i, 0));
		}

		return new ComplexPolynomial(derivedFactors);
	}

	/**
	 * Computes polynomial value at given point z
	 * 
	 * @param z
	 *            complex point
	 * @return value of polynomial at point z
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		Complex exp = Complex.ONE;
		for (int i = factors.size() - 1; i >= 0; i--) {
			result = result.add(exp.multiply(factors.get(i)));
			exp = exp.multiply(z);
		}

		return result;
	}

	@Override
	public String toString() {

		int exp = this.order();
		List<String> polynomParts = new ArrayList<>();
		for (Complex factor : factors) {
			StringBuilder sb = new StringBuilder();
			if (factor.equals(Complex.ZERO)) {
				exp--;
				continue;
			}
			sb.append("(").append(factor).append(")");
			if (exp > 1) {
				sb.append("z^").append(exp);
			} else if (exp == 1) {
				sb.append("z");
			}
			exp--;
			polynomParts.add(sb.toString());
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < polynomParts.size(); i++) {
			sb.append(polynomParts.get(i - 1)).append("+");
		}
		sb.append(polynomParts.get(polynomParts.size() - 1));
		sb.append(" = 0 ");
		return sb.toString();
	}
}