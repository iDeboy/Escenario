/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author gmendez
 */
public class Face {

	public ArrayList<Corner> corners;
	Object3d parent;
	Material mtl = null;
	String groupName = "";

	public Face(String[] aCorners, String material, String groupname, Object3d parent) {
		corners = new ArrayList<>();

		int len = aCorners.length;
		for (int i = 1; i < len; i++) {
			corners.add(new Corner(aCorners[i], parent));
		}

		if (parent.mtls.containsKey(material)) {
			mtl = (Material) parent.mtls.get(material);
		}

		this.groupName = groupname;

	}

	public void setGroupName(String groupname) {
		this.groupName = groupname;
	}

	public ArrayList<Corner> getCorners() {
		return corners;
	}

	public String toString() {
		String info = "";
		info = info + "\n Group : " + this.groupName;
		for (Corner cr : corners) {
			info = info + "\n Corner: " + cr.strCorner;
			info = info + "\n Vertice: x=" + cr.vert.x + ", y=" + cr.vert.y + ", z=" + cr.vert.z;
			if (cr.txtr != null) {
				info = info + "\n Textura: x=" + cr.txtr.x + ", y=" + cr.txtr.y + ", z=" + cr.txtr.z;
			}
			if (cr.norm != null) {
				info = info + "\n Normal:  x=" + cr.norm.x + ", y=" + cr.norm.y + ", z=" + cr.norm.z;
			}
		}

		return info;
	}
}
