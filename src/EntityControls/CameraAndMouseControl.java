package EntityControls;

import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.math.Ray;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Makary
 */
public class CameraAndMouseControl extends AbstractAppState {
    private static final boolean TOP_DOWN = false;
    
    private final float RENDER_DISTANCE = 70; // 70
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private final Node cameraControlNode = new Node("cameraControlNode");
    private final RenderManager renderManager;
    private final Node rootNode;
    public CameraNode camNode;
    private  Camera camera;
    Material mat;
    public static Vector3f unitScreenPosition;
    public ArrayList<Node> chosenUnitNodes = new ArrayList<>(); // chosen units 
    public static ArrayList<Vector3f> unitDestination = new ArrayList<>(); // zrobic hash mapa
    public static Vector3f lookingDestination;
    public GameState gs;
    Quaternion temporaryCameraRotQuaternion = new Quaternion();
    float[] cameraRotAsAngles = new float[3];
    public static Camera handsCam;
    
    
    
    Node chosenEnemyUnitNode;
    Spatial selectionBox;
    Spatial enemySelectionBox;
    Node waypoint;
    Spatial waypointSpatial;
    public static Vector3f waypointZero; // waypoint.getWorldTranslation() but with replaced Y with 0

    
    
    private float currentY = 5;  // test
    
    
    public CameraAndMouseControl(SimpleApplication Main, GameState gs) {
        rootNode = Main.getRootNode();
        assetManager = Main.getAssetManager();
        inputManager = Main.getInputManager();
        camera = Main.getCamera();
        renderManager = Main.getRenderManager();
        this.gs = gs;
        gs.setCamc(this);
        
camera.setFrustumPerspective(45, (float)gs.getSettings().getWidth() / gs.getSettings().getHeight(), 0.01f, RENDER_DISTANCE);                  
currentY = camera.getLocation().clone().getY();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initCamera();

    }

    public void initCamera() {

        handsCam = camera.clone();
        handsCam.setViewPort(0, 1, 0, 1);
        handsCam.setLocation(new Vector3f(0, -49.5f, -2));

//        handsCam.setRotation(playerHandsNode.getLocalRotation());
        handsCam.lookAt(gs.getPlayer().getHandsNode().getWorldTranslation(), Vector3f.ZERO);

        ViewPort handsViewPort = renderManager.createMainView("View of camera #n", handsCam);
//view_n.setClearEnabled(true);
//view_n.setClearColor(true);
        handsViewPort.attachScene(gs.getPlayer().getHandsNode());
        handsViewPort.setBackgroundColor(ColorRGBA.Black);
        handsViewPort.setClearFlags(false, true, true);

//    rootNode.attachChild(cameraControlNode);
//    camNode = new CameraNode("Camera Node", camera);
//    camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
//    UnitUpdate.playerNode.attachChild(camNode);
//    camNode.setLocalTranslation(new Vector3f(0, 5, 0)); // 0,15,20 // 0,18,24 // 0,12,16
//    camNode.lookAt(UnitUpdate.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
        lookingDestination = new Vector3f(gs.getPlayer().getNode().getLocalRotation().getRotationColumn(2).x + 2, 2, gs.getPlayer().getNode().getLocalRotation().getRotationColumn(2).z + 2);
//        camNode.lookAt(lookingDestination, Vector3f.UNIT_Y);

    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

        }

    };

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {

        }
    };

    @Override
    public void cleanup() {

        super.cleanup();
    }

    @Override
    public void update(float tpf) {
if(!TOP_DOWN){
        camera.getRotation().toAngles(cameraRotAsAngles);

//check the x rotation
        if (cameraRotAsAngles[0] > FastMath.QUARTER_PI) {

            cameraRotAsAngles[0] = FastMath.QUARTER_PI;

            camera.setRotation(temporaryCameraRotQuaternion.fromAngles(cameraRotAsAngles));

        } else if (cameraRotAsAngles[0] < -FastMath.QUARTER_PI ) {

            cameraRotAsAngles[0] = -FastMath.QUARTER_PI;

            camera.setRotation(temporaryCameraRotQuaternion.fromAngles(cameraRotAsAngles));

        }
}
        
//        currentY += tpf*16;
        

        if (gs.getPlayer().getHealth() > 0) {//                                                          2.12f so you are level with humanoids
//            if(gs.getPlayer().isForward() || gs.getPlayer().isRight() || gs.getPlayer().isLeft() || gs.getPlayer().isBackward())      
//            camera.setLocation(new Vector3f(gs.getPlayer().getNode().getWorldTranslation().x, 2.12f+(float)Math.sin(currentY)/30, gs.getPlayer().getNode().getWorldTranslation().z));


        if(TOP_DOWN){
            camera.setLocation(new Vector3f(gs.getPlayer().getNode().getWorldTranslation().x, 30f, gs.getPlayer().getNode().getWorldTranslation().z));
//            camera.lookAt(new Vector3f(gs.getPlayer().getNode().getWorldTranslation().x, 0f, gs.getPlayer().getNode().getWorldTranslation().z), Vector3f.UNIT_Z);
     
        }else{
                    camera.setLocation(new Vector3f(gs.getPlayer().getNode().getWorldTranslation().x, 2.12f, gs.getPlayer().getNode().getWorldTranslation().z));

        }
//            System.out.println(gs.getPlayer().getHandsNode().getWorldTranslation());
        
            CollisionResults results = new CollisionResults();
            Ray ray = new Ray(camera.getLocation(), camera.getDirection());
            gs.getWorldNode().collideWith(ray, results);

            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                lookingDestination = new Vector3f(closest.getContactPoint().x, 0f, closest.getContactPoint().z);
                gs.getPlayer().getNode().lookAt(lookingDestination, Vector3f.UNIT_Y);
                
                

                
            }
        }
    }

    public  Camera getCamera() {
        return camera;
    }

    public  void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public static boolean isTopDown(){
    return TOP_DOWN;
    }

    
    public float[] getCamAngles(){
    return         camera.getRotation().toAngles(cameraRotAsAngles);

    }
    
}
