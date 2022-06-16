package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    // ========================== private constant ==========================

    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final Color STEM_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String STEM_TAG = "stem";
    private static final String LEAF_TAG = "leaf";
    private static final int STEM_BOUND_PROB = 10;
    private static final int CHANCES_NUM = 1;
    private static final int MAX_STEM_HEIGHT = 8;
    private static final int MIN_STEM_HEIGHT = 8;

    // =============================== fields ===============================

    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final Function<Float, Float> groundHeightAt;

    // =========================== public methods ===========================

    /**
     * Constructor.
     *
     * @param gameObjects       - The collection of all participating game objects.
     * @param layer             - The number of the layer to which the created halo should be added.
     * @param windowDimensions- The dimensions of the windows.
     * @param groundHeightAt    - A callback that return the ground height at a given location.
     */
    public Tree(GameObjectCollection gameObjects,
                int layer,
                Vector2 windowDimensions, Function<Float, Float> groundHeightAt) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.groundHeightAt = groundHeightAt;
    }

    /**
     * This method creates trees in a given range of x-values.
     *
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        minX = roundMinMax(minX, true);
        maxX = roundMinMax(maxX, false);

        for (int x = minX; x < maxX; x += Block.SIZE) {
            if (x == (int) windowDimensions.x() / 2) // can't build tree at the initial place of the avatar
                continue;

            Random random = new Random(Objects.hash(x, PepseGameManager.SEED));
            if (random.nextInt(STEM_BOUND_PROB) == CHANCES_NUM) {

                int yHeight = (((int) (groundHeightAt.apply((float) x) /
                        Block.SIZE)) * Block.SIZE) - Block.SIZE;
                int len = random.nextInt(MAX_STEM_HEIGHT) + MIN_STEM_HEIGHT;

                for (int y = yHeight; y > yHeight - len * Block.SIZE; y -= Block.SIZE)
                    createTree(x, y);

                int leafLen = random.nextInt(len / 2) + 2;
                if (leafLen % 2 == 0)
                    leafLen++;

                for (int xLeaf = x - Block.SIZE * (leafLen / 2); xLeaf < x + Block.SIZE * (leafLen / 2 + 1);
                     xLeaf += Block.SIZE) {
                    for (int yLeaf = yHeight - (len - leafLen / 2) * Block.SIZE;
                         yLeaf > yHeight - (len - leafLen / 2) * Block.SIZE - leafLen * Block.SIZE;
                         yLeaf -= Block.SIZE) {

                        createLeaf(xLeaf, yLeaf);
                    }
                }
            }
        }
    }

    // =========================== private methods ===========================

    /**
     * creating the stem of the tree
     *
     * @param x x coordinate of top left corner
     * @param y y coordinate of top left corner
     */
    private void createTree(int x, int y) {
        Block stem = new Block(
                new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(STEM_COLOR)));
        gameObjects.addGameObject(stem, layer);
        stem.setTag(STEM_TAG);
    }

    /**
     * private method for rounding the closest block.
     *
     * @param num - number to round.
     * @param bool - represent min or max.
     * @return - the round number.
     */
    private int roundMinMax(int num, Boolean bool) {
        if (bool) {
            if (num % Block.SIZE != 0) // round down minX if needed
                num -= (num < 0) ? Block.SIZE + (num % Block.SIZE) : num % Block.SIZE;
            return num;
        }
        if (num % Block.SIZE != 0) // round up maxX if needed
            num += (num < 0) ? -(num % Block.SIZE) : Block.SIZE - (num % Block.SIZE);
        return num;
    }

    /**
     * creating single leaf
     *
     * @param xLeaf x top left corner of the leaf.
     * @param yLeaf y top left corner of the leaf.
     */
    private void createLeaf(int xLeaf, int yLeaf) {
        Leaf leaf = new Leaf(
                new Vector2(xLeaf, yLeaf),
                new Vector2(Block.SIZE, Block.SIZE),
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
        gameObjects.addGameObject(leaf, LEAF_LAYER);
        leaf.setTag(LEAF_TAG);
    }
}

