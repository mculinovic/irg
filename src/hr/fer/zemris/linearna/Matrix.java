package hr.fer.zemris.linearna;

public class Matrix extends AbstractMatrix {

	
	protected double[][] elements;
	protected int rows;
	protected int cols;
	
	public Matrix(int m, int n) {
		elements = new double[m][n];
		this.rows = m;
		this.cols = n;
	}
	
	public Matrix(int m, int n, double[][] values, boolean isImmutable) {
		this.rows = m;
		this.cols = n;
		if (isImmutable) {
			this.elements = values;
		} else {
			elements = new double[m][n];
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.cols; j++) {
					elements[i][j] = values[i][j];
				}
			}
		}
	}
	
	@Override
	public int getRowsCount() {
		return this.rows;
	}

	@Override
	public int getColsCount() {
		return this.cols;
	}

	@Override
	public double get(int i, int j) {
		return elements[i][j];
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		this.elements[i][j] = value;
		return this;
	}

	@Override
	public IMatrix copy() {
		Matrix matrix = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) 
			for (int j = 0; j < this.cols; j++)
				matrix.set(i, j, this.get(i, j));
		return matrix;
	}

	@Override
	public IMatrix newInstance(int m, int n) {
		return new Matrix(m,n);
	}
	
	public static Matrix parseSimple(String input) {
		String[] rows = input.split("\\|");
		int cols = 0;
		double[][] values = null;
		for (int i = 0; i < rows.length; i++) {
			rows[i] = rows[i].trim();
			Vector vector = Vector.parseSimple(rows[i]);
			if (i == 0) {
				cols = vector.getDimension();
				values = new double[rows.length][cols];
			}
			for (int j = 0; j < cols; j++) {
				values[i][j] = vector.get(j);
			}
		}
		
		return new Matrix(rows.length, cols, values, true);
	}

}
