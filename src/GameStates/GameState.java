/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameStates;

import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import GUILayer.GuiEditor;
import GUILayer.MinimapInitializer;
import GameObjects.Mobs.HelicopterBoss1;
import GameObjects.Mobs.HumanMob;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import Map.Generator.MapGenerationResult;
import Map.Generator.MapGenerator;
import Map.Generator.TileData;
import Map.Generator.TileType;
import MapBuilder.LightingMethods;
import static MapBuilder.LightingMethods.LIGHT_MAGNITUDE;
import MapBuilder.MapBuilder;
import MapBuilder.MapInitializer;
import MapBuilder.WorldGrid;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.label.LabelControl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author 48793
 */
public class GameState extends AbstractAppState {
    

    private AssetManager assetManager;
    private InputManager inputManager;
    private Node worldNode, mapNode, debugNode, pickableNode;
    private WorldGrid worldGrid;
    private Player player;
    private ActionListener actionListener;
    private Node guiNode;
    private AppSettings settings;
    private CameraAndMouseControl camc;
    private EntityManager entityManager;
    private Nifty nifty;
    private FlyByCamera flyCam;
    private ViewPort viewPort;

    //minimap
    private float minimapMovementRatio;
    private float minimapXoffset;
    private int minimapPxSize;

    // map
    private final int CHUNK_SIZE = 1; // 4 , 
    private final int GRID_SIZE = 5; // 5
    private final int MAP_SIZE = 40; // 40
    private final int EXPECTED_ROOMS = 15;
    private int[][][] mapAsArray;
    private TileData[][][] tileDataMap; // to replace mapAsArray

    private HashMap<String, Node> chunks = new HashMap<>();
    // debug
    private Vector3f tester = new Vector3f(0, 0, 0);

