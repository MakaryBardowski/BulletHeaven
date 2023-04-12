/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

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

/**
 *
 * @author 48793
 */
public class ThrownGrenadeControl extends AbstractControl implements Savable, Cloneable {

    private ThrownGrenade owner; // can have custom fields -- example
    private GameState gs;
    private int bounceCounter = 1;
    private float velocityX = 18;
    private float velocityY = 18;
    public static Random RANDOM = new Random();
Vector3f ultimateMoveVec ;
Vector3f UMC; 
Vector3f locationAfterMovementInCell;
    public ThrownGrenadeControl(ThrownGrenade owner, GameState gs) {
        this.owner = owner;
        this.gs = gs;
        owner.getPositionNode().lookAt(owner.getDestination(), Vector3f.UNIT_Y);

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
        if (spatial != null && owner.getPositionNode().getWorldTranslation().getY() >= 18 * tpf) {

             ultimateMoveVec = new Vector3f(       owner.getPositionNode().getLocalRotation().getRotationColumn(2).getX()*velocityX*tpf, owner.getPositionNode().getLocalRotation().getRotationColumn(2).getY()*velocityX*tpf , owner.getPositionNode().getLocalRotation().getRotationColumn(2).getZ()*velocityY*tpf                            );
            
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

             locationAfterMovementInCell = new Vector3f((float) Math.floor(owner.getPositionNode().getWorldTranslation().add(UMC.mult(1f)).getX() / gs.getWorldGrid().getCellSize()), 0, (float) Math.floor(owner.getPositionNode().getWorldTranslation().add(UMC.mult(1f)).getZ() / gs.getWorldGrid().getCellSize()));

            if (locationAfterMovementInCell.getZ() < gs.getTileDataMap()[0][0].length && locationAfterMovementInCell.getZ() > -1 && gs.getTileDataMap()[0][(int) Math.floor(owner.getPositionNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) locationAfterMovementInCell.getZ()].getTileType() == TileType.FLOOR) {
                owner.getPositionNode().move(0, 0, ultimateMoveVec.getZ());
            }else{
            
            velocityY = -0.8f*velocityY;
            }
            if (locationAfterMovementInCell.getX() < gs.getTileDataMap()[0].length && locationAfterMovementInCell.getX() > -1 && gs.getTileDataMap()[0][(int) locationAfterMovementInCell.getX()][(int) Math.floor(owner.getPositionNode().getWorldTranslation().getZ() / gs.getWorldGrid().getCellSize())].getTileType() == TileType.FLOOR) {
                owner.getPositionNode().move(ultimateMoveVec.getX(), 0, 0);
            }else{
                        velocityX = -0.8f*velocityX;

            }

            
            
            
            
            
            
            
            owner.getVisualNode().rotate(3 * tpf, 3 * tpf, 3 * tpf);
            owner.getPositionNode().move(0, owner.getCurrentSpeedY() * tpf, 0);
            owner.setCurrentSpeedY(owner.getCurrentSpeedY() - owner.getGravity() * tpf);

            if (velocityX > 5 * tpf) {
                velocityX -= 5 * tpf;
            } else if (velocityX < -5 * tpf) {
                velocityX += 5 * tpf;
            }
            
            if (velocityY > 5 * tpf) {
                velocityY -= 5 * tpf;
            } else if (velocityY < -5 * tpf) {
                velocityY += 5 * tpf;
            }

        } else if (bounceCounter <= 2) {
            owner.setCurrentSpeedY(5 / bounceCounter++);
            owner.getPositionNode().move(0, 18 * tpf, 0);
            System.out.println("usawiam");
        } else {

            owner.explode(gs);
            owner.getPositionNode().removeControl(this);

        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final ThrownGrenadeControl control = new ThrownGrenadeControl(owner, gs);
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
