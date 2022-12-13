/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.jogamp.opengl.*;

/**
 *
 * @author gmendez
 */
public class Object3d {

	public ArrayList<Face> fcs = new ArrayList<>();
	public ArrayList<Line> lns = new ArrayList<>();
	public ArrayList<Point> pts = new ArrayList<>();
	ArrayList<Object3d> objs = new ArrayList<>();
	ArrayList<Vertice> verts = new ArrayList<>();
	ArrayList<Vertice> vnrms = new ArrayList<>();
	ArrayList<Vertice> vtxtr = new ArrayList<>();

	HashMap<String, Material> mtls = new HashMap<>();

	String type;
	String groupname;
	String fileMat;
	String smooth_shading;

	public Object3d() {

	}

	public Object3d(String ptype) {
		this.type = ptype;
	}

	public Object3d(float vertices[][]) {
		int n = vertices.length;
		for (int i = 0; i < n; i++) {
			verts.add(new Vertice(vertices[i]));
		}
	}

	public Object3d(float vertices[][], float normal[][]) {
		int n = vertices.length;
		for (int i = 0; i < n; i++) {
			verts.add(new Vertice(vertices[i]));
		}

		n = normal.length;
		for (int i = 0; i < n; i++) {
			vnrms.add(new Vertice(normal[i]));
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addVertice(float vertice[]) {
		verts.add(new Vertice(vertice));
	}

	public void addVertice(String x, String y, String z) {
		verts.add(new Vertice(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z)));
	}

	public void addNormal(String x, String y, String z) {
		vnrms.add(new Vertice(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z)));
	}

	public void addFace(String sFace[], String material, String groupname) {
		fcs.add(new Face(sFace, material, groupname, this));
	}

	public void addLine(String sLine[], String material, String groupname) {
		lns.add(new Line(sLine, material, groupname, this));
	}

	public void addPoint(String[] sPoint, String material, String groupname) {
		pts.add(new Point(sPoint, material, groupname, this));
	}

	public void addVertTex(String x, String y) {
		vtxtr.add(new Vertice(Float.parseFloat(x), Float.parseFloat(y), 0.0f));
	}

	public void addMateriales(HashMap<String, Material> mtls) {
		this.mtls = mtls;
	}

	public void addSmoothShading(String s) {
		this.smooth_shading = s;
	}

	public String toString() {
		String info;

		info = "(" + this.type + "):\n";

		info += "Smooth&Shade: " + this.smooth_shading + "\n";

		info = info + "\nVertices:";

		for (Vertice vert : verts) {
			info = info + "\nx=" + vert.x + ", y=" + vert.y + ", z=" + vert.z;
		}

		info = info + "\n\nNormales:";
		for (Vertice vert : vnrms) {
			info = info + "\nx=" + vert.x + ", y=" + vert.y + ", z=" + vert.z;
		}

		info = info + "\n\nMateriales:";

		for (Map.Entry me : mtls.entrySet()) {
			Material m = (Material) me.getValue();
			info = info + "\n" + m;
		}

		info = info + "\n\nFaces:";
		int i = 1;
		for (Face fc : fcs) {
			info = info + "Face " + (i++) + ": " + fc + "\n";
		}

		info = info + "\n\nObjetos Hijo:";
		for (Object3d obj3d : objs) {
			info = info + obj3d;
		}

		return info;
	}

	void setGroupName(String grpname) {
		this.groupname = grpname;
	}

	public void draw(GL2 gl) {

		for (Face fc : fcs) {
			if (fc.mtl != null) {
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, fc.mtl.ambient, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, fc.mtl.diffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, fc.mtl.specular, 0);
				gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, fc.mtl.shininess);

				if (fc.mtl.isTextureD) {
					gl.glEnable(GL2.GL_TEXTURE_2D);
					gl.glEnable(GL2.GL_BLEND);
					gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

					fc.mtl.txtrs.get(0).bind(gl);
					fc.mtl.txtrs.get(0).enable(gl);
				}
			}

			if (this.smooth_shading != null && !this.smooth_shading.isEmpty()) {
				gl.glShadeModel(GL2.GL_SMOOTH);
			}

			switch (fc.corners.size()) {
				case 1:
					gl.glBegin(GL2.GL_POINTS);
					break;
				case 2:
					gl.glBegin(GL2.GL_LINES);
					break;
				case 3:
					gl.glBegin(GL2.GL_TRIANGLES);
					break;
				case 4:
					gl.glBegin(GL2.GL_QUADS);
					break;
			}

			for (Corner cr : fc.corners) {
				if (cr.norm != null) {
					gl.glNormal3fv(cr.norm.getVertex3fv());
				}

				if (cr.txtr != null) {
					gl.glTexCoord3fv(cr.txtr.getVertex3fv());
				}
				if (cr.vert != null) {
					gl.glVertex3fv(cr.vert.getVertex3fv());
				}
			}
			gl.glEnd();

			if (fc.mtl != null && fc.mtl.isTextureD) {
				fc.mtl.txtrs.get(0).disable(gl);
			}
		}

		for (Line ln : lns) {

			if (ln.mtl != null) {
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ln.mtl.ambient, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, ln.mtl.diffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, ln.mtl.specular, 0);
				gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, ln.mtl.shininess);

				if (ln.mtl.isTextureD) {
					gl.glEnable(GL2.GL_TEXTURE_2D);
					gl.glEnable(GL2.GL_BLEND);
					gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

					ln.mtl.txtrs.get(0).bind(gl);
					ln.mtl.txtrs.get(0).enable(gl);
				}
			}

			if (!this.smooth_shading.isEmpty()) {
				gl.glShadeModel(GL2.GL_SMOOTH);
			}

			gl.glBegin(GL2.GL_LINE);

			for (Corner cr : ln.corners) {
				if (cr.norm != null) {
					gl.glNormal3fv(cr.norm.getVertex3fv());
				}

				if (cr.txtr != null) {
					gl.glTexCoord3fv(cr.txtr.getVertex3fv());
				}

				gl.glVertex3fv(cr.vert.getVertex3fv());
			}
			gl.glEnd();

			if (ln.mtl != null && ln.mtl.isTextureD) {
				ln.mtl.txtrs.get(0).disable(gl);
			}
		}

		for (Point pt : pts) {

			if (pt.mtl != null) {
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, pt.mtl.ambient, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, pt.mtl.diffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, pt.mtl.specular, 0);
				gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, pt.mtl.shininess);

				if (pt.mtl.isTextureD) {
					gl.glEnable(GL2.GL_TEXTURE_2D);
					gl.glEnable(GL2.GL_BLEND);
					gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);

					pt.mtl.txtrs.get(0).bind(gl);
					pt.mtl.txtrs.get(0).enable(gl);
				}
			}

			if (!this.smooth_shading.isEmpty()) {
				gl.glShadeModel(GL2.GL_SMOOTH);
			}

			gl.glBegin(GL2.GL_POINTS);

			for (Corner cr : pt.corners) {
				if (cr.norm != null) {
					gl.glNormal3fv(cr.norm.getVertex3fv());
				}

				if (cr.txtr != null) {
					gl.glTexCoord3fv(cr.txtr.getVertex3fv());
				}

				gl.glVertex3fv(cr.vert.getVertex3fv());
			}
			gl.glEnd();

			if (pt.mtl != null && pt.mtl.isTextureD) {
				pt.mtl.txtrs.get(0).disable(gl);
			}
		}
	}

	void printVectors() {
		for (Face fc : fcs) {
			for (Corner cr : fc.corners) {
				System.out.println(cr.vert);
			}
		}
	}
}
