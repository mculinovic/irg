package fraktali;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex {

	/**
	 * Complex number 0
	 */
	public static final Complex ZERO = new Complex(0, 0);

	/**
	 * Complex number 1
	 */
	public static final Complex ONE = new Complex(1, 0);

	/**
	 * Complex number -1
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/**
	 * Complex number i
	 */
	public static final Complex IM = new Complex(0, 1);

	/**
	 * Complex number -i
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Real part of complex number
	 */
	public double re;

	/**
	 * Imaginary part of complex number
	 */
	public double im;

	/**
	 * Default constructor
	 */
	public Complex() {
	};

	/**
	 * Constructor
	 * 
	 * @param re
	 *            real part
	 * @param im
	 *            imaginary part
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates complex number module
	 * 
	 * @return module of complex number
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Returns result of multiplying with given complex number c
	 * 
	 * @param c
	 *            complex number for multiplying
	 * @return new Complex number, result of operation
	 */
	public Complex multiply(Complex c) {
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Returns result of dividing with given complex number c
	 * 
	 * @param c
	 *            complex number for dividing
	 * @return new Complex number, result of operation
	 */
	public Complex divide(Complex c) {
		Complex denominator = new Complex(1 / (c.re * c.re + c.im * c.im), 0);
		Complex nominator = this.multiply(new Complex(c.re, -c.im));
		return nominator.multiply(denominator);
	}

	/**
	 * Returns result of adding with given complex number c
	 * 
	 * @param c
	 *            complex number for addition
	 * @return new Complex number, result of operation
	 */
	public Complex add(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Returns result of subtracting with given complex number c
	 * 
	 * @param c
	 *            complex number for subtraction
	 * @return new Complex number, result of operation
	 */
	public Complex sub(Complex c) {
		return new Complex(re - c.re, im - c.im);
	}

	/**
	 * Returns negated value of complex number (this * (-1))
	 * 
	 * @return negated complex number
	 */
	public Complex negate() {
		return this.multiply(ONE_NEG);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (re != 0) {
			sb.append(re);
		}

		if (im != 0) {
			if (im > 0) {
				sb.append("+");
			}
			sb.append("i").append("(").append(im).append(")");
		}

		if (re == 0 && im == 0) {
			sb.append(0);
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Complex)) {
			return false;
		}

		Complex c = (Complex) obj;
		if (this.re == c.re && this.im == c.im) {
			return true;
		}

		return false;
	}

	/**
	 * Method parses given complex number from {@link String} to {@link Complex}
	 * .
	 * 
	 * @param complex
	 * @return
	 */
	public static Complex parseComplex(String complex) {

		Complex result = null;
		complex = complex.replace(" ", "");

		Pattern complexNumber = Pattern.compile("([+-]?\\d+)?([+-]?i\\d*)?");
		Matcher matcher = complexNumber.matcher(complex);

		if (matcher.find()) {
			double cRe = 0.0;
			double cIm = 0.0;
			if (matcher.group(1) != null && matcher.group(1).length() > 0) {
				cRe = Double.parseDouble(matcher.group(1));
			}
			if (matcher.group(2) != null && matcher.group(2).length() > 0) {
				String cImString = matcher.group(2);
				cImString = cImString.replace("i", "");
				if (cImString.length() == 0 || cImString.equals("+")
						|| cImString.equals("-")) {
					cImString += "1";
				}
				cIm = Double.parseDouble(cImString);

			}
			result = new Complex(cRe, cIm);
		}

		return result;
	}

	/**
	 * @return real part of complex number
	 */
	public double getRe() {
		return re;
	}

	/**
	 * @return imaginary part of complex number
	 */
	public double getIm() {
		return im;
	}

}