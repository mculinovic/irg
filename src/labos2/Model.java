package labos2;

import java.util.ArrayList;
import java.util.List;

public class Model {
	
	private List<Triangle> coloredTriangles;
	private int activeColor;
	private int height;
	private int width;
	private Triangle currentTriangle;
	private int[] currentPosition;
	
	private final int RED = 0;
	private final int GREEN = 1;
	private final int BLUE = 2;
	private final int CYAN = 3;
	private final int YELLOW = 4;
	private final int MAGENTO = 5;
	
	
	public Model() {
		coloredTriangles = new ArrayList<Triangle>();
		activeColor =  RED;
		currentPosition = new int[2];
	}
	
	public float[] getColor(int current) {
		float[] color = new float[3];
		switch(current) {
		case RED: color[0] = 255; color[1] = 0; color[2] = 0;
			break;
		case GREEN: color[0] = 0; color[1] = 128; color[2] = 0; 
			break;
		case BLUE: color[0] = 0; color[1] = 0; color[2] = 255; 
			break; 
		case CYAN: color[0] = 0; color[1] = 255; color[2] = 255; 
			break;
		case YELLOW: color[0] = 255; color[1] = 255; color[2] = 0; 
			break; 
		case MAGENTO: color[0] = 255; color[1] = 0; color[2] = 255; 
			break; 
		default: break;
		}
		return color;
	}
	
	public void addPoint(int x, int y) {
		Triangle triangle = currentTriangle;
		if (currentTriangle == null || triangle.getPointCounter() == 3) {
			triangle = new Triangle(activeColor);
			coloredTriangles.add(triangle);
			currentTriangle = triangle;
		}
		triangle.addPoint(x, y);
	}

	public int[] getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int x, int y) {
		currentPosition[0] = x;
		currentPosition[1] = y;
	}

	public List<Triangle> getColoredTriangles() {
		return coloredTriangles;
	}
	
	public float[] getTriangleColor(Triangle t) {
		return getColor(t.getColor());
	}
	
	public float[] getActiveColor() {
		return getColor(activeColor);
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

	public void nextColor() {
		activeColor = (activeColor + 1) % 6; 
		
	}

	public void previousColor() {
		activeColor = (activeColor + 6 - 1) % 6;
		
	}
	
	

}
