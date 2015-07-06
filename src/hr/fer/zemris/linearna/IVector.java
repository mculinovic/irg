package hr.fer.zemris.linearna;

import hr.fer.zemris.exceptions.IncompatibleOperandException;

public interface IVector {
	
	public double get(int i);
	
	public IVector set(int i, double value);
	
	public int getDimension();
	
	public IVector copy();
	
	public IVector copyPart(int n);
	
	public IVector newInstance(int n);
	
	public IVector add(IVector other) throws IncompatibleOperandException;
	
	public IVector nAdd(IVector other) throws IncompatibleOperandException;
	
	public IVector sub(IVector other) throws IncompatibleOperandException;
	
	public IVector nSub(IVector other) throws IncompatibleOperandException;
	
	public IVector scalarMultiply(double value);
	
	public IVector nScalarMultiply(double value);
	
	public double norm();
	
	public IVector normalize();
	
	public IVector nNormalize();
	
	public double cosine(IVector other) throws IncompatibleOperandException;
	
	public double scalarProduct(IVector other) throws IncompatibleOperandException;
	
	public IVector nVectorProduct(IVector other) throws IncompatibleOperandException;
	
	public IVector nFromHomogeneus();
	
	public IMatrix toRowMatrix(boolean value);
	
	public IMatrix toColumnMatrix(boolean value);
	
	public double[] toArray();

}
