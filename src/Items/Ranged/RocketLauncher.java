/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Ranged;

import Particles.BloodParticle;
import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import EntityControls.PlayerMuzzleFlashControl;
import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameObjects.Mobs.Player;
import GameObjects.Projectile;
import GameObjects.RocketProjectile;
import Items.Weapon;
import GameStates.GameState;
import Items.ItemInterface;
import Items.LeftHandEquippableItem;
import Items.RightHandEquippableItem;
import Items.WeaponInterface;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import Particles.BillboardParticle2D;
import Particles.ShellParticle;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.TextureKey;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class RocketLauncher extends RangedWeapon implements ItemInterface, LeftHandEquippableItem, RightHandEquippableItem {

    public RocketLauncher(Node node, String name, String iconPath, String texturePath, String dropPath, String dropTexturePath, String mobPath, String mobTexturePath, boolean droppable, float damage, float attackSpeed, float accuracyPenalty, boolean showMuzzleFlash, int muzzleFlashTime, int currentAmmo, int maxAmmo) {
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
        Node pistolModel = (Node) p.getAssetManager().loadModel(getName());
        pistolModel.scale(0.8f);
        p.setRightHandNode(pistolModel); // set right hand model as this weapons model
        LightingMethods.setLightingMaterial(p.getRightHandNode(), this.getTexturePath(), 1.1f, p.getAssetManager());
        p.getHandsNode().attachChild(p.getRightHandNode()); // attach the new model

        p.setRightHandAnimControl(p.getRightHandNode().getChild("Armature").getControl(AnimControl.class));
        p.setRightHandAnimChannel(p.getRightHandAnimControl().createChannel());
        p.getRightHandAnimChannel().setAnim("Idle");
        p.getRightHandAnimChannel().setSpeed(0);

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

//            p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").setCullHint(Spatial.CullHint.Inherit);
//            ShellParticle.createShell(gs, (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone")), (p.getRightHandNode().getChild("Armature").getControl(SkeletonControl.class).getAttachmentsNode("ShellBone").getWorldTranslation()), em, new Vector3f(-3, 0, 0));

            Ray ray = new Ray(p.getPositionNode().getWorldTranslation().clone().setY(2), shotDirection);
            String headName = "";
            nodeToCheckCollisionOn.collideWith(ray, results);
            // if hit head hitbox = false
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                String hit = closest.getGeometry().getParent().getParent().getParent().getName();

            
            

                
                    String modelName = "Models/javelinRocket/javelinRocket.j3o";
                    String texName = "Models/javelinRocket/javelinRocket.png";
//                    String modelName = "Models/c4/c4.j3o";
//                    String texName = "Models/c4/explosive.png";
                    Node prNode = (Node) gs.getAssetManager().loadModel(modelName);
                    prNode.scale(2);
//                    Texture t = gs.getAssetManager().loadTexture(new TextureKey("Models/c4/explosive.png", false));

                    LightingMethods.setLightingMaterialColored(prNode, texName, 0.7f, gs.getAssetManager(), ColorRGBA.White);
                    attachFire(prNode, gs);

                    gs.getDebugNode().attachChild(prNode);
                    prNode.setLocalTranslation(p.getPositionNode().getWorldTranslation().clone().setY(1.7f));
                    RocketProjectile pr = new RocketProjectile(prNode, gs, closest.getContactPoint(), 0.8f, 4f,p.getAllegiance(),getDamage());

                
            }
            setCurrentAmmo(getCurrentAmmo() - 1);

            if (isShowMuzzleFlash()) {
                p.getNode().addControl(new PlayerMuzzleFlashControl(p, getMuzzleFlashTime()));
            }
            gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());

        }
    }

    @Override
    public void reload(Player p, GameState gs) {
        if (!p.getRightHandAnimChannel().getAnimationName().equals("Reload")) {

            p.getRightHandAnimChannel().setAnim("Reload");
            p.getRightHandAnimChannel().setSpeed(1); // modifies shooting speed
            p.getRightHandAnimChannel().setLoopMode(LoopMode.DontLoop);
            setCurrentAmmo(getMaxAmmo());
            gs.getNifty().getCurrentScreen().findControl("ammo", LabelControl.class).setText((int) getCurrentAmmo() + "/" + (int) getMaxAmmo());
        }
    }
    
    private ParticleEmitter attachFire(Node node, GameState gs) {
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 60);
        Material mat_red = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", gs.getAssetManager().loadTexture("Textures/effects/fireParticle.png"));
        fire.setMaterial(mat_red);
//        fire.setImagesX(2);
//        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(0.1f); // 1.5 
        fire.setEndSize(0.05f); // 0.1
        fire.setGravity(0, 3, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        node.attachChild(fire);
                fire.setParticlesPerSec(60);

        return fire;
    }

}
