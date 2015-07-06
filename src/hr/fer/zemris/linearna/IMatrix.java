package hr.fer.zemris.linearna;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;

public interface IMatrix {
	
	public int getRowsCount();
	
	public int getColsCount();
	
	public double get(int i, int j);
	
	public IMatrix set(int i, int j, double value);
	
	public IMatrix copy();
	
	public IMatrix newInstance(int m, int n);
	
	public IMatrix nTranspose(boolean liveView);
	
	public IMatrix add(IMatrix other) throws IncompatibleOperandException;
	
	public IMatrix nAdd(IMatrix other) throws IncompatibleOperandException;
	
	public IMatrix sub(IMatrix other) throws IncompatibleOperandException;
	
	public IMatrix nSub(IMatrix other) throws IncompatibleOperandException;
	
	public IMatrix nMultiply(IMatrix other) throws IncompatibleOperandException;
	
	public double determinant() throws NoSquareMatrixException;
	
	public IMatrix subMatrix(int row, int column, boolean liveView);
	
	public IMatrix nInvert() throws NoSquareMatrixException;
	
	public double[][] toArray();
	
	public IVector toVector(boolean isVector);	

}
