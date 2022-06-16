public class SnartypamtsPlayer implements Player {

    // =============================== fields ===============================

    private WhateverPlayer whateverPlayer = new WhateverPlayer();

    // =========================== public methods ===========================

    /**
     * Constructor.
     */
    public SnartypamtsPlayer() { }

    /**
     * Implementation of function which belongs to Player.
     * Play turn according to Snartypamts player strategy.
     * @param board - Board for the game.
     * @param mark - The mark we put on the board.
     */
    public void playTurn(Board board, Mark mark) {
        for (int col = 1; col < Board.SIZE; col++) {
            for (int row = 0; row < Board.SIZE; row++) {
                if (board.putMark(mark, row, col))
                    return;
                if (board.getMark(row, col) != mark && (Board.SIZE - row) < Board.WIN_STREAK) {
                    row = Board.SIZE;
                }
            }
        }
        whateverPlayer.playTurn(board, mark);
    }
}


