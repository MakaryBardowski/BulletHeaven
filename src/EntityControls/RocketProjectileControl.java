/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.Mob;
import GameObjects.ThrownGrenade;
import GameStates.GameState;
import Map.Generator.TileType;
import com.jme3.animation.LoopMode;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.Random;
import GameObjects.Projectile;
import MapBuilder.LightingMethods;

/**
 *
 * @author 48793
 */
public class RocketProjectileControl extends AbstractControl implements Savable, Cloneable {

    private Projectile owner; // can have custom fields -- example
    private GameState gs;
    public static Random RANDOM = new Random();
    Vector3f ultimateMoveVec;
    Vector3f UMC;
    Vector3f locationAfterMovementInCell;

    public RocketProjectileControl(Projectile owner, GameState gs, Vector3f destination) {
        this.owner = owner;
        this.gs = gs;
        owner.getNode().lookAt(destination, Vector3f.UNIT_Y);

    } // empty serialization constructor

    /**
     * Optional custom constructor with arguments that can init custom fields.
     * Note: you cannot modify the spatial here yet!
     */
    /**
     * This method is called when the control is added to the spatial, and when
     * the control is removed from the spatial (setting a null value). It can be
     * used for both initialization and cleanup.
     */
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        /* Example:
        if (spatial != null){
            // initialize
        }else{
            // cleanup
        }
         */
    }

    /**
     * Implement your spatial's behaviour here. From here you can modify the
     * scene graph and the spatial (transform them, get and set userdata, etc).
     * This loop controls the spatial while the Control is enabled.
     */
    @Override
    protected void controlUpdate(float tpf) {
        if (spatial != null && owner.getNode().getWorldTranslation().getY() >= 18 * tpf) {

            ultimateMoveVec = new Vector3f(owner.getNode().getLocalRotation().getRotationColumn(2).getX() * 25 * tpf, owner.getNode().getLocalRotation().getRotationColumn(2).getY() * 25 * tpf, owner.getNode().getLocalRotation().getRotationColumn(2).getZ() * 25 * tpf);

            int maxRange = 3; // no flashlight = 2
            float lightStrength = 0.16f;
//            LightingMethods.updateLightingInLocationLinearGradient(owner.getNode().getWorldTranslation(), maxRange, lightStrength, gs);

            UMC = ultimateMoveVec.clone();
            if (UMC.getX() < 0) {
                UMC.setX(UMC.getX() - 0.1f);
            }
            if (UMC.getX() > 0) {
                UMC.setX(UMC.getX() + 0.1f);
            }

            if (UMC.getZ() < 0) {
                UMC.setZ(UMC.getZ() - 0.1f);
            }
            if (UMC.getZ() > 0) {
                UMC.setZ(UMC.getZ() + 0.1f);
            }

            locationAfterMovementInCell = new Vector3f((float) Math.floor(owner.getNode().getWorldTranslation().add(UMC.mult(1f)).getX() / gs.getWorldGrid().getCellSize()), 0, (float) Math.floor(owner.getNode().getWorldTranslation().add(UMC.mult(1f)).getZ() / gs.getWorldGrid().getCellSize()));

            if (locationAfterMovementInCell.getZ() < gs.getTileDataMap()[0][0].length && locationAfterMovementInCell.getZ() > -1 && gs.getTileDataMap()[0][(int) Math.floor(owner.getNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) locationAfterMovementInCell.getZ()].getTileType() == TileType.FLOOR) {
                owner.getNode().move(0, 0, ultimateMoveVec.getZ());
            } else {
//                LightingMethods.terminateLightSource(owner.getNode().getWorldTranslation(), maxRange, gs);

                owner.hit(gs);

                owner.getNode().removeControl(this);
            }

            if (locationAfterMovementInCell.getX() < gs.getTileDataMap()[0].length && locationAfterMovementInCell.getX() > -1 && gs.getTileDataMap()[0][(int) locationAfterMovementInCell.getX()][(int) Math.floor(owner.getNode().getWorldTranslation().getZ() / gs.getWorldGrid().getCellSize())].getTileType() == TileType.FLOOR) {
                owner.getNode().move(ultimateMoveVec.getX(), 0, 0);
            } else {
//                                LightingMethods.terminateLightSource(owner.getNode().getWorldTranslation(), maxRange, gs);

                owner.hit(gs);
                owner.getNode().removeControl(this);

            }

//            System.out.println("x: "+(int) Math.floor(owner.getNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())+"\n y: "+(int) locationAfterMovementInCell.getZ());
            if (owner.getNode().getWorldTranslation().getY() + UMC.getY() > owner.getCollisionRange() && owner.getNode().getWorldTranslation().getY() + UMC.getY() < (gs.getWorldGrid().getCellSize() + gs.getTileDataMap()[0][(int) Math.floor(owner.getNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) owner.getNode().getWorldTranslation().getZ()/gs.getWorldGrid().getCellSize()].getUltimateHeight() * gs.getWorldGrid().getCellSize()) - owner.getCollisionRange()) {
                owner.getNode().move(0, ultimateMoveVec.getY(), 0);
            } else {
//                                LightingMethods.terminateLightSource(owner.getNode().getWorldTranslation(), maxRange, gs);

                owner.hit(gs);

                owner.getNode().removeControl(this);

            }

            for (CollidableInterface c : owner.getEntitiesFromTilesInRange(gs.getWorldGrid(), owner.getCollisionRange())) {
                Mob mob = (Mob) c;

                if (mob.getAllegiance() != owner.getAllegiance() && getDistance2D(mob.getNode().getWorldTranslation(), owner.getNode().getWorldTranslation()) <= owner.getCollisionRange()) {
//                    LightingMethods.terminateLightSource(owner.getNode().getWorldTranslation(), maxRange, gs);
                    owner.hit(gs);

                    owner.getNode().removeControl(this);
                }

            }
//            LightingMethods.deleteLightingInLocationLinearGradient(owner.getNode().getWorldTranslation(), maxRange, lightStrength, gs);

        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final RocketProjectileControl control = new RocketProjectileControl(owner, gs, new Vector3f(0, 0, 0));
        /* Optional: use setters to copy userdata into the cloned control */
        // control.setIndex(i); // example
        control.setSpatial(spatial);
        return control;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        /* Optional: rendering manipulation (for advanced users) */
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        // im.getCapsule(this).read(...);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        // ex.getCapsule(this).write(...);
    }

    private float getDistance2D(Vector3f a, Vector3f b) {

        return (float) Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getZ() - b.getZ()) * (a.getZ() - b.getZ()));

    }
}
