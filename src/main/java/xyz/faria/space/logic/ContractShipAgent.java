package xyz.faria.space.logic;

import xyz.faria.space.events.ShipBus;
import xyz.faria.space.logic.navigation.Navigator;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;

public class ContractShipAgent extends ShipAgent {

    public ContractShipAgent(Ship ship, ShipService shipService, ShipBus shipBus, SystemService systemService, Navigator navigator) {
        super(ship, shipService, shipBus, systemService, navigator);
    }

    @Override
    public void updateTick() {

    }

    @Override
    public Long getNextUpdateTick() {
        return 0L;
    }

    private enum ContractAgentStatus {
        INIT,
        ACTIVE,
        COMPLETED,
        FAILED
    }
}
