package hr.fer.zemris.linearna;

import java.text.DecimalFormat;

import hr.fer.zemris.exceptions.IncompatibleOperandException;

public abstract class AbstractVector implements IVector {
	
	public AbstractVector() {}

	@Override
	public abstract double get(int i);

	@Override
	public abstract IVector set(int i, double value);

	@Override
	public abstract int getDimension();

	@Override
	public abstract IVector copy();

	@Override
	public IVector copyPart(int n) {
		int dimension = this.getDimension();
		double[] values = new double[n];
		for (int i = 0; i < n; ++i) {
			if (i > dimension - 1) {
				values[i] = 0.0;
			} else {
				values[i] = this.get(i);
			}
		}
		return new Vector(values);
	}

	@Override
	public abstract IVector newInstance(int n);

	@Override
	public IVector add(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension())
			throw new IncompatibleOperandException();
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) + other.get(i));
		}
		return this;
	}

	@Override
	public IVector nAdd(IVector other) throws IncompatibleOperandException {
		return this.copy().add(other);
	}

	@Override
	public IVector sub(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension())
			throw new IncompatibleOperandException();
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) - other.get(i));
		}
		return this;
	}

	@Override
	public IVector nSub(IVector other) throws IncompatibleOperandException {
		return this.copy().sub(other);
	}

	@Override
	public IVector scalarMultiply(double value) {
		for (int i = this.getDimension() - 1; i >= 0; i--)
			this.set(i, value * this.get(i));
		return this;
	}

	@Override
	public IVector nScalarMultiply(double value) {
		return this.copy().scalarMultiply(value);
	}

	@Override
	public double norm() {
		double norm = 0.0;
		for (int i = this.getDimension() - 1; i >= 0; i--)
			norm += this.get(i) * this.get(i);
		return Math.sqrt(norm);
	}

	@Override
	public IVector normalize() {
		int dimension = this.getDimension();
		double norm = this.norm();
		for (int i = 0; i < dimension; i++) {
			this.set(i, this.get(i) / norm);
		}
		return this;
	}

	@Override
	public IVector nNormalize() {
		return this.copy().normalize();
	}

	@Override
	public double cosine(IVector other) throws IncompatibleOperandException {
	
		return this.scalarProduct(other) / (this.norm() * other.norm());
	}

	@Override
	public double scalarProduct(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension())
			throw new IncompatibleOperandException();
		double scalarProduct = 0.0;
		for (int i = this.getDimension() - 1; i >= 0; i--)
			scalarProduct += this.get(i) * other.get(i);
		return scalarProduct;
	}

	@Override
	public IVector nVectorProduct(IVector other) throws IncompatibleOperandException {
		if (this.getDimension() != other.getDimension() || other.getDimension() != 3)
			throw new IncompatibleOperandException();
		
		double[] values = new double[3];
		values[0] = this.get(1) * other.get(2) - this.get(2) * other.get(1);
		values[1] = this.get(0) * other.get(2) - this.get(2) * other.get(0);
		values[1] *= (-1);
		values[2] = this.get(0) * other.get(1) - this.get(1) * other.get(0);
		
		return new Vector(values);
	}

	@Override
	public IVector nFromHomogeneus() {
		double[] values = new double[this.getDimension() - 1];
		double homogeneusCoordinate = this.get(this.getDimension() - 1);
		for (int i = this.getDimension() - 2; i >= 0; i--) 
			values[i] = this.get(i) / homogeneusCoordinate;
		return new Vector(values);
	}

	@Override
	public double[] toArray() {
		int dimension = this.getDimension();
		double[] values = new double[dimension];
		for (int i = 0; i < dimension; i++)
			values[i] = this.get(i);
		return values;
	};
	
	@Override
	public IMatrix toRowMatrix(boolean value) {
		if (value) {
			return new MatrixVectorView(this, true);
		} else {
			Matrix matrix = new Matrix(1, this.getDimension());
			int n = this.getDimension();
			for (int i = 0; i < n; i++)
				matrix.set(0, i, this.get(i));
			return matrix;
		}
	}
	
	@Override
	public IMatrix toColumnMatrix(boolean value) {
		if (value) {
			return new MatrixVectorView(this, false);
		} else {
			Matrix matrix = new Matrix(this.getDimension(), 1);
			int n = this.getDimension();
			for (int i = 0; i < n; i++)
				matrix.set(i, 0, this.get(i));
			return matrix;
		}
	}
	
	@Override
	public String toString() {
		return this.toString(3);
	}
	
	public String toString(int precision) {
		String format = "0.";
		for (int i = 0; i < precision; ++i)
			format += "0";
		DecimalFormat formatter = new DecimalFormat(format);
		String output = "";
		int dimension = this.getDimension();
		for (int i = 0; i < dimension; i++) {
			output += formatter.format(this.get(i));
			if (i < dimension - 1) output += " ";
		}
		return output;
	}

}
