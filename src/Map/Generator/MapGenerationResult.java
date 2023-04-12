/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map.Generator;

import MapBuilder.WorldGrid;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author 48793
 */
public class MapGenerationResult {
    
    private int[][][] map;
    private TileData[][][] tileDataMap;
    private WorldGrid worldGrid;
    private int[] playerStartCoords;
    private ArrayList<int[]> roomStartCoords;
    private HashMap<int[],int[]> roomsByOrigin;

    public MapGenerationResult(int[][][] map,TileData[][][] tileDataMap, WorldGrid worldGrid, int[] playerStartCoords, ArrayList<int[]> roomStartCoords, HashMap<int[], int[]> roomsByOrigin) {
        this.map = map;
        this.worldGrid = worldGrid;
        this.playerStartCoords = playerStartCoords;
        this.roomStartCoords = roomStartCoords;
        this.roomsByOrigin = roomsByOrigin;
        this.tileDataMap =tileDataMap;
    }

 

    
    
    public int[][][] getMap() {
        return map;
    }

    public void setMap(int[][][] map) {
        this.map = map;
    }

    public WorldGrid getWorldGrid() {
        return worldGrid;
    }

    public void setWorldGrid(WorldGrid worldGrid) {
        this.worldGrid = worldGrid;
    }

    public int[] getPlayerStartCoords() {
        return playerStartCoords;
    }

    public void setPlayerStartCoords(int[] playerStartCoords) {
        this.playerStartCoords = playerStartCoords;
    }

    public ArrayList<int[]> getRoomStartCoords() {
        return roomStartCoords;
    }

    public void setRoomStartCoords(ArrayList<int[]> roomStartCoords) {
        this.roomStartCoords = roomStartCoords;
    }

    public HashMap<int[], int[]> getRoomsByOrigin() {
        return roomsByOrigin;
    }

    public void setRoomsByOrigin(HashMap<int[], int[]> roomsByOrigin) {
        this.roomsByOrigin = roomsByOrigin;
    }

    public TileData[][][] getTileDataMap() {
        return tileDataMap;
    }

    public void setTileDataMap(TileData[][][] tileDataMap) {
        this.tileDataMap = tileDataMap;
    }
    
    
    
}
