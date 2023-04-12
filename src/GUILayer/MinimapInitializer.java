/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUILayer;

import GameStates.GameState;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author 48793
 */
public class MinimapInitializer {

    public Geometry generateMinimap(int[][][] map, AssetManager a, Node guiNode, int[] playerPos, GameState gs) {
        // Create a plane.
        int initialSize = 10;
        int scale = (int) Math.floor( gs.getSettings().getWidth()*0.014f);
        int mapToTextureSizeMultiplier = 10;
        Quad boardShape = new Quad(initialSize, initialSize);
        Geometry board = new Geometry("Board", boardShape);

// Create the board image.
        BufferedImage boardImage = new BufferedImage(map[0].length * mapToTextureSizeMultiplier, map[0][0].length * mapToTextureSizeMultiplier, BufferedImage.TYPE_INT_RGB);

        Graphics2D boardGraphics = boardImage.createGraphics();

        for (int x = 0; x < map[0].length; x++) {

            for (int y = 0; y < map[0][0].length; y++) {
                Color color = new Color(Color.HSBtoRGB(0, 1, 0.f));

                if (map[0][x][y] == 1) {
                    color = new Color(Color.HSBtoRGB(0, 1, 0.19f));

                }

                boardGraphics.setColor(color);

                boardGraphics.fillRect((y * mapToTextureSizeMultiplier) - 1, (x * mapToTextureSizeMultiplier) - 1, 30, 30);

            }

        }

        com.jme3.texture.Image image = new AWTLoader().load(boardImage, false);

        Texture2D boardTexture = new Texture2D(image);

        Material material = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");

        material.setTexture("ColorMap", boardTexture);

        board.setMaterial(material);

        guiNode.attachChild(board);
        board.scale(scale);
        int xOffset = gs.getSettings().getWidth() - scale * initialSize;
        gs.setMinimapXoffset(xOffset);
//        int xOffset = 0;

        board.move(xOffset, 0, 0);
        gs.setMinimapMovementRatio(((float)(scale*initialSize)/(float)(map[0].length * mapToTextureSizeMultiplier)*10) );
        gs.setMinimapPxSize(scale*initialSize);
        createPlayerIndicator(map, playerPos, a, guiNode, xOffset, gs,scale*initialSize,map[0].length * mapToTextureSizeMultiplier);

        return board;
    }

    public Node createPlayerIndicator(int[][][] map, int[] playerPosition, AssetManager a, Node guiNode, int xOffset, GameState gs,int minimapPxSize,int mapTextureSize ) {
        // Create a plane.
        Node playerNode = new Node("player indicator node");
        Quad boardShape = new Quad(10.0f, 10.0f);
        Geometry board = new Geometry("Board", boardShape);

        System.out.println();
        System.out.println("player pos as cell: " + Arrays.toString(playerPosition));

        Material material = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");

        material.setTexture("ColorMap", a.loadTexture("Textures/GUI/playerIndicator.png"));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
        material.setColor("Color", new ColorRGBA(0, 1, 0, .7f));

        board.setMaterial(material);

        playerNode.attachChild(board);
        float size = 0.004f*minimapPxSize;
        board.scale(size);
        guiNode.attachChild(playerNode);
        board.move(xOffset - 4 * size, -4 * size, 0); // -8 cuz the image is 32x32
        // minimap is 250px high
//    playerNode.move(Vector3f.ZERO);
        playerNode.addControl(new MinimapPlayerIndicatorControl(playerNode, gs.getPlayer(), gs.getWorldGrid().getCellSize(),((float)minimapPxSize/(float)mapTextureSize)*10 ));
        return playerNode;
    }

}
