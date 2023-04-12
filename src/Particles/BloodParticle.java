/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Particles;

import EntityControls.EntityManager;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import Particles.Particle3d;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class BloodParticle extends Particle3d{
    
       public BloodParticle(Node node, Vector3f direction, boolean isRising, Vector3f flyDirection) {
        super(node, direction, isRising, flyDirection);
    }
       
          public BloodParticle(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection,float fallSpeed) {
        super(node,direction,isRising,flyDirection,fallSpeed);
    }
        
        public BloodParticle(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection,float fallSpeed,float rotationSpeed) {
        super(node,direction,isRising,flyDirection,fallSpeed,rotationSpeed);
    }
   

    public static void createBloodParticlesSmall(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em){
    Random r = new Random();
                for(int i = 0 ;i<r.nextInt(3)+4;i++){
                    
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/bloodDrop/bloodDrop.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
                bloodDrop.move(offset);
                bloodDrop.move(r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/colors/redcolor32.png", 0.7f, gs.getAssetManager());
                    BloodParticle bp = new BloodParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, new Vector3f(r.nextFloat()*2-1,r.nextFloat()*2,r.nextFloat()*2-1   ) );
                    em.getBloodParticles().add(bp);
                }
    
    }
    
    
     public static void createBloodParticlesMedium(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em){
    Random r = new Random();
                for(int i = 0 ;i<r.nextInt(9)+15;i++){
                    
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/bloodDrop/bloodDrop.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
                bloodDrop.move(offset);
                bloodDrop.move(r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/colors/redcolor32.png", 0.7f, gs.getAssetManager());
                    BloodParticle bp = new BloodParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, new Vector3f(r.nextFloat()*3-1.5f,r.nextFloat()*3.5f,r.nextFloat()*3-1.5f   ) );
                    em.getBloodParticles().add(bp);
                }
    
    }
     
     public static void createBloodParticlesSmall(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em,Vector3f direction){
    Random r = new Random();
        Vector3f bloodSplashDirection = new Vector3f(0,0,0);

                for(int i = 0 ;i<r.nextInt(3)+4;i++){
                      
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/bloodDrop/bloodDrop.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
                bloodDrop.move(offset);
                bloodDrop.move(r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/colors/redcolor32.png", 0.7f, gs.getAssetManager());

                     bloodSplashDirection =direction;

                     float angle = r.nextFloat()*15-7.5f;
                     angle = (float) Math.toRadians(angle);
                     bloodSplashDirection.setX(  (float)(Math.cos(angle)*direction.getX()) - ((float) Math.sin(angle)*direction.getZ()));
                     bloodSplashDirection.setZ(  (float)Math.sin(angle)*direction.getX() + (float) Math.cos(angle)*direction.getZ());
                     
                     bloodSplashDirection.setY(  (float)Math.sin(Math.toDegrees(angle))*direction.getX() + (float) Math.cos(Math.toDegrees(angle))*direction.getY());


                    BloodParticle bp = new BloodParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, bloodSplashDirection.mult(19) ,12,0);
                    em.getBloodParticles().add(bp);
                }
    
    }
    
    
     public static void createBloodParticlesMedium(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em,Vector3f direction){
    Random r = new Random();
    Vector3f bloodSplashDirection = new Vector3f(0,0,0);
                for(int i = 0 ;i<r.nextInt(16)*6+15*6;i++){
                    
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/bloodDrop/bloodDrop.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
                bloodDrop.move(offset);
                bloodDrop.move(r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/colors/redcolor32.png", 0.7f, gs.getAssetManager());

                     bloodSplashDirection =direction;

                     float angle = r.nextFloat()*15-7.5f;
                     angle = (float) Math.toRadians(angle);
                     bloodSplashDirection.setX(  (float)(Math.cos(angle)*direction.getX()) - ((float) Math.sin(angle)*direction.getZ()));
                     bloodSplashDirection.setZ(  (float)Math.sin(angle)*direction.getX() + (float) Math.cos(angle)*direction.getZ());
                     
                     bloodSplashDirection.setY(  (float)Math.sin(Math.toDegrees(angle))*direction.getX() + (float) Math.cos(Math.toDegrees(angle))*direction.getY());


                    BloodParticle bp = new BloodParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, bloodSplashDirection.mult(19) ,12,0);
                    em.getBloodParticles().add(bp);
                }

    }
    
    
     public static void createBloodParticlesLarge(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em){
    Random r = new Random();
                for(int i = 0 ;i<r.nextInt(18)+31;i++){
                    
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/bloodDrop/bloodDrop.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
                bloodDrop.move(offset);
                bloodDrop.move(r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f,r.nextFloat()*0.2f-0.1f);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/colors/redcolor32.png", 0.7f, gs.getAssetManager());
                    BloodParticle bp = new BloodParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, new Vector3f(r.nextFloat()*9-4.5f,r.nextFloat()*30.5f,r.nextFloat()*9-4.5f   ) );
                    em.getBloodParticles().add(bp);
                }
    
    }
  

 
     
     
}
