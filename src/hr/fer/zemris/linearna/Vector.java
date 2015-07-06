package hr.fer.zemris.linearna;

public class Vector extends AbstractVector {
	
	private double[] elements;
	private int dimension;
	private boolean readOnly;
	
	public Vector(int dimension) {
		elements = new double[dimension];
		this.dimension = dimension;
	}
	
	public Vector(double... values) {
		elements = new double[values.length];
		this.dimension = values.length;
		for (int i = 0; i < values.length; i++) {
			elements[i] = values[i];
		}
	}
	
	public Vector(boolean readOnly, boolean isImmutable, double[] values) {
		if (readOnly) {
			this.readOnly = readOnly;
		}
		if (isImmutable) {
			this.elements = values;
		}
		else {
			elements = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				elements[i] = values[i];
			}
		}
		this.dimension = values.length;
	}

	@Override
	public double get(int i) {
		return elements[i];
	}

	@Override
	public IVector set(int i, double value) {
		if (!readOnly) {
			elements[i] = value;
		}
		return this;			
	}

	@Override
	public int getDimension() {
		return this.dimension;
	}

	@Override
	public IVector copy() {
		return new Vector(readOnly, false, elements);
	}

	@Override
	public IVector newInstance(int n) {
		return new Vector(n);
	}
	
	public static Vector parseSimple(String input) {
		String[] stringValues = input.split("\\s+");
		double[] values = new double[stringValues.length];
		for (int i = 0; i < stringValues.length; i++) {
			values[i] = Double.parseDouble(stringValues[i]);
		}
		return new Vector(values);
	}

}
