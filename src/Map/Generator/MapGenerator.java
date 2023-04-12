/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map.Generator;

import EntityControls.CameraAndMouseControl;
import GameStates.GameState;
import MapBuilder.LightingMethods;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class MapGenerator {

    private Random r = new Random();

    private int connectedRoomCurrentIndex = 0;
    private int[] playerStartPos;

    public MapGenerationResult generateMap(int mapSize, int expectedRoom) { // map size has to be a product of grid size

        int numLayers = 3;

        int[][][] map = new int[numLayers][mapSize][mapSize]; // map 

        HashMap< int[], int[]> roomsByOrigin = new HashMap<>(); // int[] as key is x,z room start coords
        // int[] as value is room size x,z
        ArrayList<int[]> roomStartCoords = new ArrayList<>(); //used to get values from roomsByOrigin

        while (roomStartCoords.isEmpty()) {
            generateRoom(map, 5, 5, 0, 0, roomsByOrigin, roomStartCoords);
        }
        playerStartPos = roomStartCoords.get(0);

        roomPlacement(map, expectedRoom, roomsByOrigin, roomStartCoords);
        roomConnection(map, roomsByOrigin, roomStartCoords);

//        for (int i = 0; i < map[0].length; i++) {
//            System.out.print("\n[");
//            for (int j = 0; j < map[0][0].length; j++) {
//                System.out.print(map[0][i][j] + ",");
//            }
//            System.out.print("]");
//        }

        TileData[][][] aMap = new TileData[numLayers][mapSize][mapSize];

        for (int layer = 0; layer < map.length; layer++) {
            for (int x = 0; x < mapSize; x++) {
                for (int z = 0; z < mapSize; z++) {
                    TileData td = new TileData();

                    if (map[layer][x][z] == 1) {
                        td.setTileType(TileType.FLOOR);
                    } else {
                        td.setTileType(TileType.EMPTY);
                    }

                    if (layer + 1 < map.length) {
                        if (map[layer + 1][x][z] != 1 && !CameraAndMouseControl.isTopDown()) {
                            td.setIsRoofed(true);
                        }
                    }

                    if (x + 1 < map[layer].length && map[layer][x + 1][z] != 1 || x + 1 >= map[0].length) // prawa sciana
                    {
                        td.setEastWall(true);
                    }
                    if (x - 1 >= 0 && map[layer][x - 1][z] != 1 || x - 1 < 0) // prawa sciana
                    {
                        td.setWestWall(true);
                    }
                    if (z + 1 < map[layer][0].length && map[layer][x][z + 1] != 1 || z + 1 >= map[layer][0].length) // prawa sciana
                    {
                        td.setNorthWall(true);
                    }
                    if (z - 1 >= 0 && map[layer][x][z - 1] != 1 || z - 1 < 0) // prawa sciana
                    {
                        td.setSouthWall(true);
                    }

                    aMap[layer][x][z] = td;
                }

            }
        }
        
        
        
        
        for(int layer = aMap.length-1; layer>=0;layer--){
        for(int x = 0; x< aMap[layer].length;x++ ){
        for(int y = 0; y < aMap[layer][0].length;y++){
        
            if(aMap[layer][x][y].isRoofed()){
            
                for(int i = 0; i< aMap.length;i++){
                aMap[i][x][y].setUltimateHeight((byte)layer);
                }
            
            }
            
        }
        }
            
            
            
        }
        
        
        

//        System.out.print("\n\n\nMAPA WYSOKOSCI PIETRA 0\n");

//        int pietroPrint = 0;

//        for (int i = 0; i < aMap[0].length; i++) {
//            System.out.print("\n[");
//            for (int j = 0; j < aMap[pietroPrint][0].length; j++) {
//                System.out.print(aMap[pietroPrint][i][j].getUltimateHeight() + ",");
//            }
//            System.out.print("]");
//        }

        return new MapGenerationResult(map, aMap, null, playerStartPos, roomStartCoords, roomsByOrigin);
    }

    private int[][][] roomConnection(int[][][] map, HashMap<int[], int[]> rby, ArrayList<int[]> roomStartCoords) {
        while (connectedRoomCurrentIndex < roomStartCoords.size()) {
            RoomGenerationResult result = connectRooms(map, rby, roomStartCoords, connectedRoomCurrentIndex);
            connectedRoomCurrentIndex += result.getFailed();
            map = result.getNewMap();

        }

        return map;

    }

    private RoomGenerationResult connectRooms(int[][][] map, HashMap<int[], int[]> rby, ArrayList<int[]> roomStartCoords, int connectedRoomCurrentIndex) {
        int firstRoomRandomIndex = connectedRoomCurrentIndex;
        int secondRoomRandomIndex = r.nextInt(roomStartCoords.size());

        while (secondRoomRandomIndex == firstRoomRandomIndex) {
            secondRoomRandomIndex = r.nextInt(roomStartCoords.size());
        }

        int[] room1StartCoords = roomStartCoords.get(firstRoomRandomIndex).clone();
        int[] room2StartCoords = roomStartCoords.get(secondRoomRandomIndex).clone();

        // z ponizszym ustawieniem korytarze moga wchodzic do pokoi gdzies indziej niz  wmiejscu startu pokoju
        room1StartCoords[0] = room1StartCoords[0] + (int) Math.floor(rby.get(roomStartCoords.get(firstRoomRandomIndex))[0] * r.nextFloat());
        room1StartCoords[1] = room1StartCoords[1] + (int) Math.floor(rby.get(roomStartCoords.get(firstRoomRandomIndex))[1] * r.nextFloat());
        room2StartCoords[0] = room2StartCoords[0] + (int) Math.floor(rby.get(roomStartCoords.get(secondRoomRandomIndex))[0] * r.nextFloat());
        room2StartCoords[1] = room2StartCoords[1] + (int) Math.floor(rby.get(roomStartCoords.get(secondRoomRandomIndex))[1] * r.nextFloat());
//        room1StartCoords[0] = room1StartCoords[0];
//        room1StartCoords[1] = room1StartCoords[1];
//
//        room2StartCoords[0] = room2StartCoords[0];
//        room2StartCoords[1] = room2StartCoords[1];

        int startX = room1StartCoords[0];
        int startY = room1StartCoords[1];
        int endX = room2StartCoords[0];
        int endY = room2StartCoords[1];

        int connectionLayer = 0;  // corridors spawn on layer 0 by default.

        if (startX <= endX && startY <= endY) { // prawy gorny rog

            for (int sx = startX; sx < endX; sx++) {
                map[connectionLayer][sx][startY] = 1;

            }

            for (int sy = startY; sy < endY; sy++) {
                map[connectionLayer][endX][sy] = 1;

            }
        } else if (startX <= endX && startY >= endY) { // prawy dolny
            for (int sx = startX; sx < endX; sx++) {
                map[connectionLayer][sx][startY] = 1;

            }

            for (int sy = startY; sy >= endY; sy--) {
                map[connectionLayer][endX][sy] = 1;

            }

        } else if (startX > endX && startY <= endY) { // lewy gorny
            for (int sx = startX; sx > endX; sx--) {
                map[connectionLayer][sx][startY] = 1;

            }

            for (int sy = startY; sy < endY; sy++) {
                map[connectionLayer][endX][sy] = 1;

            }
        } else if (startX > endX && startY > endY) { // lewy dolny
            for (int sx = startX; sx > endX; sx--) {
                map[connectionLayer][sx][startY] = 1;

            }

            for (int sy = startY; sy >= endY; sy--) {
                map[connectionLayer][endX][sy] = 1;

            }
        }

        return new RoomGenerationResult(map, 1);
    }

    private int[][][] roomPlacement(int[][][] map, int expectedRooms, HashMap<int[], int[]> roomsByOrigin, ArrayList<int[]> roomStartCoords) {
        int minRoomSizeX = 4;
        int minRoomSizeY = 4;
        int roomSizeVariationX = 3;
        int roomSizeVariationY = 3;

        int roomsPlaced = 0;

        while (roomsPlaced < expectedRooms) {
            RoomGenerationResult result = generateRoom(map, minRoomSizeX, minRoomSizeY, roomSizeVariationX, roomSizeVariationY, roomsByOrigin, roomStartCoords);
            roomsPlaced += result.getFailed();
            map = result.getNewMap();

        }

        return map;

    }

    private RoomGenerationResult generateRoom(int[][][] map, int minRoomSizeX, int minRoomSizeY, int roomSizeVariationX, int roomSizeVariationY, HashMap<int[], int[]> roomsByOrigin, ArrayList<int[]> roomStartCoords) {

        int roomLayer = 0; // deefault room layer = 0

        int roomSizeX = r.nextInt((roomSizeVariationX + 1)) + minRoomSizeY;
        int roomSizeY = r.nextInt((roomSizeVariationY + 1)) + minRoomSizeY;

        int roomPosX = minRoomSizeX + r.nextInt(map[0].length - minRoomSizeX);
        int roomPosY = minRoomSizeX + r.nextInt(map[0].length - minRoomSizeX);

        // check if the room can be placed
        for (int x = roomPosX; x < roomPosX + roomSizeX; x++) {
            for (int y = roomPosY; y < roomPosY + roomSizeY; y++) {

                if ((x >= map[0].length || y >= map[roomLayer][0].length) || map[roomLayer][x][y] != 0) {
                    return new RoomGenerationResult(map, 0);
                }

            }
        }
        // check if the room can be placed

        for (int x = roomPosX - 1; x < roomPosX + roomSizeX + 1; x++) {

            for (int y = roomPosY - 1; y < roomPosY + roomSizeY + 1; y++) {
                if (!(x >= map[roomLayer].length || y >= map[roomLayer][0].length) && x > 0 && y > 0 && map[roomLayer][x][y] == 0) {
                    map[roomLayer][x][y] = 9; // its the border preventing rooms from overlappin

                    if (roomLayer + 1 < map.length) {
                        map[roomLayer + 1][x][y] = 9;
                    }
                }
            }
        }

        for (int x = roomPosX; x < roomPosX + roomSizeX; x++) {
            for (int y = roomPosY; y < roomPosY + roomSizeY; y++) {
                map[roomLayer][x][y] = 1;

                if (roomLayer + 1 < map.length) {
                    map[roomLayer + 1][x][y] = 1;
                }
            }
        }

        int[] rsc = new int[]{roomPosX, roomPosY};
        roomsByOrigin.put(rsc, new int[]{roomSizeX, roomSizeY});
        roomStartCoords.add(rsc);

        return new RoomGenerationResult(map, 1);

    }

    public int[] getPlayerStartPos() {
        return playerStartPos;
    }

    public void setPlayerStartPos(int[] playerStartPos) {
        this.playerStartPos = playerStartPos;
    }

}
