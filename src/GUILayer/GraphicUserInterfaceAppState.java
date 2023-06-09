/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUILayer;

import EntityControls.CameraAndMouseControl;
import EntityControls.EntityManager;
import GameObjects.Mobs.Mob;
import GameObjects.Mobs.Player;
import GameStates.GameState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.label.LabelControl;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.Random;

/**
 *
 * @author 48793
 */
public class GraphicUserInterfaceAppState extends BaseAppState {

    private Nifty nifty;
    private GameState gs;
    private int cnt = 0;
    private int hotBarCnt = 0;
    private final double eqMargin = 0;
    private final int ROW_NUM = 6;
    private double eqSlotSizePx;
    private int equipmentSlotAdded = 0;

    private static final int HP_BAR_TOP_MARGIN = -92;

    private float percentMobHealthbarLength = 0.33f;
    private float percentMobHealthbarHeight = 0.01f;

    public GraphicUserInterfaceAppState(GameState gs) {
        this.gs = gs;
    }

    @Override
    protected void initialize(Application app) {

        //It is technically safe to do all initialization and cleanup in the
        //onEnable()/onDisable() methods. Choosing to use initialize() and
        //cleanup() for this is a matter of performance specifics for the
        //implementor.
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
    }

    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
    }

    //onEnable()/onDisable() can be used for managing things that should
    //only exist while the state is enabled. Prime examples would be scene
    //graph attachment or input listener attachment.
    public void bind(Nifty nifty, Screen screen) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onEnable() {
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                getApplication().getAssetManager(),
                getApplication().getInputManager(),
                getApplication().getAudioRenderer(),
                getApplication().getGuiViewPort());

        nifty = niftyDisplay.getNifty();
        gs.setNifty(nifty);
        System.out.println("ENABLE--------------------------");
        getApplication().getGuiViewPort().addProcessor(niftyDisplay);
        ((SimpleApplication) getApplication()).getFlyByCamera().setDragToRotate(true);

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        // <screen>
        nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen") {
            {
                controller(new MainScreenController(gs)); // Screen properties

                layer(new LayerBuilder("Chat") {
                    {
                        childLayoutHorizontal();
                        paddingTop("82%");
                        paddingLeft("1%");


                        control(new LabelBuilder("Chat", "") {
                            {
                                alignCenter();
                                valignCenter();
                                height("85%");
                                width("25%");
//                                backgroundColor("#A30000");
                                visibleToMouse(false);
                                textVAlign(VAlign.Top);
                                textHAlign(Align.Left);
                                this.font("Interface/Fonts/pixelCC0_noOutline.fnt");
                                
                                wrap(true);

//                                 onActiveEffect(new EffectBuilder("textSize") {
//                                    {
//                                        effectParameter("endSize", "0.8");
//
//                                    }
//                                });

                            }
                        });
                    }
                });

                layer(new LayerBuilder("modifiableLayer") {
                    {
                        childLayoutHorizontal(); // layer properties, add more...

                        // <panel>
                        panel(new PanelBuilder("modifiable_bars") {
                            {
                                childLayoutHorizontal(); // panel properties, add more...
                                paddingLeft("3.1%"); // 3.1%
                                paddingTop(HP_BAR_TOP_MARGIN + "%");

                                // GUI elements
                                control(new LabelBuilder("hp_bar", "") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        height("" + (percentMobHealthbarHeight * 100) + "%");
                                        width("25%");
                                        backgroundColor("#A30000");
                                        visibleToMouse(false);
                                    }
                                });

                                //.. add more GUI elements here
                            }
                        });
                        // </panel>

                    }
                });

                layer(new LayerBuilder("ammo") {
                    {
                        childLayoutHorizontal();

                        panel(new PanelBuilder("ammo") {
                            {
                                childLayoutHorizontal(); // panel properties, add more...
                                paddingLeft("3.1%"); // 3.1%
                                paddingTop("90%");

                                // GUI elements
                                control(new LabelBuilder("ammo_label", "") {
                                    {
                                        this.font("Interface/Fonts/pixel25.fnt");
                                        alignCenter();
                                        valignCenter();
                                        height("50%");
                                        width("25%");
//                                        backgroundColor("#A30000");
                                        visibleToMouse(false);
                                    }
                                });

                                //.. add more GUI elements here
                            }
                        });

                    }
                });

                layer(new LayerBuilder("mobHealthBarGray") {
                    {
                        childLayoutHorizontal();
                        paddingTop(HP_BAR_TOP_MARGIN + "%");
                        paddingLeft("" + ((100 - percentMobHealthbarLength * 100) / 2) + "%");
                        final float length = ((percentMobHealthbarLength * nifty.getRenderEngine().getNativeWidth()));

                        control(new LabelBuilder("hp_bar_target_gray", "") {
                            {
                                visible(false);
                                alignCenter();
                                valignCenter();
                                height("" + (percentMobHealthbarHeight * 100) + "%");
                                width("" + length + "px");
                                backgroundColor("#525252");
                                visibleToMouse(false);
                            }
                        });

                    }
                });

                layer(new LayerBuilder("mobHealthBarYellow") {
                    {
                        childLayoutHorizontal();

                        paddingTop(HP_BAR_TOP_MARGIN + "%");
                        paddingLeft("" + ((100 - percentMobHealthbarLength * 100) / 2) + "%");
                        final float length = ((percentMobHealthbarLength * nifty.getRenderEngine().getNativeWidth()));

                        control(new LabelBuilder("hp_bar_target_yellow", "") {
                            {
                                alignCenter();
                                valignCenter();
                                height("" + (percentMobHealthbarHeight * 100) + "%");
                                width("" + 0 + "px");
                                backgroundColor("#FFBF00");
                                visibleToMouse(false);
                            }
                        });

                    }
                });

                layer(new LayerBuilder("mobHealthBarRed") {
                    {
                        childLayoutHorizontal();
                        paddingTop(HP_BAR_TOP_MARGIN + "%");
                        paddingLeft("" + ((100 - percentMobHealthbarLength * 100) / 2) + "%");

                        control(new LabelBuilder("hp_bar_target", "") {
                            {
                                alignCenter();
                                valignCenter();
                                height("1%");
                                width("0%");
                                backgroundColor("#A30000");
                                visibleToMouse(false);
                            }
                        });

                    }
                });

                layer(new LayerBuilder("mobHealthBarLabel") {
                    {
                        childLayoutHorizontal();
                        paddingTop(HP_BAR_TOP_MARGIN - 0.2 + "%");
                        paddingLeft("" + ((100 - percentMobHealthbarLength * 100) / 2) + "%");
                        final float length = ((percentMobHealthbarLength * nifty.getRenderEngine().getNativeWidth()));

                        control(new LabelBuilder("hp_bar_target_label", "Scavenger") {
                            {
                                alignCenter();
                                valignCenter();
                                height("1%");
                                this.font("Interface/Fonts/pixelCC0_noOutline.fnt");

                                width("" + length + "px");
//                                backgroundColor("#A30000");
                                visibleToMouse(false);
                            }
                        });

                    }
                });

                // DRAW HOTBAR
                layer(new LayerBuilder("hotbar") {
                    {
                        childLayoutHorizontal();
                        paddingLeft("" + ((nifty.getRenderEngine().getNativeWidth() - (0.08 * nifty.getRenderEngine().getNativeHeight() * gs.getPlayer().getHotbar().length)) / 2) + "px");
                        paddingTop("60%");

                        for (int slotNumber = 0; slotNumber < gs.getPlayer().getHotbar().length; slotNumber++) {
                            final int i = slotNumber;
                            image(new ImageBuilder("hotbarslot" + i) {
                                {
                                    visible(true);
                                    filename("Textures/GUI/equipmentSlotEmpty.png");
//      

                                    height(nifty.getRenderEngine().getNativeHeight() * 0.08 + "px");
                                    width(nifty.getRenderEngine().getNativeHeight() * 0.08 + "px");
                                    visibleToMouse(true);
                                    // kod od ustawianai okienek
                                    marginLeft("" + (eqMargin + 2) + "px");
                                    marginTop("80%");

                                    // kod od ustawiania okienek
                                }
                            });

                        }

                    }
                });

                // DRAW EQUIPPED SLOTS
                layer(new LayerBuilder("equippedLayer") {
                    {
                        childLayoutHorizontal();
                        paddingTop("10%");
                        paddingLeft("2%");

                    }
                });

                // DRAW EQUIPMENT SLOTS
                eqSlotSizePx = nifty.getRenderEngine().getNativeHeight() * 0.08;

                for (int j = 0; j < ROW_NUM; j++) {

                    layer(new LayerBuilder("playerEquipmentLayer") {
                        {

                            childLayoutHorizontal();
                            paddingLeft("80%");
                            paddingTop("30%");

                            cnt++;
                            final double marginTop = cnt * eqSlotSizePx;
                            System.out.println("marginTop: " + marginTop);
                            for (int slotNumber = 0; slotNumber < gs.getPlayer().getEquipment().length / ROW_NUM; slotNumber++) {
                                final int i = slotNumber;
//                                cnt = slotNumber;
                                image(new ImageBuilder("slot" + equipmentSlotAdded) {
                                    {
                                        visible(false);
                                        filename("Textures/GUI/equipmentSlotEmpty.png");
//                        alignCenter();
//                        valignCenter();
//                                    height("8%");
//                                    width("8%");

                                        height(nifty.getRenderEngine().getNativeHeight() * 0.08 + "px");
                                        width(nifty.getRenderEngine().getNativeHeight() * 0.08 + "px");
                                        visibleToMouse(true);
                                        interactOnClick("playerEquipItem(" + equipmentSlotAdded++ + ")");
//                        interactOnMouseOver("getItemInfo("+i+")");

//onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
//      }});
//                                    onEndHoverEffect(new HoverEffectBuilder("border") {
//                                        {
//                                            effectParameter("hoverHeight", "100%");
//                                            effectParameter("hoverWidth", "100%");
//                                            effectParameter("color", "#ffff");
//                                            effectParameter("border", "2px");
//                                            effectParameter("onStartEffect", "hideTooltip()");
//                                        }
//                                    });
                                        onStartHoverEffect(new HoverEffectBuilder("border") {
                                            {
                                                effectParameter("hoverHeight", "100%");
                                                effectParameter("hoverWidth", "100%");
                                                effectParameter("color", "#000");
                                                effectParameter("border", "3px");
                                                effectParameter("onStartEffect", "getItemInfo(" + equipmentSlotAdded + ")");

                                            }
                                        });
                                        // kod od ustawianai okienek
                                        marginLeft("" + (eqMargin + 2) + "px");
                                        marginTop("" + marginTop + "px");

                                        // kod od ustawiania okienek
                                    }
                                });

                            }

                        }
                    });

                }

                // <layer>
                layer(new LayerBuilder("staticLayer") {
                    {
                        childLayoutAbsolute(); // layer properties, add more...

                        panel(new PanelBuilder("itemTooltip") {
                            {

                                childLayoutVertical(); // layer properties, add more...
                                height("8%");
                                width("12%");

                                visible(false);

                                image(new ImageBuilder("s") {
                                    {
                                        filename("Textures/GUI/tooltipFrame.png");
                                        childLayoutVertical(); // layer properties, add more...
//                   label("item");
                                    }
                                });

                            }
                        });

                    }
                });
                layer(new LayerBuilder("age") {
                    {
                        childLayoutAbsolute(); // layer properties, add more...

                        panel(new PanelBuilder("TooltipLabel") {
                            {
                                childLayoutVertical(); // layer properties, add more...
                                height("8%");
                                width("8%");

                                visible(false);

                                control(new LabelBuilder("TooltipLabelControl", "") {
                                    {
                                        wrap(true);
//                         filename("Textures/GUI/tooltipFrame.png");
                                        childLayoutVertical(); // layer properties, add more...
//                   label("item");
                                    }
                                });
                            }
                        });
                    }
                });
                // </layer>
            }
        }.build(nifty));
        // </screen>

        nifty.gotoScreen("Screen_ID"); // start the screen   
        gs.getFlyCam().setDragToRotate(false);
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void update(float tpf) {
        nifty.getCurrentScreen().findElementById("hp_bar").setWidth((int) (gs.getPlayer().getHealth() * 8 * (getApplication().getGuiViewPort().getCamera().getWidth() * 0.00215f)));
        checkTargetedMob(gs.getPlayer(), gs, gs.getCamc(), gs.getEntityManager(), gs.getWorldNode(), gs.getDebugNode());

        if (gs.getPlayer().getCurrentTarget() != null) {

            float length = ((gs.getPlayer().getCurrentTarget().getHealth() / gs.getPlayer().getCurrentTarget().getMaxHealth()) * (percentMobHealthbarLength * nifty.getRenderEngine().getNativeWidth()));
            nifty.getCurrentScreen().findElementById("hp_bar_target").setWidth((int) (length));
//            nifty.getCurrentScreen().findControl("hp_bar_target", LabelControl.class).setText(Float.toString(gs.getPlayer().getCurrentTarget().getHealth()));
            if (nifty.getCurrentScreen().findElementById("hp_bar_target_yellow").getWidth() > nifty.getCurrentScreen().findElementById("hp_bar_target").getWidth()) {
                nifty.getCurrentScreen().findElementById("hp_bar_target_yellow").setWidth((int) (nifty.getCurrentScreen().findElementById("hp_bar_target_yellow").getWidth() - 300 * tpf));
            }
        }

        if (nifty.getCurrentScreen().findElementById("hp_bar_target_yellow").getWidth() <= 0) {
            nifty.getCurrentScreen().findElementById("hp_bar_target_gray").setVisible(false);
            nifty.getCurrentScreen().findElementById("hp_bar_target_label").setVisible(false);

        }

    }

    private void checkTargetedMob(Player p, GameState gs, CameraAndMouseControl c, EntityManager em, Node nodeToCheckCollisionOn, Node debugNode) {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(c.getCamera().getLocation(), c.getCamera().getDirection());
        nodeToCheckCollisionOn.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            String hit = closest.getGeometry().getParent().getParent().getParent().getName();
            Mob enemyHit = em.getEnemiesAlive().get(hit);
            if (enemyHit == null && closest.getGeometry().getParent().getParent().getParent().getParent() != null) {
                hit = closest.getGeometry().getParent().getParent().getParent().getParent().getName();
                enemyHit = em.getEnemiesAlive().get(hit);
            }

            if (enemyHit != null) {
                boolean switched = p.getCurrentTarget() != enemyHit;
                if (switched) {
                    System.out.println(switched);
                }
                p.setCurrentTarget(enemyHit);

                if (switched) {
                    nifty.getCurrentScreen().findControl("hp_bar_target_label", LabelControl.class).setText(enemyHit.getMobName());
//                    nifty.getCurrentScreen().findControl("hp_bar_target_label", LabelControl.class).setText("Mob \\#80ff00#"+enemyHit.getMobName()); COLORED TEXT

                    nifty.getCurrentScreen().findElementById("hp_bar_target_yellow").setWidth((int) ((p.getCurrentTarget().getHealth() / p.getCurrentTarget().getMaxHealth()) * (percentMobHealthbarLength * nifty.getRenderEngine().getNativeWidth())));
                    nifty.getCurrentScreen().findElementById("hp_bar_target_gray").setVisible(true);
                    nifty.getCurrentScreen().findElementById("hp_bar_target_label").setVisible(true);

                }
//                Sphere sphere1 = new Sphere(30, 30, 0.02f);
//                Spatial mark1 = new Geometry("BOOM!", sphere1);
//                Material mark_mat1 = new Material(gs.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//                mark_mat1.setColor("Color", ColorRGBA.Red);
//                mark1.setMaterial(mark_mat1);
//                                debugNode.attachChild(mark1);
//                mark1.move(closest.getContactPoint());
            }
        }

    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    public GameState getGs() {
        return gs;
    }

    public void setGs(GameState gs) {
        this.gs = gs;
    }

}
