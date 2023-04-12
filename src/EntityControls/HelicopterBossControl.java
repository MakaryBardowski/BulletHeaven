/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.HelicopterBoss1;
import GameObjects.Mobs.Mob;
import GameStates.GameState;
import Map.Generator.TileType;
import com.jme3.animation.LoopMode;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class HelicopterBossControl extends AbstractControl implements Savable, Cloneable {

    private HelicopterBoss1 owner; // can have custom fields -- example
    private GameState gs;
    Vector3f ultimateMoveVec = new Vector3f(0, 0, 0);
    Vector3f UMC;
    Vector3f locationAfterMovementInCell;
    boolean isRising = true;
    float verticalMovementSpeed = 1f;

    int maxAltitude = 35; //35
    int minAltitude = 30; //30

    boolean engineBrokenR;
    boolean engineBrokenL;
    float pitch = 0f;

    float forwardMultiplier = 0;
    float rotationMultiplier = 0;

    Vector3f pull = new Vector3f(0, 0, 0);

    float speedGainSineArgument = 0;
    float shakingCnt = 0;

    Vector3f currVisualPos = null;
//    float destroyedFallingSpeed = -FastMath.PI/2; // pops up after destroyed
    float destroyedFallingSpeed = -2 * FastMath.PI / 2;

    public static Random RANDOM = new Random();

    public HelicopterBossControl(HelicopterBoss1 owner, GameState gs) {
        this.owner = owner;
        this.gs = gs;
        owner.getVisualNode().rotate(FastMath.DEG_TO_RAD * 13, 0, 0);
        owner.getNode().move(0, minAltitude, 0);
        currVisualPos = owner.getVisualNode().getWorldTranslation();

//            Box box1 = new Box(1,1,1);
//    Geometry blue = new Geometry("Box", box1);
//    blue.setLocalTranslation(new Vector3f(1,-1,1));
//    Material mat1 = new Material(gs.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
//    mat1.setColor("Color", ColorRGBA.Blue);
//    blue.setMaterial(mat1);
//    owner.getVisualNode().attachChild(blue);
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
        
        if (spatial != null && !owner.isDead()) {
            owner.removeFromGrid(gs.getWorldGrid());
            currVisualPos = owner.getVisualNode().getWorldTranslation();

            //Movement goes here
            Mob target = null;
            for (CollidableInterface m : owner.getEntitiesFromTilesInRange(gs.getWorldGrid(), owner.getRof())) {
                Mob mob = (Mob) m;
                if (mob.getAllegiance() != owner.getAllegiance() && mob.getNode().getWorldTranslation().distance(owner.getNode().getWorldTranslation()) <= owner.getRof()) {
                    owner.setCurrentTarget(mob);
                    target = mob;
                }
            }

            if (gs.getPlayer().getAllegiance() != owner.getAllegiance() && gs.getPlayer().getPositionNode().getWorldTranslation().distance(owner.getNode().getWorldTranslation()) <= 5000) {
                owner.setCurrentTarget(gs.getPlayer());
                target = gs.getPlayer();
//                gs.getPlayer().setHealth(0);
            }

            checkIfEnginesBroken(tpf);
            ultimateMoveVec = Vector3f.ZERO.clone();
            if (target != null) {

                float dist = target.getNode().getWorldTranslation().distance(owner.getNode().getWorldTranslation());
                if (dist > 80) {
                    owner.getNode().lookAt(target.getNode().getWorldTranslation().clone().setY(owner.getNode().getWorldTranslation().getY()), Vector3f.UNIT_Y);

                    forwardMultiplier = calculateForwardMultiplier(tpf);
                    ultimateMoveVec.addLocal(findForward().subtract(owner.getNode().getWorldTranslation()).normalize());
                    move(tpf, ultimateMoveVec, true);

                } else if (dist < 80) {

                    forwardMultiplier = calculateBackwardMultiplier(tpf);

                    ultimateMoveVec.addLocal(findBackward().subtract(owner.getNode().getWorldTranslation()).normalize());
                    move(tpf, ultimateMoveVec, false);

                } else {

                    hover(tpf);

                    move(tpf, ultimateMoveVec, true);

                }

            } else {
                hover(tpf);

            }

            rotationMultiplier += tpf;
            if (rotationMultiplier > FastMath.PI) {
                rotationMultiplier = -FastMath.PI;
            }

            if (rotationMultiplier < -FastMath.PI) {
                rotationMultiplier = -FastMath.PI;
            }

            owner.getVisualNode().rotate(FastMath.sin(rotationMultiplier) * tpf / 30, 0, 0);

            // Movement goes here
            owner.insert(gs.getWorldGrid());
            

        } else {

            if (owner.getNode().getWorldTranslation().getY() <= 0) {
                owner.removeFromGrid(gs.getWorldGrid());
                owner.getNode().removeControl(this);
            } else {
                if (destroyedFallingSpeed < -FastMath.PI/2) {
                    destroyedFallingSpeed += tpf * 1;
                } else {
                    destroyedFallingSpeed += tpf * 3;
                }

                if (destroyedFallingSpeed >= FastMath.PI / 2) {
                    destroyedFallingSpeed = FastMath.PI / 2;

                }
                owner.getNode().rotate(tpf / 3, tpf * 3, 0);
                owner.getNode().move(owner.getNode().getLocalRotation().getRotationColumn(2).multLocal(tpf * FastMath.sin(destroyedFallingSpeed)*30));
                owner.getNode().move(0, -tpf *( FastMath.sin(destroyedFallingSpeed)*0.7f * 30  +9 ), 0);
            }
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final HelicopterBossControl control = new HelicopterBossControl(owner, gs);
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

    private void hover(float tpf) {
        if (isRising) {
            verticalMovementSpeed = (float) (10 / ((Math.pow((currVisualPos.getY() - 32.5), 2) / 30) + 1.2f) - 6.8f);

//                            verticalMovementSpeed = (float) (10/(( Math.pow((currVisualPos.getY()-(  (maxAltitude-minAltitude)/2  )),2)/30)+1.2f) -6.8f);
            owner.getVisualNode().move(0, verticalMovementSpeed * 0.3f * tpf, 0);

            if (currVisualPos.getY() > maxAltitude) {
                isRising = !isRising;
            }

        } else if (!isRising) {
            verticalMovementSpeed = (float) (10 / ((Math.pow((currVisualPos.getY() - 32.5), 2) / 30) + 1.2f) - 6.8f);
//                            verticalMovementSpeed = (float) (10/(( Math.pow((currVisualPos.getY()-(  (maxAltitude-minAltitude)/2  )),2)/30)+1.2f) -6.8f);

            owner.getVisualNode().move(0, -verticalMovementSpeed * 0.3f * tpf, 0);

            if (currVisualPos.getY() < minAltitude) {
                isRising = !isRising;
            }
        }

    }

    private void destroyEngineL(float tpf) {
//  Vector3f forward = new Vector3f(0,0,-1); // backward
        Vector3f forward = new Vector3f(1, 0, 0);

        owner.getNode().localToWorld(forward, forward);
//owner.getNode().rotate(0, tpf, 0);

        pull = forward;
        engineBrokenL = true;
    }

    private void destroyEngineR(float tpf) {
//  Vector3f forward = new Vector3f(0,0,-1); // backward
        Vector3f forward = new Vector3f(1, 0, 0);

        owner.getNode().localToWorld(forward, forward);
//owner.getNode().rotate(0, tpf, 0);

        pull = forward;
        engineBrokenR = true;
    }

    private void move(float tpf, Vector3f movementVec, boolean fwd) {
        if (fwd) {
            owner.getNode().move(movementVec.multLocal(tpf * (forwardMultiplier) * owner.getBaseSpeed() * 5));
        } else {
            owner.getNode().move(movementVec.multLocal(tpf * (-forwardMultiplier) * owner.getBaseSpeed() * 6));
        }

    }

    private Vector3f findLeft() {
        Vector3f forward = new Vector3f(-1, 0, 0);

        owner.getNode().localToWorld(forward, forward);

        return forward;
    }

    private Vector3f findRight() {
        Vector3f forward = new Vector3f(1, 0, 0);

        owner.getNode().localToWorld(forward, forward);

        return forward;

    }

    private Vector3f findForward() {
        Vector3f forward = new Vector3f(0, 0, 1);

        owner.getNode().localToWorld(forward, forward);

        return forward;

    }

    private Vector3f findBackward() {
        Vector3f forward = new Vector3f(0, 0, -1);

        owner.getNode().localToWorld(forward, forward);

        return forward;

    }

    private float calculateForwardMultiplier(float tpf) {
        speedGainSineArgument += tpf * 2;
////            
        if (speedGainSineArgument > FastMath.PI) {
            speedGainSineArgument = FastMath.PI;
        }

        forwardMultiplier = FastMath.sin(speedGainSineArgument / 2) * 0.4f;

//        if (pitch < FastMath.DEG_TO_RAD * 15) {
//               owner.getVisualNode().rotate(FastMath.sin(rotationMultiplier)*tpf, 0, 0);
//            pitch +=FastMath.sin(rotationMultiplier)*tpf;
//        }
        if (forwardMultiplier > 1) {
            forwardMultiplier = 1;
        }

        return forwardMultiplier;
    }

    private float calculateBackwardMultiplier(float tpf) {

        speedGainSineArgument -= tpf * 2;
////            
        if (speedGainSineArgument < -FastMath.PI) {
            speedGainSineArgument = -FastMath.PI;
        }

        forwardMultiplier = FastMath.sin(speedGainSineArgument / 2) * 0.4f;

        if (forwardMultiplier < -1) {
            forwardMultiplier = -1;
        }

//        if (pitch > FastMath.DEG_TO_RAD * 3) {
//               owner.getVisualNode().rotate(FastMath.sin(rotationMultiplier)*tpf, 0, 0);
//            pitch += FastMath.sin(rotationMultiplier)*tpf;
//
//        }
        return forwardMultiplier;
    }

    private void checkIfEnginesBroken(float tpf) {
        if (!engineBrokenR && owner.getHpEngineR() <= 0) {
            destroyEngineR(tpf);
        }

        if (!engineBrokenL && owner.getHpEngineL() <= 0) {
            destroyEngineL(tpf);

        }
    }

}
