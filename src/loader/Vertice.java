/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 *
 * @author gmendez
 */
public class Vertice {
    
    public float x;
    public float y;
    public float z;
    float h;
    float vf[]; 
    FloatBuffer fb;
    
    public Vertice(){
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        h = 0.0f;
    }
    
    public Vertice(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        h = 0.0f;
        vf = new float[]{x,y,z};
        
        fb = this.toFB(); // Convierte a FloatBuffer
    }

    public Vertice(float vertice[]) throws IndexOutOfBoundsException {        
        vf=vertice;
        
        x=vertice[0];
        y=vertice[1];
        z=vertice[2];        
        
        fb = this.toFB(); // Convierte a FloatBuffer
    }
    
    public FloatBuffer toFB(){
        ByteBuffer bb = ByteBuffer.allocateDirect(vf.length*4);
        bb.order(ByteOrder.nativeOrder());
        
        FloatBuffer tfb;
        tfb = bb.asFloatBuffer();
        tfb.put(vf);
        tfb.position(0);
        
        return tfb;        
    }
    
    public FloatBuffer getVertex3fv(){
        return fb;
    }
    
    public String toString(){
        String info="";
        
        info = info + "x="+x+", y="+y+", z="+z;
        
        return info;
    }
}
