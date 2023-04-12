/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.Mob;
import GameStates.GameState;
import MapBuilder.LightingMethods;
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
public class LightFlashControl extends AbstractControl implements Savable, Cloneable {

    private float expirationTime;
    private float timePassed;
    private int lightRange;
    private float lightStrength;
    private GameState gs;

    public LightFlashControl(GameState gs, float expirationTimeMillis, int lightRange, float lightStrength) {
        expirationTime = expirationTimeMillis / 1000;
        this.lightRange = lightRange;
        this.lightStrength = lightStrength;
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
        if (spatial != null){
                    LightingMethods.updateLightingInLocationLinearGradient(spatial.getWorldTranslation(), lightRange, lightStrength, gs);
//                    LightingMethods.deleteLightingInLocationLinearGradient(spatial.getWorldTranslation(), lightRange, lightStrength, gs);

        }
    
    }

    /**
     * Implement your spatial's behaviour here. From here you can modify the
     * scene graph and the spatial (transform them, get and set userdata, etc).
     * This loop controls the spatial while the Control is enabled.
     */
    @Override
    protected void controlUpdate(float tpf) {
        timePassed += tpf;
//            LightingMethods.updateLightingInLocationLinearGradient(spatial.getWorldTranslation(), lightRange, lightStrength, gs);
//                    LightingMethods.deleteLightingInLocationLinearGradient(spatial.getWorldTranslation(), lightRange, lightStrength, gs);

        if (timePassed >= expirationTime && spatial != null) {

            LightingMethods.terminateLightSource(spatial.getWorldTranslation(), lightRange, gs);

            

            
            spatial.removeFromParent();
            spatial.removeControl(this);
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final LightFlashControl control = new LightFlashControl(gs, expirationTime, lightRange, lightStrength);
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
