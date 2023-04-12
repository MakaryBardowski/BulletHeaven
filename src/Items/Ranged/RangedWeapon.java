/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Ranged;

import EntityControls.ParticleEmitControl;
import GameObjects.Mobs.Allegiance;
import GameObjects.Mobs.HelicopterBoss1;
import GameStates.GameState;
import Items.Weapon;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import java.util.Random;


/**
 *
 * @author 48793
 */
public abstract class RangedWeapon extends Weapon{
       private float maxAmmo;
       private float currentAmmo;
       private float accuracyPenalty; // in degrees
       private boolean showMuzzleFlash;
       private int muzzleFlashTime;
       
    public RangedWeapon(Node node, String name, String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable, float damage,float attackSpeed,float accuracyPenalty,boolean showMuzzleFlash,int muzzleFlashTime,int ca,int ma) {
        super(node, name, iconPath, texturePath,dropPath,dropTexturePath,mobPath,mobTexturePath, droppable, damage,attackSpeed);
        this.accuracyPenalty = accuracyPenalty;
        this.showMuzzleFlash = showMuzzleFlash;
        this.muzzleFlashTime = muzzleFlashTime;
        currentAmmo = ca;
        maxAmmo = ma;

    }

    public float getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(float maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public float getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(float currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public float getAccuracyPenalty() {
        return accuracyPenalty;
    }

    public void setAccuracyPenalty(float accuracyPenalty) {
        this.accuracyPenalty = accuracyPenalty;
    }

    public boolean isShowMuzzleFlash() {
        return showMuzzleFlash;
    }

    public void setShowMuzzleFlash(boolean showMuzzleFlash) {
        this.showMuzzleFlash = showMuzzleFlash;
    }

    public int getMuzzleFlashTime() {
        return muzzleFlashTime;
    }

    public void setMuzzleFlashTime(int muzzleFlashTime) {
        this.muzzleFlashTime = muzzleFlashTime;
    }
       
    
    
 
       
   
    
}
