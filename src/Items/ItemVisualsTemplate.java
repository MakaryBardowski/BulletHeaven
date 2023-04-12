/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

/**
 *
 * @author 48793
 */
public class ItemVisualsTemplate {
    private String modelPath; // on player
    private String texturePath; // on player
    private String iconPath; // icon 
    private String worldModelName;  // on mob
    private String worldModelTextureName; // on mob
    private String dropPath; // on ground
    private String dropTexturePath; // on ground

    public ItemVisualsTemplate(String modelPath, String texturePath, String iconPath, String worldModelName, String worldModelTextureName,String dropPath,String dropTexturePath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.iconPath = iconPath;
        this.worldModelName = worldModelName;
        this.worldModelTextureName = worldModelTextureName;
        this.dropPath = dropPath;
        this.dropTexturePath=dropTexturePath;
    }

    
    
    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getWorldModelName() {
        return worldModelName;
    }

    public void setWorldModelName(String worldModelName) {
        this.worldModelName = worldModelName;
    }

    public String getWorldModelTextureName() {
        return worldModelTextureName;
    }

    public void setWorldModelTextureName(String worldModelTextureName) {
        this.worldModelTextureName = worldModelTextureName;
    }

    public String getDropPath() {
        return dropPath;
    }

    public void setDropPath(String dropPath) {
        this.dropPath = dropPath;
    }

    public String getDropTexturePath() {
        return dropTexturePath;
    }

    public void setDropTexturePath(String dropTexturePath) {
        this.dropTexturePath = dropTexturePath;
    }
    
    
    
    
}
