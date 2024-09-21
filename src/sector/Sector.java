package sector;

import java.util.ArrayList;
import java.util.List;

public abstract class Sector implements SectorInterface {
    protected int x;
    protected int y;
    protected List<SectorInterface> neighbors;
    protected SectorState state;

    /**
     * Constructor for sector
     *
     * @param x     x position in the forest
     * @param y     y position in the forest
     * @param state first state of the sector
     */
    public Sector(int x, int y, SectorState state) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();
        this.state = state;
    }

    @Override
    public int getXPosition() {
        return x;
    }

    @Override
    public int getYPosition() {
        return y;
    }

    @Override
    public List<SectorInterface> getNeighbors() {
        return neighbors;
    }

    @Override
    public void addNeighbors(List<SectorInterface> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    @Override
    public SectorState getState() {
        return state;
    }
}
