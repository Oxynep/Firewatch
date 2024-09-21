package sector;

public class ForestSector extends Sector {

    public ForestSector(int x, int y, SectorState state) {
        super(x, y, state);
    }

    /**
     * forest sector can be ignited only if they are in the normal state
     *
     * @return True if sector can be ignited
     */
    @Override
    public boolean canBeIgnite() {
        return this.state.equals(SectorState.NORMAL);
    }

    @Override
    public boolean canBeExtinguish() {
        return this.state.equals(SectorState.FIRE);
    }

    @Override
    public boolean ignite() {
        if (this.canBeIgnite()) {
            this.state = SectorState.FIRE;
            return true;
        }
        return false;
    }

    @Override
    public boolean extinguish() {
        if (this.canBeExtinguish()) {
            this.state = SectorState.DESTROYED;
            return true;
        }
        return false;
    }
}
