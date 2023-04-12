/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Throwable;

import Items.Ranged.*;
import Items.Weapon;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.Random;


/**
 *
 * @author 48793
 */
public abstract class ThrowableWeapon extends Weapon{
       private float maxAmmo;
       private float currentAmmo;

    public ThrowableWeapon(Node node, String name, String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable, float damage,float attackSpeed) {
        super(node, name, iconPath, texturePath,dropPath,dropTexturePath,mobPath,mobTexturePath, droppable, damage,attackSpeed);
        
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
       
       
   
    
}
