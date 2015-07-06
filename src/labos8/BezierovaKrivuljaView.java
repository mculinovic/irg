package labos8;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.exceptions.NoSquareMatrixException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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

public class BezierovaKrivuljaView {

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
				final BezierovaKrivuljaModel model = new BezierovaKrivuljaModel();
				final Point currentPoint = new Point(-1, -1);
				final IntContainer pozicija = new IntContainer();

				// Reagiranje na pritiske tipki na misu
				glcanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							model.addPoint(e.getX(), e.getY());
							glcanvas.display();
						}
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							int index = model.contains(e.getX(), e.getY());
							if (index != -1) {
								pozicija.pozicija = index;
								model.removeAtIndex(index);
								glcanvas.display();
							}
						}							
					}
					
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							model.addPointAt(currentPoint.x, currentPoint.y, pozicija.pozicija);
							currentPoint.x = -1;
							currentPoint.y = -1;
							pozicija.pozicija = -1;
							glcanvas.display();
						}
					}

				});

				// Reagiranje na pomicanje pokazivaca misa
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
					}
					
					@Override
					public void mouseDragged(MouseEvent e) {
//						if (e.getButton() == MouseEvent.BUTTON3) {
							currentPoint.x = e.getX();
							currentPoint.y = e.getY();
							glcanvas.display();
//						}
					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							model.clear();
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

						gl2.glClearColor(0, 255, 0, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						if (currentPoint.x != -1) {
							model.addPointAt(currentPoint.x, currentPoint.y, pozicija.pozicija);
						}
						
						gl2.glColor3f(255, 0, 0);
						nacrtajPoligon(gl2);

						gl2.glColor3f(0, 0, 255);						
						nacrtajKrivulju(gl2, model.points);

						List<Point> interpoliraniVrhovi = interpoliraj(model.points);
						
						if (currentPoint.x != -1) {
							model.removeAtIndex(pozicija.pozicija);
						}
						gl2.glColor3f(0, 0, 0);
						nacrtajKrivulju(gl2, interpoliraniVrhovi);
					}

					private List<Point> interpoliraj(List<Point> points) {
						if (points == null || points.isEmpty()) {
							return null;
						}
						List<Point> r = new ArrayList<>();

						int n = points.size() - 1;

						double[][] p = new double[n + 1][2];
						int i = 0;
						for (Point point : points) {
							p[i][0] = point.x;
							p[i][1] = point.y;
							i++;
						}

						IMatrix P = new Matrix(n + 1, 2, p, true);

						List<Integer> factors = computeFactors(n);

						double[][] b = new double[n + 1][n + 1];
						for (i = 0; i <= n; i++) {
							double t = 1.0 / n * i;
							for (int j = 0; j <= n; j++) {
								if (j == 0) {
//									System.out.println(n);
//									System.out.println(i + " " + j);
									b[i][j] = factors.get(j)
											 * Math.pow(1 - t, n);
								} else if (j == n) {
									b[i][j] = factors.get(j) * Math.pow(t, n);
								} else {
									b[i][j] = factors.get(j) * Math.pow(t, j)
											* Math.pow(1 - t, n - j);
								}
							}
						}

						IMatrix B = new Matrix(n + 1, n + 1, b, true);
						IMatrix R = null;

						try {
							R = B.nInvert().nMultiply(P);
						} catch (IncompatibleOperandException
								| NoSquareMatrixException e) {
							e.printStackTrace();
						}

						for (i = 0; i <= n; i++) {
							r.add(new Point((int)R.get(i,0), (int) R.get(i,1)));
						}
						
						return r;
					}

					private void nacrtajKrivulju(GL2 gl2, List<Point> points) {

						if (points == null || points.isEmpty()) {
							return;
						}
						
						Point p = new Point();
						int n = points.size() - 1;
						int divs = 60;
						List<Integer> factors = computeFactors(n);
						double b;

						gl2.glBegin(GL2.GL_LINE_STRIP);

						for (int i = 0; i <= divs; i++) {
							double t = 1.0 / divs * i;
							p.x = 0;
							p.y = 0;
							for (int j = 0; j <= n; j++) {
								if (j == 0) {
									b = factors.get(j) * Math.pow(1 - t, n);
								} else if (j == n) {
									b = factors.get(j) * Math.pow(t, n);
								} else {
									b = factors.get(j) * Math.pow(t, j)
											* Math.pow(1 - t, n - j);
								}
								p.x += b * points.get(j).x;
								p.y += b * points.get(j).y;
							}
							gl2.glVertex2f(p.x, p.y);
						}

						gl2.glEnd();
						factors = null;

					}

					private List<Integer> computeFactors(int n) {
						List<Integer> factors = new ArrayList<>();
						int a = 1;

						for (int i = 1; i <= n + 1; i++) {
							factors.add(a);
							a = a * (n - i + 1) / i;
						}

						return factors;
					}

					private void nacrtajPoligon(GL2 gl2) {

						gl2.glBegin(GL2.GL_LINE_STRIP);
						List<Point> points = model.points;
						int n = points.size();
						for (int i = 0; i < n; i++) {
							gl2.glVertex2d(points.get(i).x, points.get(i).y);
						}
						gl2.glEnd();
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
				jframe.setSize(640, 480);
				jframe.setLocationRelativeTo(null);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
		});
	}
	
	static class IntContainer {
		
		int pozicija;
		
		public IntContainer() {
		}
	}
}