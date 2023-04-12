/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Ranged;

import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import EntityControls.FlamethrowerProjectileControl;
import EntityControls.PlayerMuzzleFlashControl;
import EntityControls.RocketProjectileControl;
import GameObjects.FlamethrowerProjectile;
import GameObjects.Mobs.HumanMob;
import Particles.ShellParticle;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameObjects.Mobs.Player;
import GameObjects.RocketProjectile;
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
import Particles.FlamethrowerParticleEmitter;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class Flamethrower extends RangedWeapon implements ItemInterface, LeftHandEquippableItem, RightHandEquippableItem {
         private  static Random r = new Random();
         private FlamethrowerParticleEmitter fire;
         private ParticleEmitter smoke;
    public Flamethrower(Node node, String name, String iconPath, String texturePath, String dropPath, String dropTexturePath, String mobPath, String mobTexturePath, boolean droppable, float damage, float attackSpeed, float accuracyPenalty, boolean showMuzzleFlash, int muzzleFlashTime, int currentAmmo, int maxAmmo) {
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
        smgModel.scale(1.5f);

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
fire = attachFire(p.getNode(),gs,p.getNode().getWorldRotation().getRotationColumn(2) );
smoke = attachSmoke(p.getNode(),gs,p.getNode().getWorldRotation().getRotationColumn(2) );

//Node offsetNode =             p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("MuzzleBone");
//Vector3f offset = new Vector3f(-0.3f,1.8f,1);
Vector3f offset = new Vector3f(-0.35f,1.8f,1f);

fire.setLocalTranslation(offset);
smoke.setLocalTranslation(offset);

    }

    @Override
    public void shootAsPlayer(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode, float tpf) {

        if (p.getRightHandAnimChannel() != null && p.getRightHandAnimChannel().getAnimationName().equals("Shoot") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) {
            p.getRightHandAnimChannel().setAnim("BackToAttack");
            p.getRightHandAnimChannel().setSpeed(( p.getEquippedRightHand()).getAttackSpeed());
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            // make the gun recoil
        } else if (p.getRightHandAnimChannel() != null && (p.getRightHandAnimChannel().getAnimationName().equals("ToIdle") ||p.getRightHandAnimChannel().getAnimationName().equals("Reload")  )  && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0) {
            p.getRightHandAnimChannel().setAnim("Idle");
            p.getRightHandAnimChannel().setSpeed(((Weapon) p.getEquippedRightHand()).getAttackSpeed());
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            // make the gun recoil
        } else if(!p.isShooting() && p.getRightHandAnimChannel() != null && p.getRightHandAnimChannel().getAnimationName().equals("BackToAttack") && p.getRightHandAnimChannel().getAnimMaxTime() - p.getRightHandAnimChannel().getTime() == 0){
                    p.getRightHandAnimChannel().setAnim("ToIdle");
            p.getRightHandAnimChannel().setSpeed(0.5f);
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
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
//            Vector3f shotDirection = new Vector3f(p.getPositionNode().getWorldRotation().getRotationColumn(2).getX(),c.getCamera().getDirection().clone().getY(),p.getPositionNode().getWorldRotation().getRotationColumn(2).getZ());




//            float angle = r.nextFloat() * getAccuracyPenalty() - getAccuracyPenalty() / 2;
//            angle = (float) Math.toRadians(angle);
//            shotDirection.setX((float) (Math.cos(angle) * shotDirection.getX()) - ((float) Math.sin(angle) * shotDirection.getZ()));
//            shotDirection.setZ((float) Math.sin(angle) * shotDirection.getX() + (float) Math.cos(angle) * shotDirection.getZ());
//            shotDirection.setY(shotDirection.getY() + r.nextFloat() * 0.2f - 0.1f);

//            p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").setCullHint(Spatial.CullHint.Inherit);
//            ShellParticle.createShell(gs, (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone")), (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").getWorldTranslation()), em, new Vector3f(-3, 0, 0));

            Ray ray = new Ray(p.getPositionNode().getWorldTranslation().clone().setY(2), shotDirection);
            String headName = "";
            nodeToCheckCollisionOn.collideWith(ray, results);
            // if hit head hitbox = false
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                String hit = closest.getGeometry().getParent().getParent().getParent().getName();

                
                                
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
//             
                
                
            fire.getParticleInfluencer().setInitialVelocity(p.getPositionNode().getWorldRotation().getRotationColumn(2).mult(30)); // 30 for medium range
         
            
 
            smoke.getParticleInfluencer().setInitialVelocity(p.getPositionNode().getWorldRotation().getRotationColumn(2).mult(31)); // 30 for medium range
//
//            


//fire.emitParticles(20);






smoke.emitParticles(20);

                    Node prNode = new Node();
                    gs.getDebugNode().attachChild(prNode);
                    prNode.setLocalTranslation(p.getPositionNode().getWorldTranslation().clone().setY(1.7f));

                    FlamethrowerProjectile pr = new FlamethrowerProjectile(prNode, gs, closest.getContactPoint(), 0.8f, 4f,p.getAllegiance(),getDamage(),fire.emitParticlesNum(20));
//                prNode.addControl(new FlamethrowerProjectileControl(pr,gs,closest.getContactPoint(),flames.getParticles() ));
//
//                




            }
            
            setCurrentAmmo(getCurrentAmmo() - 1);

            if (isShowMuzzleFlash()) {
//                p.getNode().addControl(new PlayerMuzzleFlashControl(p, getMuzzleFlashTime()));
            }
            gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());

        } else {
            reload(p, gs);

        }
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
    
    
    private final Vector3f getRight(Node node)

