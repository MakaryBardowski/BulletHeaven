/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Items.Armor.Helmet;
import Items.Armor.Vest;
import Items.Melee.Sword;
import Items.Ranged.AssaultRifle;
import Items.Ranged.Flamethrower;
import Items.Ranged.Pistol;
import Items.Ranged.RangedWeapon;
import Items.Ranged.RocketLauncher;
import Items.Ranged.Shotgun;
import Items.Ranged.SMG;
import Items.Throwable.Grenade;
import com.jme3.scene.Node;

/**
 *
 * @author 48793
 */
public class ItemCreator {
    
    // weapons
    private ItemVisualsTemplate dEagle = new ItemVisualsTemplate("Models/pistolet/pistolet.j3o",         "Textures/assetTextures/WeaponsPalette/DesertEagleFP.png",       "Textures/GUI/equipmentDEagle.png","Models/pistolet/pistoletsimple.j3o","Textures/assetTextures/WeaponsPalette/DesertEaglesimple.png","Models/pistolet/pistoletsimpledrop.j3o","Textures/assetTextures/WeaponsPalette/DesertEaglesimple.png");
      private ItemVisualsTemplate usp = new ItemVisualsTemplate("Models/USPS/USPS.j3o",         "Textures/assetTextures/WeaponsPalette/USPS_FP.png",       "Textures/GUI/equipmentDEagle.png","Models/USPS/USPsimple.j3o","Textures/assetTextures/WeaponsPalette/USPsimple.png","Models/USPS/USPsimpledrop.j3o","Textures/assetTextures/WeaponsPalette/USPsimple.png");

    private ItemVisualsTemplate smg1 = new ItemVisualsTemplate("Models/SMG1/SMG1.j3o",                   "Textures/assetTextures/WeaponsPalette/SMG1.png",              "Textures/GUI/equipmentSMG1.png","Models/SMG1/SMG1simple.j3o","Textures/assetTextures/WeaponsPalette/SMG1simple.png","Models/SMG1/SMG1simpledrop.j3o","Textures/assetTextures/WeaponsPalette/SMG1simple.png");
    private ItemVisualsTemplate sword1 = new ItemVisualsTemplate("Models/staticSword1/staticSword1.j3o", "Textures/assetTextures/WeaponsPalette/Sword1.png",            "Textures/GUI/equipmentSword1.png","Models/staticSword1/staticSword1simple.j3o","Textures/assetTextures/WeaponsPalette/Sword1simple.png","Models/staticSword1/staticSword1simpledrop.j3o","Textures/assetTextures/WeaponsPalette/Sword1simple.png");
    private ItemVisualsTemplate shotgun1 = new ItemVisualsTemplate("Models/Shotgun1/Shotgun1.j3o",       "Textures/assetTextures/WeaponsPalette/Shotgun1.png",          "Textures/GUI/equipmentShotgun1.png","Models/Shotgun1/Shotgun1simple.j3o","Textures/assetTextures/WeaponsPalette/Shotgun1simple.png","Models/Shotgun1/Shotgun1simpledrop.j3o","Textures/assetTextures/WeaponsPalette/Shotgun1simple.png");
    private ItemVisualsTemplate grenade = new ItemVisualsTemplate("Models/GrenadeFP/GrenadeFP.j3o",         "Textures/assetTextures/WeaponsPalette/GrenadeFP.png",       "Textures/GUI/equipmentFragGrenade.png",null,null,"Models/Grenade/Grenade.j3o","Textures/assetTextures/WeaponsPalette/Grenade.png");

    
    private ItemVisualsTemplate javelin = new ItemVisualsTemplate("Models/javelin/javelin.j3o",         "Models/javelin/javelin.png",       "Textures/GUI/equipmentDEagle.png","Models/javelin/javelinSimple.j3o","Models/javelin/javelin.png","Models/javelin/javelinDrop.j3o","Models/javelin/javelin.png");

        private ItemVisualsTemplate flamethrower = new ItemVisualsTemplate("Models/Flamethrower/Flamethrower.j3o",         "Models/Flamethrower/flamethrower_Tex.png",       "Textures/GUI/equipmentDEagle.png","Models/javelin/javelinSimple.j3o","Models/javelin/javelin.png","Models/javelin/javelinDrop.j3o","Models/javelin/javelin.png");

