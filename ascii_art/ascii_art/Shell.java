package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.*;
import java.util.stream.Stream;

/**
 * The interface for the user. Executes various of operations and functions backstage.
 * By using the function run, it creates a new Ascii image.
 */
public class Shell {

    // ========================== private constant ==========================

    private static final String CMD_EXIT = "exit";
    private static final String CMD_CHAR_SET = "chars";
    private static final char SPACE = ' ';
    private static final String INIT_CMD = ">>> ";
    private static final String CMD_ADD = "add";
    private static final String CMD_REMOVE = "remove";
    private static final String ADD_ALL = "all";
    private static final String ADD_SPACE = "space";
    private static final String CMD_RES = "res";
    private static final String CMD_CONSOLE = "console";
    private static final String CMD_RENDER = "render";
    private static final String ERR_INVALID_INPUT = "ERROR: You entered an invalid input.";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final char LAST_CHAR = '~';
    private static final char RANGE_SYMBOL = '-';
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String ERR_EXCEEDING_MAX = "ERROR: The operation could not be performed due" +
            " to exceeding the boundaries of chars in row";
    private static final String WIDTH_SET_MSG = "Width set to %d";
    private static final String UP_PARAM = "up";
    private static final String DOWN_PARAM = "down";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private static final String EMPTY_STRING = "";
    private static final int BIGGEST_CHAR_VALUE = 127;
    private static final int NUM_TO_EXTEND_OR_REDUCE_RES = 2;

    // =============================== fields ===============================

    private Image img;
    private Set<Character> charSet = new HashSet<>();
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private boolean consoleRenderer = false;

    // =========================== public methods ===========================

    /**
     * Constructor.
     *
     * @param img - Image to convert.
     */
    public Shell(Image img) {
        this.img = img;

        minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow),
                minCharsInRow);

        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);


        addOrRemoveChars(INITIAL_CHARS_RANGE, CMD_ADD);
    }

    /**
     * The main function of this class - according to the input from the user, it decides which
     * operation to execute. In case of invalid input it prints an error message to std.out.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(INIT_CMD);
        String cmd = scanner.next().trim();
        while (true) {
            var param = scanner.nextLine().trim();
            if (cmd.equals(CMD_EXIT) && param.equals(EMPTY_STRING))
                break;
            switch (cmd) {
                case CMD_ADD:
                case CMD_REMOVE:
                    addOrRemoveChars(param, cmd);
                    break;
                case CMD_RES:
                    resChange(param);
                    break;
                case CMD_CONSOLE:
                    if ((param.equals(EMPTY_STRING))) {
                        consoleRenderer = true;
                        break;
                    }
                case CMD_RENDER:
                    if ((param.equals(EMPTY_STRING))) {
                        render();
                        break;
                    }
                case CMD_CHAR_SET:
                    if ((param.equals(EMPTY_STRING))) {
                        showChars();
                        break;
                    }
                default:
                    System.out.println(ERR_INVALID_INPUT);
                    break;
            }
            System.out.print(INIT_CMD);
            cmd = scanner.next();
        }
    }

    // =========================== private methods ===========================

    /**
     * Is Called when the user inserts the command "render", and it handles this case.
     */
    private void render() {
        if (img != null) {
            Character[] charArray = new Character[charSet.size()];
            charSet.toArray(charArray);

            char[][] chars = charMatcher.chooseChars(charsInRow, charArray);
            if (consoleRenderer)
                output = new ConsoleAsciiOutput();
            else
                output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
            output.output(chars);
        }
    }

    /**
     * Extend or reduce the resolution according to the input.
     *
     * @param s - the input from the user.
     */
    private void resChange(String s) {
        if (s == null)
            System.out.println(ERR_INVALID_INPUT);

        else if (s.equals(UP_PARAM)) {
            if (charsInRow * NUM_TO_EXTEND_OR_REDUCE_RES > maxCharsInRow)
                System.out.println(ERR_EXCEEDING_MAX);
            else {
                charsInRow *= NUM_TO_EXTEND_OR_REDUCE_RES;
                System.out.println(String.format(WIDTH_SET_MSG, charsInRow));
            }
        } else if (s.equals(DOWN_PARAM)) {
            if (charsInRow / NUM_TO_EXTEND_OR_REDUCE_RES < minCharsInRow)
                System.out.println(ERR_EXCEEDING_MAX);
            else {
                charsInRow /= NUM_TO_EXTEND_OR_REDUCE_RES;
                System.out.println(String.format(WIDTH_SET_MSG, charsInRow));
            }
        } else
            System.out.println(ERR_INVALID_INPUT);
    }

    /**
     * This function prints the char-set.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c -> System.out.print(c + SPACE));
        System.out.println();
    }

    /**
     * This function add or remove chars according to the input.
     *
     * @param s   - which chars to add or remove.
     * @param cmd - symbolize "add" or "remove".
     */
    private void addOrRemoveChars(String s, String cmd) {
        char[] range = parseCharRange(s);

        if (range == null) {
            System.out.println(ERR_INVALID_INPUT);
            return;
        }
        if (cmd.equals(CMD_ADD))
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::add);
        else
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::remove);
    }

    /**
     * Creates an array with 2 cells that presents the range and the chars we should add or remove.
     *
     * @param param - the input from the user.
     * @return - A Chars array with 2 cells.
     */
    private static char[] parseCharRange(String param) {
        if (param == null || param.contains(" "))
            return null;

        else if (param.length() == 1 && param.charAt(0) <= BIGGEST_CHAR_VALUE)
            return new char[]{param.charAt(0), param.charAt(0)};

        else if (param.equals(ADD_ALL))
            return new char[]{SPACE, LAST_CHAR};

        else if (param.equals(ADD_SPACE))
            return new char[]{SPACE, SPACE};

        else if (param.length() == 3 && param.charAt(1) == RANGE_SYMBOL) {
            if (param.charAt(0) > param.charAt(2))
                return new char[]{param.charAt(2), param.charAt(0)};
            return new char[]{param.charAt(0), param.charAt(2)};
        } else
            return null;
    }
}