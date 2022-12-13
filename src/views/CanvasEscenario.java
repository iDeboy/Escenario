package views;

import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import loader.ObjReader;
import loader.Object3d;
import models.CamaraModel;
import models.ColorConverter;
import models.Light;

/**
 *
 * @author Honorio Acosta Ruiz
 */
public class CanvasEscenario extends GLCanvas implements GLEventListener {

	public static final int FPS = 60;

	private CamaraModel camara;

	private GLU glu;
	private GLUT glut;

	private Light light;

	private Object3d lamp;
	private Object3d desktop;
	private Object3d monitor;

	public CanvasEscenario() {
		initComponents();
	}

	private void initComponents() {
		addGLEventListener(this);

		camara = new CamaraModel(3.3f, 1.3f, 9.6f, 45, 0, 0.1f, 20, -1.5f, 0.5f, -1.4f, 0, 1, 0);
		light = new Light(GL2.GL_LIGHT0);

		KeyboardFocusManager.
						getCurrentKeyboardFocusManager().
						addPropertyChangeListener("focusOwner", (PropertyChangeEvent e) -> {
							//System.out.println(e.toString());
							requestFocusInWindow();
						});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				var keyCode = e.getKeyCode();
				var camMov = 0.1f;

				if (e.isShiftDown()) {
					// Camara movement
					switch (keyCode) {
						case VK_W: {
							camara.addY(camMov);
							break;
						}
						case VK_UP: {
							camara.addFov(camMov);
							break;
						}
						case VK_S: {
							camara.addY(-camMov);
							break;
						}
						case VK_DOWN: {
							camara.addFov(-camMov);
							break;
						}
						case VK_D: {
							camara.addX(camMov);
							break;
						}
						case VK_A: {
							camara.addX(-camMov);
							break;
						}
						case VK_Q: {
							camara.addZ(camMov);
							break;
						}
						case VK_E: {
							camara.addZ(-camMov);
							break;
						}
					}

				} else if (e.isControlDown()) {

					// Point movement (Head movement)
					switch (keyCode) {
						case VK_W: {
							camara.addYPoint(camMov);
							break;
						}
						case VK_UP: {
							camara.addFar(camMov);
							break;
						}
						case VK_S: {
							camara.addYPoint(-camMov);
							break;
						}
						case VK_DOWN: {
							camara.addFar(-camMov);
							break;
						}
						case VK_D: {
							camara.addXPoint(camMov);
							break;
						}
						case VK_A: {
							camara.addXPoint(-camMov);
							break;

						}
						case VK_Q: {
							camara.addZPoint(camMov);
							break;
						}
						case VK_E: {
							camara.addZPoint(-camMov);
							break;
						}
					}

				} else {

					// Selected figure movement
					switch (keyCode) {
						case VK_W: {
							light.addY(0.05f);
							break;
						}
						case VK_S: {
							light.addY(-0.05f);
							break;
						}
						case VK_D: {
							light.addX(0.05f);
							break;
						}
						case VK_A: {
							light.addX(-0.05f);
							break;
						}
						case VK_Q: {
							light.addZ(0.05f);
							break;
						}
						case VK_E: {
							light.addZ(-0.05f);
							break;
						}
					}
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				System.out.println(Arrays.toString(camara.getPosition()));

				System.out.println(Arrays.toString(camara.getPointingPosition()));

			}

		});
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		glu = new GLU();
		glut = new GLUT();

		gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);

		gl.glClearDepth(1.0f);

		gl.glEnable(GL_DEPTH_TEST);

		gl.glDepthFunc(GL_LEQUAL);

		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT,
						ColorConverter.convertToFME(new Color(72, 109, 177)), 0);

		gl.glEnable(GL2.GL_NORMALIZE);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();

		gl.glEnable(GL2.GL_LIGHTING);

		// Load obj
		ObjReader or = new ObjReader();

		try {
			lamp = or.Load("./data/lamp_office.obj");
			System.out.println("Objeto  cargado");

			desktop = or.Load("./data/escritorio.obj");
			System.out.println("Objeto  cargado");

			monitor = or.Load("./data/curvedTVForTable.obj");
			System.out.println("Objeto  cargado");

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(camara.getFov(), camara.getAspect(),
						camara.getNear(), camara.getFar());

		glu.gluLookAt(camara.getX(), camara.getY(), camara.getZ(),
						camara.getPointAtX(), camara.getPointAtY(), camara.getPointAtZ(),
						camara.getLookAtX(), camara.getLookAtY(), camara.getLookAtZ());

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();  // reset the model-view matrix

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

//		gl.glPushMatrix();
//		{
//			gl.glMaterialfv(GL_FRONT, GL_AMBIENT, ColorConverter.convertToFME(Color.DARK_GRAY), 0);
//			gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, ColorConverter.convertToFME(Color.DARK_GRAY), 0);
//			gl.glMaterialfv(GL_FRONT, GL_SPECULAR, ColorConverter.convertToFME(Color.DARK_GRAY), 0);
//			gl.glMateriali(GL_FRONT, GL_SHININESS, 5);
//			gl.glMaterialfv(GL_FRONT, GL_EMISSION, ColorConverter.convertToFME(Color.BLACK), 0);
//
//			gl.glTranslatef(2f, 2f, 2f);
//			glut.glutSolidIcosahedron();
//		}
//		gl.glPopMatrix();
		light.draw(gl, glut);

		// Draw obj
		if (lamp != null) {

			gl.glPushMatrix();
			{
				gl.glRotatef(90f, 0f, 1f, 0f);
				gl.glScalef(0.05f, 0.05f, 0.05f);
				gl.glTranslatef(-45f, 20f, -20f);
				lamp.draw(gl);
			}
			gl.glPopMatrix();

		}

		if (desktop != null) {

			gl.glPushMatrix();
			{
				gl.glRotatef(25f, 0f, 1f, 0f);
				gl.glScalef(0.5f, 0.5f, 0.5f);
				gl.glTranslatef(0f, 2f, 2f);
				desktop.draw(gl);
			}
			gl.glPopMatrix();

		}

		if (monitor != null) {

			gl.glPushMatrix();
			{
				gl.glRotatef(25f, 0f, 1f, 0f);
				gl.glScalef(0.03f, 0.03f, 0.03f);
				gl.glTranslatef(-55f, 43f, 40f);
				monitor.draw(gl);
			}
			gl.glPopMatrix();

		}

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		GL2 gl = drawable.getGL().getGL2();

		if (height == 0) {
			height = 1;
		}
		camara.setAspect((float) width / (float) height);

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(camara.getFov(), camara.getAspect(),
						camara.getNear(), camara.getFar());

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset

	}

}
