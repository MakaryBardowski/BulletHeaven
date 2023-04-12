/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUILayer;

import GameStates.GameState;
import com.jme3.ui.Picture;

/**
 *
 * @author 48793
 */
public class GuiEditor {
    
    
    public void attachCrosshair(GameState gs){
    Picture crosshair = new Picture("crosshair");
        crosshair.setImage(gs.getAssetManager(), "Textures/GUI/crosshair.png", true);
        crosshair.setWidth(gs.getSettings().getHeight()*0.04f);
        crosshair.setHeight(gs.getSettings().getHeight()*0.04f); //0.04f
        crosshair.setPosition((gs.getSettings().getWidth()/2)-gs.getSettings().getHeight()*0.04f/2, gs.getSettings().getHeight()/2-gs.getSettings().getHeight()*0.04f/2);
        gs.getGuiNode().attachChild(crosshair);
    }
    
}
