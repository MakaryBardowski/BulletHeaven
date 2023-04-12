/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameStates.GameState;
import MapBuilder.WorldGrid;
import Particles.BillboardParticle2D;
import Particles.BloodParticle;
import Particles.SparkParticle;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Sphere;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author 48793
 */
public class FragGrenade extends ThrownGrenade {

    public FragGrenade(Node positionNode, Node visualNode, Vector3f start, Vector3f destination, float gravity, float currentSpeedY, float damage, float blastRadius) {
        super(positionNode, visualNode, start, destination, gravity, currentSpeedY, damage, blastRadius);
    }

    @Override
    public void explode(GameState gs) {

        Vector3f particlePos;
        for (CollidableInterface c : getEntitiesFromTilesInRange(gs.getWorldGrid(), getBlastRadius())) {

            Mob mob = (Mob) c;
            if (mob.getNode().getWorldTranslation().distance(getPositionNode().getWorldTranslation()) <= getBlastRadius()) {
                mob.receiveDamage(getDamage(), gs, gs.getEntityManager(),null);
                particlePos = mob.getNode().getWorldTranslation().clone();
                particlePos.setY(1);
                BloodParticle.createBloodParticlesLarge(gs, gs.getDebugNode(), particlePos, gs.getEntityManager());
            }

        }
        
                        BillboardParticle2D.explosionEffect(gs.getDebugNode(),getPositionNode().getWorldTranslation(),gs,1.5f);

        
//        SparkParticle.createGrenadeSparks(gs, gs.getDebugNode(), getPositionNode().getWorldTranslation(), gs.getEntityManager());
        this.getPositionNode().removeFromParent();
        this.getVisualNode().removeFromParent();




        
//        Material mark_mat1 = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        mark_mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
//        mark_mat1.setColor("Color", new ColorRGBA(1, 0, 0, .2f));
//
//        Sphere sphere1 = new Sphere(30, 30, getBlastRadius());
//        Spatial mark1 = new Geometry("BOOM!", sphere1);
//        mark1.setMaterial(mark_mat1);
//        gs.getDebugNode().attachChild(mark1);
//        mark1.move(getPositionNode().getWorldTranslation());
//        mark1.setQueueBucket(RenderQueue.Bucket.Transparent);

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
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getPositionNode().getWorldTranslation().getX() - (getRadius()), 0,
                getPositionNode().getWorldTranslation().getZ() - (getRadius()));

        Vector3f max = new Vector3f(
                getPositionNode().getWorldTranslation().getX() + (getRadius()), 0,
                getPositionNode().getWorldTranslation().getZ() + (getRadius()));

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

    @Override
    public HashSet<CollidableInterface> getEntitiesFromTilesInRange(WorldGrid wg, float distance) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getPositionNode().getWorldTranslation().getX() - (distance), 0,
                getPositionNode().getWorldTranslation().getZ() - (distance));

        Vector3f max = new Vector3f(
                getPositionNode().getWorldTranslation().getX() + (distance), 0,
                getPositionNode().getWorldTranslation().getZ() + (distance));

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

}
