package xyz.faria.space.logic;

import xyz.faria.space.models.Ship;

public class ContractShipAgent implements ShipAgent {

    private final Ship ship;
    private ContractAgentStatus status;

    public ContractShipAgent(Ship ship) {
        this.ship = ship;

        this.status = ContractAgentStatus.INIT;
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
