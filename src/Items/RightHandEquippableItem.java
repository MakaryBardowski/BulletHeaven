/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import GameObjects.Mobs.Player;
import GameStates.GameState;

/**
 *
 * @author 48793
 */
public interface RightHandEquippableItem {
    
    public void playerEquipItemR(Player p,GameState gs);

    public void mobEquipItemR();
    

}
