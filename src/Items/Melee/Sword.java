/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Melee;

import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import EntityControls.PlayerMuzzleFlashControl;
import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameObjects.Mobs.Player;
import Items.Weapon;
import GameStates.GameState;
import Items.ItemInterface;
import Items.LeftHandEquippableItem;
import Items.Ranged.Shotgun;
import Items.RightHandEquippableItem;
import Items.WeaponInterface;
import MapBuilder.LightingMethods;
import Particles.BillboardParticle2D;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class Sword extends MeleeWeapon implements ItemInterface, LeftHandEquippableItem, RightHandEquippableItem {

    private static int attackNumber = 0;

    public Sword(Node node, String name, String iconPath, String texturePath, String dropPath, String dropTexturePath, String mobPath, String mobTexturePath, boolean droppable, float damage, float attackSpeed,float range) {
        super(node, name, iconPath, texturePath, dropPath, dropTexturePath, mobPath, mobTexturePath, droppable, damage, attackSpeed,range);
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
        float muzzleFlashLightness = 2.5f; // 1.0 is yellow/orange , 1.5f is white/yellow
        float scale = 0.55f;
        p.getRightHandNode().removeFromParent();

        p.setRightHandNode((Node) p.getAssetManager().loadModel("Models/swordSkeleton/swordSkeleton.j3o")); // swords share animations, so attach the skeleton
        Node swordItself = (Node) p.getAssetManager().loadModel(getName());

        p.getRightHandNode().scale(scale);

        LightingMethods.setLightingMaterial(swordItself, this.getTexturePath(), 1.1f, p.getAssetManager());
        SkeletonControl rightHandSkeletonControl = p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class); // get skeleton
        rightHandSkeletonControl.getAttachmentsNode("MeleeWeaponNode").attachChild(swordItself); // attach the sword model to the attachment node
        p.getHandsNode().attachChild(p.getRightHandNode()); // attach the new model

        p.setRightHandAnimControl(p.getRightHandNode().getChild("Armature").getControl(AnimControl.class));
        p.setRightHandAnimChannel(p.getRightHandAnimControl().createChannel());

        setPowerAttackThreshold(0.3f);

        p.getRightHandAnimChannel().setAnim("Idle");

        p.setEquippedRightHand(this);
                       gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText("");

    }

    @Override
    public void shootAsPlayer(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float tpf) {

        if (p.getRightHandAnimChannel() != null && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) {
            if (p.getRightHandAnimChannel().getAnimationName().equals("Shoot")) {
                p.getRightHandAnimChannel().setAnim("BackToAttack");
            } else if (p.getRightHandAnimChannel().getAnimationName().equals("Shoot1")) {
                p.getRightHandAnimChannel().setAnim("BackToAttack1");
            }

            p.getRightHandAnimChannel().setSpeed(((Weapon) p.getEquippedRightHand()).getAttackSpeed());
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            // make the gun recoil
        }

        checkIfShooting(p, gs, c, em, nodeToCheckCollisionOn, debugNode, tpf);

    }

    private void checkIfShooting(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float tpf) {

        if (p.getRightHandAnimChannel() != null && !p.isShooting()) {

            if (p.getRightHandAnimChannel().getAnimationName().contains("Windup") && ((p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() <= 0f || p.getRightHandAnimChannel().getAnimationName().equals("WindupPowerAttack")))) {
                float damageMultiplier = 1f;
                if (getPowerAttackCurrentTime() >= getPowerAttackThreshold() * 5) {
                    damageMultiplier = 1.5f;
                }

                setPowerAttackCurrentTime(0);
                if (p.getRightHandAnimChannel().getAnimationName().equals("Windup") || p.getRightHandAnimChannel().getAnimationName().equals("WindupPowerAttack")) {
                    p.getRightHandAnimChannel().setAnim("Shoot");
                    p.getRightHandAnimChannel().setSpeed(getAttackSpeed()*3f); // modifies shooting speed

                } else if (p.getRightHandAnimChannel().getAnimationName().equals("Windup1")) {
                    p.getRightHandAnimChannel().setAnim("Shoot1");
                    p.getRightHandAnimChannel().setSpeed(getAttackSpeed() * 3f); // modifies shooting speed

                }
                p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);

                playerShoot(p, gs, c, em, nodeToCheckCollisionOn, debugNode, damageMultiplier);

            }
        }

        if (p.isShooting() && p.getRightHandAnimChannel() != null) {

            setPowerAttackCurrentTime(getPowerAttackCurrentTime() + tpf);

            if (getPowerAttackCurrentTime() >= getPowerAttackThreshold() * 5 && p.getRightHandAnimChannel().getAnimationName().equals("Windup")) {
                p.getRightHandAnimChannel().setAnim("WindupPowerAttack");
                p.getRightHandAnimChannel().setSpeed(getAttackSpeed()); // modifies shooting speed
                p.getRightHandAnimChannel().setLoopMode(LoopMode.Loop);
            } else {
                setPowerAttackCurrentTime(getPowerAttackCurrentTime() + tpf);
            }

            if (!p.getRightHandAnimChannel().getAnimationName().contains("Windup") && (p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0)) {
                if ((p.getRightHandAnimChannel().getAnimationName().contains("BackToAttack") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) ^ !p.getRightHandAnimChannel().getAnimationName().contains("BackToAttack")) {
                    if (attackNumber == 0) {
                        p.getRightHandAnimChannel().setAnim("Windup");
                        p.getRightHandAnimChannel().setSpeed(getAttackSpeed()*3);
                    } else if (attackNumber == 1) {
                        p.getRightHandAnimChannel().setAnim("Windup1");
                        p.getRightHandAnimChannel().setSpeed(getAttackSpeed()*3);
                    }
                    attackNumber++;
                    if(attackNumber == 2){
                    attackNumber = 0;
                    }
                    p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);

                }
            }

        }

    }

    private void playerShoot(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float powerAttackMultiplier) {
        CollisionResults results = new CollisionResults();

        Ray ray = new Ray(p.getPositionNode().getWorldTranslation().clone().setY(2), c.getCamera().getDirection().clone());
        ray.setLimit(getRange());
        String headName = "";
        nodeToCheckCollisionOn.collideWith(ray, results);
        // if hit head hitbox = false
        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            String hit = closest.getGeometry().getParent().getParent().getParent().getName();

            Mob enemyHit = em.getEnemiesAlive().get(hit);
            headName = closest.getGeometry().getName();
            if (enemyHit == null && closest.getGeometry().getParent().getParent().getParent().getParent() != null) {
                hit = closest.getGeometry().getParent().getParent().getParent().getParent().getName();
                enemyHit = em.getEnemiesAlive().get(hit);
                headName = closest.getGeometry().getParent().getName();
            }

            System.out.println("---------------");
            System.out.println("hit: " + hit);
            System.out.println("enemyHit: " + enemyHit);
            if (enemyHit != null) {

                int dmgMultiplier = 1;

                BloodParticle.createBloodParticlesMedium(gs, debugNode, closest.getContactPoint(), em, c.getCamera().getDirection());
                BloodParticle.createBloodParticlesMedium(gs, debugNode, closest.getContactPoint(), em);

                gs.getPlayer().dealDamage((MobInterface) enemyHit, (((Weapon) gs.getPlayer().getEquippedRightHand()).getDamage() * dmgMultiplier * powerAttackMultiplier), gs, em,"");
                System.out.println("hp po trafieniu: " + enemyHit.getHealth());


            }else{
                if(p.getPositionNode().getWorldTranslation().distance(closest.getContactPoint()) <= getRange())
                                            BillboardParticle2D.wallHitSparks(gs.getDebugNode(),closest.getContactPoint().clone(),gs);

            }
            
            
        }

    }
    @Override
    public void reload(Player p, GameState gs){
  
    }
}
