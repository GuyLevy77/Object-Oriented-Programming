package ascii_art;

import image.Image;

import java.util.logging.Logger;

/**
 * This class executes all the operations in the class Shell.
 */
public class Driver {

    // ========================== private constant ==========================

    private static final String NUM_OF_PARAM_ILLEGAL_MSG = "USAGE: java asciiArt ";
    private static final String NULL_IMG_MSG = "Failed to open image file ";

    // ================================ Main ================================

    /**
     * The main function - creates a Shell objects and run it in order to create an Ascii image.
     *
     * @param args - Path of the image.
     * @throws Exception - It is possible to throw exceptions.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(NUM_OF_PARAM_ILLEGAL_MSG);
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe(NULL_IMG_MSG +
                    args[0]);
            return;
        }
        new Shell(img).run();
    }
}