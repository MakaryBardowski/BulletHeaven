/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapBuilder;

import GameObjects.Mobs.CollidableInterface;
import GameStates.GameState;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author 48793
 */
public class WorldGrid {
 
    private final int cellSize;
    private HashMap<String,HashSet<CollidableInterface>> contents;
    
    
    public WorldGrid(int cellSize){
    this.cellSize = cellSize;
    contents = new HashMap();
    }
    
    public int getCellSize(){
        return cellSize;
    }
    
    public String hash(Vector3f point){
    int[] loc = new int[2];
    loc[0] = cellSize*(int)(Math.floor(point.getX()/cellSize));


    loc[1] = cellSize*(int)(Math.floor(point.getZ()/cellSize));

    return loc[0]+"."+loc[1];

    }
    
    
    public void insert(CollidableInterface object){
        object.insert(this);
    }
    
    public HashSet<CollidableInterface> getNearby(CollidableInterface object,float radius){
        return object.getFromCellsImIn(this);
    }
    
    

  
    
    
    public HashSet<CollidableInterface> getInsideCell(Vector3f vec){
        HashSet<CollidableInterface> output = new HashSet<>();
        if(contents.get( hash(vec)) != null){
    output.addAll( contents.get( hash(vec)) );
        }
        return output;
    }
    
   
    
    public HashMap<String,HashSet<CollidableInterface>> getContents(){
    return contents;
    }
    
//    public void remove(CollidableInterface object){
//      object.remove(this);
//    }
    

    
    
     public  void addDebugTile(GameState gs,Vector3f pos){
          Material allowedMaterial = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    allowedMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
    allowedMaterial.setColor("Color", new ColorRGBA(0, 1, 0, .4f));

    Node node = (Node) gs.getAssetManager().loadModel("Models/accessiblePlane/accessiblePlane.j3o");
    node.setMaterial(allowedMaterial);
    gs.getMapNode().attachChild(node);
    node.scale(cellSize);
    String[] parts =  hash(pos).split("\\.");
    
    node.move(Integer.parseInt(parts[0])+(float)cellSize/2,0.1f,Integer.parseInt(parts[1])+(float)cellSize/2);
   gs.getDebugNode().attachChild(node);
     }
    
}
    
