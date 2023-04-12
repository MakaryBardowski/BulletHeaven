/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Throwable;

import Items.Ranged.*;
import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import EntityControls.PlayerMuzzleFlashControl;
import EntityControls.ThrownGrenadeControl;
import GameObjects.FragGrenade;
import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameObjects.Mobs.Player;
import Items.Weapon;
import GameStates.GameState;
import Items.ItemInterface;
import Items.LeftHandEquippableItem;
import Items.RightHandEquippableItem;
import Items.WeaponInterface;
import MapBuilder.LightingMethods;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class Grenade extends ThrowableWeapon implements ItemInterface, LeftHandEquippableItem, RightHandEquippableItem, WeaponInterface {

       private float powerAttackThreshold = 2;
    private float powerAttackCurrentTime;
    private float blastRadius;

    
    public Grenade(Node node, String name, String iconPath, String texturePath, String dropPath, String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable, float damage, float attackSpeed,float blastRadius) {
        super(node, name, iconPath, texturePath, dropPath, dropTexturePath,mobPath,mobTexturePath, droppable, damage, attackSpeed);
        this.blastRadius = blastRadius;
    }

    @Override
    public void equipOnPlayer(Player p, GameState gs) {
        playerEquipItemR(p, gs);
    }

    @Override
    public void equipOnMob(HumanMob m, GameState gs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mobEquipItemR() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mobEquipItemL() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playerEquipItemL(Player p, GameState gs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playerEquipItemR(Player p, GameState gs) {
float grenadeScale = 0.7f;

p.getRightHandNode().removeFromParent();
        Node grenadeNode = (Node) p.getAssetManager().loadModel(getName());
        grenadeNode.scale(grenadeScale);
        p.setRightHandNode(grenadeNode); // set right hand model as this weapons model
        LightingMethods.setLightingMaterial(p.getRightHandNode(), this.getTexturePath(), 1.1f, p.getAssetManager());
        p.getHandsNode().attachChild(p.getRightHandNode()); // attach the new model

        p.setRightHandAnimControl(p.getRightHandNode().getChild("Armature").getControl(AnimControl.class));
        p.setRightHandAnimChannel(p.getRightHandAnimControl().createChannel());
        p.getRightHandAnimChannel().setAnim("Idle");


        p.setEquippedRightHand(this);
                       gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText("");

    }
  @Override
    public void shootAsPlayer(Player p,GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode,float tpf) {
    checkIfShooting(p,gs,c,em,nodeToCheckCollisionOn,debugNode,tpf);
    
    }
    
    
     private void checkIfShooting(Player p,GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode,float tpf){
    
         
         
    if (p.getRightHandAnimChannel() != null && !p.isShooting()) {
        
        if(p.getRightHandAnimChannel().getAnimationName().equals("Throw") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() <= 0f){
                        p.getRightHandNode().removeFromParent();

        }

            if (p.getRightHandAnimChannel().getAnimationName().contains("Windup") && ( (p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() <= 0f || p.getRightHandAnimChannel().getAnimationName().equals("WindupPowerAttack")))) {

                

                powerAttackCurrentTime = 0;

                p.getRightHandAnimChannel().setAnim("Throw");
                p.getRightHandAnimChannel().setSpeed(5); // modifies shooting speed
                p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);

                playerShoot(p,gs,gs.getCamc(),gs.getEntityManager(),gs.getMapNode(),gs.getDebugNode());

            }
        }

        if (p.isShooting() && p.getRightHandAnimChannel() != null) {

            powerAttackCurrentTime += tpf;

            if (powerAttackCurrentTime >= powerAttackThreshold* 1 && p.getRightHandAnimChannel().getAnimationName().equals("Windup")) {
                p.getRightHandAnimChannel().setAnim("WindupPowerAttack");
                p.getRightHandAnimChannel().setSpeed(getAttackSpeed()); // modifies shooting speed
                p.getRightHandAnimChannel().setLoopMode(LoopMode.Loop);
            } else {
                powerAttackCurrentTime +=tpf;
            }

            if (!p.getRightHandAnimChannel().getAnimationName().equals("Windup") && (p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0)) {
                if ((p.getRightHandAnimChannel().getAnimationName().equals("BackToAttack") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) ^ !p.getRightHandAnimChannel().getAnimationName().equals("BackToAttack")) {
                    p.getRightHandAnimChannel().setAnim("Windup");
                    p.getRightHandAnimChannel().setSpeed(getAttackSpeed()); // modifies shooting speed
                    p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);

                }
            }

        }
    
    }
    
    private void playerShoot(Player p,GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode){
      
        
        
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(c.getCamera().getLocation(), c.getCamera().getDirection());
        nodeToCheckCollisionOn.collideWith(ray, results);
        // if hit head hitbox = false
        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            
            
            float gravity = 9;
            float currentSpeedY = 6;
            Vector3f grenadeStartPosition = p.getPositionNode().getWorldTranslation().clone();
            Vector3f grenadeDestinationPosition = closest.getContactPoint().clone();
            grenadeStartPosition.setY(1.5f);
            Node thrownGrenadeModelNode = (Node)gs.getAssetManager().loadModel("Models/Grenade/Grenade.j3o"); // rotation applies to this node
            Node grenadePositionNode = new Node("grenade position node");
            grenadePositionNode.attachChild(thrownGrenadeModelNode); // movement applies to this node
            FragGrenade grenade = new FragGrenade(grenadePositionNode,thrownGrenadeModelNode, grenadeStartPosition ,grenadeDestinationPosition,gravity,currentSpeedY,getDamage(),blastRadius);
            nodeToCheckCollisionOn.attachChild(grenadePositionNode);
            LightingMethods.setLightingMaterial(thrownGrenadeModelNode, "Textures/assetTextures/WeaponsPalette/Grenade.png", 0.7f, gs.getAssetManager()
            );
            grenadePositionNode.move(grenade.getStart());
            grenadePositionNode.addControl(new ThrownGrenadeControl(grenade,gs));
          
//                Sphere sphere1 = new Sphere(30, 30, 3f);
//                Spatial mark1 = new Geometry("BOOM!", sphere1);
//                Material mark_mat1 = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//                mark_mat1.setColor("Color", ColorRGBA.Red);
//                mark1.setMaterial(mark_mat1);
//                                debugNode.attachChild(mark1);
//                mark1.move(closest.getContactPoint());
            }
        }
        public void reload(Player p, GameState gs){

    }
    }


