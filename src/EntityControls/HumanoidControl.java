/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.Mob;
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

/**
 *
 * @author 48793
 */
public class HumanoidControl extends AbstractControl implements Savable, Cloneable {

    private Mob owner; // can have custom fields -- example
    private GameState gs;
    Vector3f ultimateMoveVec;
    Vector3f UMC;
    Vector3f locationAfterMovementInCell;
    public static Random RANDOM = new Random();

    public HumanoidControl(Mob owner, GameState gs) {
        this.owner = owner;
        this.gs = gs;
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

        if (spatial != null && owner.getHealth() > 0) {
            owner.removeFromGrid(gs.getWorldGrid());

            //Movement goes here
            Mob target = null;
            for (CollidableInterface m : owner.getEntitiesFromTilesInRange(gs.getWorldGrid(), owner.getRof())) {
                Mob mob = (Mob) m;
                if (mob.getAllegiance() != owner.getAllegiance() && mob.getNode().getWorldTranslation().distance(owner.getNode().getWorldTranslation()) <= owner.getRof()) {
                    owner.setCurrentTarget(mob);
                    target = mob;
                }
            }

            if (target != null) {
                if (!owner.getACh().getAnimationName().equals("Run")) {
                    owner.getACh().setAnim("Run");
                    owner.getACh().setSpeed(1.5f);
                }
                owner.getNode().lookAt(owner.getCurrentTarget().getNode().getWorldTranslation(), Vector3f.UNIT_Y);

                if (owner.getCurrentTarget().getNode().getWorldTranslation().distance(owner.getNode().getWorldTranslation()) > 4) {
                    ultimateMoveVec = owner.getNode().getWorldRotation().getRotationColumn(2).mult(owner.getBaseSpeed() * tpf);
                    UMC = ultimateMoveVec.clone();
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

                    locationAfterMovementInCell = new Vector3f((float) Math.floor(owner.getNode().getWorldTranslation().add(UMC.mult(1f)).getX() / gs.getWorldGrid().getCellSize()), 0, (float) Math.floor(owner.getNode().getWorldTranslation().add(UMC.mult(1f)).getZ() / gs.getWorldGrid().getCellSize()));

                    if (locationAfterMovementInCell.getZ() < gs.getTileDataMap()[0][0].length && locationAfterMovementInCell.getZ() > -1 && gs.getTileDataMap()[0][(int) Math.floor(owner.getNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) locationAfterMovementInCell.getZ()].getTileType() == TileType.FLOOR) {
                        owner.getNode().move(0, 0, ultimateMoveVec.getZ());
                    }
                    if (locationAfterMovementInCell.getX() < gs.getTileDataMap()[0].length && locationAfterMovementInCell.getX() > -1 && gs.getTileDataMap()[0][(int) locationAfterMovementInCell.getX()][(int) Math.floor(owner.getNode().getWorldTranslation().getZ() / gs.getWorldGrid().getCellSize())].getTileType() == TileType.FLOOR) {
                        owner.getNode().move(ultimateMoveVec.getX(), 0, 0);
                    }

                }
            } else {
                if (!owner.getACh().getAnimationName().equals("Idle")) {
                    owner.getACh().setAnim("Idle");
                    System.out.println("ustawiam animacje run");
                }
            }

            // Movement goes here
            owner.insert(gs.getWorldGrid());

        } else {
            owner.removeFromGrid(gs.getWorldGrid());

            owner.getNode().removeControl(this);
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final HumanoidControl control = new HumanoidControl(owner, gs);
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
}
