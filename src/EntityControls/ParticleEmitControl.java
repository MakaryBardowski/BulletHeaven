/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.Mob;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.animation.LoopMode;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class ParticleEmitControl extends AbstractControl implements Savable, Cloneable {

    private int numStages;
    private float timePassed;
    private float intervalsMs;

    private Node[] stages;
    private Vector3f pos;
    private int counter;
private GameState gs;
private String texturePath;
private float magnitudeMultiplier;
    public ParticleEmitControl(float intervalsMs, Node[] stages,GameState gs,String texturePath,float magnitudeMultiplier) {
        this.stages = stages;
        this.intervalsMs = intervalsMs / 1000;
        this.gs = gs;
        this.texturePath = texturePath;
        this.magnitudeMultiplier =magnitudeMultiplier;
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
        if(spatial !=null){
//                          LightingMethods.setLightingMaterial((Node)spatial, "Textures/effects/shootEffect.png", LIGHT_MAGNITUDE * 100f, gs.getAssetManager());
                          LightingMethods.setLightingMaterialColoredParticle((Node)spatial, texturePath, LIGHT_MAGNITUDE * magnitudeMultiplier, gs.getAssetManager(),ColorRGBA.White);

        pos = spatial.getWorldTranslation().clone();
        }
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
        timePassed += tpf;
        if (timePassed >= intervalsMs && counter >= stages.length && spatial != null) {
            spatial.removeFromParent();
            spatial.removeControl(this);
        }

        if (timePassed >= intervalsMs && counter < stages.length) {
            
            Node p = spatial.getParent();
            spatial.removeFromParent();
            
            spatial = stages[counter];
            p.attachChild(spatial);
            spatial.setLocalTranslation(pos);
                  spatial.addControl(new BillboardControl());
                  spatial.addControl(this);
            timePassed = 0;
            counter++;
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final ParticleEmitControl control = new ParticleEmitControl(intervalsMs, stages,gs,texturePath,magnitudeMultiplier);
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
