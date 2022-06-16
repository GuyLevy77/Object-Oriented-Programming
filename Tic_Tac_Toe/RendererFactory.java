public class RendererFactory {

    // ========================== private constant ==========================

    private static final String CONSOLE_RENDERER = "console";
    private static final String VOID_RENDERER = "none";

    // =========================== public methods ===========================

    /**
     * Constructor.
     */
    public RendererFactory() { }

    /**
     * Responsible for creating Renderer object according the strRenderer it receives.
     * @param strRenderer - A string that define the Renderer we should create.
     * @return - A Renderer.
     */
    public Renderer buildRenderer(String strRenderer) {
        switch (strRenderer) {
            case CONSOLE_RENDERER:
                return new ConsoleRenderer();
            case VOID_RENDERER:
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
