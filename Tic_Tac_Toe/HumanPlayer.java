import java.util.Scanner;

public class HumanPlayer implements Player {

    // ========================== private constant ==========================

    private static final String SCANNER_MESSAGE = "Player %s, type coordinates: ";
    private static final String ERR_MESSAGE = "Invalid coordinates, type again: ";

    // =========================== public methods ===========================

    /**
     * Constructor
     */
    public HumanPlayer() { }

    /**
     * Asks for coordinates from the user and put them on
     * the board with the sign of the current player.
     * @param board - Board for the game.
     * @param mark - The mark we put on the board.
     */
    public void playTurn(Board board, Mark mark) {
        int coordinates;
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println(String.format(SCANNER_MESSAGE, mark));
            coordinates = in.nextInt();
            if (board.putMark(mark, ((coordinates / 10) - 1), (coordinates % 10) - 1))
                return;
            System.err.println(ERR_MESSAGE);
        }
    }
}