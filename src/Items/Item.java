/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import EntityControls.EntityManager;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Arrays;

/**
 *
 * @author 48793
 */
public abstract class Item {
    private static long ID = 0;
    private Node node;
    private String texturePath; // on player
    private String name; // on player
    private String iconPath; // icon
    private String dropPath; // on ground
    private String dropTexturePath; // on ground
    private String mobTexturePath; // on mob
    private String mobPath; // on mob
    private boolean droppable;
    private boolean isFalling;
    private boolean goesUp;
    private float[] flyingDirection = new float[3];
    
    
    public Item(){
    }

    public Item(Node node,String name,String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable) {
        this.node = node;
        this.texturePath = texturePath;
        this.iconPath = iconPath;
        this.name = name;
        this.droppable = droppable;
        this.dropPath = dropPath;
        this.dropTexturePath =dropTexturePath;
  this.mobTexturePath = mobTexturePath;
  this.mobPath=mobPath;
    }

    
    
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public boolean getIsFalling() {
        return isFalling;
    }

    public void setIsFalling(boolean isFalling) {
        this.isFalling = isFalling;
    }

    public boolean isGoesUp() {
        return goesUp;
    }

    public void setGoesUp(boolean goesUp) {
        this.goesUp = goesUp;
    }

    public float[] getFlyingDirection() {
        return flyingDirection;
    }

    public void setFlyingDirection(float[] flyingDirection) {
        this.flyingDirection = flyingDirection;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getDropPath() {
        return dropPath;
    }

    public void setDropPath(String dropPath) {
        this.dropPath = dropPath;
    }

    public String getDropTexturePath() {
        return dropTexturePath;
    }

    public void setDropTexturePath(String dropTexturePath) {
        this.dropTexturePath = dropTexturePath;
    }

    public String getMobTexturePath() {
        return mobTexturePath;
    }

    public void setMobTexturePath(String mobTexturePath) {
        this.mobTexturePath = mobTexturePath;
    }

    public String getMobPath() {
        return mobPath;
    }

    public void setMobPath(String mobPath) {
        this.mobPath = mobPath;
    }
    
    

    public void drop(Vector3f dropLocation,GameState gs,EntityManager e){
       Node itemNode =null;
     System.out.println("dropie: "+ dropPath);
       
    itemNode = (Node) gs.getAssetManager().loadModel(getDropPath());
    itemNode.getChild(0).setName("ITEM"+ID);

           LightingMethods.setLightingMaterial(itemNode, getDropTexturePath(), LIGHT_MAGNITUDE,gs.getAssetManager());

        
    ID++;
    setNode(itemNode);
    itemNode.move(0,1,0);
    setIsFalling(true);
    setGoesUp(true);
    
    setFlyingDirection(new float[4]);
    
  
    
    getFlyingDirection()[0] = (float)(-3 + Math.random() * (3 +3));
    getFlyingDirection()[1] = 5;
    getFlyingDirection()[2] = (float)(-3 + Math.random() * (3 +3));
    getFlyingDirection()[3] = (float)(-30 + Math.random() * (30 +30));

    
    itemNode.move(dropLocation);
    e.getPickableItems().putIfAbsent(itemNode.getChild(0).getName(), this);
    gs.getPickableNode().attachChild(itemNode);
  
  
//    updateStatsOnUnequip(actor,item);
    
    }
    
    
}
