package sector;

public enum SectorState {
    NORMAL("N"), FIRE("F"), DESTROYED("D");

    // constants for color label
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String GRAY = "\u001B[90m";
    public final String label;

    SectorState(String label) {
        this.label = label;
    }

    /**
     * @return label with color code
     */
    public String GetColorLabel() {
        return switch (this) {
            case NORMAL -> GREEN + label + RESET;
            case FIRE -> RED + label + RESET;
            case DESTROYED -> GRAY + label + RESET;
        };
    }
}
