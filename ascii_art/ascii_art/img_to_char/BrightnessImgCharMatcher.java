package ascii_art.img_to_char;


import image.Image;

import java.awt.*;
import java.util.HashMap;

/**
 * The main purpose of this class is to support "chooseChars" function - to take an image
 * and to return a compatible Asci-Image.
 */
public class BrightnessImgCharMatcher {

    // ========================== private constant ==========================

    private static final int IMAGE_PIXELS = 16;
    private static final int RGB = 255;
    private static final double MULT_RED_TO_GREY = 0.2126;
    private static final double MULT_GREEN_TO_GREY = 0.7152;
    private static final double MULT_BLUE_TO_GREY = 0.0722;

    // =============================== fields ===============================

    private final Image image;
    private final String font;
    private final HashMap<Image, Double> cache = new HashMap<>();


    // =========================== public methods ===========================

    /**
     * Constructor.
     *
     * @param image - An image to work with.
     * @param font  - The font of the image.
     */
    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * The function takes an image divides it to sub-images and find a compatible Asci-Image.
     *
     * @param numCharsInRow - The number of the chars we want in each row in the Asci-Image.
     * @param charSet       - The possible letters that can appear in the Asci-Image.
     * @return - A compatible Asci-Image.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        int pixels = image.getWidth() / numCharsInRow;
        double[] brightnessArray = charsArrayToBrightnessArray(charSet);
        return convertImageToAscii(charSet, newCharBrightness(brightnessArray), pixels);
    }

    // =========================== private methods ===========================

    /**
     * The function needs to find a compatible Asci-Image.
     *
     * @param charArray         - The possible letters that can appear in the Asci-Image.
     * @param newCharBrightness - An array of char-brightnesses after linear-stretching.
     * @param pixels            - Number of pixels for the image.
     * @return - A compatible Asci-Image.
     */
    private char[][] convertImageToAscii(Character[] charArray, double[] newCharBrightness, int pixels) {
        char[][] asciiArt = new char[image.getHeight() / pixels][image.getWidth() / pixels];
        int i = 0, j = 0;

        for (Image subImage : image.squareSubImagesOfSize(pixels)) {
            double curBrightness = calculateImageBrightness(subImage);
            int indexOfChar = findingIndexOfIdealChar(curBrightness, newCharBrightness);
            asciiArt[i][j] = charArray[indexOfChar];
            j++;
            if (j == asciiArt[0].length) {
                j = 0;
                i++;
            }
            if (i == asciiArt.length)
                break;
        }
        return asciiArt;
    }

    /**
     * An auxiliary function for convertImageToAscii.
     * Finds the char with the closest brightness to the input-brightness.
     *
     * @param brightness        - primitive object - double type.
     * @param newCharBrightness - All the possible brightnesses.
     * @return - The index of the closest brightness to the input-brightness.
     */
    private int findingIndexOfIdealChar(double brightness, double[] newCharBrightness) {
        int index = 0;
        double differ = 2; // initialize to something bigger than what possible.
        for (int i = 0; i < newCharBrightness.length; i++) {
            if (Math.abs(brightness - newCharBrightness[i]) < differ) {
                differ = Math.abs(brightness - newCharBrightness[i]);
                index = i;
            }
        }
        return index;
    }

    /**
     * Finds the brightness of a specific image.
     *
     * @param subImage - An image.
     * @return - the brightness of the subImage.
     */
    private double calculateImageBrightness(Image subImage) {
        if (cache.containsKey(subImage))
            return cache.get(subImage);
        double greyPixel;
        double sum = 0;
        int count = 0;
        for (Color pixel : subImage.pixels()) {
            greyPixel = pixel.getRed() * MULT_RED_TO_GREY + pixel.getGreen() * MULT_GREEN_TO_GREY
                    + pixel.getBlue() * MULT_BLUE_TO_GREY;
            sum += greyPixel;
            count++;
        }
        double subImgBrightness = (sum / count) / RGB;
        cache.put(subImage, subImgBrightness);
        return subImgBrightness;
    }

    /**
     * Takes an array of char's brightnesses and create a "linear stretching" array.
     *
     * @param brightnessArray - Array f brightnesses.
     * @return - The "linear stretching" array.
     */
    private double[] newCharBrightness(double[] brightnessArray) {
        int len = brightnessArray.length;
        double minBrightness = 1;
        double maxBrightness = 0;
        double[] newCharBrightness = new double[len];

        for (double brightness : brightnessArray) {
            if (minBrightness > brightness)
                minBrightness = brightness;
            if (maxBrightness < brightness)
                maxBrightness = brightness;
        }

        for (int i = 0; i < len; i++) {
            newCharBrightness[i] =
                    (brightnessArray[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return newCharBrightness;
    }

    /**
     * Taking an array of chars and return an array of the brightnesses of each char in the input array.
     *
     * @param charArray - The input char-array.
     * @return - Array of the brightnesses of each char in the input array.
     */
    private double[] charsArrayToBrightnessArray(Character[] charArray) {
        boolean[][] cs;
        double[] brightnessArray = new double[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            cs = CharRenderer.getImg(charArray[i], IMAGE_PIXELS, font);
            brightnessArray[i] = (double) numOfTrueCells(cs) / (IMAGE_PIXELS * IMAGE_PIXELS);
        }
        return brightnessArray;
    }

    /**
     * Auxiliary function for charsArrayToBrightnessArray.
     *
     * @param boolArray - Array to pass of and to count the "true cells"
     * @return - The number of the "true cells".
     */
    private int numOfTrueCells(boolean[][] boolArray) {
        int count = 0;
        for (boolean[] booleans : boolArray) {
            for (int j = 0; j < boolArray.length; j++) {
                if (booleans[j])
                    count++;
            }
        }
        return count;
    }
}