public class PlayerFactory {

    // ========================== private constant ==========================
    private static final String HUMAN_PLAYER = "human";
    private static final String WHATEVER_PLAYER = "whatever";
    private static final String CLEVER_PLAYER = "clever";
    private static final String SNARTYPAMTS_PLAYER = "snartypamts";

    // =========================== public methods ===========================

    /**
     * Constructor.
     */
    public PlayerFactory() { }

    /**
     * Responsible for creating Player object according the strString it receives.
     * @param strPlayer - A string that define the player we should create.
     * @return - A player.
     */
    public Player buildPlayer(String strPlayer) {
        switch (strPlayer) {
            case HUMAN_PLAYER:
                return new HumanPlayer();
            case WHATEVER_PLAYER:
                return new WhateverPlayer();
            case CLEVER_PLAYER:
                return new CleverPlayer();
            case SNARTYPAMTS_PLAYER:
                return new SnartypamtsPlayer();
            default:
                return null;
        }
    }
}
