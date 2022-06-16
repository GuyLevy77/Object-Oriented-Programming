public class Board {

    // =========================== public constant ==========================

    public static final int SIZE = 6;
    public static final int WIN_STREAK = 4;

    // ========================== private constant ==========================

    private final int MIN_COORDINATE = 0;
    private final int SIZE_POWER_OF_TWO = (SIZE * SIZE);

    // =============================== fields ===============================

    private final Mark[][] board = new Mark[SIZE][SIZE];
    private int numOfFullSquares = 0;
    private final int[] lastIndexFilled = new int[2];
    private Mark winner_player = Mark.BLANK;
    private boolean endOfGame = false;

    // =========================== public methods ===========================

    /**
     * Constructor for Board.
     */
    public Board() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Updates the new coordinates on the board.
     * @param mark - A mark to put on the board.
     * @return - true if succeed otherwise false.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row >= SIZE || row < MIN_COORDINATE || col >= SIZE || col < MIN_COORDINATE ||
                board[row][col] != Mark.BLANK) {
            return false;
        }
        this.board[row][col] = mark;
        lastIndexFilled[0] = row;
        lastIndexFilled[1] = col;
        numOfFullSquares++;
        updateGameResultAndWinner();
        return true;
    }

    /**
     * @param row - The first coordinated.
     * @param col - The second coordinated.
     * @return - Returns the sign of the coordinates it gets as an input.
     */
    public Mark getMark(int row, int col) {
        if (row >= SIZE || row < MIN_COORDINATE || col >= SIZE || col < MIN_COORDINATE)
            return Mark.BLANK;
        return this.board[row][col];
    }

    /**
     * Getter
     * @return - True if game ended, otherwise false.
     */
    public boolean gameEnded() { return endOfGame; }

    /**
     * Getter
     * @return - The sign of the winner.
     */
    public Mark getWinner() { return winner_player; }

    // ========================== private methods ===========================

    /**
     * Checks streak from the last coordinates that we put in the game
     * according to specific direction.
     * @param rowDelta - Symbolize the direction for the first coordinates.
     * @param colDelta -  Symbolize the direction for the second coordinates.
     * @return - amount of mark in this series direction.
     */
    private int countMarkInDirection(int rowDelta, int colDelta) {
        Mark mark = getMark(lastIndexFilled[0], lastIndexFilled[1]);
        int row = lastIndexFilled[0];
        int col = lastIndexFilled[1];
        int count = 0;

        while (row < SIZE && row >= MIN_COORDINATE && col < SIZE &&
                col >= MIN_COORDINATE && board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    /**
     * Checks the maximum streak we can get from specific
     * the last coordinates that we put.
     * @return - The maximum streak.
     */
    private int checkStreak() {
        int count;
        int temp;
        //checking row
        count = countMarkInDirection(0, -1) +
                countMarkInDirection(0, 1) - 1;
        //checking col
        temp = countMarkInDirection(-1, 0) +
                countMarkInDirection(1, 0) - 1;
        if (temp > count)
            count = temp;
        // checking slants
        temp = countMarkInDirection(-1, -1) +
                countMarkInDirection(1, 1) - 1;
        if (temp > count)
            count = temp;
        temp = countMarkInDirection(1, -1) +
                countMarkInDirection(-1, 1) - 1;
        if (temp > count)
            count = temp;
        return count;
    }

    /**
     * Update the game result and the winner by using auxiliary functions.
     */
    private void updateGameResultAndWinner() {
        if (numOfFullSquares == SIZE_POWER_OF_TWO)
            endOfGame = true;

        int count = checkStreak();
        if (count >= WIN_STREAK) {
            if (getMark(lastIndexFilled[0], lastIndexFilled[1]) == Mark.X)
                winner_player = Mark.X;
            else
                winner_player = Mark.O;
            endOfGame = true;
        }
    }
}