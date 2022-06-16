package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;


/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {

    // ========================== private constants ==========================
    private static final String SUN_TAG = "sun";
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final Float INITIAL_ANGLE = 0f;
    private static final Float FINAL_ANGLE = 360f;
    private static final float SUN_SIZE = 150f;
    private static final float SUN_FACTOR_BY_WIND = 0.5f;

    // =========================== public methods ============================

    /**
     * This function creates a yellow circle that moves in the sky in an
     * elliptical path (in camera coordinates).
     *
     * @param gameObjects      - The collection of all participating game objects.
     * @param layer            - The number of the layer to which the created sun should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param cycleLength      - The amount of seconds it should take the created
     *                         game object to complete a full cycle.
     * @return A new game object representing the sun.
     */
    public static danogl.GameObject create(danogl.collisions.GameObjectCollection gameObjects,
                                           int layer,
                                           danogl.util.Vector2 windowDimensions,
                                           float cycleLength) {

        Renderable renderable = new OvalRenderable(SUN_COLOR);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SUN_SIZE, SUN_SIZE), renderable);

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // moves with the camera.
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);

        new Transition<>(
                sun,                                                  // the game object being changed
                angle -> sun.setCenter(Vector2.UP.rotated(angle).
                        multX(windowDimensions.x() / 2).
                        multY(windowDimensions.y() / 2).
                        add(windowDimensions.mult(SUN_FACTOR_BY_WIND))),            // the method to call
                INITIAL_ANGLE,                                        // initial transition value
                FINAL_ANGLE,                                          // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,                 // use a cubic interpolator
                cycleLength,                                          // transition fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }

}
