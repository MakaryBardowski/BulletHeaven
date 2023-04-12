/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import EntityControls.ParticleEmitControl;
import EntityControls.PlayerMuzzleFlashControl;
import GameStates.GameState;
import Items.Item;
import Items.Item;
import Items.ItemCreator;
import Items.LeftHandEquippableItem;
import Items.Melee.MeleeWeapon;
import Items.Ranged.RangedWeapon;
import Items.RightHandEquippableItem;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;

/**
 *
 * @author 48793
 */
public abstract class Weapon extends Item implements WeaponInterface{

    private boolean isTwoHanded;
    private float attackSpeed;
    private float damage;

    public Weapon(Node node, String name, String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable, float damage, float attackSpeed) {
        super(node, name, iconPath, texturePath,dropPath,dropTexturePath,mobPath,mobTexturePath,droppable);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    

    public boolean isIsTwoHanded() {
        return isTwoHanded;
    }

    public void setIsTwoHanded(boolean isTwoHanded) {
        this.isTwoHanded = isTwoHanded;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

}
