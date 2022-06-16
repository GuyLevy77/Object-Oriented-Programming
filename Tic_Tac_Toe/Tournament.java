public class Tournament {

    // ========================== private constant ==========================

    private static final String END_OF_TOURNAMENT_MESSAGE =
            "=== player 1: %d | player 2: %d | Draws: %d ===\r";
    private static final String ERR_MESSAGE =
            "Usage: java Tournament [round count] [render target: console/none]" +
            " [player1: human/clever/whatever/snartypamts]" +
                    " [player2: human/clever/whatever/snartypamts]";
    private static final int ARG_NUM = 4;
    private static final int MIN_ROUNDS = 1;
    private static final int ARG_ROUNDS = 0;
    private static final int ARG_RENDERER = 1;
    private static final int ARG_PLAYER1 = 2;
    private static final int ARG_PLAYER2 = 3;

    // =============================== fields ===============================

    private int rounds;
    private Renderer renderer;
    private Player[] players = new Player[2];

    // =========================== public methods ===========================

    /**
     * Constructor.
     */
    Tournament(int rounds, Renderer renderer, Player[] players) {

        this.rounds = rounds;
        this.renderer = renderer;
        for (int i = 0; i < 2; i++) {
            this.players[i] = players[i];
        }
    }

    /**
     * Running tournaments according to the data it received in the constructor.
     */
    public void playTournament() {
        int[] winsOfPlayers = {0, 0, 0}; // {player1, player2, draw}
        Game[] games = {new Game(players[0], players[1], renderer),
                new Game(players[1], players[0], renderer)};
        Mark result;
        for (int i = 0; i < rounds; i++) {
            result = games[i % 2].run();
            switch (result) {
                case X:
                    winsOfPlayers[i % 2]++;
                    break;
                case O:
                    winsOfPlayers[1 - (i % 2)]++;
                    break;
                default:
                    winsOfPlayers[2]++;
                    break;
            }
        }
        System.out.println(String.format(END_OF_TOURNAMENT_MESSAGE, winsOfPlayers[0],
                winsOfPlayers[1], winsOfPlayers[2]));
    }

    // ================================ Main ================================

    /**
     * The main function, creating objects and operate the games.
     * @param args - Array of string that contains (number of plays, string that
     * symbolize if we want to print the board, and two players).
     */
    public static void main(String[] args) {
        if (args.length != ARG_NUM || Integer.parseInt(args[ARG_ROUNDS]) < MIN_ROUNDS) {
            System.err.println(ERR_MESSAGE);
            return;
        }

        int rounds = Integer.parseInt(args[ARG_ROUNDS]);
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[ARG_RENDERER]);

        PlayerFactory playerFactory = new PlayerFactory();
        Player[] players = new Player[2];
        players[0] = playerFactory.buildPlayer(args[ARG_PLAYER1]);
        players[1] = playerFactory.buildPlayer(args[ARG_PLAYER2]);

        if (renderer == null || players[0] == null || players[1] == null) {
            System.err.println(ERR_MESSAGE);
            return;
        }

        Tournament tournament = new Tournament(rounds, renderer, players);
        tournament.playTournament();
    }
}





