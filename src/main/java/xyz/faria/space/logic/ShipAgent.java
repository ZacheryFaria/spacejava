package xyz.faria.space.logic;

public interface ShipAgent {

    /**
     * Called when it's time to update a ship and process data
     */
    void updateTick();

    /**
     * When the desired updateTick should be called
     */
    Long getNextUpdateTick();

}
