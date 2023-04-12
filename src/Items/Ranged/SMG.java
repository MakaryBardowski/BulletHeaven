/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Ranged;

import EntityControls.BulletTraceControl;
import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import EntityControls.PlayerMuzzleFlashControl;
import GameObjects.Mobs.HumanMob;
import Particles.ShellParticle;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameObjects.Mobs.Player;
import Items.Weapon;
import GameStates.GameState;
import Items.ItemInterface;
import Items.LeftHandEquippableItem;
import Items.Melee.MeleeWeapon;
import Items.RightHandEquippableItem;
import Items.WeaponInterface;
import MapBuilder.LightingMethods;
import MapBuilder.MapInitializer;
import static MapBuilder.MapInitializer.putShape;
import Particles.BillboardParticle2D;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class SMG extends RangedWeapon implements ItemInterface, LeftHandEquippableItem, RightHandEquippableItem {

    public SMG(Node node, String name, String iconPath, String texturePath, String dropPath, String dropTexturePath, String mobPath, String mobTexturePath, boolean droppable, float damage, float attackSpeed, float accuracyPenalty, boolean showMuzzleFlash, int muzzleFlashTime, int currentAmmo, int maxAmmo) {
        super(node, name, iconPath, texturePath, dropPath, dropTexturePath, mobPath, mobTexturePath, droppable, damage, attackSpeed, accuracyPenalty, showMuzzleFlash, muzzleFlashTime, currentAmmo, maxAmmo);
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
        p.getRightHandNode().removeFromParent();

        Node smgModel = (Node) p.getAssetManager().loadModel(getName());
        smgModel.scale(0.9f);

        p.setRightHandNode(smgModel); // set right hand model as this weapons model
        LightingMethods.setLightingMaterial(p.getRightHandNode(), this.getTexturePath(), 1.1f, p.getAssetManager());
        p.getHandsNode().attachChild(p.getRightHandNode()); // attach the new model

        p.setRightHandAnimControl(p.getRightHandNode().getChild("Armature").getControl(AnimControl.class));
        p.setRightHandAnimChannel(p.getRightHandAnimControl().createChannel());
        p.getRightHandAnimChannel().setAnim("Idle");

        p.setVisualMuzzleNode(p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("MuzzleBone"));
        Node playerMuzzleFlash = (Node) gs.getAssetManager().loadModel("Models/MuzzleFlashModel/MuzzleFlashModel.j3o");
        LightingMethods.setLightingMaterial(playerMuzzleFlash, "Textures/assetTextures/WeaponsPalette/MuzzleFlash.png", muzzleFlashLightness, gs.getAssetManager());
        playerMuzzleFlash.scale(2);
        p.getVisualMuzzleNode().attachChild(playerMuzzleFlash);
        p.getVisualMuzzleNode().setCullHint(Spatial.CullHint.Always);

        p.setEquippedRightHand(this);
        System.out.println("currentAmmo: " + getCurrentAmmo());
        gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());

    }

    @Override
    public void shootAsPlayer(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float tpf) {

        if (p.getRightHandAnimChannel() != null && p.getRightHandAnimChannel().getAnimationName().equals("Shoot") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) {
            p.getRightHandAnimChannel().setAnim("BackToAttack");
            p.getRightHandAnimChannel().setSpeed(((Weapon) p.getEquippedRightHand()).getAttackSpeed());
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            // make the gun recoil
        } else if (p.getRightHandAnimChannel() != null && p.getRightHandAnimChannel().getAnimationName().equals("Reload") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) {
            p.getRightHandAnimChannel().setAnim("Idle");
            p.getRightHandAnimChannel().setSpeed(((Weapon) p.getEquippedRightHand()).getAttackSpeed());
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            // make the gun recoil
        }

        checkIfShooting(p, gs, c, em, nodeToCheckCollisionOn, debugNode, tpf);

    }

    private void checkIfShooting(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float tpf) {

        if (p.isShooting() && p.getRightHandAnimChannel() != null) {

            if (!p.getRightHandAnimChannel().getAnimationName().equals("Shoot") && !p.getRightHandAnimChannel().getAnimationName().equals("Reload")) {
                if ((p.getRightHandAnimChannel().getAnimationName().equals("BackToAttack") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) ^ !p.getRightHandAnimChannel().getAnimationName().equals("BackToAttack")) {
                    playerShoot(p, gs, c, em, nodeToCheckCollisionOn, debugNode);

                }
            }

        }

    }

    private void playerShoot(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode) {
        if (getCurrentAmmo() > 0) {
            p.getRightHandAnimChannel().setAnim("Shoot");
            p.getRightHandAnimChannel().setSpeed(((Weapon) p.getEquippedRightHand()).getAttackSpeed()); // modifies shooting speed
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);

            CollisionResults results = new CollisionResults();
            Vector3f shotDirection = c.getCamera().getDirection().clone();
            Random r = new Random();
            float angle = r.nextFloat() * getAccuracyPenalty() - getAccuracyPenalty() / 2;
            angle = (float) Math.toRadians(angle);
            shotDirection.setX((float) (Math.cos(angle) * shotDirection.getX()) - ((float) Math.sin(angle) * shotDirection.getZ()));
            shotDirection.setZ((float) Math.sin(angle) * shotDirection.getX() + (float) Math.cos(angle) * shotDirection.getZ());
            shotDirection.setY(shotDirection.getY() + r.nextFloat() * 0.02f - 0.01f);

            p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").setCullHint(Spatial.CullHint.Inherit);
            ShellParticle.createShell(gs, (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone")), (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").getWorldTranslation()), em, new Vector3f(-3, 0, 0));

            
    
        
        
        
        
        
            
            Ray ray = new Ray(p.getPositionNode().getWorldTranslation().clone().setY(2), shotDirection);
            String headName = "";
            nodeToCheckCollisionOn.collideWith(ray, results);
            // if hit head hitbox = false
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                
                
