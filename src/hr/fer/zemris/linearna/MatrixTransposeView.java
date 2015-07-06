package hr.fer.zemris.linearna;

public class MatrixTransposeView extends AbstractMatrix {

	private IMatrix matrix;
	
	public MatrixTransposeView(IMatrix matrix) {
		this.matrix = matrix;
	}
	
	@Override
	public int getRowsCount() {
		return matrix.getColsCount();
	}

	@Override
	public int getColsCount() {
		return matrix.getRowsCount();
	}

	@Override
	public double get(int i, int j) {
		return matrix.get(j, i);
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		matrix.set(j, i, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixTransposeView(this.matrix);
	}

	@Override
	public IMatrix newInstance(int m, int n) {
		return new Matrix(m, n);//opet jako cudno
	}

}