{

Vector3f right = new Vector3f(1,0,0);

node.localToWorld(right,right);

return right;

}
    
    
       private FlamethrowerParticleEmitter attachFire(Node node, GameState gs,Vector3f d) {
        FlamethrowerParticleEmitter flames = new FlamethrowerParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 5000);
        Material mat_red = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", gs.getAssetManager().loadTexture("Textures/effects/fireParticle.png"));
        flames.setMaterial(mat_red);
//        flames.rotate(0,FastMath.DEG_TO_RAD*15,0);


        flames.move(0,1.8f,0);
//        flames.setImagesX(2);
//        flames.setImagesY(2); // 2x2 texture animation
        flames.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
//        flames.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        flames.setStartColor(new ColorRGBA(1f, 1f, 1f, 0.5f)); // yellow

        flames.getParticleInfluencer().setInitialVelocity(d.mult(30));
        flames.setStartSize(0.05f); // 1.5 
        flames.setEndSize(0.3f); // 0.1
        flames.setGravity(0, 1, 0);
        flames.setLowLife(0.5f);
        flames.setHighLife(1f);   // 1
        flames.getParticleInfluencer().setVelocityVariation(0.06f); // 0.1f lub 0.07
        node.attachChild(flames);
                flames.setParticlesPerSec(0);
flames.setInWorldSpace(true);
                
                
        return flames;
    }

       
       
         private ParticleEmitter attachSmoke(Node node, GameState gs,Vector3f d) {
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 180);
        Material mat_red = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", gs.getAssetManager().loadTexture("Textures/effects/fireParticle.png"));
       mat_red.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        fire.setMaterial(mat_red);
//        flames.rotate(0,FastMath.DEG_TO_RAD*15,0);


        fire.move(0,1.8f,0);
                node.attachChild(fire);

//        flames.setImagesX(2);
//        flames.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(0f, 0f, 0f, 1f));   // red
//        flames.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.setStartColor(new ColorRGBA(0f, 0f, 0f, 0.5f)); // yellow

        fire.getParticleInfluencer().setInitialVelocity(d.mult(31));
        fire.setStartSize(0.03f); // 1.5 
        fire.setEndSize(0.6f); // 0.1
        fire.setGravity(0, -2, 0);
        fire.setLowLife(2f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.055f); // 0.1f lub 0.07
                fire.setParticlesPerSec(0);
fire.setInWorldSpace(true);
                
             
        return fire;
    }
       
       
}
