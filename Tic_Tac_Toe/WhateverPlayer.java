import java.util.Random;

public class WhateverPlayer implements Player {

    // =============================== fields ===============================

    private Random random = new Random();

    // =========================== public methods ===========================

    /**
     * Constructor.
     */
    public WhateverPlayer() { }

    /**
     * Implementation of function which belongs to player.
     * Play turn according to Whatever player strategy.
     * @param board - Board for the game.
     * @param mark - The mark we put on the board.
     */
    public void playTurn(Board board, Mark mark) {
        int totalSquares = Board.SIZE * Board.SIZE;
        int[] emptySquaresArray = new int[totalSquares];
        int boundOfRand = 0;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.getMark(row, col) == Mark.BLANK) {
                    emptySquaresArray[boundOfRand] = row * 10 + col; //Add coordinates as a number.
                    boundOfRand++;
                }
            }
        }

        int coordinates = emptySquaresArray[random.nextInt(boundOfRand)];
        // Splits the chosen number.
        int rowCoordinate = coordinates / 10;
        int colCoordinate = coordinates % 10;
        board.putMark(mark, rowCoordinate, colCoordinate);
    }
}
