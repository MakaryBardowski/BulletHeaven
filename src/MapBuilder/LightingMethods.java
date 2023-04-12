/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapBuilder;

import EntityControls.LightFlashControl;
import GameObjects.Mobs.CollidableInterface;
import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author 48793
 */
public class LightingMethods {

    public static float LIGHT_MAGNITUDE = 0.04f; // 0.04

    public static void setLightingMaterial(Node node, String texturePath, float Magnitude, AssetManager a) {
        Material mat_default = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        mat_default.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat_default.setColor("Ambient", ColorRGBA.White.mult(Magnitude));
        mat_default.setColor("Diffuse", ColorRGBA.White.mult(1));
        
//        mat_default.setBoolean("UseVertexColor", true);
//        mat_default.setBoolean( "VertexLighting", true );
   

        Texture t = a.loadTexture(new TextureKey(texturePath,false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        mat_default.setTexture("DiffuseMap", t); // with Lighting.j3md

        node.setMaterial(mat_default);

    }

    public static void modifyMobLighting(Mob m, float Magnitude, AssetManager a, ColorRGBA color) {
        if(m.getModelElementsForShading() != null)
            for (Geometry g : m.getModelElementsForShading()) {
                
                if(g == null)
                    break;
                
                g.getMaterial().setColor("Ambient", color.mult(0));
                g.getMaterial().setColor("Ambient", color.mult(Magnitude));
                g.getMaterial().setColor("Diffuse", ColorRGBA.White.mult(1));

            }
    }

    public static void setLightingMaterialColored(Node node, String texturePath, float Magnitude, AssetManager a, ColorRGBA color) {
        Material mat_default = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        mat_default.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat_default.setColor("Ambient", color.mult(Magnitude));
        mat_default.setColor("Diffuse", color.mult(1));
                    Texture t = a.loadTexture(new TextureKey(texturePath, false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        mat_default.setTexture("DiffuseMap", t); // with Lighting.j3md

        node.setMaterial(mat_default);

    }
    
    

// sets lightness for particles (wall hit sparks, rocket explosion etc)
    public static void setLightingMaterialColoredParticle(Node node, String texturePath, float Magnitude, AssetManager a, ColorRGBA color) {
        Material mat_default = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        mat_default.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat_default.setColor("Ambient", color.mult(Magnitude));
        mat_default.setColor("Diffuse", color.mult(1));
                    Texture t = a.loadTexture(new TextureKey(texturePath, false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        mat_default.setTexture("DiffuseMap", t); // with Lighting.j3md
        node.setMaterial(mat_default);

    }



// main lighting method
  
    public static void updateLightingInLocationLinearGradient(Vector3f location, int lightSourceRange, float lightSourceStrength, GameState gs) {
        int x = (int) (Math.floor(location.getX() / gs.getWorldGrid().getCellSize()));
        int y = (int) (Math.floor(location.getZ() / gs.getWorldGrid().getCellSize()));
        
        
        int cellX = (int)Math.floor(location.getX()/ gs.getWorldGrid().getCellSize()) ;
        int cellY  = (int)Math.floor(location.getZ()/ gs.getWorldGrid().getCellSize() );

        
        for (int deltaX = -lightSourceRange; deltaX < lightSourceRange; deltaX++) {

            for (int deltaY = -lightSourceRange; deltaY < lightSourceRange; deltaY++) {

                Node n = gs.getChunks().get("Chunk" + (x + deltaX) + "/" + (y + deltaY));

                Vector3f or = new Vector3f(x, 0, y);
                Vector3f ne = new Vector3f(x + deltaX, 0, y + deltaY);

                float dist = or.distance(ne);

                if (n != null) {

                    float endVal = LIGHT_MAGNITUDE;
                    float startVal = (LIGHT_MAGNITUDE + lightSourceStrength); // no lantern =LIGHT_MAGNITUDE + 0.06f
                    dist = lightSourceRange - dist;

                    if (dist < 0) {
                        dist = 0;
                    }
                    float lighting = endVal + (dist * 0.6f) * (startVal - endVal);

                    LightingMethods.setLightingMaterial(n, "Textures/walls/testRoomTile.png", lighting, gs.getAssetManager());

                    //TEST ENTITY LIGHTING - SHITTY CODE
                    for (CollidableInterface ci : gs.getWorldGrid().getInsideCell(new Vector3f(location.getX() + deltaX * gs.getWorldGrid().getCellSize(), 0, location.getZ() + deltaY * gs.getWorldGrid().getCellSize()))) {
                        Mob m = (Mob) ci;

                        LightingMethods.modifyMobLighting(m, lighting, gs.getAssetManager(), ColorRGBA.White);

                    }

                }

            }
        }
    }
    
    
  
    
    

    public static void terminateLightSource(Vector3f location, int lightSourceRange, GameState gs) { // immediately destroy light source
        int x = (int) (Math.floor(location.getX() / gs.getWorldGrid().getCellSize()));
        int y = (int) (Math.floor(location.getZ() / gs.getWorldGrid().getCellSize()));
        for (int deltaX = -lightSourceRange; deltaX < lightSourceRange; deltaX++) {

            for (int deltaY = -lightSourceRange; deltaY < lightSourceRange; deltaY++) {

                Node n = gs.getChunks().get("Chunk" + (x + deltaX) + "/" + (y + deltaY));

                Vector3f or = new Vector3f(x, 0, y);
                Vector3f ne = new Vector3f(x + deltaX, 0, y + deltaY);

                if (n != null) {

                    float lighting = LIGHT_MAGNITUDE;

                    
                    
                    LightingMethods.setLightingMaterial(n, "Textures/walls/testRoomTile.png", lighting, gs.getAssetManager());
                    

                    //TEST ENTITY LIGHTING - SHITTY CODE
                    for (CollidableInterface ci : gs.getWorldGrid().getInsideCell(new Vector3f(location.getX() + deltaX * gs.getWorldGrid().getCellSize(), 0, location.getZ() + deltaY * gs.getWorldGrid().getCellSize()))) {
                        Mob m = (Mob) ci;

                        LightingMethods.modifyMobLighting(m, lighting, gs.getAssetManager(), ColorRGBA.White);

                    }

                }

            }
        }
    }
    
    public static void createExplosionFlash(Vector3f location, int timeMillis , int lightRange, float lightStrength,GameState gs){
    Node explosionLightNode = new Node();
    gs.getDebugNode().attachChild(explosionLightNode);
    explosionLightNode.setLocalTranslation(location);
    explosionLightNode.addControl(new LightFlashControl(gs,timeMillis,lightRange,lightStrength));
    }
    
    
    
    
    
    
        
    public static void addStaticLightSource(GameState gs,Vector3f location){
        LightingMethods.updateLightingInLocationLinearGradient(location, 6, 0.16f, gs);
    }
    
    
    


}
