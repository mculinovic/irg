package labos4;

import java.util.ArrayList;
import java.util.List;

public class Poligon {

	private List<BridPoligona> elementi;
	private boolean orijentacija;
	private boolean konveksan;
	
	public Poligon() {
		elementi = new ArrayList<>();
	}
	
	private void racunajKoeficijenteBridova() {
		int n = elementi.size();
		int t1 = n - 1;
		
		for (int t2 = 0; t2 < n; t2++) {
			BridPoligona b1 = elementi.get(t1);
			BridPoligona b2 = elementi.get(t2);
			b1.brid.a = b1.vrh.y - b2.vrh.y;
			b1.brid.b = - (b1.vrh.x - b2.vrh.x);
			b1.brid.c = b1.vrh.x * b2.vrh.y - b1.vrh.y * b2.vrh.x;
			b1.lijevi =  b1.vrh.y < b2.vrh.y;
			t1 = t2;
		}
	}
	
	public boolean provjeriKonveksnost() {
		
		racunajKoeficijenteBridova();
		this.setOrijentacija(false);
		this.setKonveksan(false);
		
		int n = elementi.size();
		
		if (n < 3) {
			return true;
		}
		
		int t1 = n - 2;
		int iznad = 0;
		int ispod = 0;
		@SuppressWarnings("unused")
		int naBridu = 0;
		
		for (int t2 = 0; t2 < n; t2++, t1++) {
			if (t1 >= n) {
				t1 = 0;
			}
			BridPoligona b1 = elementi.get(t1);
			BridPoligona b2 = elementi.get(t2);
			int r = b1.brid.a * b2.vrh.x + b1.brid.b * b2.vrh.y + b1.brid.c;
			if ( r == 0) {
				naBridu++;
			} else if (r > 0) {
				iznad++;
			} else {
				ispod++;
			}
		}
		
		if (ispod == 0) {
			setKonveksan(true);
			for(BridPoligona e: elementi) 
				e.lijevi = !e.lijevi;
		} else if (iznad == 0) {
			setKonveksan(true);
			setOrijentacija(true);
		}
		
		return isKonveksan();
	}

	public boolean isKonveksan() {
		return konveksan;
	}

	public void setKonveksan(boolean konveksan) {
		this.konveksan = konveksan;
	}

	public boolean isOrijentacija() {
		return orijentacija;
	}

	public void setOrijentacija(boolean orijentacija) {
		this.orijentacija = orijentacija;
	}

	public boolean jeUnutar(Tocka t) {
		int n = elementi.size();
		
		int iznad = 0;
		int ispod = 0;
		@SuppressWarnings("unused")
		int naBridu = 0;
		
		for (int i = 0; i < n; i++) {
			BridPoligona b = elementi.get(i);
			int r = b.brid.a * t.x + b.brid.b * t.y + b.brid.c;
			if ( r == 0) {
				naBridu++;
			} else if (r > 0) {
				iznad++;
			} else {
				ispod++;
			}
		}
		
		if (iznad == 0 || ispod == 0) {
			return true;
		}
		return false;
	}

	public List<BridPoligona> getElementi() {
		return elementi;
	}

	public void addPoint(Tocka t) {
		elementi.add(new BridPoligona(t, new Brid(), false));
		provjeriKonveksnost();
	}

	public void removePoint() {
		elementi.remove(elementi.size() - 1);
		provjeriKonveksnost();
	}
}
