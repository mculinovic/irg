package labos8;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BezierovaKrivuljaModel {
	
	public List<Point> points = new ArrayList<>();
	
	private final static int THRESHOLD = 5;
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}

	public void clear() {
		points.clear();
	}

	public boolean contains(Point p) {
		if (points.contains(p)) {
			return true;
		}
		return false;
	}

	public void remove(Point p) {
		points.remove(p);
	}

	public int contains(int x, int y) {
		int index = 0;
		for (Point p : points) {
			if (Math.abs(p.x - x) <= THRESHOLD && Math.abs(p.y - y) <= THRESHOLD) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public void removeAtIndex(int index) {
		points.remove(index);
	}

	public void addPointAt(int x, int y, int pozicija) {	
		points.add(pozicija, new Point(x,y));
	}
}
