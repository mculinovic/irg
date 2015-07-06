package fraktali;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

public class Zadatak1 {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		final Flag flag = new Flag();
		List<Double> limits = new ArrayList<>();
		limits.add(-2.0);
		limits.add(1.0);
		limits.add(-1.2);
		limits.add(1.2);
		final Stack<List<Double>> stack = new Stack<>();
		stack.push(limits);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);

				// Reagiranje na pritiske tipki na misu
				glcanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						List<Double> limits = stack.peek();
						int ymin = 0, ymax = 479, xmin = 0, xmax = 639;
						double umin = limits.get(0), umax = limits.get(1), vmin = limits.get(2), vmax = limits.get(3);
						int x = e.getX();
						int y = e.getY();
						Complex c = new Complex();
						c.re = (x - xmin) / (double) (xmax - xmin)
								* (umax - umin) + umin;
						c.im = (y - ymin) / (double) (ymax - ymin)
								* (vmax - vmin) + vmin;
						List<Double> newLimits = new ArrayList<>();
						double width = umax - umin;
						double height = vmax - vmin;
						newLimits.add(c.re - width/32);
						newLimits.add(c.re + width/32);
						newLimits.add(c.im  - height/32);
						newLimits.add(c.im + height/32);
						stack.push(newLimits);
						glcanvas.display();
					}
				});

				// Reagiranje na pomicanje pokazivaca misa
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {

					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {

						if (e.getKeyCode() == KeyEvent.VK_1) {
							flag.kvadratna = true;
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_2) {
							flag.kvadratna = false;
							glcanvas.display();
						}

						// vrsta sjenčanja
						if (e.getKeyCode() == KeyEvent.VK_B) {
							flag.crnoBijelo = true;
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_C) {
							flag.crnoBijelo = false;
							glcanvas.display();
						}
						
						if (e.getKeyCode() == KeyEvent.VK_X) {
							if (stack.size() > 1) {
								stack.pop();
								glcanvas.display();
							}
						}

						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							while (!(stack.size() == 1)) {
								stack.pop();
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
						//glu.gluOrtho2D(0.0f, width, 0.0f, height);

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
						@SuppressWarnings("unused")
						GLU glu = GLU.createGLU(gl2);

						gl2.glClearColor(1, 1, 1, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						renderScene(gl2);
					}

					private void renderScene(GL2 gl2) {

						gl2.glPointSize(1);
						gl2.glBegin(GL.GL_POINTS);
						List<Double> limits = stack.peek();
						int ymin = 0, ymax = 479, xmin = 0, xmax = 639;
						double umin = limits.get(0), umax = limits.get(1), vmin = limits.get(2), vmax = limits.get(3);
						int maxLimit = 128;
						//int maxLimit = 16 * 16 * 16;

						for (int y = ymin; y <= ymax; y++) {
							for (int x = xmin; x <= xmax; x++) {
								Complex c = new Complex();
								c.re = (x - xmin) / (double) (xmax - xmin)
										* (umax - umin) + umin;
								c.im = (y - ymin) / (double) (ymax - ymin)
										* (vmax - vmin) + vmin;
								int n = 0;
								if (flag.kvadratna) {
									n = divergenceTest(c, maxLimit);
								} else {
									n = divergenceTest2(c, maxLimit);
								}
								if (flag.crnoBijelo) {
									colorScheme1(n, gl2);
								} else {
									colorScheme2(n, gl2, maxLimit);
								}
								gl2.glVertex2f(x, y);

							}
						}
						gl2.glEnd();
					}

					private int divergenceTest2(Complex c, int maxLimit) {
						Complex z = new Complex(0, 0);
						for (int i = 1; i <= maxLimit; i++) {
							Complex next = z.multiply(z);
							next = next.multiply(z);
							next = next.add(c);
							z.re = next.re;
							z.im = next.im;
							double kvadratModula = z.re * z.re + z.im * z.im;
							if (kvadratModula > 4) {
								return i;
							}
						}
						return -1;
					}

					private void colorScheme2(int n, GL2 gl2, int maxLimit) {

						if (n == -1) {
							gl2.glColor3f(0f, 0f, 0f);
						} else if (maxLimit < 16) {
							int r = (int) ((n - 1) / (double) (maxLimit - 1)
									* 255 + 0.5);
							int g = 255 - r;
							int b = ((n - 1) % (maxLimit / 2)) * 255
									/ (maxLimit / 2);
							gl2.glColor3f((float) (r / 255f),
									(float) (g / 255f), (float) (b / 255f));
						} else {
							int lim = maxLimit < 32 ? maxLimit : 32;
							int r = (n - 1) * 255 / lim;
							int g = ((n - 1) % (lim / 4)) * 255 / (lim / 4);
							int b = ((n - 1) % (lim / 8)) * 255 / (lim / 8);
							gl2.glColor3f((float) (r / 255f),
									(float) (g / 255f), (float) (b / 255f));
						}
					}

					private void colorScheme1(int n, GL2 gl2) {

						if (n == -1) {
							gl2.glColor3f(0f, 0f, 0f);
						} else {
							gl2.glColor3f(1f, 1f, 1f);
						}

					}

					private int divergenceTest(Complex c, int maxLimit) {

						Complex z = new Complex(0, 0);
						for (int i = 1; i <= maxLimit; i++) {
							double next_re = z.re * z.re - z.im * z.im + c.re;
							double next_im = 2 * z.re * z.im + c.im;
							z.re = next_re;
							z.im = next_im;
							double kvadratModula = z.re * z.re + z.im * z.im;
							if (kvadratModula > 4) {
								return i;
							}
						}
						return -1;
					}

				});

				final JFrame jframe = new JFrame("Zadatak 1");
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

	static class Flag {

		boolean kvadratna = false;
		boolean crnoBijelo = false;
	}

}
