package labos3;

import java.util.ArrayList;
import java.util.List;

public class Model {

	List<Line> lines;
	private int height;
	private int width;
	private int[] currentPosition;
	private int pointCounter;
	private boolean control;
	private boolean lineClipping;

	
	public Model() {
		lines = new ArrayList<Line>();
		currentPosition = new int[2];
		pointCounter = 0;
	}
	
	
	public void addPoint(int x, int y) {
		if (pointCounter == 0) {
			Line line = new Line();
			line.addPoint(x, y);
			lines.add(line);
		} else {
			lines.get(lines.size() - 1).addPoint(x,y);
		}
		pointCounter = (pointCounter + 1) % 2;
	}

	public int[] getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int x, int y) {
		currentPosition[0] = x;
		currentPosition[1] = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public List<Line> getLines() {
		return lines;
	}


	public boolean isControl() {
		return control;
	}


	public void invertControl() {
		control = !control;
	}


	public boolean isLineClipping() {
		return lineClipping;
	}


	public void invertLineClipping() {
		lineClipping = !lineClipping;
	}

}
