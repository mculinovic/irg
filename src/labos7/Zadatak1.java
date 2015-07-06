package labos7;

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

public class Zadatak1 {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("At least one file name should be provided");
		}

		final ObjectModel model = readFromFile(args[0]);
		model.normalize();
		double increment = 1;
		final Eye eye = new Eye(3,1, increment);

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
						
						if (e.getKeyCode() == KeyEvent.VK_L){
							eye.increment();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_R) {
							eye.decrement();
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
						gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1, 100);
						
						gl2.glViewport(0, 0,  width, height);
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
						
						gl2.glClearColor(0, 255, 0, 0);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						glu.gluLookAt(eye.getX(), 4.0, eye.getZ(), 0, 0, 0, 0, 1, 0);
						
						
						gl2.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
						gl2.glEnable(GL2.GL_CULL_FACE);
						gl2.glCullFace(GL2.GL_BACK);
						renderScene(gl2);
					}

					private void renderScene(GL2 gl2) {
						
						Vertex3D[] vertices = model.getVertices();
						
						gl2.glColor3f(255, 0, 0);
						for (Face3D face : model.getFaces()) {
							gl2.glBegin(GL2.GL_POLYGON);
							for (int i = 0; i < face.indexes.length; i++) {
								Vertex3D vertex = vertices[face.indexes[i]];
								gl2.glVertex3f((float)vertex.x, (float)vertex.y, (float)vertex.z);
							}
							gl2.glEnd();
						}
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

}
