package hr.fer.zemris.linearna;

import java.text.DecimalFormat;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;

public abstract class AbstractMatrix implements IMatrix{

	public AbstractMatrix() {
		
	}
	
	@Override
	public abstract int getRowsCount();

	@Override
	public abstract int getColsCount();

	@Override
	public abstract double get(int i, int j);

	@Override
	public abstract IMatrix set(int i, int j, double value);

	@Override
	public abstract IMatrix copy();

	@Override
	public abstract IMatrix newInstance(int m, int n);

	@Override
	public IMatrix nTranspose(boolean liveView) {
		if (liveView) {
			return new MatrixTransposeView(this);
		} else {
			Matrix transposedMatrix = new Matrix(this.getColsCount(), this.getRowsCount());
			int m  = this.getRowsCount();
			int n = this.getColsCount();
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					transposedMatrix.set(j, i, this.get(i, j));
				}
			}
			return transposedMatrix;
		}
	}

	@Override
	public IMatrix add(IMatrix other) throws IncompatibleOperandException {
		if (this.getRowsCount() != other.getRowsCount() ||
				this.getColsCount() != other.getColsCount())
			throw new IncompatibleOperandException();
		int rows = this.getRowsCount();
		int cols = this.getColsCount();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.set(i, j, this.get( i, j) + other.get(i, j));
		return this;
	}

	@Override
	public IMatrix nAdd(IMatrix other) throws IncompatibleOperandException {
		return this.copy().add(other);
	}

	@Override
	public IMatrix sub(IMatrix other) throws IncompatibleOperandException {
		if (this.getRowsCount() != other.getRowsCount() ||
				this.getColsCount() != other.getColsCount())
			throw new IncompatibleOperandException();
		int rows = this.getRowsCount();
		int cols = this.getColsCount();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.set(i, j, this.get( i, j) - other.get(i, j));
		return this;
	}

	@Override
	public IMatrix nSub(IMatrix other) throws IncompatibleOperandException {
		return this.copy().sub(other);
	}

	@Override
	public IMatrix nMultiply(IMatrix other) throws IncompatibleOperandException {
		if (this.getColsCount() != other.getRowsCount())
			throw new IncompatibleOperandException();
		
		int rows = this.getRowsCount();
		int cols = other.getColsCount();
		int thisCols = this.getColsCount();
	    double[][] values = new double[rows][cols];
	    for (int i = 0; i < rows; i++)
	    	for (int j = 0; j < cols; j++)
	    		for (int k = 0; k < thisCols; k++)
	    			values[i][j] += this.get(i, k) * other.get(k, j);
	    return new Matrix(rows, cols, values, true);
	}

	@Override
	public double determinant() throws NoSquareMatrixException {
		if (this.getColsCount() != this.getRowsCount())
			throw new NoSquareMatrixException();
		
		int dimension = this.getRowsCount();
		if (dimension == 1) 
			return this.get(0, 0);
		if (dimension == 2) {
			return (this.get(0, 0) * this.get(1, 1)) - (this.get(0, 1) * this.get(1, 0));
		}
		
		double det = 0.0;
		for (int i = 0; i < dimension; i++) {
			det += isEven(i) * this.get(0, i) * this.subMatrix(0, i, true).determinant();
		}
		return det;
	}

	private int isEven(int i) {
		if (i % 2 == 0)
			return 1;
		else
			return -1;
	}

	@Override
	public IMatrix subMatrix(int row, int column, boolean liveView) {
		if (liveView) {
			return new MatrixSubMatrixView(this, row, column);
		} else {
			int rows = this.getRowsCount();
			int cols = this.getColsCount();
			Matrix matrix = new Matrix(rows - 1, cols - 1);
			int subRow = -1;
			for (int i = 0; i < rows; i++) {
				if (i == row)
					continue;
				subRow++;
				int subCol = -1;
				for (int j = 0; j < cols; j++) {
					if (j == column)
						continue;
					subCol++;
					matrix.set(subRow, subCol, this.get(i, j));
				}
			}
			return matrix;
		}
	}

	@Override
	public IMatrix nInvert() throws NoSquareMatrixException {
		return ((AbstractMatrix)this.nCofactor().nTranspose(true)).multiplyByConstant(1.0/this.determinant());
	}

	private IMatrix multiplyByConstant(double d) {
		int rows = this.getRowsCount();
		int cols = this.getColsCount();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.set(i, j, this.get(i, j) * d);
		return this;
	}

	private IMatrix nCofactor() throws NoSquareMatrixException {
		int rows = this.getRowsCount();
		int cols = this.getColsCount();
		Matrix matrix = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix.set(i, j, isEven(i) * isEven(j) * this.subMatrix(i, j, true).determinant());
			}
		}
		return matrix;
	}

	@Override
	public double[][] toArray() {
		int m = this.getRowsCount();
		int n = this.getColsCount();
		
		double[][] values = new double[m][n];
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				values[i][j] = this.get(i, j);
		return values;
	}

	@Override
	public IVector toVector(boolean isVector) {
		
		if (isVector) {
			return new VectorMatrixView(this);
		}
		else {
			int m = this.getRowsCount();
			int n = this.getColsCount();
			double[] values = new double[m * n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					values[i * m + j] = this.get(i, j);
				}
			}
			return new Vector(values);
		}
	}
	
	public String toString() {
		return toString(3);
	}
	
	public String toString(int precision) {
		String format = "0.";
		for (int i = 0; i < precision; ++i)
			format += "0";
		DecimalFormat formatter = new DecimalFormat(format);
		String output = new String();
		int m = this.getRowsCount();
		int n = this.getColsCount();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				output += formatter.format(this.get(i, j));
				if (j != n - 1) output+= " ";
				else output += "\n";
			}
		}
		
		return output;
	}

}
