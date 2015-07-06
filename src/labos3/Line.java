package labos3;


public class Line {

	private int[] start;
	private int[] end;
	private boolean isStarted;
	private boolean isCompleted;
	private int[] parallelStart;
	private int[] parallelEnd;
	
	public Line() {
		start = new int[2];
		end = new int[2];
		parallelStart = new int[2];
		parallelEnd = new int[2];
	}
	
	public Line(int xs, int ys, int xe, int ye) {
		this();
		start[0] = xs;
		start[1] = ys;
		end[0] = xe;
		end[1] = ye;
	}

	public void addPoint(int x, int y) {
		if (!isStarted) {
			start[0] = x;
			start[1] = y;
			isStarted = true;
		} else {
			end[0] = x;
			end[1] = y;
			isCompleted = true;
			createParallelLine();
		}
	}

	private void createParallelLine() {
		double[] vector = new double[2];
		vector[0] = end[0] - start[0];
		vector[1] = end[1] - start[1];
		double temp = vector[0];
		vector[0] = vector[1];
		vector[1] = -temp;
		double norm = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1]);
		vector[0] /= norm;
		vector[1] /= norm;
		vector[0] *= -4;
		vector[1] *= -4;
		parallelStart[0] = start[0] + (int) vector[0];
		parallelStart[1] = start[1] + (int) vector[1];
		parallelEnd[0] = end[0] + (int) vector[0];
		parallelEnd[1] = end[1] + (int) vector[1];
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public int[] getStart() {
		return start;
	}

	public int[] getEnd() {
		return end;
	}

	public int[] getParallelStart() {
		return parallelStart;
	}

	public int[] getParallelEnd() {
		return parallelEnd;
	}
	
	
}
