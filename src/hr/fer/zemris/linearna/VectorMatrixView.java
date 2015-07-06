package hr.fer.zemris.linearna;

public class VectorMatrixView extends AbstractVector {
	
	private IMatrix matrix;
	private int dimension;
	private boolean rowMatrix;

	public VectorMatrixView(IMatrix matrix) {
		this.dimension = matrix.getRowsCount() * matrix.getColsCount();
		this.matrix = matrix;
		if (matrix.getRowsCount() == 1) this.rowMatrix = true;
	}
	
	@Override
	public double get(int i) {
		if (rowMatrix)
			return matrix.get(1, i);
		return matrix.get(i/dimension, i%dimension);
	}

	@Override
	public IVector set(int i, double value) {
		if (rowMatrix) 
			matrix.set(1, i, value);
		matrix.set(i/dimension, i%dimension, value);
		return this;
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		return new VectorMatrixView(matrix);
	}

	@Override
	public IVector newInstance(int n) {
		return new Vector(n);//opet ne kuzim
	}
	
	

}
