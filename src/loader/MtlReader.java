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
import java.util.Iterator;

/**
 *
 * @author gmendez
 */
public class MtlReader {

	private HashMap<String, Material> mtls;
	private Object3d o;
	private Material m;

	private File file;
	private FileReader fr;
	private BufferedReader br = null;
	private String line;

	private String filenameMtl = null;
	final private String delims = "\\s+";
	private String[] tokens;
	private int index = 1;
	private String mtlName;

	private int ln = 0;

	public MtlReader(String filename) {
		filenameMtl = filename;
	}

	public HashMap<String, Material> Load(String filenameMtl) throws IOException {
		this.filenameMtl = filenameMtl;
		return Load();
	}

	public HashMap<String, Material> Load() throws IOException {

		file = new File(filenameMtl);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		mtls = new HashMap<>();

		String baseDir = file.getParent();

		while ((line = br.readLine()) != null) {
			++ln;
			if (line.trim().startsWith("#") || line.trim().isEmpty()) {
				continue;
			}

			tokens = line.split(delims);

			switch (tokens[0]) {
				case "newmtl":
					mtlName = tokens[1];
					m = new Material(mtlName);
					mtls.put(mtlName, m);
					m.baseDir = baseDir;
					break;
				case "Ns":
					m.putShininess(tokens[1]);
					break;
				case "Ka":
					m.putAmbient(tokens[1], tokens[2], tokens[3]);
					break;
				case "Kd":
					m.putDiffuse(tokens[1], tokens[2], tokens[3]);
					break;
				case "Ks":
					m.putSpecular(tokens[1], tokens[2], tokens[3]);
					break;
				case "Tr":
					m.putTransparency(tokens[1]);
					break;
				case "d":
					m.putTransparency(tokens[1]);
					break;
				case "illum":
					m.putIlluminationMode(tokens[1]);
					break;
				case "map_Ka":
					m.putTxtrMapAmbient(tokens[1]);
					break;
				case "map_Kd":
					m.putTxtrMapDiffuse(tokens[1]);
					break;
				default:

			}
		}

		br.close();
		fr.close();
		return mtls;
	}

	public void printMtls() {
		Iterator itr = mtls.entrySet().iterator();
		while (itr.hasNext()) {
			Object mtl = itr.next();
			System.out.println(mtl);
		}

	}

}
