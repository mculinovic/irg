package hr.fer.zemris.linearna;

public class MatrixVectorView extends AbstractMatrix {
	
	private IVector vector;
	private boolean asRowMatrix;
	
	public MatrixVectorView(IVector vector, boolean asRowMatrix) {
		this.vector = vector;
		this.asRowMatrix = asRowMatrix;
	}

	@Override
	public int getRowsCount() {
		if (asRowMatrix) {
			return 1;
		} else {
			return vector.getDimension();
		}
	}

	@Override
	public int getColsCount() {
		if (asRowMatrix) {
			return vector.getDimension();
		} else {
			return 1;
		}
	}

	@Override
	public double get(int i, int j) {
		if (asRowMatrix) {
			return vector.get(j);
		} else {
			return vector.get(i);
		}
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		if (asRowMatrix) {
			vector.set(j, value);
		} else {
			vector.set(i, value);
		}
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixVectorView(this.vector, this.asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int m, int n) {
		return new Matrix(m,n); //opet mi ovo nije jasno nikak -.-'
	}

}
