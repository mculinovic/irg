package hr.fer.zemris.linearna;

public class MatrixSubMatrixView extends AbstractMatrix {

	private int[] rowIndexes;
	private int[] colIndexes;
	
	private IMatrix matrix;
	
	public MatrixSubMatrixView(IMatrix matrix, int row, int column) {
		this.matrix = matrix;
		int rows = matrix.getRowsCount();
		int columns = matrix.getColsCount();
		rowIndexes = new int[rows - 1];
		colIndexes = new int[columns - 1];
		for (int i = 0, pos = 0; i < rows; i++) {
			if (i != row) {
				rowIndexes[pos] = i;
				pos++;
			}
		}
		for (int i = 0, pos = 0; i < columns; i++) {
			if (i != column) {
				colIndexes[pos] = i;
				pos++;
			}
		}
		
	}
	
	private MatrixSubMatrixView(IMatrix matrix, int[] rows, int[] columns) {
		this.matrix = matrix;
		rowIndexes = new int[rows.length];
		colIndexes = new int[columns.length];
		for (int i = 0; i < rows.length; i++) {
			rowIndexes[i] = rows[i];
		}
		for (int i = 0; i < columns.length; i++) {
			colIndexes[i] = columns[i];
		}
	}
	
	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return colIndexes.length;
	}

	@Override
	public double get(int i, int j) {
		return matrix.get(rowIndexes[i], colIndexes[j]);
	}

	@Override
	public IMatrix set(int i, int j, double value) {
		matrix.set(i, j, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixSubMatrixView(this.matrix, rowIndexes, colIndexes);
	}

	@Override
	public IMatrix newInstance(int m, int n) {
		return new Matrix(m,n);//ovo mi je jako cudno!!
	}

}
