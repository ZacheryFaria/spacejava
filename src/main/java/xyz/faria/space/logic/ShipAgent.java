package xyz.faria.space.logic;

import xyz.faria.space.events.ShipBus;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;
import xyz.faria.space.services.WaypointService;
import xyz.faria.space.spaceapi.client.ApiClient;

public abstract class ShipAgent {

    protected final ApiClient apiClient;
    protected final WaypointService waypointService;
    protected final ShipService shipService;
    protected final ShipBus shipBus;
    protected final SystemService systemService;
    protected Ship ship;

    public ShipAgent(ApiClient apiClient, Ship ship, ShipService shipService, ShipBus shipBus,
        SystemService systemService, WaypointService waypointService) {
        this.apiClient = apiClient;
        this.ship = ship;
        this.shipService = shipService;
        this.shipBus = shipBus;
        this.systemService = systemService;
        this.waypointService = waypointService;
    }

    /**
     * Called when it's time to update a ship and process data
     */
    public abstract void updateTick();

    /**
     * When the desired updateTick should be called
     */
    public abstract Long getNextUpdateTick();

}
