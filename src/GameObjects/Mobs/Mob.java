/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import EntityControls.DeadBodyControl;
import EntityControls.EntityManager;
import Items.Item;
import EntityControls.HumanoidControl;
import GUILayer.MinimapMobIndicatorControl;
import GameObjects.GameObject;
import GameStates.GameState;
import Items.ItemCreator;
import Items.ItemInterface;
import Items.LeftHandEquippableItem;
import Items.RightHandEquippableItem;
import Items.Weapon;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author 48793
 */
public abstract class Mob extends GameObject implements MobInterface, CollidableInterface {

    private static long ID = 0;
    private String mobName;
    private Node node; //model
    private AnimControl aCtrl;  // od animacji
    private AnimChannel ACh; // od animacji
    private float health;
    private float maxHealth;
    private float armor;
    private float baseSpeed;

    private Item[] equipment = new Item[18]; // 6 rows 3 cols
    private Geometry[] modelElementsForShading; // 0 - helmet, 1 - torso, 2-3 R/L hand , 4-5 R/L leg 

//    private Weapon equippedRightHand;
//    private Weapon equippedLeftHand;
//
//    private Node headNode;
//    private Node torsoNode;
//    
//    private Item helmet;
//    private Item torso;
//    private Item boots;
//    private Item gloves;
//    private Node projectileSpawnNode;
//    
    // ai
    private float rof; // range of sight
    private Mob currentTarget; // current target to be followed or attacked
    private Allegiance allegiance;

    private Node bloodPuddleNode;

    private Node deadNode; // the mob is attached to this node after death (so he is not hittable anymore)

    public Mob(Node node, AnimControl aCtrl, AnimChannel ACh,Geometry[] modelElementsForShading, Node deadNode, String mobName, Allegiance allegiance) {
        this.node = node;
        this.aCtrl = aCtrl;
        this.ACh = ACh;
        this.deadNode = deadNode;
        this.mobName = mobName;
        this.modelElementsForShading = modelElementsForShading;
        node.setName(Long.toString(ID++));
        this.allegiance = allegiance;
        rof = 20;

    }

    public Mob(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed,Geometry[] modelElementsForShading, Node deadNode, String mobName, Allegiance allegiance) {
        this(node, aCtrl, ACh,modelElementsForShading, deadNode, mobName, allegiance);
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.baseSpeed = baseSpeed;

    }

    public Mob(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed, Weapon equippedRightHand, Weapon equippedLefthand, Item helmet, Item torso, Item boots, Item gloves,Geometry[] modelElementsForShading, Node deadNode, String mobName, Allegiance allegiance) {
        this(node, aCtrl, ACh, health, armor, baseSpeed,modelElementsForShading, deadNode, mobName, allegiance);

    }

 

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public AnimControl getaCtrl() {
        return aCtrl;
    }

    public void setaCtrl(AnimControl aCtrl) {
        this.aCtrl = aCtrl;
    }

    public AnimChannel getACh() {
        return ACh;
    }

    public void setACh(AnimChannel ACh) {
        this.ACh = ACh;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getArmor() {
        return armor;
    }

    public void setArmor(float armor) {
        this.armor = armor;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public Item[] getEquipment() {
        return equipment;
    }

    public void setEquipment(Item[] equipment) {
        this.equipment = equipment;
    }

    public Node getBloodPuddleNode() {
        return bloodPuddleNode;
    }

    public void setBloodPuddleNode(Node bloodPuddleNode) {
        this.bloodPuddleNode = bloodPuddleNode;
    }

    public Node getDeadNode() {
        return deadNode;
    }

    public void setDeadNode(Node deadNode) {
        this.deadNode = deadNode;
    }

    public static long getID() {
        return ID;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getMobName() {
        return mobName;
    }

    public void setMobName(String mobName) {
        this.mobName = mobName;
    }

    public float getRof() {
        return rof;
    }

    public void setRof(float rof) {
        this.rof = rof;
    }

    public Mob getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Mob currentTarget) {
        this.currentTarget = currentTarget;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }
    
        public Geometry[] getModelElementsForShading() {
        return modelElementsForShading;
    }

    public void setModelElementsForShading(Geometry[] modelElementsForShading) {
        this.modelElementsForShading = modelElementsForShading;
    }
    
    



}
