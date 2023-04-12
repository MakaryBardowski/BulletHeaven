/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
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
public class BulletTraceControl extends AbstractControl implements Savable, Cloneable {

    private Material owner; // can have custom fields -- example
    private float currColor;
    
    public static Random RANDOM = new Random();

    public BulletTraceControl(Material owner,float currColor) {
        this.owner = owner;
        this.currColor = currColor;
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
//        /* Example:
//        if (spatial != null){
//        }
//        else{
//            // cleanup
//        }
         
    }

    /**
     * Implement your spatial's behaviour here. From here you can modify the
     * scene graph and the spatial (transform them, get and set userdata, etc).
     * This loop controls the spatial while the Control is enabled.
     */
    @Override
    protected void controlUpdate(float tpf) {
        currColor -= tpf;
        owner.setColor("Color", new ColorRGBA(169f/255f,169f/255f,169f/255f,currColor));

        if(currColor <= 0){
        spatial.removeFromParent();
        spatial.removeControl(this);
        
        }
            
        
        
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final BulletTraceControl control = new BulletTraceControl(owner,currColor);
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

