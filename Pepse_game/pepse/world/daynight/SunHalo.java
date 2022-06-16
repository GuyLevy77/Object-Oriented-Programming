package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;

import java.awt.*;

/**
 * Represents the halo of sun.
 */
public class SunHalo {

    // ========================== private constants ==========================

    private static final String SUN_HALO_TAG = "sunHalo";
    private static final float SUN_HALO_FACTOR = 1.7f;

    // =========================== public methods ===========================

    /**
     * This function creates a halo around a given object that represents the sun.
     * The halo will be tied to the given sun, and will always move with it.
     *
     * @param gameObjects - The collection of all participating game objects.
     * @param layer       - The number of the layer to which the created halo should be added.
     * @param sun-        A game object representing the sun (it will be followed by the created game object).
     * @param color       - The color of the halo.
     * @return A new game object representing the sun's halo.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {

        Renderable renderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(
                sun.getTopLeftCorner(),
                sun.getDimensions().mult(SUN_HALO_FACTOR), renderable);

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // moves with the camera.
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(SUN_HALO_TAG);
        return sunHalo;
    }

}
