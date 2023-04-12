/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import EntityControls.RocketProjectileControl;
import GameObjects.Mobs.Allegiance;
import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import Particles.BillboardParticle2D;
import Particles.BloodParticle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public class RocketProjectile extends Projectile{

    private float blastRadius; 
    private float damage;
    
    public RocketProjectile(Node node, GameState gs, Vector3f destination,float collisionRadius, float blastRadius,Allegiance allegiance,float damage) {
        super(node, gs, destination,collisionRadius,allegiance);
        this.damage = damage;
        this.blastRadius = blastRadius;
                node.addControl(new RocketProjectileControl(this,gs,destination));

    }

        @Override
    public void hit(GameState gs) {
        
        Vector3f particlePos;
        for (CollidableInterface c : getEntitiesFromTilesInRange(gs.getWorldGrid(), blastRadius)) {

            Mob mob = (Mob) c;
            
            
            if (mob.getNode().getWorldTranslation().distance(getNode().getWorldTranslation()) <= blastRadius) {
                mob.receiveDamage(damage, gs, gs.getEntityManager(),null);
                System.out.println(mob);
                particlePos = mob.getNode().getWorldTranslation().clone();
                particlePos.setY(1);
                BloodParticle.createBloodParticlesLarge(gs, gs.getDebugNode(), particlePos, gs.getEntityManager());
            }

        }
        
                        BillboardParticle2D.explosionEffect(gs.getDebugNode(),getNode().getWorldTranslation(),gs,1.5f);

//        LightingMethods.createExplosionFlash(getNode().getWorldTranslation().clone(), 250, 6, 0.3f, gs);
//        SparkParticle.createGrenadeSparks(gs, gs.getDebugNode(), getPositionNode().getWorldTranslation(), gs.getEntityManager());
        this.getNode().removeFromParent();


    }
    
}
