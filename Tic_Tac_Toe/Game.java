public class Game {

    // =============================== fields ===============================

    private final Renderer renderer;
    private final Player[] players = new Player[2];
    private static final Mark[] playerSign = {Mark.X, Mark.O};

    // =========================== public methods ===========================

    /**
     * Constructor of Game.
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.renderer = renderer;
        this.players[0] = playerX;
        this.players[1] = playerO;
    }

    /**
     * This function responsible for running the game.
     * @return - The winner if existed.
     */
    public Mark run() {
        Board board = new Board();
        renderer.renderBoard(board);
        int curPlayer = 0;

        while (!board.gameEnded()) {
            players[curPlayer].playTurn(board, playerSign[curPlayer]);
            renderer.renderBoard(board);
            curPlayer = 1 - curPlayer; // Changes player.
        }
        return board.getWinner();
    }
}