        // kevlar
    private ItemVisualsTemplate gasMask = new ItemVisualsTemplate("Models/Utility/helmetGasMask.j3o","Textures/assetTextures/UtilityTextures/helmetGasMasksimple.png","Textures/GUI/equipmentGasMask.png", "Models/Aramid/Head3.j3o","Textures/assetTextures/AramidPalette/Head3.png","Models/Utility/helmetGasMasksimpledrop.j3o","Textures/assetTextures/UtilityTextures/helmetGasMasksimple.png");
    private ItemVisualsTemplate kevlarVest = new ItemVisualsTemplate("Models/Utility/helmetGasMask.j3o","Textures/assetTextures/AramidPalette/AramidChest.png","Textures/GUI/equipmentAramidChest.png", "Models/Aramid/AramidTorso.j3o","Textures/assetTextures/AramidPalette/AramidChest.png","Models/AramidChest/AramidChestsimpledrop.j3o","Textures/assetTextures/AramidPalette/AramidChestsimple.png");
    private ItemVisualsTemplate kevlarBoots = new ItemVisualsTemplate("Models/Utility/helmetGasMask.j3o","Textures/assetTextures/UtilityTextures/helmetGasMask.png","Textures/GUI/equipmentGasMask.png", "Models/Aramid/Head3.j3o","Textures/assetTextures/AramidPalette/Head3.png","drop","dropTexture");
    private ItemVisualsTemplate kevlarGloves = new ItemVisualsTemplate("Models/Utility/helmetGasMask.j3o","Textures/assetTextures/UtilityTextures/helmetGasMask.png","Textures/GUI/equipmentGasMask.png", "Models/Aramid/Head3.j3o","Textures/assetTextures/AramidPalette/Head3.png","drop","dropTexture");
        // riot
    private ItemVisualsTemplate riotHelmet = new ItemVisualsTemplate("Models/RiotSet/RiotHelmetDrop.j3o","Textures/assetTextures/RiotPalette/RiotHelmetDrop.png","Textures/GUI/equipmentGasMask.png", "Models/RiotSet/RiotHelmetHead.j3o","Textures/assetTextures/RiotPalette/HeadRiotHelmet.png","Models/RiotSet/RiotHelmetDrop.j3o","Textures/assetTextures/RiotPalette/RiotHelmetDrop.png");
    private ItemVisualsTemplate riotVest = new ItemVisualsTemplate("Models/RiotSet/riotChest.j3o","Textures/assetTextures/RiotPalette/riotChestWorldTexture.png","Textures/GUI/equipmentGasMask.png", "Models/RiotSet/riotChest.j3o","Textures/assetTextures/RiotPalette/riotChestWorldTexture.png","Models/RiotSet/riotChestsimple.j3o","Textures/assetTextures/RiotPalette/RiotChestsimple.png");

    
    public Grenade createGrenade(ItemVisualsTemplate template, float attackSpeed, float damage,float blastRadius) {
        Node node = new Node();
        Grenade dEagle = new Grenade(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,blastRadius);
        dEagle.setName(template.getModelPath());
        dEagle.setTexturePath(template.getTexturePath());
        dEagle.setIconPath(template.getIconPath());

        return dEagle;
    }

    ;
    
    
    public Pistol createPistol(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        Pistol dEagle = new Pistol(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,accPen,showFlash,flashTime,6,6);
        dEagle.setName(template.getModelPath());
        dEagle.setTexturePath(template.getTexturePath());
        dEagle.setIconPath(template.getIconPath());

        return dEagle;
    }

    ;  // will be used to create an item on picking up
       
              public SMG createSMG(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        SMG smg = new SMG(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,accPen,showFlash,flashTime,30,30);
        smg.setName(template.getModelPath());
        smg.setTexturePath(template.getTexturePath());
        smg.setIconPath(template.getIconPath());

        return smg;
    }
              
              
                public Flamethrower createFlamethrower(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        Flamethrower ft = new Flamethrower(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,accPen,showFlash,flashTime,40,40);
        ft.setName(template.getModelPath());
        ft.setTexturePath(template.getTexturePath());
        ft.setIconPath(template.getIconPath());

        return ft;
    }
              
                    public RocketLauncher createRocketLauncher(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        RocketLauncher smg = new RocketLauncher(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,accPen,showFlash,flashTime,1,1);
        smg.setName(template.getModelPath());
        smg.setTexturePath(template.getTexturePath());
        smg.setIconPath(template.getIconPath());

        return smg;
    }

    ;  // will be used to create an item on picking up
              
                     public AssaultRifle createAssaultRifle(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        AssaultRifle assaultRifle = new AssaultRifle(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,accPen,showFlash,flashTime,35,35);
        assaultRifle.setName(template.getModelPath());
        assaultRifle.setTexturePath(template.getTexturePath());
        assaultRifle.setIconPath(template.getIconPath());

        return assaultRifle;
    }

    ;  // will be used to create an item on picking up
                     
                     
                            public Sword createSword(ItemVisualsTemplate template, float attackSpeed, float damage) {
        Node node = new Node();
        Sword sword = new Sword(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true, damage, attackSpeed,6.5f);
        sword.setName(template.getModelPath());
        sword.setTexturePath(template.getTexturePath());
        sword.setIconPath(template.getIconPath());

        return sword;
    }

    ;  // will be used to create an item on picking up
                            
                            
                            
