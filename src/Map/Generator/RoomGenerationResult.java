/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map.Generator;

/**
 *
 * @author 48793
 */
public class RoomGenerationResult {
    private int[][][] newMap;
    private int failed;

    public RoomGenerationResult(int[][][] newMap, int failed) {
        this.newMap = newMap;
        this.failed = failed;
    }
    
    

    public int[][][] getNewMap() {
        return newMap;
    }

    public void setNewMap(int[][][] newMap) {
        this.newMap = newMap;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }


    
    
    
    
}
