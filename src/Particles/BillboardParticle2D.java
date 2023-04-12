/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Particles;

import EntityControls.ParticleEmitControl;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;

/**
 *
 * @author 48793
 */
public class BillboardParticle2D {
       public static void wallHitSparks(Node node, Vector3f hitplace,GameState gs){
      Node sf = (Node) gs.getAssetManager().loadModel("Models/shootEffect/shootEffect1.j3o");
              LightingMethods.setLightingMaterial(sf, "Textures/effects/shootEffect.png", LIGHT_MAGNITUDE * 100f, gs.getAssetManager());
       Node sf1 = (Node) gs.getAssetManager().loadModel("Models/shootEffect/shootEffect2.j3o");
              LightingMethods.setLightingMaterial(sf, "Textures/effects/shootEffect.png", LIGHT_MAGNITUDE * 100f, gs.getAssetManager());
       Node sf2 = (Node) gs.getAssetManager().loadModel("Models/shootEffect/shootEffect3.j3o");
              LightingMethods.setLightingMaterial(sf, "Textures/effects/shootEffect.png", LIGHT_MAGNITUDE * 100f, gs.getAssetManager());
       Node sf3 = (Node) gs.getAssetManager().loadModel("Models/shootEffect/shootEffect4.j3o");
              LightingMethods.setLightingMaterial(sf, "Textures/effects/shootEffect.png", LIGHT_MAGNITUDE * 100f, gs.getAssetManager());
       


            Node[] stages = new Node[]{sf,sf1,sf2,sf3};
              node.attachChild(sf);

      sf.setLocalTranslation(hitplace);
      sf.addControl(new BillboardControl());
      sf.addControl( new ParticleEmitControl(50,stages,gs,"Textures/effects/shootEffect.png",100) ); //100 jest dynamiczne
    
    }
   
   
   
      public static void explosionEffect(Node node, Vector3f hitplace,GameState gs,float scale){
      Node sf = (Node) gs.getAssetManager().loadModel("Models/explosionEffect/explosionEffect1.j3o");
              LightingMethods.setLightingMaterialColoredParticle(sf, "Textures/effects/explosionEffect.png", LIGHT_MAGNITUDE * 1, gs.getAssetManager(),ColorRGBA.White);
     
              Node sf1 = (Node) gs.getAssetManager().loadModel("Models/explosionEffect/explosionEffect2.j3o");
              LightingMethods.setLightingMaterialColoredParticle(sf1, "Textures/effects/explosionEffect.png", LIGHT_MAGNITUDE * 1, gs.getAssetManager(),ColorRGBA.White);
     
              Node sf2 = (Node) gs.getAssetManager().loadModel("Models/explosionEffect/explosionEffect3.j3o");
              LightingMethods.setLightingMaterialColoredParticle(sf2, "Textures/effects/explosionEffect.png", LIGHT_MAGNITUDE * 1, gs.getAssetManager(),ColorRGBA.White);
      
              
              Node sf3 = (Node) gs.getAssetManager().loadModel("Models/explosionEffect/explosionEffect6Test.j3o");
              LightingMethods.setLightingMaterialColoredParticle(sf3, "Textures/effects/explosionEffect.png", LIGHT_MAGNITUDE * 1, gs.getAssetManager(),ColorRGBA.White);
       
        Node sf4 = (Node) gs.getAssetManager().loadModel("Models/explosionEffect/explosionEffect5.j3o");
              LightingMethods.setLightingMaterialColoredParticle(sf4, "Textures/effects/explosionEffect.png", LIGHT_MAGNITUDE * 1, gs.getAssetManager(),ColorRGBA.White);
       


            Node[] stages = new Node[]{sf,sf1,sf2,sf3,sf4};
            
            
          
            for(Node n : stages){
            n.scale(scale);
            }
            
              node.attachChild(sf);

              int timeMs =75; // 75
              
      sf.setLocalTranslation(hitplace);
      sf.addControl(new BillboardControl());
      sf.addControl( new ParticleEmitControl(timeMs,stages,gs,"Textures/effects/explosionEffect.png",30)); 
      
    
    }
}
