package labos6;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Example {

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
//				final Model model = new Model();
//				model.setWidth(640);
//				model.setHeight(480);

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
						
						//gl2.glViewport(0, 0, width, height);
						//gl2.glViewport(width/2, height/2, width, height);
						gl2.glViewport(0, 0,  width/2, height/2);
						//gl2.glViewport(width/2 - width/4, height/2, width - width/4, height);
						//gl2.glViewport(width/2 - width/8, height/2 - height/8, width/2 + width/8, height/2 + height/8);
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
						
						gl2.glClearColor(1, 1, 1, 0);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						
						gl2.glColor3f(1, 0, 0);
						gl2.glBegin(GL.GL_LINE_STRIP);
						gl2.glVertex3f(-0.9f, -0.9f, -0.9f);
						gl2.glVertex3f(0.9f, -0.9f, -0.9f);
						gl2.glEnd();
						
						gl2.glColor3f(1, 0, 0);
						gl2.glBegin(GL.GL_LINE_STRIP);
						gl2.glVertex3f(-0.9f, -0.7f, -0.9f);
						gl2.glVertex3f(0.9f, -0.7f, 3.1f);
						gl2.glEnd();
						
					}

					
				});

				final JFrame jframe = new JFrame("Example");
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
}