package labos4;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class View {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				final Model model = new Model();
				model.setWidth(640);
				model.setHeight(480);

				// Reagiranje na pritiske tipki na misu
				glcanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {

						if (!model.stanje) {
							if (model.konveksnost) {
								model.addPoint(e.getX(), e.getY());
								if (!model.getPoligon().provjeriKonveksnost()) {
									System.out
											.println("Vrh koji želite dodati se ne prihvaća!");
									model.removePoint();
								}
							} else {
								model.addPoint(e.getX(), e.getY());
							}
							glcanvas.display();
						} else {
							boolean provjera = model.getPoligon().jeUnutar(
									new Tocka(e.getX(), e.getY()));
							if (provjera) {
								System.out.println("Tocka(" + e.getX() + ", "
										+ e.getY() + ") je unutar poligona");
							} else {
								System.out.println("Tocka(" + e.getX() + ", "
										+ e.getY() + ") je izvan poligona");
							}
						}
					}
				});

				// Reagiranje na pomicanje pokazivaca misa
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {

						model.setCurrentPosition(e.getX(), e.getY());
						glcanvas.display();
					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {

						// pritisnuta tipka K
						if (e.getKeyCode() == KeyEvent.VK_K) {
							e.consume();
							// omoguci promjenu iscrtavanja poligona samo ako
							// smo u stanju 1
							if (model.stanje) {
								return;
							}
							// promijeni konveksnost ako je to moguće
							if ((!model.konveksnost && model.getPoligon()
									.isKonveksan()) || model.konveksnost) {
								model.konveksnost = !model.konveksnost;
								glcanvas.display();
							} else {
								// u suprotnom ispisi poruku o gresci
								System.out
										.println("Zadani poligon već nije konveksan!");
								System.out
										.println("Promjena zastavice nije moguća");
							}
						}

						// pritisnuta tipka p
						if (e.getKeyCode() == KeyEvent.VK_P) {
							e.consume();
							// omoguci popunjavanje samo ako smo u stanju 1
							if (model.stanje) {
								return;
							}
							model.popunjavanje = !model.popunjavanje;
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_N) {
							e.consume();
							model.stanje = !model.stanje;
							if (!model.stanje) {
								model.restart();
							}
							glcanvas.display();
						}
					}
				});

				// Reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno...
				glcanvas.addGLEventListener(new GLEventListener() {
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
							int y, int width, int height) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();

						GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width, height, 0.0f);

						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();

						gl2.glViewport(0, 0, width, height);
					}

					@Override
					public void init(GLAutoDrawable glautodrawable) {

					}

					@Override
					public void dispose(GLAutoDrawable glautodrawable) {

					}

					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();

						if (!model.konveksnost) {
							gl2.glClearColor(1, 1, 1, 1);
						} else {
							gl2.glClearColor(0, 1, 0, 1);
						}
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						// Crtanje poligona u stanju 1
						if (!model.stanje) {

							if (model.popunjavanje) {
								gl2.glBegin(GL2.GL_LINES);
								gl2.glColor3f(0, 0, 0);
								popuniPoligon(gl2);
								gl2.glEnd();
							}
							gl2.glBegin(GL2.GL_LINE_LOOP);
							gl2.glColor3f(0, 0, 0);

							nacrtajPoligon(gl2);

							int[] trenutnaPozicija = model.getCurrentPosition();
							gl2.glVertex2f(trenutnaPozicija[0],
									trenutnaPozicija[1]);
							gl2.glEnd();
						} else {

							if (!model.popunjavanje) {
								gl2.glBegin(GL2.GL_LINE_LOOP);
								gl2.glColor3f(0, 0, 0);

								nacrtajPoligon(gl2);

								gl2.glEnd();
							} else {
								gl2.glBegin(GL2.GL_LINES);
								gl2.glColor3f(0, 0, 0);
								popuniPoligon(gl2);
								gl2.glEnd();
							}
						}
					}

					// algoritam iz knjige
					// smije se koristiti GL_LINES I GL_POINTS
					private void popuniPoligon(GL2 gl2) {
						int t1 = 0;
						int t2 = 0;
						int xmin = 0;
						int xmax = 0;
						int ymin = 0;
						int ymax = 0;
						double L = 0.0;
						double D = 0.0;
						double x = 0.0;

						List<BridPoligona> elementi = model.getPoligon()
								.getElementi();
						xmin = xmax = elementi.get(0).vrh.x;
						ymin = ymax = elementi.get(0).vrh.y;
						int n = elementi.size();
						for (int i = 1; i < n; i++) {
							BridPoligona b = elementi.get(i);
							if (xmin > b.vrh.x) {
								xmin = b.vrh.x;
							}
							if (xmax < b.vrh.x) {
								xmax = b.vrh.x;
							}
							if (ymin > b.vrh.y) {
								ymin = b.vrh.y;
							}
							if (ymax < b.vrh.y) {
								ymax = b.vrh.y;
							}
						}

						for (int y = ymin; y <= ymax; y++) {
							L = xmin;
							D = xmax;
							t1 = n - 1;
							for (t2 = 0; t2 < n; t1 = t2++) {
								BridPoligona b1 = elementi.get(t1);
								BridPoligona b2 = elementi.get(t2);
								if (b1.brid.a == 0) {
									if (b1.vrh.y == y) {
										if (b1.vrh.x < b2.vrh.x) {
											L = b1.vrh.x;
											D = b2.vrh.x;
										} else {
											L = b2.vrh.x;
											D = b1.vrh.x;
										}
										break;
									}
								} else {
									x = (-b1.brid.b * y - b1.brid.c)
											/ (double) b1.brid.a;
									if (b1.lijevi) {
										if (L < x) {
											L = x;
										}
									} else {
										if (D > x) {
											D = x;
										}
									}
								}
							}
							if (L > D) {
								continue; //??????
							}
							gl2.glVertex2f((int) L, y);
							gl2.glVertex2f((int) D, y);
						}
					}

					private void nacrtajPoligon(GL2 gl2) {

						List<BridPoligona> tocke = model.getPoligon()
								.getElementi();
						int brojTocaka = tocke.size();
						for (int i = 0; i < brojTocaka; i++) {
							gl2.glVertex2f(tocke.get(i).vrh.x,
									tocke.get(i).vrh.y);
						}
					}
				});

				final JFrame jframe = new JFrame("Primjer crtanja poligona");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent windowevent) {
						jframe.dispose();
						System.exit(0);
					}
				});
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(model.getWidth(), model.getHeight());
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
		});
	}
}