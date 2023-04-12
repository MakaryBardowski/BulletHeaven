/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.Mobs;

import EntityControls.DeadBodyControl;
import EntityControls.EntityManager;
import EntityControls.HumanoidControl;
import GUILayer.MinimapMobIndicatorControl;
import GameStates.GameState;
import Items.Armor.Helmet;
import Items.Armor.Vest;
import Items.Item;
import Items.ItemCreator;
import Items.ItemInterface;
import Items.Weapon;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import MapBuilder.WorldGrid;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class HumanMob extends Mob {

    private Weapon equippedRightHand;
    private Weapon equippedLeftHand;

    private Item helmet;
    private Item torso;
    private Item boots;
    private Item gloves;
    private Node projectileSpawnNode;
    private Node[] bodyParts; // 0 - helmet, 1 - torso, 2-3 R/L hand , 4-5 R/L leg 

    public HumanMob(Node node, AnimControl aCtrl, AnimChannel ACh, Node[] bodyParts, Geometry[] bodyPartsForLighting, Node deadNode, String mobName, Allegiance allegiance) {
        super(node, aCtrl, ACh, bodyPartsForLighting, deadNode, mobName, allegiance);
        this.bodyParts = bodyParts;

    }

    public HumanMob(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed, Node[] bodyParts, Geometry[] bodyPartsForLighting, Node deadNode, String mobName, Allegiance allegiance) {
        this(node, aCtrl, ACh, bodyParts, bodyPartsForLighting, deadNode, mobName, allegiance);
        setHealth(health);
        setMaxHealth(health);
        setArmor(armor);
        setBaseSpeed(baseSpeed);
    }

    public HumanMob(Node node, AnimControl aCtrl, AnimChannel ACh, float health, float armor, float baseSpeed, Weapon equippedRightHand, Weapon equippedLefthand, Item helmet, Item torso, Item boots, Item gloves, Node[] bodyParts, Geometry[] bodyPartsForLighting, Node deadNode, String mobName, Allegiance allegiance) {
        this(node, aCtrl, ACh, health, armor, baseSpeed, bodyParts, bodyPartsForLighting, deadNode, mobName, allegiance);
        this.equippedRightHand = equippedRightHand;
        this.equippedLeftHand = equippedLefthand;
        this.helmet = helmet;
        this.torso = torso;
        this.boots = boots;
        this.gloves = gloves;

    }

    @Override
    public void receiveDamage(float d, GameState gs, EntityManager e, String metadata) {
        if (!isDead()) {
            d = d - getArmor();
            if (d <= 0) {
                gs.chatPutMessage("\\#e00000#Your attack proves ineffective!");

            } else {
                setHealth(getHealth() - (d));
                gs.chatPutMessage("The " + getMobName() + " \\#ffffff#takes " + "\\#ffb300#" + d + " \\#ffffff#damage!");

            }

            if (getHealth() <= 0) {
                die(gs, e);
            }
        }
    }

    @Override
    public void dealDamage(MobInterface target, float damage, GameState gs, EntityManager e, String metadata) {
        target.receiveDamage(damage, gs, e, metadata);
    }

    public void cleanup() { // cleanup after death

    }

    @Override
    public ItemInterface equipItem(ItemInterface i, boolean rightHand, GameState gs) {
        boolean equippedOnPlayer = false;
        if (this instanceof Player) {
            equippedOnPlayer = true;
        }

        if (!equippedOnPlayer) {
            i.equipOnMob(this, gs);
        } else {
            i.equipOnPlayer((Player) this, gs);
        }

        return i;
    }

    @Override
    public boolean isDead() {
        return getHealth() <= 0;
    }

    @Override
    public void die(GameState gs, EntityManager e) {
        
        
        dropEquipment(gs, e);
        Random r = new Random();

        getDeadNode().attachChild(getNode());
        getDeadNode().attachChild(getBloodPuddleNode());
        getBloodPuddleNode().move(getNode().getWorldTranslation());
        getBloodPuddleNode().rotate(0, r.nextInt(2 * 3), 0);
        e.getEnemiesAlive().remove(getNode().getName());
        getNode().addControl(new DeadBodyControl(this, 1));
    }

    @Override
    public void checkCollision() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCollision() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insert(WorldGrid wg) {

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (getRadius()), 0,
                getNode().getWorldTranslation().getZ() - (getRadius()));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (getRadius()), 0,
                getNode().getWorldTranslation().getZ() + (getRadius()));

        if (wg.getContents().get(wg.hash(min)) == null) {
            wg.getContents().put(wg.hash(min), new HashSet<CollidableInterface>());
        } else {

            wg.getContents().get(wg.hash(min)).add(this);

        }

        if (wg.getContents().get(wg.hash(max)) == null) {
            wg.getContents().put(wg.hash(max), new HashSet<CollidableInterface>());
        } else {
            wg.getContents().get(wg.hash(max)).add(this);

        }

        Vector3f newMin = new Vector3f(min.getX(), 0, max.getZ());

        if (wg.getContents().get(wg.hash(newMin)) == null) {
            wg.getContents().put(wg.hash(newMin), new HashSet<CollidableInterface>());
        } else {
            wg.getContents().get(wg.hash(newMin)).add(this);

        }

        newMin = new Vector3f(max.getX(), 0, min.getZ());

        if (wg.getContents().get(wg.hash(newMin)) == null) {
            wg.getContents().put(wg.hash(newMin), new HashSet<CollidableInterface>());
        } else {
            wg.getContents().get(wg.hash(newMin)).add(this);

        }

    }

    @Override
    public HashSet<CollidableInterface> getFromCellsImIn(WorldGrid wg) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (getRadius()), 0,
                getNode().getWorldTranslation().getZ() - (getRadius()));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (getRadius()), 0,
                getNode().getWorldTranslation().getZ() + (getRadius()));

        min.setX(wg.getCellSize() * (int) (Math.floor(min.getX() / wg.getCellSize())));
        min.setZ(wg.getCellSize() * (int) (Math.floor(min.getZ() / wg.getCellSize())));

        max.setX(wg.getCellSize() * (int) (Math.floor(max.getX() / wg.getCellSize())));
        max.setZ(wg.getCellSize() * (int) (Math.floor(max.getZ() / wg.getCellSize())));
        Vector3f pom = new Vector3f();

        for (int x = (int) min.x; x <= max.x; x += wg.getCellSize()) {
            for (int z = (int) min.z; z <= max.z; z += wg.getCellSize()) {
                pom.set(x, 0, z);
                if (wg.getContents().get(wg.hash(pom)) != null) {
                    output.addAll(wg.getContents().get(wg.hash(new Vector3f(x, 0, z))));
                }

            }
        }

        return output;
    }

    @Override
    public void removeFromGrid(WorldGrid wg) {
        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (getRadius()), 0,
                getNode().getWorldTranslation().getZ() - (getRadius()));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (getRadius()), 0,
                getNode().getWorldTranslation().getZ() + (getRadius()));

        HashSet<CollidableInterface> ct = wg.getContents().get(wg.hash(min));
        if (ct != null) {
            ct.remove(this);
        }
        ct = wg.getContents().get(wg.hash(max));

        if (ct != null) {
            ct.remove(this);
        }

        Vector3f newMin = new Vector3f(min.getX(), 0, max.getZ());
        ct = wg.getContents().get(wg.hash(newMin));

        if (ct != null) {
            ct.remove(this);

        }

        newMin = new Vector3f(max.getX(), 0, min.getZ());

        ct = wg.getContents().get(wg.hash(newMin));

        if (ct != null) {
            ct.remove(this);
        }
    }

    @Override
    public HashSet<CollidableInterface> getEntitiesFromTilesInRange(WorldGrid wg, float distance) {
        HashSet<CollidableInterface> output = new HashSet<>();

        Vector3f min = new Vector3f(
                getNode().getWorldTranslation().getX() - (distance), 0,
                getNode().getWorldTranslation().getZ() - (distance));

        Vector3f max = new Vector3f(
                getNode().getWorldTranslation().getX() + (distance), 0,
                getNode().getWorldTranslation().getZ() + (distance));

        min.setX(wg.getCellSize() * (int) (Math.floor(min.getX() / wg.getCellSize())));
        min.setZ(wg.getCellSize() * (int) (Math.floor(min.getZ() / wg.getCellSize())));

        max.setX(wg.getCellSize() * (int) (Math.floor(max.getX() / wg.getCellSize())));
        max.setZ(wg.getCellSize() * (int) (Math.floor(max.getZ() / wg.getCellSize())));
        Vector3f pom = new Vector3f();

        for (int x = (int) min.x; x <= max.x; x += wg.getCellSize()) {
            for (int z = (int) min.z; z <= max.z; z += wg.getCellSize()) {
                pom.set(x, 0, z);
                if (wg.getContents().get(wg.hash(pom)) != null) {
                    output.addAll(wg.getContents().get(wg.hash(new Vector3f(x, 0, z))));
                }

            }
        }

        return output;
    }

    public Weapon getEquippedRightHand() {
        return equippedRightHand;
    }

    public void setEquippedRightHand(Weapon equippedRightHand) {
        this.equippedRightHand = equippedRightHand;
    }

    public Weapon getEquippedLeftHand() {
        return equippedLeftHand;
    }

    public void setEquippedLeftHand(Weapon equippedLeftHand) {
        this.equippedLeftHand = equippedLeftHand;
    }

    public Item getHelmet() {
        return helmet;
    }

    public void setHelmet(Item helmet) {
        this.helmet = helmet;
    }

    public Item getTorso() {
        return torso;
    }

    public void setTorso(Item torso) {
        this.torso = torso;
    }

    public Item getBoots() {
        return boots;
    }

    public void setBoots(Item boots) {
        this.boots = boots;
    }

    public Item getGloves() {
        return gloves;
    }

    public void setGloves(Item gloves) {
        this.gloves = gloves;
    }

    public Node getProjectileSpawnNode() {
        return projectileSpawnNode;
    }

    public void setProjectileSpawnNode(Node projectileSpawnNode) {
        this.projectileSpawnNode = projectileSpawnNode;
    }

    public Node[] getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(Node[] bodyParts) {
        this.bodyParts = bodyParts;
    }

    private void dropHelmet(GameState gs, EntityManager e) {
        if (helmet != null && helmet.isDroppable()) {
            helmet.drop(getNode().getWorldTranslation(), gs, e);
            helmet = null;
        }

    }

    private void dropTorso(GameState gs, EntityManager e) {
        if (torso != null && torso.isDroppable()) {
            torso.drop(getNode().getWorldTranslation(), gs, e);
            torso = null;
        }

    }

    private void dropBoots(GameState gs, EntityManager e) {
        if (boots != null && boots.isDroppable()) {
            boots.drop(getNode().getWorldTranslation(), gs, e);
            boots = null;
        }

    }

    private void dropGloves(GameState gs, EntityManager e) {
        if (gloves != null && gloves.isDroppable()) {
            gloves.drop(getNode().getWorldTranslation(), gs, e);
            gloves = null;
        }

    }

    private void dropRightHand(GameState gs, EntityManager e) {
        if (equippedRightHand != null && equippedRightHand.isDroppable()) {
            equippedRightHand.drop(getNode().getWorldTranslation(), gs, e);
            equippedRightHand = null;
        }

    }

    private void dropLeftHand(GameState gs, EntityManager e) {
        if (equippedLeftHand != null && equippedLeftHand.isDroppable()) {
            equippedLeftHand.drop(getNode().getWorldTranslation(), gs, e);
            equippedLeftHand = null;
        }

    }

    private void dropEquippedItems(GameState gs, EntityManager e) {
        dropHelmet(gs, e);
        dropTorso(gs, e);
        dropBoots(gs, e);
        dropGloves(gs, e);
        dropRightHand(gs, e);
        dropLeftHand(gs, e);

    }

    public void dropEquipment(GameState gs, EntityManager e) {

        dropEquippedItems(gs, e);
        Item item = null;
        for (int i = 0; i < getEquipment().length; i++) {
            item = getEquipment()[i];
            if (item != null) {
                item.drop(getNode().getWorldTranslation(), gs, e);
                getEquipment()[i] = null;
            }
        }

    }

    public static Mob spawnScavenger(Node nodeToBeAttachedTo, GameState gs, EntityManager em, Node deadNode) {
        String newMobName = "Scavenger";

        AssetManager a = gs.getAssetManager();
        Node newMobNode = (Node) a.loadModel("Models/testSkeleton/testSkeleton.j3o");
        AnimControl animControl = newMobNode.getChild("Armature").getControl(AnimControl.class);
        AnimChannel animChannel = animControl.createChannel();

        SkeletonControl newObjectSkeletonControl = newMobNode.getChild("Armature").getControl(SkeletonControl.class);

        boolean showHitboxes = false;

        //body hitbox for no projectile weapons
        Box bodyHitbox = new Box(0.7f, 1, 0.4f);
        Spatial bodyHitboxSpat = new Geometry("bodyHitbox", bodyHitbox);
        Material bodyHitboxMat = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
        bodyHitboxMat.getAdditionalRenderState().setWireframe(showHitboxes);

        if (!showHitboxes) {
            bodyHitboxSpat.setCullHint(Spatial.CullHint.Always);// always hide hitbox
        }
        bodyHitboxMat.getAdditionalRenderState().setLineWidth(4);
        bodyHitboxMat.setColor("Color", ColorRGBA.Green);
        bodyHitboxSpat.setMaterial(bodyHitboxMat);
        newMobNode.attachChild(bodyHitboxSpat);
        newObjectSkeletonControl.getAttachmentsNode("Spine").attachChild(bodyHitboxSpat);
        bodyHitboxSpat.move(0, -0.5f, 0); // offsetting it 

        // body hitbox for no projectile weapons
        //hitbox for no projectile weapons
        Box headHitbox = new Box(0.33f, 0.4f, 0.33f);
        Spatial headHitboxSpat = new Geometry("headHitbox", headHitbox);
        Material headHitboxMat = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");
        headHitboxMat.getAdditionalRenderState().setWireframe(showHitboxes);

        if (!showHitboxes) {
            headHitboxSpat.setCullHint(Spatial.CullHint.Always);// always hide hitbox
        }

        headHitboxMat.getAdditionalRenderState().setLineWidth(4);
        headHitboxMat.setColor("Color", ColorRGBA.Red);
        headHitboxSpat.setMaterial(headHitboxMat);
        newMobNode.attachChild(headHitboxSpat);
        headHitboxSpat.move(0, 0, 0); // offsetting it a bit because head bone tends to start at the base of the neck
        // hitbox for no projectile weapons

        Node head = (Node) a.loadModel("Models/Aramid/Head3.j3o");
        head.setName("headHitbox"); // make head mdoel
        head.attachChild(headHitboxSpat);

        Node torso = (Node) a.loadModel("Models/Aramid/AramidTorso.j3o");
        Node legL = (Node) a.loadModel("Models/Aramid/AramidLegL.j3o");
        Node legR = (Node) a.loadModel("Models/Aramid/AramidLegR.j3o");
        Node handR = (Node) a.loadModel("Models/Aramid/AramidHandR.j3o");
        Node handL = (Node) a.loadModel("Models/Aramid/AramidHandL.j3o");

        LightingMethods.setLightingMaterial(head, "Textures/assetTextures/AramidPalette/Head3.png", LIGHT_MAGNITUDE, a);
        LightingMethods.setLightingMaterial(torso, "Textures/assetTextures/AramidPalette/AramidChest.png", LIGHT_MAGNITUDE, a);
        LightingMethods.setLightingMaterial(legL, "Textures/assetTextures/AramidPalette/AramidLegL.png", LIGHT_MAGNITUDE, a);
        LightingMethods.setLightingMaterial(legR, "Textures/assetTextures/AramidPalette/AramidLegR.png", LIGHT_MAGNITUDE, a);
        LightingMethods.setLightingMaterial(handR, "Textures/assetTextures/AramidPalette/AramidHandR.png", LIGHT_MAGNITUDE, a);
        LightingMethods.setLightingMaterial(handL, "Textures/assetTextures/AramidPalette/AramidHandL.png", LIGHT_MAGNITUDE, a);

        newObjectSkeletonControl.getAttachmentsNode("Spine").attachChild(torso);
        newObjectSkeletonControl.getAttachmentsNode("Head").attachChild(head);
        newObjectSkeletonControl.getAttachmentsNode("HandR").attachChild(handR);
        newObjectSkeletonControl.getAttachmentsNode("HandL").attachChild(handL);
        newObjectSkeletonControl.getAttachmentsNode("LegR").attachChild(legR);
        newObjectSkeletonControl.getAttachmentsNode("LegL").attachChild(legL);

        // randomise weapon and add it to the model
        Weapon mobWeapon = randomWeapon();
        SkeletonControl rightHandSkeletonControl = handR.getChild("Armature").getControl(SkeletonControl.class);
        Node weapon = (Node) a.loadModel(mobWeapon.getMobPath());
        rightHandSkeletonControl.getAttachmentsNode("WeaponBone").attachChild(weapon);
        weapon.scale(0.5f);
        LightingMethods.setLightingMaterial(weapon, mobWeapon.getMobTexturePath(), LIGHT_MAGNITUDE, a);
        // randomise weapon and add it to the model
        float health = 100;
        Node[] bodyParts = new Node[]{head, torso, handR, handL, legR, legL};

        Geometry[] bodyPartsForLighting = new Geometry[7]; // 6 for 1x head, 1x torso ,2x hand ,2x leg , 1x weaponR , 1x weaponL
        bodyPartsForLighting[0] = ((Geometry) (head.getChild(0)));
        bodyPartsForLighting[1] = ((Geometry) (torso.getChild(0)));
        bodyPartsForLighting[2] = ((Geometry) (((Node) handR.getChild(0))).getChild(0));
        bodyPartsForLighting[3] = ((Geometry) (handL.getChild(0)));
        bodyPartsForLighting[4] = ((Geometry) (legR.getChild(0)));
        bodyPartsForLighting[5] = ((Geometry) (legL.getChild(0)));
        bodyPartsForLighting[6] = (Geometry) weapon.getChild(0);


        HumanMob newMob = new HumanMob(newMobNode, animControl, animChannel, health, 0, 3, bodyParts, bodyPartsForLighting, deadNode, newMobName, Allegiance.HOSTILE);
        animChannel.setAnim("Idle");
        ItemCreator itemCreator = new ItemCreator();
        newMob.setBloodPuddleNode((Node) a.loadModel("Models/bloodPuddle/bloodPuddle.j3o"));
        LightingMethods.setLightingMaterial(newMob.getBloodPuddleNode(), "Textures/assetTextures/UtilityTextures/bloodPuddle.png", LIGHT_MAGNITUDE, a);

        // attach the mob (as a model) to the node (like map node etc)
        nodeToBeAttachedTo.attachChild(newMobNode);
        // attach the mob (as a model) to the node (like map node etc)

        System.out.println(newMobNode.getName());

        // let the game know that the mob is a living entity
        em.getEnemiesAlive().put(newMobNode.getName(), newMob);
        // let the game know that the mob is a living entity

        newMob.equipItem(randomHelmet(), showHitboxes, gs);
        newMob.equipItem(randomVest(), showHitboxes, gs);
        newMob.setEquippedRightHand(mobWeapon);
        
        newMob.getEquipment()[0] = itemCreator.createGrenade(itemCreator.getGrenade(), 1, 100, 4);

        //minimap indicator
        Node playerNode = new Node("player indicator node");
        Quad boardShape = new Quad(10.0f, 10.0f);
        Geometry board = new Geometry("Board", boardShape);

        Material material = new Material(a, "Common/MatDefs/Misc/Unshaded.j3md");

        material.setTexture("ColorMap", a.loadTexture("Textures/GUI/mobIndicator.png"));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);  // !
        material.setColor("Color", new ColorRGBA(1, 1, 1, .7f));

        board.setMaterial(material);

        playerNode.attachChild(board);
        float size = 0.004f * gs.getMinimapPxSize();
        board.scale(size);
        gs.getGuiNode().attachChild(playerNode);
        board.move(gs.getMinimapXoffset() - 4 * size, -4 * size, 0); // -8 cuz the image is 32x32

        newMob.getNode().addControl(new MinimapMobIndicatorControl(playerNode, newMob, gs.getWorldGrid().getCellSize(), gs.getMinimapMovementRatio()));
        //minimap indicator

        // ai
        newMob.getNode().addControl(new HumanoidControl(newMob, gs));

        newMob.setRadius(0.7f);
        return newMob;
    }

    private static Weapon randomWeapon() {
        float damage = 0;
        ItemCreator itemCreator = new ItemCreator();

        int rand = new Random().nextInt(6);
        switch (rand) {
            case 0:
                damage = 18;
                return itemCreator.createPistol(itemCreator.getdEagle(), 7f, damage, 1.3f, true, 50);
            case 1:
                damage = 18;
                return itemCreator.createPistol(itemCreator.getUsp(), 15f, 8f, 0.7f, false, 0);
            case 2:
                damage = 5;
                return itemCreator.createSMG(itemCreator.getSmg1(), 1, damage, 1.3f, true, 50);
            case 3:
                damage = 15;
                return itemCreator.createSword(itemCreator.getSword1(), 2, damage);
            case 4:
                damage = 30;
                return itemCreator.createShotgun(itemCreator.getShotgun1(), 1, damage, 5f, true, 25);
            case 5:
                damage = 75;
                return itemCreator.createRocketLauncher(itemCreator.getJavelin(), 0.8f, damage, 0.7f, true, 25);
            default:
                return null;
        }

    }

    private static Helmet randomHelmet() {
        Random r = new Random();
        ItemCreator itemCreator = new ItemCreator();
        int helmetRandom = r.nextInt(2);

        switch (helmetRandom) {
            case 0:
                return (itemCreator.createHelmet(itemCreator.getGasMask()));
            case 1:
                return (itemCreator.createHelmet(itemCreator.getRiotHelmet()));
        }
        return null;

    }

    private static Vest randomVest() {
        Random r = new Random();
        ItemCreator itemCreator = new ItemCreator();
        int vestRandom = r.nextInt(2);

        switch (vestRandom) {
            case 0:
                return itemCreator.createVest(itemCreator.getRiotVest());
            case 1:
                return itemCreator.createVest(itemCreator.getKevlarVest());
        }
        return null;

    }
}
