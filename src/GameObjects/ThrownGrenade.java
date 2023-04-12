/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public abstract class ThrownGrenade extends ThrownObject implements ExplosiveInterface{
    private float blastRadius;
    private float damage;
    public ThrownGrenade(Node positioNode,Node visualNode ,Vector3f start, Vector3f destination,float gravity, float currentSpeedY,float damage,float blastRadius) {
        super(positioNode,visualNode, start, destination,gravity,currentSpeedY);
        this.blastRadius = blastRadius;
        this.damage = damage;
    }

    public float getBlastRadius() {
        return blastRadius;
    }

    public void setBlastRadius(float blastRadius) {
        this.blastRadius = blastRadius;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
    
    
    
}