//                
//                  Arrow arrow = new Arrow(shotDirection.mult(50));
//        putShape(gs.getDebugNode(), arrow, ColorRGBA.Red, gs).setLocalTranslation(new Vector3f(p.getPositionNode().getWorldTranslation().getX(),2,p.getPositionNode().getWorldTranslation().getZ())); //x - czerwony
//             Sphere sphere1 = new Sphere(30, 30, 0.1f);
//                Spatial mark1 = new Geometry("BOOM!", sphere1);
//                Material mark_mat1 = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//                mark_mat1.setColor("Color", ColorRGBA.Red);
//                mark1.setMaterial(mark_mat1);
//                                debugNode.attachChild(mark1);
//                mark1.move(closest.getContactPoint());
//                    System.out.println("attachuje sfere");
//                
//                
                
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

                    if (headName.contains("head")) {
                        dmgMultiplier = 2;
                        System.out.println("Headshot!");
                        BloodParticle.createBloodParticlesMedium(gs, debugNode, closest.getContactPoint(), em, c.getCamera().getDirection());
                        BloodParticle.createBloodParticlesMedium(gs, debugNode, closest.getContactPoint(), em);

                    } else {
                        BloodParticle.createBloodParticlesSmall(gs, debugNode, closest.getContactPoint(), em);

                    }
                    gs.getPlayer().dealDamage((MobInterface) enemyHit, ((gs.getPlayer().getEquippedRightHand()).getDamage() * dmgMultiplier), gs, em,headName);
                    System.out.println("hp po trafieniu: " + enemyHit.getHealth());

                    if (dmgMultiplier == 2 && enemyHit.getHealth() <= 0 && gs.getPlayer().getEquippedRightHand() instanceof Shotgun) {
                        Node bloodSplatter = (Node) gs.getAssetManager().loadModel("Models/Utility/HeadBloodParticles.j3o");
                        AnimControl bloodControl = bloodSplatter.getChild("Armature").getControl(AnimControl.class);
                        AnimChannel bloodChannel = bloodControl.createChannel();
                        closest.getGeometry().getParent().getParent().attachChild(bloodSplatter);
                        bloodSplatter.move(closest.getGeometry().getParent().getLocalTranslation());
                        LightingMethods.setLightingMaterial(bloodSplatter, "Textures/assetTextures/UtilityTextures/HeadBloodParticles.png", 0.7f, gs.getAssetManager());
                        bloodChannel.setAnim("Splatter");
                        bloodChannel.setLoopMode(LoopMode.DontLoop);
//                        enemyHit.getHeadNode().removeFromParent();

                    }
             
                }else{   
                                            BillboardParticle2D.wallHitSparks(gs.getDebugNode(),closest.getContactPoint().clone(),gs);
                }
               spawnTrace(gs,closest.getContactPoint(),p.getPositionNode().getWorldTranslation().clone().setY(1.8f));

            }
            setCurrentAmmo(getCurrentAmmo() - 1);

            if (isShowMuzzleFlash()) {
                p.getNode().addControl(new PlayerMuzzleFlashControl(p, getMuzzleFlashTime()));
            }
            gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());

        } else {
            reload(p, gs);

        }
    }

    
    
    
    
    
    
    private void spawnTrace(GameState gs,Vector3f cp,Vector3f st){
    
      Box box2 = new Box(0.02f,0.02f,1f);
        Geometry red = new Geometry("Box", box2);
        Material mat2 = new Material(gs.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        
        float traceAlpha = 0.2f;
        mat2.setColor("Color", new ColorRGBA(169f/255f,169f/255f,169f/255f,traceAlpha));
        
        
        
        mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate transparency
red.setQueueBucket(Bucket.Transparent);
        
        
        
        red.setMaterial(mat2);
        
        
        red.addControl(new BulletTraceControl(mat2,traceAlpha));
        
        
        
        gs.getDebugNode().attachChild(red);
       
       red.setLocalTranslation( st.add( cp.subtract(st).mult( 0.5f )   ));
       red.scale(1,1,cp.distance(st));
    red.lookAt(cp, Vector3f.UNIT_Y);
    
    }
    
    
    
    
    
    
    
    
    @Override
    public void reload(Player p, GameState gs) {
        if (!p.getRightHandAnimChannel().getAnimationName().equals("Reload")) {

            p.getRightHandAnimChannel().setAnim("Reload");
            p.getRightHandAnimChannel().setSpeed(1.3f); // modifies shooting speed
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            setCurrentAmmo(getMaxAmmo());
            gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());
        }
    }

}
