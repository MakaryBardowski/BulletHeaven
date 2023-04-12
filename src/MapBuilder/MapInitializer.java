/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MapBuilder;

import GameStates.GameState;
import Map.Generator.MapGenerationResult;
import Map.Generator.MapGenerator;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;

/**
 *
 * @author 48793
 */
public class MapInitializer {

    private AssetManager assetManager;

    public MapInitializer(AssetManager assetManager) {
        this.assetManager = assetManager;

    }

    public MapGenerationResult initialize(Node lightNode, Node floorNode, GameState gs, int gridSize,int mapSize,int expectedRooms) {
        initLight(lightNode);
        MapGenerator mg = new MapGenerator();
        MapGenerationResult mgr = mg.generateMap(mapSize, expectedRooms);
        mgr.setWorldGrid(setupWorldGrid(gs, gridSize));
        attachCoordinateAxes(gs.getDebugNode(), Vector3f.ZERO, gs);
        return mgr;
    }

    private WorldGrid setupWorldGrid(GameState gs, int gridSize) {
        WorldGrid grid = new WorldGrid(gridSize);
//        drawGrid(gs,grid);
        return grid;
    }

    private void initLight(Node lightNode) {
//        DirectionalLight sun = new DirectionalLight();
//        sun.setColor(ColorRGBA.White.mult(1.25f)); //1.25f 
//        lightNode.addLight(sun); // shouldnt be any light from above, only the light player gives off (min value)

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.05f));
        lightNode.addLight(al);
    }

    private void drawGrid(GameState gs, WorldGrid grid) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(2f);

        mat.setColor("Color", ColorRGBA.Magenta);

        //wizualizacja glownej siatki
        for (int i = -15; i < 15; i++) {
            for (int j = -15; j < 15; j++) {
                Geometry g = new Geometry("wireframe grid", new Grid(2, 2, 1f));

                g.setMaterial(mat);
                g.center().move(grid.getCellSize() * i + 0.5f, 0, grid.getCellSize() * j + 0.5f);
                g.scale(grid.getCellSize());
                gs.getDebugNode().attachChild(g);
            }
        }
    }

    //jmonkey exclusive
    public static void attachCoordinateAxes(Node node, Vector3f pos, GameState gs) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X.mult(50));
        putShape(node, arrow, ColorRGBA.Red, gs).setLocalTranslation(pos); //x - czerwony

        arrow = new Arrow(Vector3f.UNIT_Y);
        putShape(node, arrow, ColorRGBA.Green, gs).setLocalTranslation(pos);//y - zielony

        arrow = new Arrow(Vector3f.UNIT_Z.mult(50));
        putShape(node, arrow, ColorRGBA.Blue, gs).setLocalTranslation(pos);//z - niebieski
    }

    public static Geometry putShape(Node node, Mesh shape, ColorRGBA color, GameState gs) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(4);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        node.attachChild(g);
        return g;
    }

}
