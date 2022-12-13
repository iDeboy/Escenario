/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loader;


/**
 *
 * @author gmendez
 */
public class Corner {
    String delims="/";
    
    int idxVertice=-1;
    int idxTexture=-1;
    int idxNormal=-1;
    
    public Vertice vert=null;
    public Vertice txtr=null;
    public Vertice norm=null;
    
    String  strCorner;
    
    public Corner(String strCorner , Object3d parent){
         
         String items[] = strCorner.split("/");
         
         this.strCorner = strCorner;
                           
         if (items.length>2 && !items[2].isEmpty()){
             idxNormal= Integer.parseInt(items[2]);
             norm = parent.vnrms.get(idxNormal-1);
         }

         if (items.length>1 && !items[1].isEmpty()){             
             idxTexture= Integer.parseInt(items[1]);
             txtr = parent.vtxtr.get(idxTexture-1);
         }

         if (items.length>0 && !items[0].isEmpty()){
             idxVertice= Integer.parseInt(items[0]);
             vert = parent.verts.get(idxVertice-1);
         }                                            
              
    }
    
}
