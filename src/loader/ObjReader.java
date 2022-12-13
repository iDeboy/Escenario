/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author gmendez
 */
public class ObjReader {

	private HashMap<String, Material> mtls;
	private Object3d obj;

	private File file;
	private FileReader fr;
	private BufferedReader br = null;
	private String line;

	private String filenameObj = null;
	private String filenameMtl = null;
	private String baseDir = "";
	final private String delims = "\\s+";
	private String[] tokens;

	public ObjReader() {

	}

	public ObjReader(String filename) {
		filenameObj = filename;
	}

	public Object3d Load(String filenameObj) throws IOException {
		String material = "";
		String groupname = "";
		int ln = 0;
		int estado = 0;

		obj = null;

		this.filenameObj = filenameObj;

		file = new File(filenameObj);
		fr = new FileReader(file);
		br = new BufferedReader(fr);

		baseDir = file.getParent();

		while ((line = br.readLine()) != null) {
			++ln;
			if (line.trim().startsWith("#") || line.trim().isEmpty()) {
				continue;
			}

			if (estado == 0 && line.trim().toLowerCase().startsWith("v")) {

				if (obj == null) { // Supone no cuenta con definición de materiales                           
					obj = new Object3d("objeto");
				}
				estado = 1;
			}
			tokens = line.split(delims);
			try {
				switch (tokens[0]) {
					case "mtllib":
						filenameMtl = tokens[1];
						MtlReader mtlRdr = new MtlReader(baseDir + "/" + filenameMtl);

						if (obj == null) {
							obj = new Object3d();
						}
						obj.addMateriales(mtlRdr.Load());
						break;

					case "o": // Objeto
						if (obj == null) { // Supone no cuenta con definición de materiales                           
							obj = new Object3d(tokens[1]);
						} else {
							obj.setType(tokens[1]);
						}

						break;

					case "g": // Grupo
						groupname = tokens[1];
						break;

					case "v":  // Vertices                           

						obj.addVertice(tokens[1], tokens[2], tokens[3]);
						break;

					case "vn": // Vertices normales
						obj.addNormal(tokens[1], tokens[2], tokens[3]);
						break;

					case "vt": // Vertices texturas
						obj.addVertTex(tokens[1], tokens[2]);
						break;

					case "s":  // Smooth Shading
						obj.addSmoothShading(tokens[1]);
						break;

					case "usemtl":  // Uso de material
						material = tokens[1];
						break;

					case "f":  // Caras
						obj.addFace(tokens, material, groupname);
						estado = 2;
						break;

					case "l":  // Linea
						obj.addLine(tokens, material, groupname);
						break;

					case "p":  // Puntos
						obj.addPoint(tokens, material, groupname);
						break;

					default:
						break;
				}
			} catch (Exception e) {
				System.out.println("Error en carga de archivo, linea: " + ln + ", contenido: " + line);
				System.out.println(" eroor  ");
			}
		}

		br.close();
		fr.close();
		System.out.println("# Lineas procesadas: " + ln);

		return obj;
	}

	public void printObjs() {
		System.out.println(obj);

	}

}
