/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Armor;

import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import Items.ItemInterface;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * * @author 48793
 */
public class Helmet extends Armor implements ItemInterface{

    public Helmet(Node node, String name, String iconPath, String texturePath,String dropPath,String dropTexturePath,String mobPath,String mobTexturePath, boolean droppable) {
        super(node, name, iconPath, texturePath,dropPath,dropTexturePath,mobPath,mobTexturePath ,droppable);
    }
    
    
    @Override
    public void equipOnMob(HumanMob m , GameState gs) {
        m.setHelmet(this);
Node parentToAttachTo = m.getBodyParts()[0].getParent();
       m.getBodyParts()[0].removeFromParent();
       Node newHead = (Node) gs.getAssetManager().loadModel(getMobPath());
       m.getBodyParts()[0] = (newHead);
       m.getModelElementsForShading()[0] = ((Geometry) (newHead.getChild(0)));

       parentToAttachTo.attachChild(newHead);
       LightingMethods.setLightingMaterial(newHead, getMobTexturePath(), LIGHT_MAGNITUDE, gs.getAssetManager());
       newHead.setName("headHitbox");
    }

    @Override
    public void equipOnPlayer(Player p, GameState gs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
