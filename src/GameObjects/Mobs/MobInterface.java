/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import EntityControls.EntityManager;
import GameStates.GameState;
import Items.Item;
import Items.ItemInterface;

/**
 *
 * @author 48793
 */
public interface MobInterface {
    
    public ItemInterface equipItem(ItemInterface i,boolean rightOrLeftClick,GameState gs);
    public void receiveDamage(float d,GameState gs, EntityManager e,String metadata); // metadata is used to pass which type of headshot is registered - engine shot. headshot etc
    public void dealDamage(MobInterface target, float damage,GameState gs, EntityManager e,String metadata);
    public void die(GameState gs, EntityManager e);
    public boolean isDead();
    
    
}
