package labos9;

import hr.fer.zemris.exceptions.IncompatibleOperandException;
import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IRG;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Vector;

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
import labos6.Eye;

public class Zadatak2 {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("At least one file name should be provided");
		}

		final ObjectModel model = readFromFile(args[0]);
		model.normalize();
		final Flag flag = new Flag();
		model.izracunajNormaleVrhova();
		double increment = 1;
		final Eye eye = new Eye(3, 1, increment);

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

						if (e.getKeyCode() == KeyEvent.VK_L) {
							eye.increment();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_R) {
							eye.decrement();
							glcanvas.display();
						}

						// vrsta sjenčanja
						if (e.getKeyCode() == KeyEvent.VK_K) {
							flag.konstantnoSjencanje = true;
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_G) {
							flag.konstantnoSjencanje = false;
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_1) {
							System.out.println("Bez odbacivanja");
							flag.flag = 1;
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_2) {
							flag.flag = 2;
							System.out.println("Odbacivanje algoritmom 1");
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_3) {
							flag.flag = 3;
							System.out.println("Odbavivanje algoritmom 2");
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_4) {
							flag.flag = 4;
							System.out.println("Odbacivanje algoritmom 3");
							glcanvas.display();
						}

						// korištenje z spremnika
						if (e.getKeyCode() == KeyEvent.VK_Z) {
							flag.zspremnik = !flag.zspremnik;
							StringBuilder sb = new StringBuilder();
							sb.append("Z spremnik se koristi: ");
							sb.append(flag.zspremnik ? "da" : "ne");
							System.out.println(sb.toString());
							glcanvas.display();
						}

						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							eye.reset();
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
						// gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1, 100);

						// double fovy = Math.atan(1.0/2.0) * 2;
						// glu.gluPerspective(Math.toDegrees(fovy), 1, 1, 100);

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

						if (flag.zspremnik) {
							gl2.glEnable(GL2.GL_DEPTH_TEST);
						}

						gl2.glClearColor(0, 255, 0, 0);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT
								| GL2.GL_DEPTH_BUFFER_BIT);

						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						IMatrix tp = null;
						IMatrix pr = null;
						IMatrix m = null;
						try {
							tp = IRG.lookAtMatrix(
									new Vector(eye.getX(), 4, eye.getZ()),
									new Vector(0, 0, 0), new Vector(0, 1, 0));
							pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5,
									1, 100);
							m = tp.nMultiply(pr);
						} catch (IncompatibleOperandException e1) {
							e1.printStackTrace();
						}

						if (flag.flag == 2) {
							model.determineFaceVisibilities1(new Vector(eye
									.getX(), 4, eye.getZ()));
						}
						if (flag.flag == 3) {
							model.determineFaceVisibilities2(new Vector(eye
									.getX(), 4, eye.getZ()));
						}

						// postavke za poligon
						// gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);

						// izvor svjetlosti
						IVector lightVector = new Vector(4, 5, 3);

						if (flag.konstantnoSjencanje) {
							gl2.glShadeModel(GL2.GL_FLAT);
						} else {
							gl2.glShadeModel(GL2.GL_SMOOTH);
						}

						try {
							renderScene(gl2, eye, lightVector, m);
						} catch (IncompatibleOperandException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					private void renderScene(GL2 gl2, Eye eye,
							IVector lightVector, IMatrix m)
							throws IncompatibleOperandException {

						if (flag.konstantnoSjencanje) {
							crtajKonstantno(gl2, eye, lightVector, m);
						} else {
							crtajGouradovo(gl2, eye, lightVector, m);
						}
					}

					private void crtajGouradovo(GL2 gl2, Eye eye,
							IVector lightVector, IMatrix m)
							throws IncompatibleOperandException {
						Vertex3D[] vertices = model.getVertices();

						for (Face3D face : model.getFaces()) {
							if (flag.flag == 2 || flag.flag == 3) {
								if (!face.isVisible()) {
									continue;
								}
							}
							gl2.glBegin(GL2.GL_POLYGON);
							List<IVector> vrhovi = new ArrayList<>();
							for (int i = 0; i < face.indexes.length; i++) {
								Vertex3D vertex = vertices[face.indexes[i]];
								IVector v = new Vector(vertex.x, vertex.y,
										vertex.z, 1.0);
								IVector tv = v.toRowMatrix(false).nMultiply(m)
										.toVector(false).nFromHomogeneus();
								vrhovi.add(tv);
								// gl2.glVertex2f((float) tv.get(0), (float)
								// tv.get(1));
							}
							if (flag.flag == 4) {
								if (!IRG.isAntiClockwise(vrhovi.get(0),
										vrhovi.get(1), vrhovi.get(2))) {
									continue;
								}
							}
							for (int i = 0; i < face.indexes.length; i++) {
								Vertex3D vertex = vertices[face.indexes[i]];
								// IVector vrh = new Vector(vertex.x, vertex.y,
								// vertex.z);
								IVector vrh = vrhovi.get(i);
								IVector norm = (new Vector(face.getA(), face
										.getB(), face.getC())).normalize();
								float[] color = izracunajBoju(vertex.normala,
										vrh, eye, lightVector);
								gl2.glColor3f(color[0], color[1], color[2]);
								gl2.glVertex3f((float) vrh.get(0),
										(float) vrh.get(1), (float) vrh.get(2));
							}
							gl2.glEnd();
						}

					}

					private void crtajKonstantno(GL2 gl2, Eye eye,
							IVector lightVector, IMatrix m)
							throws IncompatibleOperandException {
						Vertex3D[] vertices = model.getVertices();

						for (Face3D face : model.getFaces()) {
							if (flag.flag == 2 || flag.flag == 3) {
								if (!face.isVisible()) {
									continue;
								}
							}
							gl2.glBegin(GL2.GL_POLYGON);
							List<IVector> vrhovi = new ArrayList<>();
							for (int i = 0; i < face.indexes.length; i++) {
								Vertex3D vertex = vertices[face.indexes[i]];
								IVector v = new Vector(vertex.x, vertex.y,
										vertex.z, 1.0);
								IVector tv = v.toRowMatrix(false).nMultiply(m)
										.toVector(false).nFromHomogeneus();
								vrhovi.add(tv);
								// gl2.glVertex2f((float) tv.get(0), (float)
								// tv.get(1));
							}
							if (flag.flag == 4) {
								if (!IRG.isAntiClockwise(vrhovi.get(0),
										vrhovi.get(1), vrhovi.get(2))) {
									continue;
								}
							}
							IVector norm = (new Vector(face.getA(),
									face.getB(), face.getC()).normalize());

							float centerX = 0;
							float centerY = 0;
							float centerZ = 0;
							for (int i = 0; i < face.indexes.length; i++) {
								// centerX += vertices[face.indexes[i]].x;
								// centerY += vertices[face.indexes[i]].y;
								// centerZ += vertices[face.indexes[i]].z;
								centerX += vrhovi.get(i).get(0);
								centerY += vrhovi.get(i).get(1);
								centerZ += vrhovi.get(i).get(2);
							}
							centerX /= 3.0;
							centerY /= 3.0;
							centerZ /= 3.0;

							IVector center = new Vector(centerX, centerY,
									centerZ);

							float[] color = izracunajBoju(norm, center, eye,
									lightVector);
							gl2.glColor3f(color[0], color[1], color[2]);

							for (int i = 0; i < face.indexes.length; i++) {
								// Vertex3D vertex = vertices[face.indexes[i]];
								//
								// gl2.glVertex3f((float) vertex.x,
								// (float) vertex.y, (float) vertex.z);
								IVector vrh = vrhovi.get(i);
								gl2.glVertex3f((float) vrh.get(0),
										(float) vrh.get(1), (float) vrh.get(2));
							}
							gl2.glEnd();
						}
					}

					private float[] izracunajBoju(IVector norm, IVector center,
							Eye eye, IVector lightVector) {
						float[] color = new float[3];
						IVector eyeVector = new Vector(eye.getX(), 4.0, eye
								.getZ());

						// ambijentna kompoenta
						color[0] = 0.2f;
						color[1] = 0.2f;
						color[2] = 0.2f;

						// izracun difuzne komponente
						// Id = Ii * kd * (l scalar n)
						IVector l = null;
						try {
							l = lightVector.nSub(center).normalize();
							color[0] += 0.8 * 1 * Math.max(
									l.scalarProduct(norm), 0);
							color[1] += 0.8 * 1 * Math.max(
									l.scalarProduct(norm), 0);
							color[2] += 0 * 1 * Math.max(l.scalarProduct(norm),
									0);
						} catch (IncompatibleOperandException ignorable) {
						}

						// izracun reflektirane komponente
						// Is = Ii * ks * (r scalar v)^n
						IVector n1 = null;
						IVector r = null;
						IVector v = null;
						try {
							n1 = norm.nScalarMultiply(l.scalarProduct(norm));
							r = n1.scalarMultiply(2).sub(l).normalize();
							v = eyeVector.nSub(center).normalize();
							color[0] += 0 * 0.01 * Math.pow(
									Math.max(r.scalarProduct(v), 0), 96);
							color[1] += 0 * 0.01 * Math.pow(
									Math.max(r.scalarProduct(v), 0), 96);
							color[2] += 0 * 0.01 * Math.pow(
									Math.max(r.scalarProduct(v), 0), 96);
						} catch (IncompatibleOperandException ignorable) {
						}

						return color;
					}

				});

				final JFrame jframe = new JFrame("Zadatak 2");
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

	private static ObjectModel readFromFile(String fileName) {

		List<Vertex3D> vertices = new ArrayList<>();
		List<Face3D> faces = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(fileName)),
					"UTF-8"));

			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				if (line.isEmpty()) {
					continue;
				}
				line = line.trim();
				String[] data = line.split("\\s+");
				switch (data[0]) {
				case "v":
					vertices.add(new Vertex3D(Double.parseDouble(data[1]),
							Double.parseDouble(data[2]), Double
									.parseDouble(data[3])));
					break;
				case "f":
					faces.add(new Face3D(Integer.parseInt(data[1]) - 1, Integer
							.parseInt(data[2]) - 1,
							Integer.parseInt(data[3]) - 1));
					break;
				default:
					continue;
				}
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

		return new ObjectModel(vertices, faces);
	}

	static class Flag {

		boolean konstantnoSjencanje = false;
		boolean zspremnik = false;
		int flag = 1;
	}

}
