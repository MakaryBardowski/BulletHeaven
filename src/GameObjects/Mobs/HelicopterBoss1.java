/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import EntityControls.DeadBodyControl;
import EntityControls.EntityManager;
import EntityControls.HelicopterBossControl;
import GUILayer.MinimapMobIndicatorControl;
import GameStates.GameState;
import Items.Item;
import Items.ItemInterface;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import MapBuilder.WorldGrid;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class HelicopterBoss1 extends Mob {

    private Node visualNode;
    private float hpEngineR;
    private float hpEngineL;
    private Node engineR;
    private Node engineL;

    public HelicopterBoss1(Node node, AnimControl aCtrl, AnimChannel ACh,Geometry[] bodyPartsForLighting, Node deadNode, String mobName, Allegiance allegiance, float health, float armor, float baseSpeed, Node visualNode) {
        super(node, aCtrl, ACh, health, armor, baseSpeed,bodyPartsForLighting, deadNode, mobName, allegiance);
        this.visualNode = visualNode;
        hpEngineR = health / 3;
        hpEngineL = health / 3;

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
            if (headshotType.charAt(0) == 'e') {
                d *= 1.5;
            }

            if (headshotType.charAt(headshotType.length() - 1) == 'L') {
                hpEngineL -= d;
                if (hpEngineL < 0) {
                    engineL.removeFromParent();
                    engineL = (Node) gs.getAssetManager().loadModel("Models/helicopterBossEngineBrokenL/helicopterBossEngineBrokenL.j3o");

                    LightingMethods.setLightingMaterial(engineL, "Textures/assetTextures/bossEngineL.png", LIGHT_MAGNITUDE * 2.5f, gs.getAssetManager());
                    visualNode.attachChild(engineL);

                    engineL.move(0.55f, 0.4f, 0.19f);
                    attachFire(engineL, gs);
                gs.chatPutMessage("\\#00d615#The " + getMobName() + " \\#00d615#left \\#00d615#engine \\#e00000#explodes!");

                }
            } else if (headshotType.charAt(headshotType.length() - 1) == 'R') {
                hpEngineR -= d;
                if (hpEngineR < 0) {
                    engineR.removeFromParent();
                    engineR = (Node) gs.getAssetManager().loadModel("Models/helicopterBossEngineBrokenR/helicopterBossEngineBrokenR.j3o");

                    LightingMethods.setLightingMaterial(engineR, "Textures/assetTextures/bossEngineL.png", LIGHT_MAGNITUDE * 2.5f, gs.getAssetManager());
                    visualNode.attachChild(engineR);

                    engineR.move(-0.7f, 0.4f, 0.2f);
                    attachFire(engineR, gs);
                gs.chatPutMessage("\\#00d615#The " + getMobName() + " \\#00d615#right \\#00d615#engine \\#e00000#explodes!");

                }
            }

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

    public static HelicopterBoss1 spawnBoss(GameState gs) {
        float scale = 4; //7
        float health = 300;
        float armor = 3;
        float speed = 4;

        Node mainhelicopterPositionNode = new Node("HelicopterPositionNode");
        Node mainHelicopterVisualNode = (Node) (gs.getAssetManager().loadModel("Models/helicopterBoss/helicopterBoss.j3o"));
        mainHelicopterVisualNode.setName("HelicopterNode");
        AnimControl animControl = mainHelicopterVisualNode.getChild("Armature").getControl(AnimControl.class);
        AnimChannel animChannel = animControl.createChannel();
        animChannel.setAnim("Idle");
//   AnimControl animControl = null;
//        AnimChannel animChannel = null;
//gs.getPlayer().setHealth(0);

        animChannel.setSpeed(5);
        HelicopterBoss1 hb = new HelicopterBoss1(mainhelicopterPositionNode, animControl, animChannel, null, new Node(), "\\#00d9ff#HelikopterBoss1", Allegiance.HOSTILE, health, armor, speed, mainHelicopterVisualNode);
        LightingMethods.setLightingMaterial(mainHelicopterVisualNode, "Textures/assetTextures/bossHelicopter.png", LIGHT_MAGNITUDE * 2.5f, gs.getAssetManager());
      
        gs.getMapNode().attachChild(mainhelicopterPositionNode);
        mainhelicopterPositionNode.attachChild(mainHelicopterVisualNode);
        mainhelicopterPositionNode.scale(scale);
        mainhelicopterPositionNode.addControl(new HelicopterBossControl(hb, gs));
        gs.getEntityManager().getEnemiesAlive().put(mainhelicopterPositionNode.getName(), hb);

        gs.chatPutMessage("The boss spawns!"); // doesn appear because nifty is created after the log command

        hb.engineR = (Node) gs.getAssetManager().loadModel("Models/helicopterBossEngineR/helicopterBossEngineR.j3o");
        LightingMethods.setLightingMaterial(hb.engineR, "Textures/assetTextures/bossEngineR.png", LIGHT_MAGNITUDE * 2.5f, gs.getAssetManager());
        hb.engineL = (Node) gs.getAssetManager().loadModel("Models/helicopterBossEngineL/helicopterBossEngineL.j3o");
        LightingMethods.setLightingMaterial(hb.engineL, "Textures/assetTextures/bossEngineL.png", LIGHT_MAGNITUDE * 2.5f, gs.getAssetManager());

        mainHelicopterVisualNode.attachChild(hb.engineR);
        mainHelicopterVisualNode.attachChild(hb.engineL);
        hb.engineR.move(-0.7f, 0.4f, 0.2f);
        hb.engineL.move(0.55f, 0.4f, 0.19f);
        hb.engineR.setName("headHitbox");
        hb.engineL.setName("headHitbox");

        System.out.println(mainHelicopterVisualNode);
        System.out.println(mainHelicopterVisualNode.getChildren());
        
        
        
        
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
       ;
//        mainhelicopterPositionNode.setLocalTranslation(gs.getPlayer().getPositionNode().getWorldTranslation());
        return hb;
    }

    private ParticleEmitter attachFire(Node node, GameState gs) {
        ParticleEmitter fire = new ParticleEmitter("Emitter", Type.Triangle, 30);
        Material mat_red = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", gs.getAssetManager().loadTexture("Textures/effects/fireParticle.png"));
        fire.setMaterial(mat_red);
//        fire.setImagesX(2);
//        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(2f); // 1.5 
        fire.setEndSize(0.1f); // 0.1
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        node.attachChild(fire);
        return fire;
    }

    public Node getVisualNode() {
        return visualNode;
    }

    public void setVisualNode(Node visualNode) {
        this.visualNode = visualNode;
    }

    public float getHpEngineR() {
        return hpEngineR;
    }

    public void setHpEngineR(float hpEngineR) {
        this.hpEngineR = hpEngineR;
    }

    public float getHpEngineL() {
        return hpEngineL;
    }

    public void setHpEngineL(float hpEngineL) {
        this.hpEngineL = hpEngineL;
    }

    public Node getEngineR() {
        return engineR;
    }

    public void setEngineR(Node engineR) {
        this.engineR = engineR;
    }

    public Node getEngineL() {
        return engineL;
    }

    public void setEngineL(Node engineL) {
        this.engineL = engineL;
    }
    
    

}
