/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public interface WeaponInterface {
    
    public void shootAsPlayer(Player p,GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode,float tpf);
    public void reload(Player p,GameState gs);
}
