package pepse.world;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * An avatar can move around the world.
 */
public class Avatar extends GameObject {

    // ========================== private constants ==========================

    private static final String AVATAR_CONST_IMG_PATH = "assets/mario3.png";
    private static final double TIME_BETWEEN_CLIPS = 0.05;
    private static final float INITIAL_ENERGY = 100;
    private static final float ENERGY_DELTA = 0.5f;
    private static final float AVATAR_WIDTH = 50;
    private static final float AVATAR_HEIGHT = 80;
    private static final float JUMP_HEIGHT = 15;
    private static final float AVATAR_VEL = 300;
    private static final float AVATAR_ACC = 5000;
    private static final String[] AVATAR_STR_IMAGES = new String[]
            {"assets/mario1.png", "assets/mario2.png", "assets/mario3.png",
                    "assets/mario4.png", "assets/mario5.png", "assets/mario6.png"};

    // =============================== fields ===============================

    private final Renderable renderable;
    private final AnimationRenderable animationRenderable;
    private final UserInputListener inputListener;
    private float energy;

    // =========================== public methods ===========================

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = INITIAL_ENERGY;
        this.inputListener = inputListener;
        this.renderable = imageReader.readImage(AVATAR_CONST_IMG_PATH, true);

        this.animationRenderable = new AnimationRenderable(AVATAR_STR_IMAGES,
                imageReader, true, TIME_BETWEEN_CLIPS);
    }

    /**
     * creates the avatar of the game.
     *
     * @param gameObjects   game object collection of the game
     * @param layer         layer to add the avatar to
     * @param topLeftCorner top left corner of the initial place of the avatar
     * @param inputListener input listener so the avatar can be controlled by the user.
     * @param imageReader   image reader for render the avatar
     * @return new instance of avatar player.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {

        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_WIDTH, AVATAR_HEIGHT),
                imageReader.readImage(AVATAR_CONST_IMG_PATH, true),
                inputListener, imageReader);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * updating each delta time, so the avatar will be able to listen to the user commands.
     *
     * @param deltaTime each delta time the avatar will update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        avatarControlledByUser();
    }

    // =========================== private methods ===========================

    /**
     * this function checks the command of the user and act accordingly to them. manage all the interaction
     * between "the avatar" and the user.
     */
    private void avatarControlledByUser() {
        Vector2 movementDir = Vector2.ZERO;
        // left arrow was pressed
        movementDir = arrowWasPressed(movementDir, KeyEvent.VK_LEFT, Vector2.LEFT, true);

        // right arrow was pressed
        movementDir = arrowWasPressed(movementDir, KeyEvent.VK_RIGHT, Vector2.RIGHT, false);

        // space was pressed
        movementDir = spaceWasPressed(movementDir);

        // in case the vertical velocity is not 0 and also the shift button is not pressed
        if ((getVelocity().y() != 0 && !inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) || energy == 0) {
            movementDir.multY(0);
        }

        // the avatar is not moving (at rest)
        if (getVelocity().x() == 0 && getVelocity().y() == 0) {
            energy += ENERGY_DELTA;
        }

        // avatar is not moving in the x-axis
        if (getVelocity().x() == 0) {
            renderer().setRenderable(renderable);
        }

        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        setVelocity(movementDir.mult(AVATAR_VEL));
        this.transform().setAccelerationY(AVATAR_ACC);
    }

    /**
     * check if the space button was pressed and act accordingly.
     *
     * @param movementDir the movement dir to change according to the user command.
     * @return the new movement dir after change, if there was.
     */
    private Vector2 spaceWasPressed(Vector2 movementDir) {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (getVelocity().y() == 0) {
                movementDir = movementDir.add(Vector2.UP.mult(JUMP_HEIGHT));
            }
            if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy > 0) {
                movementDir = movementDir.add(Vector2.UP);
                energy -= ENERGY_DELTA;
            }
        }
        return movementDir;
    }

    /**
     * check if one of the arrows (left/right) was pressed, the parameters sent accordingly.
     *
     * @param movementDir the movement dir to change according to the arrow.
     * @param button      left arrow or right arrow
     * @param direction   direction vector (Vector2.RIGHT, Vector2.LEFT)
     * @param flipped     true/false according to direction.
     * @return the new movement dir according to changes.
     */
    private Vector2 arrowWasPressed(Vector2 movementDir, int button, Vector2 direction, boolean flipped) {
        if (inputListener.isKeyPressed(button)) {
            movementDir = movementDir.add(direction);
            renderer().setIsFlippedHorizontally(flipped);
            if (getVelocity().y() == 0) {
                renderer().setRenderable(animationRenderable);
            }
        }
        return movementDir;
    }
}

