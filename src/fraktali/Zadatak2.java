package fraktali;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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

import labos5.Face3D;
import labos5.ObjectModel;
import labos5.Vertex3D;

public class Zadatak2 {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Potrebni predati datoteku s podacima");
			System.exit(1);
		}

		final Model model = readFromFile(args[0]);

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
						// glu.gluOrtho2D(0.0f, width, height, 0.0f);
						glu.gluOrtho2D(0.0f, width, 0.0f, height);

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
						GLU glu = GLU.createGLU(gl2);

						gl2.glClearColor(1, 1, 1, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						renderScene(gl2);
					}

					private void renderScene(GL2 gl2) {

						int limit = model.limit;
						Collections.sort(model.transformacije);

						gl2.glPointSize(1);
						gl2.glColor3f(0.0f, 0.7f, 0.3f);
						gl2.glBegin(GL2.GL_POINTS);
						double x0 = 0.0;
						double y0 = 0.0;

						for (int brojac = 0; brojac < model.pointsNumber; brojac++) {
							x0 = 0;
							y0 = 0;
							for (int iter = 0; iter < limit; iter++) {
								double x = 0, y = 0;

								int p = (int) (Math.random() * 101);

								for (Transformacija t : model.transformacije) {
									if (p <= t.p) {
										x = t.a * x0 + t.b * y0 + t.e;
										y = t.c * x0 + t.d * y0 + t.f;
										break;
									}
								}
								x0 = x;
								y0 = y;
							}

							//System.out.println(x0 + ": " + y0);
							gl2.glVertex2i(zaokruzi(x0 * model.eta1
									+ model.eta2), zaokruzi(y0 * model.eta3
									+ model.eta4));
						}

						gl2.glEnd();

					}

					private int zaokruzi(double d) {
						if (d >= 0)
							return (int) (d + 0.5);
						return (int) (d - 0.5);
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
				jframe.setSize(600, 600);
				jframe.setLocationRelativeTo(null);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
		});
	}

	static class Model {

		public List<Transformacija> transformacije = new ArrayList<>();

		public int pointsNumber;
		public int limit;
		public int eta1;
		public int eta2;
		public int eta3;
		public int eta4;

	}

	static class Transformacija implements Comparable<Transformacija> {

		public double a;
		public double b;
		public double c;
		public double d;
		public double e;
		public double f;
		public int p;

		@Override
		public int compareTo(Transformacija o) {
			return Integer.valueOf(p).compareTo(Integer.valueOf(o.p));
		}
	}

	private static Model readFromFile(String fileName) {

		Model model = new Model();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(fileName)),
					"UTF-8"));

			String line = reader.readLine();
			String[] podaci = line.split("\\s+");
			model.pointsNumber = Integer.valueOf(podaci[0]);

			line = reader.readLine();
			podaci = line.split("\\s+");
			model.limit = Integer.valueOf(podaci[0]);

			line = reader.readLine();
			podaci = line.split("\\s+");
			model.eta1 = Integer.valueOf(podaci[0]);
			model.eta2 = Integer.valueOf(podaci[1]);

			line = reader.readLine();
			podaci = line.split("\\s+");
			model.eta3 = Integer.valueOf(podaci[0]);
			model.eta4 = Integer.valueOf(podaci[1]);

			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				if (line.isEmpty()) {
					continue;
				}
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				String[] data = line.split("\\s+");

				Transformacija t = new Transformacija();
				t.a = Double.valueOf(data[0]);
				t.b = Double.valueOf(data[1]);
				t.c = Double.valueOf(data[2]);
				t.d = Double.valueOf(data[3]);
				t.e = Double.valueOf(data[4]);
				t.f = Double.valueOf(data[5]);
				t.p = model.transformacije.size() > 0 ? model.transformacije
						.get(model.transformacije.size() - 1).p : 0;
				t.p += (int) (Double.valueOf(data[6]) * 100);
				model.transformacije.add(t);
			}

			reader.close();

		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding UTF-8 is not supported!");
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + "doesn't exist!");
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error reading file " + fileName);
			System.out.println("Terminating program and printing stack trace!");
			e.printStackTrace();
			System.exit(1);
		}

		return model;
	}

}
