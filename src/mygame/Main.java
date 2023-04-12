package mygame;

import EntityControls.CameraAndMouseControl;
import GUILayer.GraphicUserInterfaceAppState;
import GameStates.GameState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        
    }

    @Override
    public void simpleInitApp() {
        
        
        
        
        GameState gs = new GameState(rootNode, assetManager, inputManager, guiNode, settings, flyCam, viewPort);

        viewPort.setBackgroundColor(ColorRGBA.Black);
        flyCam.setMoveSpeed(40);
        stateManager.attach(gs);
        stateManager.attach(new CameraAndMouseControl(this, gs));
        stateManager.attach(new GraphicUserInterfaceAppState(gs));

        //enable screenshots
        ScreenshotAppState screenShotState = new ScreenshotAppState();
        stateManager.attach(screenShotState);

        flyCam.setZoomSpeed(70);

  

        addFog();
//        setDisplayFps(false);

//        setDisplayStatView(false);


//    flyCam.setEnabled(true);




    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    
    private void addFog(){
          FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        //fpp.setNumSamples(4);
        int numSamples = getContext().getSettings().getSamples();
        if( numSamples > 0 ) {
            fpp.setNumSamples(numSamples); 
        }
        FogFilter fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0f, 0f, 0f, 1.0f));
//                fog.setFogColor(new ColorRGBA(96f/255f, 104f/255f, 110f/255f, 1.0f));

        fog.setFogDistance(100000);
        fog.setFogDensity(1f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
        
    }
    
}
