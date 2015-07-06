package labos4;

public class Model {
	
	private Poligon poligon;
	
	private int height;
	private int width;
	
	private int[] currentPosition;
	
	public boolean stanje;
	public boolean popunjavanje;
	public boolean konveksnost;
	
	
	public Model() {
		currentPosition = new int[2];
		stanje = false;
		poligon = new Poligon();
	}
	
	public void addPoint(int x, int y) {
		Tocka t = new Tocka(x, y);
		poligon.addPoint(t);
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

	public Poligon getPoligon() {
		return poligon;
	}

	private void setPoligon(Poligon poligon) {
		this.poligon = poligon;
	}
	
	public void restart() {
		setPoligon(new Poligon());
		konveksnost = false;
		popunjavanje = false;
	}

	public void removePoint() {
		poligon.removePoint();
	}
	
	

}
