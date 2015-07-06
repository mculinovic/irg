package labos3;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.io.Console;
import java.util.Arrays;
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
				/*
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						System.out.println("Mis pomaknut na: x = " + e.getX() +
							", y = " + (model.getHeight() - e.getY()));

						model.setCurrentPosition(e.getX(), e.getY());
						
						glcanvas.display();
					}
				});*/

				//Reagiranje na pritiske tipaka na tipkovnici
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_O) {
							e.consume();				
							model.invertLineClipping();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_K) {
							e.consume();
							model.invertControl();
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
						int height = glautodrawable.getHeight(); 

						gl2.glClearColor(1, 1, 1, 1);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						model.setWidth(width);
						model.setHeight(height);
						
						if(model.isLineClipping()) {
							gl2.glBegin(GL.GL_LINE_LOOP);
							gl2.glColor3f(0, 128, 0);
							gl2.glVertex2f(width/2 - width/4, height/2 - height/4);
							gl2.glVertex2f(width/2 - width/4, height/2 + height/4);
							gl2.glVertex2f(width/2 + width/4, height/2 + height/4);
							gl2.glVertex2f(width/2 + width/4, height/2 - height/4);
							gl2.glEnd();
						}
						
						gl2.glBegin(GL.GL_POINTS);
						List<Line> lines = model.getLines();
						for (Line l: lines) {
							gl2.glColor3f(0, 0, 0);
							if (l.isCompleted()){
								if (model.isLineClipping()) {
									int[] segment = cohenSutherland(l.getStart(), l.getEnd(), glautodrawable);
									if (segment != null) {
										bresenham(Arrays.copyOfRange(segment, 0, 2), Arrays.copyOfRange(segment, 2, 4), gl2);
									}
								} else {
									bresenham(l.getStart(), l.getEnd(), gl2);
								}
								if (model.isControl()) {
									gl2.glColor3f(255, 0, 0);
									bresenham(l.getParallelStart(), l.getParallelEnd(), gl2);
								}
							}
						}
						gl2.glEnd();
						
						
					}

					
					private int[] cohenSutherland(int[] start, int[] end, GLAutoDrawable glautodrawable) {
						int width = model.getWidth();
						int height = model.getHeight();
						int xmin = width/2 - width/4;
						int xmax = width/2 + width/4;
						int ymin = height/2 - height/4;
						int ymax = height/2 + height/4;
						int[] result = new int[4];
						
						char startPoint = 0;
						char endPoint = 0;
						
						startPoint = computeCode(start[0], start[1]);
						endPoint = computeCode(end[0], end[1]);
						
						result[0] = start[0]; result[1] = start[1];
						result[2] = end[0]; result[3] = end[1];

						//provjeri slučajeve
						while(true) {
							if (startPoint == 0 && endPoint == 0) {
								break;
							} else if ((startPoint & endPoint) != 0) {
								return null;
							} else {
								char code;
								if (startPoint != 0) {
									code = startPoint;
								} else {
									code = endPoint;
								}
								
								int x = 0;
								int y = 0;
								
								if ((code & 8) != 0) {
									if (start[0] == end[0]) {
										x = start[0];
									}
									else {
										x =(int)(start[0] + (end[0] - start[0]) * (ymax - start[1])/(end[1] - start[1]));
									}
									y = ymax;
								} else if ((code & 4) != 0) {
									if (start[0] == end[0]) {
										x = start[0];
									} else {
										x =(int)(start[0] + (end[0] - start[0]) * (ymin - start[1])/(end[1] - start[1]));
									}
									y = ymin;
								} else if((code & 2) != 0) {
									if (start[1] == end[1]) {
										y = start[1];
									} else {
										y = (int)(start[1] + (end[1] - start[1]) * (xmax - start[0])/(end[0] - start[0]));
									}
									x = xmax;
								} else if ((code & 1) != 0) {
									if (start[1] == end[1]) {
										y = start[1];
									} else {
										y = (int)(start[1] + (end[1] - start[1]) * (xmin - start[0])/(end[0] - start[0]));
									}
									x = xmin;
								}
								
								if (code == startPoint) {
									result[0] = x;
									result[1] = y;
									startPoint = computeCode(result[0], result[1]);
								} else {
									result[2] = x;
									result[3] = y;
									endPoint = computeCode(result[2], result[3]);
								}
							}
						}
						
						return result;
					}

					private char computeCode(int x, int y) {
						int width = model.getWidth();
						int height = model.getHeight();
						int xmin = width/2 - width/4;
						int xmax = width/2 + width/4;
						int ymin = height/2 - height/4;
						int ymax = height/2 + height/4;
						char point = 0;
						if (y > ymax) {
							point |= 8;
						}
						if (y < ymin) {
							point |= 4;
						}
						if (x > xmax) {
							point |= 2;
						}
						if (x < xmin) {
							point |= 1;
						}
						return point;
					}

					private void bresenham(int[] start, int[] end, GL2 gl2) {
						
						if (start[0] <= end[0]) {
							if (start[1] <= end[1]) {
								bresenhamPozitivan(start[0], start[1], end[0], end[1], gl2);
							} else {
								bresenhamNegativan(start[0], start[1], end[0], end[1], gl2);
							}
						} else {
							if (start[1] >= end[1]) {
								bresenhamPozitivan(end[0], end[1], start[0], start[1], gl2);
							} else {
								bresenhamNegativan(end[0], end[1], start[0], start[1], gl2);
							}
						}
						
					}

					private void bresenhamPozitivan(int xs, int ys, int xe, int ye, GL2 gl2) {
						int x, yc, korekcija;
						int a, yf;
						
						if(ye - ys <= xe - xs) {
							a = 2 * (ye - ys);
							yc = ys; yf = -(xe - xs); korekcija = -2 * (xe - xs);
							for (x = xs; x <= xe; x++) {
								gl2.glVertex2f(x, yc);
								yf = yf + a;
								if (yf >= 0) {
									yf = yf + korekcija;
									yc = yc + 1;
								}
							}
						} else {
							x = xe; xe = ye; ye = x;
							x = xs; xs = ys; ys = x;
							a = 2 * (ye - ys);
							yc = ys; yf = -(xe - xs); korekcija = -2 * (xe - xs);
							for (x = xs; x <= xe; x++) {
								gl2.glVertex2f(yc, x);
								yf = yf + a;
								if (yf >= 0) {
									yf = yf + korekcija;
									yc = yc + 1;
								}
							}
						}
						
					}

					private void bresenhamNegativan(int xs, int ys, int xe, int ye, GL2 gl2) {
						
						int x, yc, korekcija;
						int a, yf;
						
						if (-(ye - ys) <= (xe - xs)) {
							a = 2 * (ye - ys);
							yc = ys; yf = (xe - xs); korekcija = 2 * (xe - xs);
							for (x = xs; x <= xe; x++) {
								gl2.glVertex2f(x, yc);
								yf = yf + a;
								if (yf <= 0) {
									yf = yf + korekcija;
									yc = yc - 1;
								}
							}
						} else {
							x = xe; xe = ys; ys = x;
							x = xs; xs = ye; ye = x;
							a = 2 * (ye - ys);
							yc = ys; yf = (xe - xs); korekcija = 2 * (xe - xs);
							for (x = xs; x <= xe; x++) {
								gl2.glVertex2f(yc, x);
								yf = yf + a;
								if (yf <= 0) {
									yf = yf + korekcija;
									yc = yc - 1;
								}
							}
						}
					} 
				});
				
				final JFrame jframe = new JFrame("Primjer iscrtavanja linija");
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