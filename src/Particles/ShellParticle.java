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
public class ShellParticle extends Particle3d{
    
       public ShellParticle(Node node, Vector3f direction, boolean isRising, Vector3f flyDirection) {
        super(node, direction, isRising, flyDirection);
    }
       
          public ShellParticle(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection,float fallSpeed) {
        super(node,direction,isRising,flyDirection,fallSpeed);
    }
        
        public ShellParticle(Node node,Vector3f direction, boolean isRising, Vector3f flyDirection,float fallSpeed,float rotationSpeed) {
        super(node,direction,isRising,flyDirection,fallSpeed,rotationSpeed);
    }
   
     
      public static void createShell(GameState gs,Node noteToBeAttachedTo,Vector3f offset, EntityManager em,Vector3f direction){
    Random r = new Random();
        Vector3f bloodSplashDirection = new Vector3f(0,0,0);

                for(int i = 0 ;i<1;i++){
                      
                Node bloodDrop = (Node) gs.getAssetManager().loadModel("Models/Shell/Shell.j3o");
                noteToBeAttachedTo.attachChild(bloodDrop);
//                bloodDrop.move(offset);
                    LightingMethods.setLightingMaterial(bloodDrop, "Textures/assetTextures/WeaponsPalette/Shell.png", 0.7f, gs.getAssetManager());

                     bloodSplashDirection =direction;

                     float angle = r.nextFloat()*1.5f-1.5f;
                     angle = (float) Math.toRadians(angle);
                     bloodSplashDirection.setX(  (float)(Math.cos(angle)*direction.getX()) - ((float) Math.sin(angle)*direction.getZ()));
                     bloodSplashDirection.setZ(  (float)Math.sin(angle)*direction.getX() + (float) Math.cos(angle)*direction.getZ());
                     
                     bloodSplashDirection.setY(  (float)Math.sin(Math.toDegrees(angle))*direction.getX() + (float) Math.cos(Math.toDegrees(angle))*direction.getY());


                    ShellParticle bp = new ShellParticle(bloodDrop,bloodDrop.getWorldTranslation().clone(),true, bloodSplashDirection.mult(19) ,12,0);
                    em.getShellParticles().add(bp);
                }
    
    }

 
     
     
}
