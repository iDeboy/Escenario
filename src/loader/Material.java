/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import java.util.ArrayList;
import java.util.Iterator;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author gmendez
 */
public class Material {
    
    String name;
    float  shininess=0.0f;   // Factor de Brillo
    float  ambient[];
    float  diffuse[];
    float  specular[];
    float  transparency;
    int    illuminationMode=0;
    int    gl_IlluminationMode=0;
    String txtrMapAmbient="";
    String txtrMapDiffuse="";
    boolean isTextureA=false;
    boolean isTextureD=false;
    
    ArrayList<Texture> txtrs = new ArrayList<>();;
    String baseDir="";
        
            
    public Material(String name) {
        this.name = name;
        this.ambient = new float[]{0f, 0f, 0f,1f};
        this.diffuse = new float[]{0f, 0f, 0f,1f};
        this.specular = new float[]{0f, 0f, 0f,1f};
        this.shininess = 0.0f;
        this.illuminationMode = 0;
        this.isTextureD = false;
        this.isTextureA = false;
        
        /* Significado del valor en Illumination Mode        
        0. Color on and Ambient off
        1. Color on and Ambient on
        2. Highlight on
        3. Reflection on and Ray trace on
        4. Transparency: Glass on, Reflection: Ray trace on
        5. Reflection: Fresnel on and Ray trace on
        6. Transparency: Refraction on, Reflection: Fresnel off and Ray trace on
        7. Transparency: Refraction on, Reflection: Fresnel on and Ray trace on
        8. Reflection on and Ray trace off
        9. Transparency: Glass on, Reflection: Ray trace off
        10. Casts shadows onto invisible surfaces
        */        
    }
    
    @Override
    public boolean equals(Object o) {
      if (o instanceof Material) {
        Material m = (Material)o;
        return this.name.equals(m.name);
      } else {
        return false;
      }
    }    
    
    @Override
    public int hashCode() {
        return illuminationMode * this.name.length();
    }

    public void putShininess(String factor){
        shininess = Float.parseFloat(factor);
    }

    public void putShininess(float factor){
        shininess = factor;
    }

    public void putAmbient(String r, String g, String b){
        this.ambient[0]=Float.parseFloat(r);
        this.ambient[1]=Float.parseFloat(g);
        this.ambient[2]=Float.parseFloat(b);        
    }

    public void putAmbient(float r, float g, float b){
        this.ambient[0]=r;
        this.ambient[1]=g;
        this.ambient[2]=b;
    }    

    public void putDiffuse(String r, String g, String b){
        this.diffuse[0]=Float.parseFloat(r);
        this.diffuse[1]=Float.parseFloat(g);
        this.diffuse[2]=Float.parseFloat(b);
    }

    public void putDiffuse(float r, float g, float b){
        this.diffuse[0]=r;
        this.diffuse[1]=g;
        this.diffuse[2]=b;
    }        
    
    public void putSpecular(String r, String g, String b){
        this.specular[0]=Float.parseFloat(r);
        this.specular[1]=Float.parseFloat(g);
        this.specular[2]=Float.parseFloat(b);
    }

    public void putSpecular(float r, float g, float b){
        this.specular[0]=r;
        this.specular[1]=g;
        this.specular[2]=b;
    }   

    public void putTransparency(String f){
        this.transparency=Float.parseFloat(f);
    }

    public void putTransparency(float f){
        this.transparency=f;
    }       
    
    public void putIlluminationMode(String i){
        this.illuminationMode=Integer.parseInt(i);
    }

    public void putIlluminationMode(int i){
        this.illuminationMode=i;
    }       
    
    
    public String toString() {
        String info="";        
        info = "(" + this.name + "):\n"+
                "  Shininess   : "+this.shininess+"\n"+
                "  Ambient     : "+this.ambient[0]+", "+this.ambient[1]+", "+this.ambient[2]+", "+this.ambient[3]+"\n"+
                "  Diffuse     : "+this.diffuse[0]+", "+this.diffuse[1]+", "+this.diffuse[2]+", "+this.diffuse[3]+"\n"+
                "  Specular    : "+this.specular[0]+", "+this.specular[1]+", "+this.specular[2]+", "+this.specular[3]+"\n"+
                "  Transparency: "+this.transparency+"\n"+
                "  Illumination: "+this.illuminationMode;

        return info;
    }

    void putTxtrMapAmbient(String imageFile) {
        this.txtrMapAmbient=imageFile;
        this.isTextureA = true;
    }

    void putTxtrMapDiffuse(String imageFile) {
        String typeImg = imageFile.substring(imageFile.lastIndexOf(".")+1).toLowerCase();
                    
        this.txtrMapDiffuse=imageFile;                       
        this.isTextureD = true;
        
        try {                                                
            
            BufferedImage buffImage = ImageIO.read(new File(baseDir+"/"+imageFile));           
            
            Texture txtr;
            txtr = AWTTextureIO.newTexture(GLProfile.getDefault(),buffImage,false);
            
            txtrs.add(txtr);

        } catch (IOException ioex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ioex);
            this.isTextureD = false;
        } catch (GLException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
            this.isTextureD = false;
        }
        
    }

}
