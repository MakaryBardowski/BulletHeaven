/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;

/**
 *
 * @author 48793
 */
public interface ItemInterface {
    public void equipOnMob(HumanMob m , GameState gs);
    public void equipOnPlayer(Player p, GameState gs);
}
