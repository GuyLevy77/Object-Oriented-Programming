public class CleverPlayer implements Player {

    // =========================== public methods ===========================

    /**
     * Constructor
     */
    public CleverPlayer() { }

    /**
     * Implementation of function which belongs to player.
     * Play turn according to clever player strategy.
     * @param board - Board for the game.
     * @param mark - The mark we put on the board.
     */
    public void playTurn(Board board, Mark mark) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.putMark(mark, row, col))
                    return;
            }
        }
    }
}

