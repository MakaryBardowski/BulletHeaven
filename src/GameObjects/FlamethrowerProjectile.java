/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import EntityControls.FlamethrowerProjectileControl;
import EntityControls.RocketProjectileControl;
import GameObjects.Mobs.Allegiance;
import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import Particles.BillboardParticle2D;
import Particles.BloodParticle;
import Particles.FlamethrowerParticleEmitter;
import com.jme3.effect.Particle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class FlamethrowerProjectile extends Projectile {

    private float blastRadius;
    private float damage;
    private Particle[] particles;

    public FlamethrowerProjectile(Node node, GameState gs, Vector3f destination, float collisionRadius, float blastRadius, Allegiance allegiance, float damage, Particle[] particles) {
        super(node, gs, destination, collisionRadius, allegiance);
        this.damage = damage;
        this.blastRadius = blastRadius;
        this.particles = particles;
        
        

        
        
        node.addControl(new FlamethrowerProjectileControl(this, gs, destination,particles));

    }

    @Override
    public void hit(GameState gs) {

        for (CollidableInterface c : getEntitiesFromTilesInRange(gs.getWorldGrid(), blastRadius)) {

            Mob mob = (Mob) c;

            if (mob.getAllegiance() != getAllegiance() && mob.getNode().getWorldTranslation().distance(getNode().getWorldTranslation()) <= blastRadius) {
                mob.receiveDamage(damage, gs, gs.getEntityManager(), null);
            }

        }



        this.getNode().removeFromParent();

    }

}