                                   public Shotgun createShotgun(ItemVisualsTemplate template, float attackSpeed, float damage,float accPen,boolean showFlash,int flashTime) {
        Node node = new Node();
        Shotgun shotgun = new Shotgun(node, template.getModelPath(), template.getModelPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(),true, damage, attackSpeed,accPen,showFlash,flashTime,3,3);
        shotgun.setName(template.getModelPath());
        shotgun.setTexturePath(template.getTexturePath());
        shotgun.setIconPath(template.getIconPath());

        return shotgun;
    }

    ;  // will be used to create an item on picking up
                                   
                                          public Helmet createHelmet(ItemVisualsTemplate template) {
        Node node = new Node();
        Helmet helmet = new Helmet( node, template.getModelPath(), template.getIconPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true);
        helmet.setName(template.getModelPath());
        helmet.setTexturePath(template.getTexturePath());
        helmet.setIconPath(template.getIconPath());

        return helmet;
    }
                                          
                                                 public Vest createVest(ItemVisualsTemplate template) {
        Node node = new Node();
        Vest vest = new Vest( node, template.getModelPath(), template.getIconPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true);
        vest.setName(template.getModelPath());
        vest.setTexturePath(template.getTexturePath());
        vest.setIconPath(template.getIconPath());

        return vest;
    }
                                                 
                                                        public Helmet createBoots(ItemVisualsTemplate template) {
   Node node = new Node();
        Helmet helmet = new Helmet( node, template.getModelPath(), template.getIconPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true);
        helmet.setName(template.getModelPath());
        helmet.setTexturePath(template.getTexturePath());
        helmet.setIconPath(template.getIconPath());

        return helmet;
    }
                                                        
                                                               public Helmet createGloves(ItemVisualsTemplate template) {
     Node node = new Node();
        Helmet helmet = new Helmet( node, template.getModelPath(), template.getIconPath(), template.getTexturePath(),template.getDropPath(),template.getDropTexturePath(),template.getWorldModelName(),template.getWorldModelTextureName(), true);
        helmet.setName(template.getModelPath());
        helmet.setTexturePath(template.getTexturePath());
        helmet.setIconPath(template.getIconPath());

        return helmet;
    }

    public ItemVisualsTemplate getdEagle() {
        return dEagle;
    }

    public void setdEagle(ItemVisualsTemplate dEagle) {
        this.dEagle = dEagle;
    }

    public ItemVisualsTemplate getSmg1() {
        return smg1;
    }

    public void setSmg1(ItemVisualsTemplate smg1) {
        this.smg1 = smg1;
    }

    public ItemVisualsTemplate getSword1() {
        return sword1;
    }

    public void setSword1(ItemVisualsTemplate sword1) {
        this.sword1 = sword1;
    }

    public ItemVisualsTemplate getShotgun1() {
        return shotgun1;
    }

    public void setShotgun1(ItemVisualsTemplate shotgun1) {
        this.shotgun1 = shotgun1;
    }

    public ItemVisualsTemplate getGasMask() {
        return gasMask;
    }

    public void setGasMask(ItemVisualsTemplate gasMask) {
        this.gasMask = gasMask;
    }

    public ItemVisualsTemplate getRiotHelmet() {
        return riotHelmet;
    }

    public void setRiotHelmet(ItemVisualsTemplate riotHelmet) {
        this.riotHelmet = riotHelmet;
    }

    public ItemVisualsTemplate getKevlarVest() {
        return kevlarVest;
    }

    public void setKevlarVest(ItemVisualsTemplate kevlarVest) {
        this.kevlarVest = kevlarVest;
    }

    public ItemVisualsTemplate getKevlarBoots() {
        return kevlarBoots;
    }

    public void setKevlarBoots(ItemVisualsTemplate kevlarBoots) {
        this.kevlarBoots = kevlarBoots;
    }

    public ItemVisualsTemplate getKevlarGloves() {
        return kevlarGloves;
    }

    public void setKevlarGloves(ItemVisualsTemplate kevlarGloves) {
        this.kevlarGloves = kevlarGloves;
    }

    public ItemVisualsTemplate getRiotVest() {
        return riotVest;
    }

    public void setRiotVest(ItemVisualsTemplate riotVest) {
        this.riotVest = riotVest;
    }

    public ItemVisualsTemplate getGrenade() {
        return grenade;
    }

    public void setGrenade(ItemVisualsTemplate grenade) {
        this.grenade = grenade;
    }

    public ItemVisualsTemplate getUsp() {
        return usp;
    }

    public void setUsp(ItemVisualsTemplate usp) {
        this.usp = usp;
    }

    public ItemVisualsTemplate getJavelin() {
        return javelin;
    }

    public void setJavelin(ItemVisualsTemplate javelin) {
        this.javelin = javelin;
    }

    public ItemVisualsTemplate getFlamethrower() {
        return flamethrower;
    }

    public void setFlamethrower(ItemVisualsTemplate flamethrower) {
        this.flamethrower = flamethrower;
    }
                                          
        
    
                                          
                                          
}
