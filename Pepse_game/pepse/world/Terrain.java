package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;


/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {

    // ========================== private constant ==========================

    private static final int TERRAIN_DEPTH = 20;
    private static final Color GROUND_COLOR = new Color(212, 123, 74);
    private static final int UPPER_GROUND_LAYER = Layer.STATIC_OBJECTS + 20;
    private static final String GROUND_TAG = "ground";
    private static final String UPPER_GROUND_TAG = "upper_ground";

    // =============================== fields ===============================

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int seed;

    // =========================== public methods ===========================

    /**
     * This function creates Terrain by using object.
     *
     * @param gameObjects      game objects collection of the game
     * @param groundLayer      layer of the ground
     * @param windowDimensions dimensions of the screen
     */
    public Terrain(danogl.collisions.GameObjectCollection gameObjects,
                   int groundLayer,
                   danogl.util.Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
    }

    /**
     * This method return the ground height at a given location.
     *
     * @param x- A number.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        return perlin(x); // search better noise function
    }

    /**
     * This method creates terrain in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        if (minX % Block.SIZE != 0) // round down minX if needed
            minX -= (minX < 0) ? Block.SIZE + (minX % Block.SIZE) : minX % Block.SIZE;

        if (maxX % Block.SIZE != 0) // round up maxX if needed
            maxX += (maxX < 0) ? -(maxX % Block.SIZE) : Block.SIZE - (maxX % Block.SIZE);
        // creating blocks
        for (int x = minX; x < maxX; x += Block.SIZE) {
            int yHeight = ((int) (perlin(x) / Block.SIZE)) * Block.SIZE;
            for (int y = yHeight; y < yHeight + (Block.SIZE * TERRAIN_DEPTH); y += Block.SIZE) {
                Block block = new Block(
                        new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(GROUND_COLOR)));
                if (y == yHeight || y == yHeight + Block.SIZE) {
                    gameObjects.addGameObject(block, UPPER_GROUND_LAYER);
                    block.setTag(UPPER_GROUND_TAG);
                } else {
                    gameObjects.addGameObject(block, groundLayer);
                    block.setTag(GROUND_TAG);
                }
            }
        }
    }

    // -------------------- perlin noise -------------------------

    /**
     * Create pseudorandom direction vector
     *
     * @param ix - coordinate to calculate with.
     * @return - random gradient.
     */
    float randomGradient(int ix) {
        // No precomputed gradients mean this works for any number of grid coordinates
        return (float) Math.sin(seed + ix);
    }

    /**
     * Computes the dot product of the distance and gradient vectors.
     *
     * @param ix - gradient vector.
     * @param x  - x coordinate.
     * @return - The computation.
     */
    float dotGridGradient(int ix, float x) {
        // Get gradient from integer coordinates
        float gradient = randomGradient(ix);

        // Compute the distance vector
        float dx = x - (float) ix;

        // Compute the dot-product
        return (dx * gradient);
    }

    /**
     * Compute Perlin noise at coordinates x.
     *
     * @param x x coordinate.
     * @return y coordinate - represent the height.
     */
    float perlin(float x) {
        // Determine grid cell coordinates
        float x_new = x;
        x = x / 1000;
        int x0 = (int) (x);
        int x1 = x0 + 1;

        // Determine interpolation weights
        // Could also use higher order polynomial/s-curve here
        float sx = x - (float) x0;

        // Interpolate between grid point gradients
        float n0, n1, ix0;

        n0 = dotGridGradient(x0, x);
        n1 = dotGridGradient(x1, x);
        ix0 = (n1 - n0) * (3.0f - sx * 2.0f) * sx * sx + n0;

        if (x_new >= 0) {
            return ix0 * 500 + windowDimensions.y() * 0.75f;
        }
        return ix0 * 15 + windowDimensions.y() * 0.75f;

    }
}