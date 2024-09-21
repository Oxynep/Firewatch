package sector;

import java.util.List;

public interface SectorInterface {
    /**
     * @return neighbors list
     */
    List<SectorInterface> getNeighbors();

    /**
     * add all neighbors given in param to this.neighbors
     *
     * @param neighbors will be added to this.neighbors
     */
    void addNeighbors(List<SectorInterface> neighbors);

    /**
     * @return if a sector can be ignited or not
     */
    boolean canBeIgnite();

    /**
     * @return if a sector can be extinguished or not
     */
    boolean canBeExtinguish();

    /**
     * ignite the sector if possible
     *
     * @return true if sector have been ignited
     */
    boolean ignite();

    /**
     * extinguish the sector if possible
     *
     * @return true if sector have been extinguished
     */
    boolean extinguish();

    /**
     * @return sector state
     */
    SectorState getState();

    /**
     * @return x position in the forest
     */
    int getXPosition();

    /**
     * @return y position in the forest
     */
    int getYPosition();
}
