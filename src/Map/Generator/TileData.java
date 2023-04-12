/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map.Generator;

import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public class TileData {
    private boolean northWall;
    private boolean southWall;
    private boolean westWall;
    private boolean eastWall;
    private boolean isRoofed;
    private Node floorNode;
    private TileType tileType;
    
    private byte ultimateHeight; // a whole column is assigned this height value
    
    
    private float currentLightLevel = LIGHT_MAGNITUDE;
    
    
    
    public boolean isNorthWall() {
        return northWall;
    }

    public void setNorthWall(boolean northWall) {
        this.northWall = northWall;
    }

    public boolean isSouthWall() {
        return southWall;
    }

    public void setSouthWall(boolean southWall) {
        this.southWall = southWall;
    }

    public boolean isWestWall() {
        return westWall;
    }

    public void setWestWall(boolean westWall) {
        this.westWall = westWall;
    }

    public boolean isEastWall() {
        return eastWall;
    }

    public void setEastWall(boolean eastWall) {
        this.eastWall = eastWall;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public Node getFloorNode() {
        return floorNode;
    }

    public void setFloorNode(Node floorNode) {
        this.floorNode = floorNode;
    }

    public boolean isRoofed() {
        return isRoofed;
    }

    public void setIsRoofed(boolean isRoofed) {
        this.isRoofed = isRoofed;
    }



    public float getCurrentLightLevel() {
        return currentLightLevel;
    }

    public void setCurrentLightLevel(float currentLightLevel) {
        this.currentLightLevel = currentLightLevel;
    }

    public byte getUltimateHeight() {
        return ultimateHeight;
    }

    public void setUltimateHeight(byte ultimateHeight) {
        this.ultimateHeight = ultimateHeight;
    }


    
    
    
    
    
    
    
}
