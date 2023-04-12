/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import Items.Weapon;
import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.DeadBodyControl;
import EntityControls.EntityManager;
import EntityControls.PlayerMuzzleFlashControl;
import Items.Item;
import GameStates.GameState;
import Items.ItemCreator;
import Items.Ranged.RangedWeapon;
import Items.Ranged.Shotgun;
import Items.WeaponInterface;
import Map.Generator.TileType;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import MapBuilder.MapBuilder;
import MapBuilder.MapInitializer;
import static MapBuilder.MapInitializer.putShape;
import MapBuilder.WorldGrid;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class Player extends HumanMob {

    private Node positionNode;
    private Node handsNode;
    private Mob currentTarget;
    private Item[] hotbar;
    private boolean forward;
    private boolean backward;
    private boolean left;
    private boolean right;
    private Node rightHandNode = new Node();
    private Node leftHandNode = new Node();
    private AnimChannel rightHandAnimChannel, leftHandAnimChannel, handsAnimChannel;
    private AnimControl rightHandAnimControl, leftHandAnimControl, handsAnimControl;
    private AssetManager assetManager;
    private boolean shooting;
    private Node visualMuzzleNode; // used to attach muzzle flash, below the map
    private Node muzzleNode; // factual muzzle ndoe - attached to player

    
    private static final int DEFAULT_VISION_RANGE = 6;
    private static final float DEFAULT_VISION_INTENSITY = 0.16f;


    public Player(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed, Weapon equippedRightHand, Weapon equippedLefthand, Item helmet, Item torso, Item boots, Item gloves, Node[] bodyParts,Geometry[] modelElementsForShading, Node deadNode) {
        super(node, aCtrl, ACh, health, armor, baseSpeed, equippedRightHand, equippedLefthand, helmet, torso, boots, gloves, bodyParts, modelElementsForShading, deadNode, "Player", Allegiance.FRIENDLY);
        hotbar = new Item[10];
    }

    public Player(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed, Node[] bodyParts,Geometry[] modelElementsForShading, Node deadNode) {
        super(node, aCtrl, ACh, health, armor, baseSpeed, bodyParts,modelElementsForShading, deadNode, "Player", Allegiance.FRIENDLY);
        hotbar = new Item[10];

    }

    public Player(Node node, AnimControl aCtrl, AnimChannel ACh, Node[] bodyParts,Geometry[] modelElementsForShading, Node deadNode) {
        super(node, aCtrl, ACh, bodyParts,modelElementsForShading, deadNode, "Player", Allegiance.FRIENDLY);
        hotbar = new Item[10];

    }

    @Override
    public void die(GameState gs, EntityManager e) {
        dropEquipment(gs, e);
        Random r = new Random();

        getDeadNode().attachChild(getPositionNode());
        getDeadNode().attachChild(getBloodPuddleNode());
        getBloodPuddleNode().move(getPositionNode().getWorldTranslation());
        getBloodPuddleNode().rotate(0, r.nextInt(2 * 3), 0);
        e.getEnemiesAlive().remove(getNode().getName());
        getNode().addControl(new DeadBodyControl(this, 1));
    }

    public static Player initPlayer(AssetManager a, GameState gs) {
        Node newMobNode = new Node("playerMainNode");
        Node deadNode = gs.getDebugNode();
        Node head = new Node("player headNode");
        Node torso = new Node("player torsoNode");
     Node handR = new Node("player handRNode");
        Node handL = new Node("player handLNode");
             Node legR = new Node("player legRNode");
        Node legL = new Node("player legLNode");
        
        
        AnimControl animControl = null;
        AnimChannel animChannel = null;
        Node rightWeaponNode = new Node("LWeaponNode");
        Node leftWeaponNode = new Node("RweaponNode");

        Node handsNode = new Node("handsNode");
        handsNode.setCullHint(Spatial.CullHint.Never);
        
        Node[] bodyParts = new Node[]{head,torso,handR,handL,legR,legL};
        
        Geometry[] bodyPartsForLighting = new Geometry[8]; // 6 for 1x head, 1x torso ,2x hand ,2x leg , 1x weaponR , 1x weaponL
        bodyPartsForLighting[0] = null;
        bodyPartsForLighting[1] = null;
        bodyPartsForLighting[2] = null;
        bodyPartsForLighting[3] = null;
        bodyPartsForLighting[4] = null;
        bodyPartsForLighting[5] = null;
        bodyPartsForLighting[5] = null;
        
        
        Player p = new Player(newMobNode, animControl, animChannel, 12, 0, 12, bodyParts,bodyPartsForLighting, deadNode);
        
        
                Arrow arrow = new Arrow(Vector3f.UNIT_Z.mult(7f));
        putShape(p.getNode(), arrow, ColorRGBA.Red, gs).setLocalTranslation(new Vector3f(0,0,0)); //x - czerwony


        Node handsNodeAttachmentBone = (Node) gs.getAssetManager().loadModel("Models/handsNodeAttachmentBone/handsNodeAttachmentBone.j3o");
        SkeletonControl newObjectSkeletonControl = handsNodeAttachmentBone.getChild("Armature").getControl(SkeletonControl.class);
        AnimControl handsAControl = handsNodeAttachmentBone.getChild("Armature").getControl(AnimControl.class);
        AnimChannel handsAChannel = handsAControl.createChannel();
        p.handsAnimControl = handsAControl;
        p.handsAnimChannel = handsAChannel;
        newObjectSkeletonControl.getAttachmentsNode("HandsNodeAttachmentBone").attachChild(handsNode);
        p.handsAnimChannel.setAnim("Idle");
        p.handsAnimChannel.setSpeed(2.5f);

        p.setBloodPuddleNode((Node) a.loadModel("Models/bloodPuddle/bloodPuddle.j3o"));
        LightingMethods.setLightingMaterial(p.getBloodPuddleNode(), "Textures/assetTextures/UtilityTextures/bloodPuddle.png", 0.7f, a);

        p.setAssetManager(gs.getAssetManager());

        p.positionNode = new Node();
//        
              Arrow arrow1 = new Arrow(Vector3f.UNIT_Z.mult(10f));
        putShape(p.positionNode, arrow1, ColorRGBA.Cyan, gs).setLocalTranslation(new Vector3f(0,0,0)); //x - czerwony

        
        
        gs.getWorldNode().attachChild(handsNodeAttachmentBone);
        handsNode.move(0, -50f, 0);
        handsNode.scale(0.8f);
        p.setHandsNode(handsNode);

        p.positionNode.attachChild(p.getNode());
        gs.getWorldNode().attachChild(p.positionNode);

        handsNode.attachChild(p.rightHandNode);
        handsNode.attachChild(p.leftHandNode);

        ItemCreator i = new ItemCreator();
        float attackSpeed = 0.8f;
        p.getEquipment()[0] = i.createRocketLauncher(i.getJavelin(), attackSpeed, 75, 1.3f, true, 50);
        p.getEquipment()[1] = i.createFlamethrower(i.getFlamethrower(), 2f, 15, 0, true, 50); // 2f attack speed
        p.getEquipment()[2] = i.createSMG(i.getSmg1(), 1.5f, 25, 1.3f, true, 25);

//        p.getEquipment()[1] = i.createSMG("Models/pistolet/pistolet.j3o", "Textures/GUI/equipmentDEagle.png", "Textures/assetTextures/WeaponsPalette/DesertEagle.png",1);
        p.setRadius(0.7f);
        p.insert(gs.getWorldGrid());

//    PointLight myLight = new PointLight();
//    myLight.setColor(ColorRGBA.White.mult(DEFAULT_VISION_INTENSITY*0.2f));
//    myLight.setPosition(new Vector3f(0,2,0));
//    myLight.setRadius(DEFAULT_VISION_RANGE*1);
//gs.getWorldNode().addLight(myLight);
//LightControl lightControl = new LightControl(myLight);
//p.getPositionNode().addControl(lightControl); // this spatial controls the pos
//p.pl = myLight;
        return p;
    }

    public void move(float tpf, GameState gs) { // checks for WSAD input and moves if detected
//                    LightingMethods.deleteLightingInLocationLinearGradient(getPositionNode().getWorldTranslation(), 6, 0.16f, gs);

        if (forward || backward || left || right) {
            Vector3f ultimateMoveVec = new Vector3f(0, 0, 0);

            if (forward) {

                Vector3f moveVec = getNode().getLocalRotation().getRotationColumn(2).mult(getBaseSpeed() * tpf).clone();

                ultimateMoveVec.addLocal(moveVec);

            }
            if (backward) {

                Vector3f moveVec = getNode().getLocalRotation().getRotationColumn(2).mult(getBaseSpeed() / 2 * tpf).clone().negateLocal();

                ultimateMoveVec.addLocal(moveVec);

            }
            if (left) {

                Vector3f moveVec = gs.getCamc().getCamera().getLeft().mult(getBaseSpeed() / 2 * tpf);

                ultimateMoveVec.addLocal(moveVec);

            }
            if (right) {

                Vector3f moveVec = gs.getCamc().getCamera().getLeft().negateLocal().mult(getBaseSpeed() / 2 * tpf);;

                ultimateMoveVec.addLocal(moveVec);

            }

            removeFromGrid(gs.getWorldGrid());

            Vector3f UMC = ultimateMoveVec.clone();
            if (UMC.getX() < 0) {
                UMC.setX(UMC.getX() - 0.5f);
            }
            if (UMC.getX() > 0) {
                UMC.setX(UMC.getX() + 0.5f);
            }

            if (UMC.getZ() < 0) {
                UMC.setZ(UMC.getZ() - 0.5f);
            }
            if (UMC.getZ() > 0) {
                UMC.setZ(UMC.getZ() + 0.5f);
            }

            Vector3f locationAfterMovementInCell = new Vector3f((float) Math.floor(positionNode.getWorldTranslation().add(UMC.mult(1f)).getX() / gs.getWorldGrid().getCellSize()), 0, (float) Math.floor(positionNode.getWorldTranslation().add(UMC.mult(1f)).getZ() / gs.getWorldGrid().getCellSize()));

            if (locationAfterMovementInCell.getZ() < gs.getTileDataMap()[0][0].length && locationAfterMovementInCell.getZ() > -1 && gs.getTileDataMap()[0][(int) Math.floor(positionNode.getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) locationAfterMovementInCell.getZ()].getTileType() == TileType.FLOOR) {
                getPositionNode().move(0, 0, ultimateMoveVec.getZ());
            }
            if (locationAfterMovementInCell.getX() < gs.getTileDataMap()[0].length && locationAfterMovementInCell.getX() > -1 && gs.getTileDataMap()[0][(int) locationAfterMovementInCell.getX()][(int) Math.floor(positionNode.getWorldTranslation().getZ() / gs.getWorldGrid().getCellSize())].getTileType() == TileType.FLOOR) {
                getPositionNode().move(ultimateMoveVec.getX(), 0, 0);
            }
            
            

            insert(gs.getWorldGrid());
        }

    }

    public void shoot(float tpf, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode) {

        if (getEquippedRightHand() != null) {
            ((WeaponInterface) getEquippedRightHand()).shootAsPlayer(this, gs, c, em, nodeToCheckCollisionOn, debugNode, tpf);
        }

    }

    public void checkIfPlayerPicked(Node nodeToCollideWith, GameState gs) {
        CameraAndMouseControl c = gs.getCamc();
        EntityManager e = gs.getEntityManager();
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(gs.getCamc().getCamera().getLocation(), gs.getCamc().getCamera().getDirection());
        nodeToCollideWith.collideWith(ray, results);

        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            String hit = null;

            hit = closest.getGeometry().getName();

            for (int i = 0; i < getEquipment().length; i++) {
                if (getEquipment()[i] == null) {
                    getEquipment()[i] = e.getPickableItems().get(hit);
                    closest.getGeometry().getParent().removeFromParent();
                    e.getPickableItems().remove(hit);
                    break;
                }
            }

        }
    }

    public void updateLighting(GameState gs) {
//        int maxRange = 6; // no flashlight = 2, DEBUG = 6
//        float lightStrength = 0.16f; // 0.06 ,  DEBUG = 0.16f
        LightingMethods.updateLightingInLocationLinearGradient(getPositionNode().getWorldTranslation(), DEFAULT_VISION_RANGE, DEFAULT_VISION_INTENSITY, gs);
    }

    public Node getHandsNode() {
        return handsNode;
    }

    public void setHandsNode(Node handsNode) {
        this.handsNode = handsNode;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isBackward() {
        return backward;
    }

    public void setBackward(boolean backward) {
        this.backward = backward;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
    
    


    public Node getPositionNode() {
        return positionNode;
    }

    public void setPositionNode(Node positionNode) {
        this.positionNode = positionNode;
    }

    public Node getLeftHandNode() {
        return leftHandNode;
    }

    public void setLeftHandNode(Node leftHandNode) {
        this.leftHandNode = leftHandNode;
    }

    public Node getRightHandNode() {
        return rightHandNode;
    }

    public void setRightHandNode(Node rightHandNode) {
        this.rightHandNode = rightHandNode;
    }

    public AnimChannel getRightHandAnimChannel() {
        return rightHandAnimChannel;
    }

    public void setRightHandAnimChannel(AnimChannel rightHandAnimChannel) {
        this.rightHandAnimChannel = rightHandAnimChannel;
    }

    public AnimChannel getLeftHandAnimChannel() {
        return leftHandAnimChannel;
    }

    public void setLeftHandAnimChannel(AnimChannel leftHandAnimChannel) {
        this.leftHandAnimChannel = leftHandAnimChannel;
    }

    public AnimControl getRightHandAnimControl() {
        return rightHandAnimControl;
    }

    public void setRightHandAnimControl(AnimControl rightHandAnimControl) {
        this.rightHandAnimControl = rightHandAnimControl;
    }

    public AnimControl getLeftHandAnimControl() {
        return leftHandAnimControl;
    }

    public void setLeftHandAnimControl(AnimControl leftHandAnimControl) {
        this.leftHandAnimControl = leftHandAnimControl;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public Node getVisualMuzzleNode() {
        return visualMuzzleNode;
    }

    public void setVisualMuzzleNode(Node muzzleNode) {
        this.visualMuzzleNode = muzzleNode;
    }

    public AnimChannel getHandsAnimChannel() {
        return handsAnimChannel;
    }

    public void setHandsAnimChannel(AnimChannel handsAnimChannel) {
        this.handsAnimChannel = handsAnimChannel;
    }

    public AnimControl getHandsAnimControl() {
        return handsAnimControl;
    }

    public void setHandsAnimControl(AnimControl handsAnimControl) {
        this.handsAnimControl = handsAnimControl;
    }

    public Item[] getHotbar() {
        return hotbar;
    }

    public void setHotbar(Item[] hotbar) {
        this.hotbar = hotbar;
    }

    public Mob getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Mob currentTarget) {
        this.currentTarget = currentTarget;
    }
    
    
    
    
    private final Vector3f getRight(Node node)

{

Vector3f right = new Vector3f(1,0,0);

node.localToWorld(right,right);

return right;

}
    
    
    

}
