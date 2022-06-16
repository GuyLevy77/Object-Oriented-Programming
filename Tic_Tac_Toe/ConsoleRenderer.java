
import java.util.Map;
import java.util.logging.Logger;

/**
 * A supplied class for the Tic Tac Toe exercise in the OOP MOOC by the Hebrew University.
 * Renders a given Board to the console.
 * @author Dan Nirel
 */
class ConsoleRenderer implements Renderer {
    private static final int ROWS_PER_MARK = 3;
    private static final int COLS_PER_MARK = 9;
    private static final char DEFAULT_CHAR = ' ';
    private static final char HORIZONTAL_LINE_CHAR = '-';
    private static final char VERTICAL_LINE_CHAR = '|';
    private static final int NUM_ROWS_BEFORE_BOARD = 4;
    private static final int NUM_COLS_BEFORE_BOARD = 6;

    //                                            for the marks         for lines         for indices
    private static final int BUFFER_ROWS = Board.SIZE * ROWS_PER_MARK + (Board.SIZE-1) + NUM_ROWS_BEFORE_BOARD;
    private static final int BUFFER_COLS = Board.SIZE * COLS_PER_MARK + (Board.SIZE-1) + NUM_COLS_BEFORE_BOARD;

    private static final Map<Mark, String[]> MARKS_DRAWINGS = Map.of(

            Mark.X, new String[] { 		"  X   X  ",
                    "    X    ",
                    "  X   X  "      },

            Mark.O, new String[] { 		"   OOO   ",
                    "  O   O  ",
                    "   OOO   "      },

            Mark.BLANK, new String[] { 	"         ",
                    "         ",
                    "         "      }
    );

    private char[][] buffer = new char[BUFFER_ROWS][BUFFER_COLS];

    /**
     * Initializes the renderer.
     */
    public ConsoleRenderer() {
        if(Board.SIZE > 9 || Board.SIZE < 2) {
            String errorMsg = "Board size must be in the range [2, 9]";
            Logger.getGlobal().severe(errorMsg);
            throw new Error(errorMsg);
        }

        //fill buffer with spaces
        for(int i = 0 ; i < buffer.length ; i++) {
            for(int j = 0 ; j < buffer[i].length ; j++)
                buffer[i][j] = DEFAULT_CHAR;
        }

        //draw horizontal lines
        for(int i = NUM_ROWS_BEFORE_BOARD+ROWS_PER_MARK ; i < BUFFER_ROWS ; i += ROWS_PER_MARK+1) {
            for(int j = NUM_COLS_BEFORE_BOARD ; j < BUFFER_COLS ; j++)
                buffer[i][j] = HORIZONTAL_LINE_CHAR;
        }

        //draw vertical lines
        for(int i = NUM_COLS_BEFORE_BOARD+COLS_PER_MARK ; i < BUFFER_COLS ; i += COLS_PER_MARK+1) {
            for(int j = NUM_ROWS_BEFORE_BOARD ; j < BUFFER_ROWS ; j++)
                buffer[j][i] = VERTICAL_LINE_CHAR;
        }

        //draw indices
        char index = '1';
        for(int col = NUM_COLS_BEFORE_BOARD + COLS_PER_MARK/2 ; col < BUFFER_COLS ; col += COLS_PER_MARK+1)
            buffer[NUM_ROWS_BEFORE_BOARD/2][col] = index++;
        index = '1';
        for(int row = NUM_ROWS_BEFORE_BOARD + ROWS_PER_MARK/2 ; row < BUFFER_ROWS ; row += ROWS_PER_MARK+1) {
            buffer[row][NUM_COLS_BEFORE_BOARD/2-1] = index++;
            buffer[row][NUM_COLS_BEFORE_BOARD/2] = '0';
        }
    }

    /**
     * Prints the supplied board to the console.
     * @param board the board to print.
     */
    public void renderBoard(Board board) {
        for(int i = 0 ; i < Board.SIZE ; i++) {
            for(int j = 0 ; j < Board.SIZE ; j++) {
                drawMarkInBuffer(NUM_ROWS_BEFORE_BOARD + i*(ROWS_PER_MARK+1),
                        NUM_COLS_BEFORE_BOARD + j*(COLS_PER_MARK+1), board.getMark(i, j));
            }
        }

        for(int i = 0 ; i < buffer.length ; i++) {
            for(int j = 0 ; j < buffer[i].length ; j++)
                System.out.print(buffer[i][j]);
            System.out.println();
        }
        System.out.println();
    }

    private void drawMarkInBuffer(int rowStart, int colStart, Mark mark) {
        String[] markLines = MARKS_DRAWINGS.get(mark);
        for(int i = 0 ; i < markLines.length ; i++) {
            for(int j = 0 ; j < markLines[i].length() ; j++) {
                buffer[rowStart+i][colStart+j] = markLines[i].charAt(j);
            }
        }
    }
}