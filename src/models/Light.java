package models;

import static com.jogamp.opengl.GL.GL_FRONT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;

/**
 *
 * @author Honorio Acosta Ruiz
 */
public class Light extends Object3D implements IDrawable {

	private int light;

	private Color ambientColor;
	private Color difuseColor;
	private Color specularColor;

	public Light(int light) {
		this(light, 3, 2, 3, Color.ORANGE, Color.ORANGE, Color.RED);
	}

	public Light(int light, float x, float y, float z, float h,
					Color ambientColor, Color difuseColor, Color specularColor) {
		super("Light", x, y, z, h);

		this.light = light;
		this.ambientColor = ambientColor;
		this.difuseColor = difuseColor;
		this.specularColor = specularColor;
	}

	public Light(int light, float x, float y, float z,
					Color ambientColor, Color difuseColor, Color specularColor) {
		super("Light", x, y, z);

		this.light = light;
		this.ambientColor = ambientColor;
		this.difuseColor = difuseColor;
		this.specularColor = specularColor;
	}

	@Override
	public void draw(GL2 gl, GLUT glut) {

		gl.glEnable(light);

		gl.glPushMatrix();
		{
			gl.glLightfv(light, GL2.GL_POSITION, getPosition(), 0);
			gl.glLightfv(light, GL2.GL_AMBIENT, ColorConverter.convertToFME(ambientColor), 0);
			gl.glLightfv(light, GL2.GL_DIFFUSE, ColorConverter.convertToFME(difuseColor), 0);
			gl.glLightfv(light, GL2.GL_SPECULAR, ColorConverter.convertToFME(specularColor), 0);

			//gl.glTranslatef(x, y, z);
			//glut.glutSolidSphere(0.5f, 20, 20);
		}
		gl.glPopMatrix();

	}

}
