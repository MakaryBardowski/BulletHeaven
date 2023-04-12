/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import EntityControls.DeadBodyControl;
import EntityControls.EntityManager;
import EntityControls.HelicopterBossControl;
import EntityControls.HumanoidControl;
import GUILayer.MinimapMobIndicatorControl;
import GameStates.GameState;
import Items.Item;
import Items.ItemInterface;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import MapBuilder.WorldGrid;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class Mantis extends Mob {


    public Mantis(Node node, AnimControl aCtrl, AnimChannel ACh,Geometry[] bodyPartsForLighting, Node deadNode, String mobName, Allegiance allegiance, float health, float armor, float baseSpeed, Node visualNode) {
        super(node, aCtrl, ACh, health, armor, baseSpeed,bodyPartsForLighting, deadNode, mobName, allegiance);


    }

 @Override
    public void insert(WorldGrid wg) {
        
           Vector3f min = new Vector3f(
            getNode().getWorldTranslation().getX() - (getRadius()),0,
             getNode().getWorldTranslation().getZ() - (getRadius()));   

        Vector3f max = new Vector3f(
            getNode().getWorldTranslation().getX() + (getRadius()),0,
             getNode().getWorldTranslation().getZ() + (getRadius()));   
        
        


       if(  wg.getContents().get( wg.hash(min))  == null  ){
            wg.getContents().put( wg.hash(min) , new HashSet<CollidableInterface>());
        }else{

            wg.getContents().get( wg.hash(min)).add(this);

        }
       
       if(  wg.getContents().get( wg.hash(max))  == null  ){
            wg.getContents().put( wg.hash(max) , new HashSet<CollidableInterface>());
        }else{
            wg.getContents().get( wg.hash(max)).add(this);

        }
       
     Vector3f newMin = new Vector3f(min.getX(),0,max.getZ());
       
       if(  wg.getContents().get( wg.hash(newMin))  == null  ){
            wg.getContents().put( wg.hash(newMin) , new HashSet<CollidableInterface>());
        }else{
            wg.getContents().get( wg.hash(newMin)).add(this);

        }
       
              newMin = new Vector3f(max.getX(),0,min.getZ());

       
       if(  wg.getContents().get( wg.hash(newMin))  == null  ){
            wg.getContents().put( wg.hash(newMin) , new HashSet<CollidableInterface>());
        }else{
            wg.getContents().get( wg.hash(newMin)).add(this);

        }

        
        
    }

  @Override
    public void removeFromGrid(WorldGrid wg) {
          Vector3f min = new Vector3f(
        getNode().getWorldTranslation().getX() - (getRadius()),0,
        getNode().getWorldTranslation().getZ() - (getRadius()));   

        Vector3f max = new Vector3f(
        getNode().getWorldTranslation().getX() + (getRadius()),0,
        getNode().getWorldTranslation().getZ() + (getRadius()));   
       
        HashSet<CollidableInterface> ct = wg.getContents().get(wg.hash(min));
        if(ct != null){
        ct.remove(this);
        }
        ct = wg.getContents().get(wg.hash(max));
        
        if(ct != null){
        ct.remove(this);
        }
        
        Vector3f newMin = new Vector3f(min.getX(),0,max.getZ());
        ct = wg.getContents().get(wg.hash(newMin));
        
        if(ct != null){
        ct.remove(this);

        }
        

               
        newMin = new Vector3f(max.getX(),0,min.getZ());
        
        
        ct = wg.getContents().get(wg.hash(newMin));
        
        if(ct != null){
        ct.remove(this);
        }
    }

    @Override
    public HashSet<CollidableInterface> getFromCellsImIn(WorldGrid wg) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (getRadius()), 0,
                getNode().getWorldTranslation().getZ() - (getRadius()));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (getRadius()), 0,
                getNode().getWorldTranslation().getZ() + (getRadius()));

        min.setX(wg.getCellSize() * (int) (Math.floor(min.getX() / wg.getCellSize())));
        min.setZ(wg.getCellSize() * (int) (Math.floor(min.getZ() / wg.getCellSize())));

        max.setX(wg.getCellSize() * (int) (Math.floor(max.getX() / wg.getCellSize())));
        max.setZ(wg.getCellSize() * (int) (Math.floor(max.getZ() / wg.getCellSize())));
        Vector3f pom = new Vector3f();

        for (int x = (int) min.x; x <= max.x; x += wg.getCellSize()) {
            for (int z = (int) min.z; z <= max.z; z += wg.getCellSize()) {
                pom.set(x, 0, z);
                if (wg.getContents().get(wg.hash(pom)) != null) {
                    output.addAll(wg.getContents().get(wg.hash(new Vector3f(x, 0, z))));
                }

            }
        }

        return output;
    }

    @Override
    public HashSet<CollidableInterface> getEntitiesFromTilesInRange(WorldGrid wg, float distance) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (distance), 0,
                getNode().getWorldTranslation().getZ() - (distance));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (distance), 0,
                getNode().getWorldTranslation().getZ() + (distance));

        min.setX(wg.getCellSize() * (int) (Math.floor(min.getX() / wg.getCellSize())));
        min.setZ(wg.getCellSize() * (int) (Math.floor(min.getZ() / wg.getCellSize())));

        max.setX(wg.getCellSize() * (int) (Math.floor(max.getX() / wg.getCellSize())));
        max.setZ(wg.getCellSize() * (int) (Math.floor(max.getZ() / wg.getCellSize())));
        Vector3f pom = new Vector3f();

        for (int x = (int) min.x; x <= max.x; x += wg.getCellSize()) {
            for (int z = (int) min.z; z <= max.z; z += wg.getCellSize()) {
                pom.set(x, 0, z);
                if (wg.getContents().get(wg.hash(pom)) != null) {
                    output.addAll(wg.getContents().get(wg.hash(new Vector3f(x, 0, z))));
                }

            }
        }

        return output;
    }

    @Override
    public void checkCollision() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCollision() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItemInterface equipItem(ItemInterface i, boolean rightOrLeftClick, GameState gs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveDamage(float d, GameState gs, EntityManager e, String headshotType) {

        if (!isDead()) {
           

           

            d = d - getArmor();
            if (d < 0) {
                gs.chatPutMessage("\\#e00000#Your attack proves ineffective!");

            } else {
                setHealth(getHealth() - (d));
                gs.chatPutMessage("The " + getMobName() + " \\#ffffff#takes " + "\\#ffb300#" + d + " \\#ffffff#damage!");

            }

            if (getHealth() <= 0) {
                die(gs, e);
            }
        }

    }

    @Override
    public void dealDamage(MobInterface target, float damage, GameState gs, EntityManager e, String metadata) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void die(GameState gs, EntityManager e) {
        Random r = new Random();

        int msg = r.nextInt(3);

        switch (msg) {
            case 0:
                gs.chatPutMessage("\\#00d615#Going down!");
                break;
            case 1:
                gs.chatPutMessage("\\#00d615#Losing control!");
                break;
            default:
                gs.chatPutMessage("\\#00d615#Mayday!");
                break;
        }

//        getDeadNode().attachChild(getNode());
//        getDeadNode().attachChild(getBloodPuddleNode());
//        getBloodPuddleNode().move(getNode().getWorldTranslation());
//        getBloodPuddleNode().rotate(0, r.nextInt(2*3), 0);
//        e.getEnemiesAlive().remove(getNode().getName());
//        getNode().addControl(new DeadBodyControl(this, 1));
    }

    @Override
    public boolean isDead() {

        return getHealth() <= 0;
    }

    public static Mantis spawnMantis(GameState gs) {
        float scale = 1.2f; //7
        float health = 300;
        float armor = 3;
        float speed = 4;

        Node mantisNode = (Node) (gs.getAssetManager().loadModel("Models/mantis/mantis.j3o"));
        mantisNode.setName("MantisNode");
    
        AnimControl animControl = mantisNode.getChild("Armature").getControl(AnimControl.class);
        AnimChannel torso = animControl.createChannel();
        AnimChannel hands = animControl.createChannel();


        torso.addFromRootBone("WingR");
        torso.addFromRootBone("WingL");
//        torso.addFromRootBone("Torso");
        torso.addFromRootBone("Head");

        torso.setAnim("Fly");
        torso.setSpeed(5);
        torso.setLoopMode(LoopMode.Cycle);



        // left hand will pull
//        hands.addFromRootBone("Torso");
        hands.addFromRootBone("HandR");
                hands.addFromRootBone("HandL");

        hands.setAnim("Attack");
        hands.setSpeed(1f);




AnimChannel animChannel = animControl.createChannel();

        
        
//   AnimControl animControl = null;
//        AnimChannel animChannel = null;
//gs.getPlayer().setHealth(0);

        Mantis hb = new Mantis(mantisNode, animControl, animChannel, null, new Node(), "Mutated Mantis", Allegiance.HOSTILE, health, armor, speed, mantisNode);
        LightingMethods.setLightingMaterial(mantisNode, "Models/mantis/Mantis_Tex.png", 0.3f, gs.getAssetManager());
      
        gs.getMapNode().attachChild(mantisNode);
        mantisNode.attachChild(mantisNode);
        mantisNode.scale(scale);

        gs.getEntityManager().getEnemiesAlive().put(mantisNode.getName(), hb);

        gs.chatPutMessage("The boss spawns!"); // doesn appear because nifty is created after the log command


        SkeletonControl newObjectSkeletonControl = mantisNode.getChild("Armature").getControl(SkeletonControl.class);

   Box bodyHitbox = new Box(0.3f, 0.5f, 0.4f);
        Spatial bodyHitboxSpat = new Geometry("bodyHitbox", bodyHitbox);
        Material bodyHitboxMat = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        bodyHitboxMat.getAdditionalRenderState().setWireframe(true);
bodyHitboxSpat.move(0,1,0);
  
        bodyHitboxMat.getAdditionalRenderState().setLineWidth(4);
        bodyHitboxMat.setColor("Color", ColorRGBA.Green);
        bodyHitboxSpat.setMaterial(bodyHitboxMat);
        mantisNode.attachChild(bodyHitboxSpat);
        newObjectSkeletonControl.getAttachmentsNode("Torso").attachChild(bodyHitboxSpat);
        bodyHitboxSpat.move(0, -0.5f, 0); // offsetting it 



        
        
          Node playerNode = new Node("player indicator node");
        Quad boardShape = new Quad(10.0f, 10.0f);
        Geometry board = new Geometry("Board", boardShape);


        Material material = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");

        material.setTexture("ColorMap", gs.getAssetManager().loadTexture("Textures/GUI/mobIndicator.png"));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
        material.setColor("Color", new ColorRGBA(1, 1, 1, .7f));

        board.setMaterial(material);

        playerNode.attachChild(board);
        float size = 0.004f*gs.getMinimapPxSize();
        board.scale(size);
        gs.getGuiNode().attachChild(playerNode);
        board.move(gs.getMinimapXoffset() - 4 * size, -4 * size, 0); // -8 cuz the image is 32x32
        
        
        
        
        hb.getNode().addControl(new MinimapMobIndicatorControl(playerNode,hb,gs.getWorldGrid().getCellSize(),gs.getMinimapMovementRatio()));
       
        mantisNode.setLocalTranslation(gs.getPlayer().getPositionNode().getWorldTranslation());
        
        gs.getWorldGrid().insert(hb);
        return hb;
    }


    
    

}
