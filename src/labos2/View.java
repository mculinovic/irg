package labos2;

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

				//Reagiranje na pritiske tipki na misu
				glcanvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {

						model.addPoint(e.getX(), e.getY());						
						
						glcanvas.display();
					}
				});

				//Reagiranje na pomicanje pokazivaca misa
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						
						model.setCurrentPosition(e.getX(), e.getY());						
						glcanvas.display();
					}
				});

				//Reagiranje na pritiske tipaka na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_N) {
							e.consume();
							model.nextColor();					
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_P) {
							e.consume();
							model.previousColor();
							glcanvas.display();
						}
					}
				});

				//Reagiranje na promjenu velicine platna, na zahtjev za
				//crtanjem i slicno...
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
						int width = glautodrawable.getWidth();
						//int height = glautodrawable.getHeight();
						float[] currentColor = model.getActiveColor();

						gl2.glClearColor(1, 1, 1, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						
						gl2.glBegin(GL2.GL_QUADS);
						gl2.glColor3f(currentColor[0], currentColor[1], currentColor[2]);
						gl2.glVertex2f(width, 0);
						gl2.glVertex2f(width, 20);
						gl2.glVertex2f(width - 20, 20);						
						gl2.glVertex2f(width - 20, 0);
						gl2.glEnd();
						
						
						List<Triangle> coloredTriangles = model.getColoredTriangles();
						for(Triangle t: coloredTriangles) {
							float[] triangleColor = model.getTriangleColor(t);
							gl2.glColor3f(triangleColor[0], triangleColor[1], triangleColor[2]);
							int[] x = t.getX();
							int[] y = t.getY();
							
							if (t.getPointCounter() != 3) {
								gl2.glBegin(GL2.GL_LINE_LOOP);
								int pointCounter = t.getPointCounter();
								for (int i = 0; i < pointCounter; i++) {
									gl2.glVertex2f(x[i], y[i]);
								}
								int[] currentPosition = model.getCurrentPosition();
								gl2.glVertex2f(currentPosition[0], currentPosition[1]);
								gl2.glEnd();
								
							} else {
								gl2.glBegin(GL.GL_TRIANGLES);								
								gl2.glVertex2f(x[0], y[0]);
								gl2.glVertex2f(x[1], y[1]);
								gl2.glVertex2f(x[2], y[2]);
								gl2.glEnd();
							}
						}
					}
				});
				
				final JFrame jframe = new JFrame("Primjer prikaza obojanog trokuta");
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