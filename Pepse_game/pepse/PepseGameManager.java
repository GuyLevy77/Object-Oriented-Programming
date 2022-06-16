package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.*;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends GameManager {

    // ========================== public constants ==========================

    public static final int SEED = 120;

    // ========================== private constants ==========================

    private static final int FRAMERATE = 80;
    private static final float CYCLE_LENGTH = 30;
    private static final float AVATAER_HEIGHT = 80;

    private static final String GROUND_TAG = "ground";
    private static final String UPPER_GROUND_TAG = "upper_ground";
    private static final String STEM_TAG = "stem";
    private static final String LEAF_TAG = "leaf";
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TREE_LAYER = Layer.STATIC_OBJECTS;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int UPPER_GROUND_LAYER = Layer.STATIC_OBJECTS + 20;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    // =============================== fields ===============================

    private Terrain terrain;
    private WindowController windowController;
    private Avatar avatar;
    private Tree tree;
    private float curr_place;
    private float leftTerrianCoord;
    private float rightTerrianCoord;

    // =========================== public methods ===========================

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     *
     * @param imageReader      - Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      - Contains a single method: readSound, which reads a wav file from disk.
     *                         See its documentation for help.
     * @param inputListener-   Contains a single method: isKeyPressed, which returns whether a given key is
     *                         currently pressed by the user or not. See its documentation.
     * @param windowController - Contains an array of helpful,
     *                         self explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(danogl.gui.ImageReader imageReader,
                               danogl.gui.SoundReader soundReader,
                               danogl.gui.UserInputListener inputListener,
                               danogl.gui.WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.windowController.setTargetFramerate(FRAMERATE);

        leftTerrianCoord = -windowController.getWindowDimensions().x() / 2;
        rightTerrianCoord = 3 * windowController.getWindowDimensions().x() / 2;
        // --------------- creating camera --------------

        Camera camera = new Camera(Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);

        // --------------- creating sky -----------------
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);

        // -------------- creating terrain --------------
        terrain = new Terrain(
                gameObjects(), GROUND_LAYER,
                windowController.getWindowDimensions(), SEED);
        terrain.createInRange((int) leftTerrianCoord, (int) rightTerrianCoord);

        // -------------- creating night --------------
        Night.create(
                gameObjects(), NIGHT_LAYER, windowController.getWindowDimensions(), CYCLE_LENGTH);

        // -------------- creating sun --------------
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER,
                windowController.getWindowDimensions(), CYCLE_LENGTH);

        // ------------- creating sunHalo ------------
        GameObject sunHalo = SunHalo.create(gameObjects(), SUN_HALO_LAYER,
                sun, SUN_HALO_COLOR);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        // ------------- creating tree --------------
        tree = new Tree(gameObjects(), TREE_LAYER, windowController.getWindowDimensions(),
                terrain::groundHeightAt);
        tree.createInRange(0, (int) windowController.getWindowDimensions().x() + 1);
        gameObjects().layers().shouldLayersCollide(UPPER_GROUND_LAYER,
                LEAF_LAYER, true);

        // ------------- creating avatar -------------
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                new Vector2(windowController.getWindowDimensions().x() / 2,
                        terrain.groundHeightAt(windowController.getWindowDimensions().x() / 2)
                                - AVATAER_HEIGHT),
                inputListener, imageReader);
        curr_place = avatar.getCenter().x();
        camera.setToFollow(avatar, Vector2.ZERO);
        gameObjects().layers().shouldLayersCollide(UPPER_GROUND_LAYER, AVATAR_LAYER
                , true);
    }

    /**
     * updating the game each time. deletes all the objects that are not currently in the bounds of the
     * screen and creating the game objects that supposed to be in the screen.
     *
     * @param deltaTime each delta time that the game is updating.
     */
    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
        float delta = curr_place - avatar.getCenter().x();
        if (Math.abs(delta) < windowController.getWindowDimensions().x() / 2)
            return;
        if (delta < 0) { // avatar turned right
            delete((int) (leftTerrianCoord + delta), (int) (leftTerrianCoord));
            terrain.createInRange((int) rightTerrianCoord, (int) (rightTerrianCoord - delta));
            tree.createInRange((int) rightTerrianCoord, (int) (rightTerrianCoord - delta));
            rightTerrianCoord -= delta;
            leftTerrianCoord -= delta;

        } else if (delta > 0) { // avatar turned left
            delete((int) rightTerrianCoord, (int) (rightTerrianCoord + delta));
            terrain.createInRange((int) (leftTerrianCoord - delta), (int) (leftTerrianCoord));
            tree.createInRange((int) (leftTerrianCoord - delta), (int) (leftTerrianCoord));
            rightTerrianCoord -= delta;
            leftTerrianCoord -= delta;
        }
        curr_place = avatar.getCenter().x();
    }

    // =========================== private methods ===========================

    /**
     * deletes all the game objects between mixX to maxX
     *
     * @param minX the minimal bound of range
     * @param maxX the maximal bound of range
     */
    private void delete(int minX, int maxX) {
        Set<GameObject> objectsToDel = new HashSet<>();
        for (GameObject obj : gameObjects()) {
            if ((int) obj.getCenter().x() >= minX && (int) obj.getCenter().x() <= maxX) {
                objectsToDel.add(obj);
            }
        }
        for (GameObject obj : objectsToDel) {
            String tag = obj.getTag();
            switch (tag) {
                case GROUND_TAG:
                    System.out.println(gameObjects().removeGameObject(obj, GROUND_LAYER));
                    break;
                case UPPER_GROUND_TAG:
                    System.out.println(gameObjects().removeGameObject(obj, UPPER_GROUND_LAYER));
                    break;
                case STEM_TAG:
                    gameObjects().removeGameObject(obj, TREE_LAYER);
                    break;
                case LEAF_TAG:
                    gameObjects().removeGameObject(obj, LEAF_LAYER);
                    break;
                default:
                    gameObjects().removeGameObject(obj);
                    break;
            }
        }
    }

    // ================================ Main ================================

    /**
     * Runs the entire simulation.
     *
     * @param args- This argument should not be used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}