    public GameState(Node rootNode, AssetManager assetManager, InputManager inputManager, Node guiNode, AppSettings settings, FlyByCamera flyCam, ViewPort viewPort) {
        mapNode = new Node("MAP NODE");
        debugNode = new Node("DEBUG NODE");
        pickableNode = new Node("PICKABLE NODE");
        worldNode = new Node("GMAE WORLD NODE");
        worldNode.attachChild(debugNode);
        worldNode.attachChild(pickableNode);
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.guiNode = guiNode;
        this.settings = settings;
        worldNode.attachChild(mapNode);
        rootNode.attachChild(worldNode);
        this.flyCam = flyCam;
        this.viewPort = viewPort;
        
        
        // test

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        MapGenerationResult mgr = (new MapInitializer(assetManager)).initialize(worldNode, mapNode, this, GRID_SIZE, MAP_SIZE, EXPECTED_ROOMS);

        mapAsArray = mgr.getMap();
        worldGrid = mgr.getWorldGrid();
        tileDataMap = mgr.getTileDataMap();
        player = Player.initPlayer(assetManager, this);

        player.getPositionNode().move(mgr.getPlayerStartCoords()[0] * GRID_SIZE, 0, mgr.getPlayerStartCoords()[1] * GRID_SIZE);

        MapBuilder mb = new MapBuilder();
        float halfCellDistance = ((GRID_SIZE / 2) + 0.5f);
        Vector3f initialOffset = new Vector3f(0.5f * GRID_SIZE, 0, 0.5f * GRID_SIZE);

        chunks = new HashMap<>();

        for (int x = 0; x < tileDataMap[0].length; x += CHUNK_SIZE) { // 4 == chunkSize
            for (int y = 0; y < tileDataMap[0][0].length; y += CHUNK_SIZE) {
                String nodeName = "Chunk" + x + "/" + y;
                Node chunkNode = new Node(nodeName);
                chunks.put(nodeName, chunkNode);
                mapNode.attachChild(chunkNode);
                
//                chunkNode.move(x,0,y);
                
                System.out.println(nodeName);
            }
        }

        Node currChunkNode = null;

        for (int layer = 0; layer < tileDataMap.length; layer++) {
            for (int x = 0; x < tileDataMap[0].length; x++) {

                for (int y = 0; y < tileDataMap[0][0].length; y++) {

                    currChunkNode = chunks.get("Chunk" + CHUNK_SIZE * (int) (Math.floor(x / CHUNK_SIZE)) + "/" + CHUNK_SIZE * (int) (Math.floor(y / CHUNK_SIZE)));

                    if (tileDataMap[layer][x][y].getTileType() == TileType.FLOOR) {
                        tileDataMap[layer][x][y].setFloorNode(mb.addPlane(this, x, y, initialOffset, new float[]{0, 0, 0}, currChunkNode));

                        if (tileDataMap[layer][x][y].isNorthWall()) {
                            mb.addPlane(this, x, y, initialOffset.add(initialOffset.add(new Vector3f(-halfCellDistance, halfCellDistance + layer * GRID_SIZE, 0))), new float[]{-FastMath.HALF_PI, 0, 0}, currChunkNode);
                        }
                        if (tileDataMap[layer][x][y].isSouthWall()) {
                            mb.addPlane(this, x, y, initialOffset.add(initialOffset.add(new Vector3f(-halfCellDistance, halfCellDistance + layer * GRID_SIZE, -2 * halfCellDistance))), new float[]{FastMath.HALF_PI, 0, 0}, currChunkNode);
                        }
                        if (tileDataMap[layer][x][y].isEastWall()) {
                            mb.addPlane(this, x, y, initialOffset.add(initialOffset.add(new Vector3f(0, halfCellDistance + layer * GRID_SIZE, -halfCellDistance))), new float[]{FastMath.HALF_PI, -FastMath.HALF_PI, 0}, currChunkNode);
                        }
                        if (tileDataMap[layer][x][y].isWestWall()) {
                            mb.addPlane(this, x, y, initialOffset.add(initialOffset.add(new Vector3f(-2 * halfCellDistance, halfCellDistance + layer * GRID_SIZE, -halfCellDistance))), new float[]{FastMath.HALF_PI, FastMath.HALF_PI, 0}, currChunkNode);
                        }

                        
                        if (tileDataMap[layer][x][y].isRoofed()) {
                            mb.addPlane(this, x, y, initialOffset.add(0, GRID_SIZE + layer * GRID_SIZE, 0), new float[]{FastMath.PI, 0, 0}, currChunkNode);
                        }
                        
                        

                    }
                }
            }
        }

        for (Node n : chunks.values()) {
            jme3tools.optimize.GeometryBatchFactory.optimize(n);
        }
        
        
        
//        for(int i = 0; i< 4 ; i++){
//            Random r = new Random();
//            System.out.println(  Arrays.toString(  chunks.keySet().toArray()  ) );
//            
//            
//            
//            Vector3f x = chunks.get(     chunks.keySet().toArray()[   r.nextInt(chunks.size())  ]    ).getWorldTranslation().clone();
//        LightingMethods.addStaticLightSource(this,x);
//            System.out.println("lightSource at "+x);
//    
//
//        }
        
        

        InputEditor inputEditor = new InputEditor();
        inputEditor.setupInput(this);

        GuiEditor guiEditor = new GuiEditor();
        guiEditor.attachCrosshair(this);

        EntityManager em = new EntityManager();
        this.entityManager = em;

        MinimapInitializer mmi = new MinimapInitializer();
        mmi.generateMinimap(mapAsArray, assetManager, guiNode, new int[]{(int) Math.floor(player.getPositionNode().getWorldTranslation().getX() / GRID_SIZE), (int) Math.floor(player.getPositionNode().getWorldTranslation().getZ() / GRID_SIZE)}, this);
////
        for (int i = 0; i < 30; i++) {
            Random r = new Random();
            int randomIndex = r.nextInt(mgr.getRoomStartCoords().size());
            float offsetX = mgr.getRoomStartCoords().get(randomIndex)[0] + (mgr.getRoomsByOrigin().get(mgr.getRoomStartCoords().get(randomIndex))[0]) * r.nextFloat();
            float offsetY = mgr.getRoomStartCoords().get(randomIndex)[1] + (mgr.getRoomsByOrigin().get(mgr.getRoomStartCoords().get(randomIndex))[1]) * r.nextFloat();

            HumanMob.spawnScavenger(mapNode, this, em, debugNode).getNode().move(offsetX * GRID_SIZE, 0, offsetY * GRID_SIZE);
        }
//        HelicopterBoss1.spawnBoss(this);

        player.getPositionNode().move(initialOffset);
        
        
        
        
        
        
        
        
        
        
          Box cube2Mesh = new Box( 1f,1f,1f);
    Geometry cube2Geo = new Geometry("window frame", cube2Mesh);
       Texture t = assetManager.loadTexture("Textures/walls/testRoomTile.png");
        t.setMagFilter(Texture.MagFilter.Nearest);
    Material cube2Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    cube2Mat.setColor("Color", ColorRGBA.Red);
    cube2Mat.setTexture("ColorMap", t);
    cube2Geo.setMaterial(cube2Mat);
    mapNode.attachChild(cube2Geo);
    cube2Geo.move(player.getPositionNode().getWorldTranslation().clone().setY(2));
        
        

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Node getMapNode() {
        return mapNode;
    }

    public void setMapNode(Node mapNode) {
        this.mapNode = mapNode;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public Node getGuiNode() {
        return guiNode;
    }

    public void setGuiNode(Node guiNode) {
        this.guiNode = guiNode;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }

    public CameraAndMouseControl getCamc() {
        return camc;
    }

    public void setCamc(CameraAndMouseControl camc) {
        this.camc = camc;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Node getDebugNode() {
        return debugNode;
    }

    public void setDebugNode(Node debugNode) {
        this.debugNode = debugNode;
    }

    public Node getWorldNode() {
        return worldNode;
    }

    public void setWorldNode(Node worldNode) {
        this.worldNode = worldNode;
    }

    public Node getPickableNode() {
        return pickableNode;
    }

    public void setPickableNode(Node pickableNode) {
        this.pickableNode = pickableNode;
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    public FlyByCamera getFlyCam() {
        return flyCam;
    }

    public void setFlyCam(FlyByCamera flyCam) {
        this.flyCam = flyCam;
    }

    public WorldGrid getWorldGrid() {
        return worldGrid;
    }

    public void setWorldGrid(WorldGrid worldGrid) {
        this.worldGrid = worldGrid;
    }

    public TileData[][][] getTileDataMap() {
        return tileDataMap;
    }

    public void setTileDataMap(TileData[][][] tileDataMap) {
        this.tileDataMap = tileDataMap;
    }

    public float getMinimapMovementRatio() {
        return minimapMovementRatio;
    }

    public void setMinimapMovementRatio(float minimapMovementRatio) {
        this.minimapMovementRatio = minimapMovementRatio;
    }

    public float getMinimapXoffset() {
        return minimapXoffset;
    }

    public void setMinimapXoffset(float minimapXoffset) {
        this.minimapXoffset = minimapXoffset;
    }

    public int getMinimapPxSize() {
        return minimapPxSize;
    }

    public void setMinimapPxSize(int minimapPxSize) {
        this.minimapPxSize = minimapPxSize;
    }

    public HashMap<String, Node> getChunks() {
        return chunks;
    }

    public void setChunks(HashMap<String, Node> chunks) {
        this.chunks = chunks;
    }

    public void chatPutMessage(String message) {
        if (nifty != null) {
            message = "\\#ffffff#" + message;
            String currentText = nifty.getCurrentScreen().findControl("Chat", LabelControl.class).getText();
            String[] lines = currentText.split("\\r?\\n");
            System.out.println("------------------------------------------------");
            String prevText = currentText;
            if (lines.length > 4) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < lines.length; i++) {
                    sb.append("\n");
                    sb.append(lines[i]);
                }
                prevText = sb.toString();
            }

            System.out.println(prevText);

            nifty.getCurrentScreen().findControl("Chat", LabelControl.class).setText(prevText + "\n" + message);
        }
    }

    @Override
    public void update(float tpf) {
        player.move(tpf, this); //check for WSAD and move
        player.shoot(tpf, this, camc, entityManager, mapNode, debugNode);
        /* first person
          animations and shooting    */
        
        player.updateLighting(this);

        
      

        entityManager.updateParticles(tpf);

        entityManager.updatePickableItems(tpf, this);

//                                player.pl.setPosition(new Vector3f( (int)   (player.getPositionNode().getWorldTranslation().getX()/GRID_SIZE)*GRID_SIZE + 0.5f * GRID_SIZE,2,(int) (player.getPositionNode().getWorldTranslation().getZ()/GRID_SIZE)*GRID_SIZE+0.5f * GRID_SIZE));
    }
}
