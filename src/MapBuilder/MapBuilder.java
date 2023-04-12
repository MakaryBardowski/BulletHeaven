/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapBuilder;

import GameStates.GameState;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.LodControl;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.texture.Texture;
import jme3tools.optimize.LodGenerator;

/**
 *
 * @author 48793
 */
public class MapBuilder {

    public Node addPlane(GameState gs, float x, float y, Vector3f initialOffset, float[] rotation,Node node) {
       Node floor = (Node) gs.getAssetManager().loadModel("Models/floorTile/floorTile.j3o");
                    LightingMethods.setLightingMaterial(floor, "Textures/walls/testRoomTile.png", LIGHT_MAGNITUDE, gs.getAssetManager());
//                 

        
        node.attachChild(floor);
        floor.scale(0.5f * gs.getWorldGrid().getCellSize());

        floor.move(x * gs.getWorldGrid().getCellSize(), 0, y * gs.getWorldGrid().getCellSize());
        floor.move(initialOffset);
        System.out.println(floor.getWorldTranslation());
        floor.rotate(rotation[0], rotation[1], rotation[2]);
        floor.setName("podloga");
        return floor;
    }
    
       public Node addFloorPlane(GameState gs, float x, float y, Vector3f initialOffset, float[] rotation,Node node) {
       Node floor = (Node) gs.getAssetManager().loadModel("Models/floorTile/floorTile.j3o");
                    LightingMethods.setLightingMaterial(floor, "Textures/walls/TEST_DIRT.png", LIGHT_MAGNITUDE, gs.getAssetManager());
//                 

        
        node.attachChild(floor);
        floor.scale(0.5f * gs.getWorldGrid().getCellSize());

        floor.move(x * gs.getWorldGrid().getCellSize(), 0, y * gs.getWorldGrid().getCellSize());
        floor.move(initialOffset);
        System.out.println(floor.getWorldTranslation());
        floor.rotate(rotation[0], rotation[1], rotation[2]);
        floor.setName("podloga");
        return floor;
    }
 
    
    

}
