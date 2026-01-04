package xyz.faria.space.logic;

import lombok.RequiredArgsConstructor;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;

@RequiredArgsConstructor
public abstract class ShipAgent {

    private final Ship ship;
    private final ShipService shipService;
    private final ShipBus shipBus;
    private final SystemService systemService;
    private final Navigator navigator;

    /**
     * Called when it's time to update a ship and process data
     */
    public abstract void updateTick();

    /**
     * When the desired updateTick should be called
     */
    public abstract Long getNextUpdateTick();

}
