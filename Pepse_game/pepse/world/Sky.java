package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;


/**
 * Represents the sky.
 */
public class Sky {

    // ========================== private constant ==========================

    private static final String SKY_TAG = "sky";
    private static final Color SKY_COLOR = Color.decode("#80C6E5");

    // =========================== public methods ===========================

    /**
     * This function creates a light blue rectangle which is always at the back of the window.
     *
     * @param gameObjects       - The collection of all participating game objects.
     * @param windowDimensions- The number of the layer to which the created game object should be added.
     * @param skyLayer          - The number of the layer to which the created sky should be added.
     * @return A new game object representing the sky.
     */
    public static danogl.GameObject create(
            danogl.collisions.GameObjectCollection gameObjects,
            danogl.util.Vector2 windowDimensions,
            int skyLayer) {
        GameObject sky = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(SKY_COLOR));

        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // moves with the camera.
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SKY_TAG);
        return sky;
    }
}