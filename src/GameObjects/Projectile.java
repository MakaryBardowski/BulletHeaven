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
import GameStates.GameState;
import MapBuilder.WorldGrid;
import Particles.BillboardParticle2D;
import Particles.BloodParticle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashSet;

/**
 *
 * @author 48793
 */
public abstract class Projectile extends GameObject implements ProjectileInterface {
    
    private Node node;
    private float collisionRange; // range on which the collision occurs
    private Allegiance allegiance;
    public Projectile(Node node,GameState gs,Vector3f destination,float collisionRange,Allegiance allegiance) {
        this.node = node;
        this.collisionRange = collisionRange;
        this.allegiance = allegiance;
    }
    
    

    @Override
    public void checkCollision() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCollision() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insert(WorldGrid wg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeFromGrid(WorldGrid wg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashSet<CollidableInterface> getFromCellsImIn(WorldGrid wg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     @Override
    public HashSet<CollidableInterface> getEntitiesFromTilesInRange(WorldGrid wg, float distance) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (distance), 0,
                getNode().getWorldTranslation().getZ() - (distance));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (distance), 0,
                getNode().getWorldTranslation().getZ() + (distance));

        min.setX(wg.getCellSize() * (int) (Math.floor(min.getX() / wg.getCellSize())));
        min.setZ(wg.getCellSize() * (int) (Math.floor(min.getZ() / wg.getCellSize())));

        max.setX(wg.getCellSize() * (int) (Math.floor(max.getX() / wg.getCellSize())));
        max.setZ(wg.getCellSize() * (int) (Math.floor(max.getZ() / wg.getCellSize())));
        Vector3f pom = new Vector3f();

        for (int x = (int) min.x; x <= max.x; x += wg.getCellSize()) {
            for (int z = (int) min.z; z <= max.z; z += wg.getCellSize()) {
                pom.set(x, 0, z);
                if (wg.getContents().get(wg.hash(pom)) != null) {
                    output.addAll(wg.getContents().get(wg.hash(new Vector3f(x, 0, z))));
                }

            }
        }

        return output;

    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public float getCollisionRange() {
        return collisionRange;
    }

    public void setCollisionRange(float collisionRange) {
        this.collisionRange = collisionRange;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }


    
    
}
