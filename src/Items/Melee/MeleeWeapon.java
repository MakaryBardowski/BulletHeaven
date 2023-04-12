/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Melee;

import Items.Weapon;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public abstract class MeleeWeapon extends Weapon {
    private float range;
    private float powerAttackThreshold;
    private float powerAttackCurrentTime;

    public MeleeWeapon(Node node, String name, String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable, float damage,float attackSpeed,float range) {
        super(node, name, iconPath, texturePath,dropPath,dropTexturePath,mobPath,mobTexturePath, droppable, damage,attackSpeed);
        this.range=range;
    }

    public float getPowerAttackThreshold() {
        return powerAttackThreshold;
    }

    public void setPowerAttackThreshold(float powerAttackThreshold) {
        this.powerAttackThreshold = powerAttackThreshold;
    }

    public float getPowerAttackCurrentTime() {
        return powerAttackCurrentTime;
    }

    public void setPowerAttackCurrentTime(float powerAttackCurrentTime) {
        this.powerAttackCurrentTime = powerAttackCurrentTime;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
    
    
    
    
}
