package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Component;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

/**
 * This class represents the single leaf on the game, it manages the lifetime and the death time of the leaf.
 */
public class Leaf extends Block {

    // ========================== private constants ==========================

    private static final float FADEOUT_TIME = 7;
    private static final float VEL_FALLING_LEAF = 80;
    private static final float TRANSITION_TIME = 2;
    private static final float DELTA_DIMENSION = 2;
    private static final Float INITIAL_ANGLE_TRAN_VAL = -10f;
    private static final Float FINAL_ANGLE_TRAN_VAL = 10f;
    private static final Float INITIAL_HORIZONTAL_TRAN_VAL = -50f;
    private static final Float FINAL_HORIZONTAL_TRAN_VAL = 50f;



    // =============================== fields ===============================

    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private Component horizontalTransition;
    private Transition<Vector2> dimensionsTransition;
    private Transition<Float> angleTransition;
    private boolean flag; // true while a collision happened and the vel of the leaf is still not Vector.ZERO

    // =========================== public methods ===========================

    /**
     * Construct a new Leaf instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.flag = false;
        setLifeTime();
    }

    /**
     * on collision, we want the leaf will stay on the ground in static mode, without moving so this method
     * is responsible for that.
     *
     * @param other     shouldCollideWith(other) is true, this method will be called.
     * @param collision type of collision that happened.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(horizontalTransition);
        removeComponent(angleTransition);
        removeComponent(dimensionsTransition);
        horizontalTransition = null;
        angleTransition = null;
        dimensionsTransition = null;
        setVelocity(Vector2.ZERO);
        flag = true;
    }

    /**
     * updating the object game each delta time.
     * we chose to override this method beacuse sometime the velocity of the leaf after collision didn't
     * get zero, and we wanted to make sure all that the vel of all leaves will become zero after collision
     * with the ground.
     *
     * @param deltaTime delta time for updating.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (flag && getVelocity() != Vector2.ZERO) {
            setVelocity(Vector2.ZERO);
            flag = false;
        }
    }

    // =========================== private methods ===========================

    /**
     * this method responsible for the falling of leaves, after the leaf was faded out, it calls for
     * the method which set the death time of the leaf.
     */
    private void fallingLeaves() {
        horizontalTransition = new Transition<>(
                this,                        // the game object being changed
                vel -> this.transform().setVelocityX(vel),
                INITIAL_HORIZONTAL_TRAN_VAL,                                     // initial transition value
                FINAL_HORIZONTAL_TRAN_VAL,                                       // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,              // use a cubic interpolator
                TRANSITION_TIME,                                   // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        this.transform().setVelocityY(VEL_FALLING_LEAF);
        this.renderer().fadeOut(FADEOUT_TIME, this::setDeathTime);
    }

    /**
     * this method sets the death time of the leaf, for how long the leaf will be faded out.
     */
    private void setDeathTime() {
        new ScheduledTask(this, (float) Math.random() * 30,
                false, this::setLifeTime);
    }

    /**
     * this method begins a new life cycle of the leaf. After the death time has passed, this method
     * places the leaf in its initial place and randomize a new lifetime of it.
     */
    private void setLifeTime() {
        this.setTopLeftCorner(topLeftCorner);
        this.renderer().fadeIn(0);
        new ScheduledTask(this, (float) Math.random() * 5,
                false, this::leafMovement); // scheduled the life movement of the leaf on the tree
        new ScheduledTask(this, (float) Math.random() * 30,
                false, this::fallingLeaves);
    }

    /**
     * setting transitions for the leaf movement on the tree, angle and dimensions.
     */
    private void leafMovement() {
        angleTransition = new Transition<>(
                this,                               // the game object being changed
                angle -> this.renderer().setRenderableAngle(angle),
                INITIAL_ANGLE_TRAN_VAL,                                  // initial transition value
        FINAL_ANGLE_TRAN_VAL,                                            // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,                    // use a cubic interpolator
                TRANSITION_TIME,                                          // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        dimensionsTransition = new Transition<>(
                this,                                // the game object being changed
                this::setDimensions,
                dimensions,                                                    // initial transition value
                dimensions.add(new Vector2(DELTA_DIMENSION, DELTA_DIMENSION)), // final transition value
                Transition.LINEAR_INTERPOLATOR_VECTOR,                         // use a cubic interpolator
                TRANSITION_TIME,                                          // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }
}
