/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityControls;

import Particles.ShellParticle;
import Particles.BloodParticle;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.MobInterface;
import GameStates.GameState;
import Items.Item;
import Map.Generator.TileType;
import Particles.SparkParticle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class EntityManager {

    private HashMap<String, Mob> enemiesAlive;
    private HashMap<String, Item> pickableItems;

    private HashSet<BloodParticle> bloodParticles;
        private HashSet<SparkParticle> sparkParticles;

        private HashSet<ShellParticle> shellParticles;

        private Vector3f itemLocationAfterMovementInCell;
        private Vector3f itemUltimateMoveVec;
        private Vector3f itemUMC;
        private float itemVelocityX;
        private float itemVelocityY;

    public EntityManager() {
        enemiesAlive = new HashMap<>();
        bloodParticles = new HashSet<>();
        sparkParticles = new HashSet<>();
        shellParticles = new HashSet<>();
        pickableItems = new HashMap<>();
    }


    
    public void updateParticles(float tpf){
        updateShellParticles(tpf);
        updateSparkParticles(tpf);
        updateBloodParticles(tpf);
        
    }
    

    public void updateBloodParticles(float tpf) {
        Random r = new Random();
    for (Iterator<BloodParticle> i = bloodParticles.iterator(); i.hasNext();) {
        BloodParticle bp = i.next();

            bp.getFlyDirection().setY(bp.getFlyDirection().getY() - bp.getFallSpeed() * tpf);

            if (bp.getNode().getWorldTranslation().getY() >= 0) {

                bp.getNode().rotate(r.nextFloat() * 19 * tpf, bp.getFallSpeed() * tpf, r.nextFloat() * 19 * tpf);

                if (bp.isIsRising()) {
                    bp.getNode().move(bp.getFlyDirection().getX() * tpf, bp.getFlyDirection().getY() * tpf, bp.getFlyDirection().getZ() * tpf);

                    if (bp.getNode().getWorldTranslation().getY() < 0) {
                        bp.setIsRising(false);
                    }

                } else {
                    bp.getNode().move(bp.getFlyDirection().getX() * tpf, bp.getFlyDirection().getY() * tpf, bp.getFlyDirection().getZ() * tpf);
                }
            }

            if (bp.getFlyDirection().getY() <= -0.84 * bp.getFallSpeed() ) {
                bp.getNode().removeFromParent();
                bp = null;
                i.remove();
            }

        }

    }
    
    
     public void updateSparkParticles(float tpf) {
        Random r = new Random();
    for (Iterator<SparkParticle> i = sparkParticles.iterator(); i.hasNext();) {
        SparkParticle bp = i.next();

            bp.getFlyDirection().setY(bp.getFlyDirection().getY() - bp.getFallSpeed() * tpf);

            if (bp.getNode().getWorldTranslation().getY() >= 0) {

                bp.getNode().rotate(r.nextFloat() * 19 * tpf, bp.getFallSpeed() * tpf, r.nextFloat() * 19 * tpf);

                if (bp.isIsRising()) {
                    bp.getNode().move(bp.getFlyDirection().getX() *5* tpf, bp.getFlyDirection().getY()*5* tpf, bp.getFlyDirection().getZ() *5* tpf);

                    if (bp.getNode().getWorldTranslation().getY() < 0) {
                        bp.setIsRising(false);
                    }

                } else {
                    bp.getNode().move(bp.getFlyDirection().getX()*5 * tpf, bp.getFlyDirection().getY() *5* tpf, bp.getFlyDirection().getZ()*5 * tpf);
                }
            }

            if (bp.getFlyDirection().getY() <= -0.84 * bp.getFallSpeed() ) {
                bp.getNode().removeFromParent();
                bp = null;
                i.remove();
            }

        }

    }
    
     public void updateShellParticles(float tpf) {
        Random r = new Random();
    for (Iterator<ShellParticle> i = shellParticles.iterator(); i.hasNext();) {
        ShellParticle bp = i.next();
            bp.getFlyDirection().setY(bp.getFlyDirection().getY() - bp.getFallSpeed() * tpf); // szybkosc opadania

            if (bp.getNode().getWorldTranslation().getY() >= -60) { // jesli jest jeszcze powyzej -60m wysokosci

                bp.getNode().rotate(r.nextFloat() * 19 * tpf, bp.getFallSpeed() * tpf, r.nextFloat() * 19 * tpf);

                if (bp.isIsRising()) {
                    bp.getNode().move(bp.getFlyDirection().getX() * tpf, bp.getFlyDirection().getY() * tpf, bp.getFlyDirection().getZ() * tpf);

                    if (bp.getNode().getWorldTranslation().getY() < 0) {
                        bp.setIsRising(false);
                    }

                } else {
                    bp.getNode().move(bp.getFlyDirection().getX() * tpf, bp.getFlyDirection().getY() * tpf, bp.getFlyDirection().getZ() * tpf);
                }
            }

            if (bp.getFlyDirection().getY() <= -0.84 * bp.getFallSpeed() - 10) {
                bp.getNode().removeFromParent();
                bp = null;
                i.remove();
            }

        }

    }
    
    
    
    
    public void updatePickableItems(float tpf,GameState gs){
    
        for(Item pickableItem : pickableItems.values()){
        
         if(!pickableItem.getIsFalling()){
             continue;
            }
            
        if(pickableItem.getIsFalling()){
          itemVelocityX =  pickableItem.getFlyingDirection()[0];
          itemVelocityY = pickableItem.getFlyingDirection()[2];
          
                   itemUltimateMoveVec = new Vector3f(    itemVelocityX *tpf,pickableItem.getFlyingDirection()[1]*tpf,itemVelocityY*tpf        );
            
             itemUMC = itemUltimateMoveVec.clone();
            if (itemUMC.getX() < 0) {
                itemUMC.setX(itemUMC.getX() - 0.1f);
            }
            if (itemUMC.getX() > 0) {
                itemUMC.setX(itemUMC.getX() + 0.1f);
            }

            if (itemUMC.getZ() < 0) {
                itemUMC.setZ(itemUMC.getZ() - 0.1f);
            }
            if (itemUMC.getZ() > 0) {
                itemUMC.setZ(itemUMC.getZ() + 0.1f);
            }

             itemLocationAfterMovementInCell = new Vector3f((float) Math.floor(pickableItem.getNode().getWorldTranslation().add(itemUMC.mult(1f)).getX() / gs.getWorldGrid().getCellSize()), 0, (float) Math.floor(pickableItem.getNode().getWorldTranslation().add(itemUMC.mult(1f)).getZ() / gs.getWorldGrid().getCellSize()));

            if (itemLocationAfterMovementInCell.getZ() < gs.getTileDataMap()[0][0].length && itemLocationAfterMovementInCell.getZ() > -1 && gs.getTileDataMap()[0][(int) Math.floor(pickableItem.getNode().getWorldTranslation().getX() / gs.getWorldGrid().getCellSize())][(int) itemLocationAfterMovementInCell.getZ()].getTileType() == TileType.FLOOR) {
                pickableItem.getNode().move(0, 0, itemUltimateMoveVec.getZ());
            }else{
            
            pickableItem.getFlyingDirection()[2] = -0.8f*pickableItem.getFlyingDirection()[2];
            }
            if (itemLocationAfterMovementInCell.getX() < gs.getTileDataMap()[0].length && itemLocationAfterMovementInCell.getX() > -1 && gs.getTileDataMap()[0][(int) itemLocationAfterMovementInCell.getX()][(int) Math.floor(pickableItem.getNode().getWorldTranslation().getZ() / gs.getWorldGrid().getCellSize())].getTileType() == TileType.FLOOR) {
                pickableItem.getNode().move(itemUltimateMoveVec.getX(), 0, 0);
            }else{
                        pickableItem.getFlyingDirection()[0] = -0.8f*pickableItem.getFlyingDirection()[0];

            }

            
            
                            pickableItem.getNode().move(0, pickableItem.getFlyingDirection()[1]*tpf, 0);

            
            
        pickableItem.getFlyingDirection()[1]=pickableItem.getFlyingDirection()[1]-10*tpf;
        
        pickableItem.getNode().rotate(pickableItem.getFlyingDirection()[3]*tpf/3,pickableItem.getFlyingDirection()[3]*tpf,pickableItem.getFlyingDirection()[3]*tpf/3);
        }
         if(pickableItem.getNode().getLocalTranslation().y <= 7*tpf){
            pickableItem.setIsFalling(false);
            pickableItem.getNode().getLocalRotation().set(0, pickableItem.getNode().getLocalRotation().getY(), 0, pickableItem.getNode().getLocalRotation().getZ());
        }
        
        
        
        }
    
    
    
    
    }
    
    public HashMap<String, Mob> getEnemiesAlive() {
        return enemiesAlive;
    }

    public void setEnemiesAlive(HashMap<String, Mob> enemiesAlive) {
        this.enemiesAlive = enemiesAlive;
    }

    public HashSet<BloodParticle> getBloodParticles() {
        return bloodParticles;
    }

    public void setBloodParticles(HashSet<BloodParticle> bloodParticles) {
        this.bloodParticles = bloodParticles;
    }

    public HashMap<String, Item> getPickableItems() {
        return pickableItems;
    }

    public void setPickableItems(HashMap<String, Item> pickableItems) {
        this.pickableItems = pickableItems;
    }

    public HashSet<ShellParticle> getShellParticles() {
        return shellParticles;
    }

    public void setShellParticles(HashSet<ShellParticle> shellParticles) {
        this.shellParticles = shellParticles;
    }

    public HashSet<SparkParticle> getSparkParticles() {
        return sparkParticles;
    }

    public void setSparkParticles(HashSet<SparkParticle> sparkParticles) {
        this.sparkParticles = sparkParticles;
    }
    
}
