package xyz.faria.space.logic;

import xyz.faria.space.events.ShipBus;
import xyz.faria.space.logic.navigation.Navigator;
import xyz.faria.space.models.Contract;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ContractService;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;

public class ContractShipAgent extends ShipAgent {

    private final ContractService contractService;
    private ContractAgentStatus status = ContractAgentStatus.INIT;

    private final Contract activeContract = null;

    public ContractShipAgent(Ship ship, ShipService shipService, ShipBus shipBus, SystemService systemService, Navigator navigator, ContractService contractService) {
        super(ship, shipService, shipBus, systemService, navigator);

        this.contractService = contractService;
    }

    @Override
    public void updateTick() {
        this.status = switch (this.status) {
            case INIT -> handleInit();
            case TRAVELING_TO_MARKET -> handleTravelToMarket();
            case TRAVELING_TO_CONTRACT -> handleTravelToContract();
            case RESET -> handleReset();
        };
    }

    private ContractAgentStatus handleInit() {
        // check for an active contract
        var contract = contractService.getActiveContract(ship);
        // if there is no active contract, transition to RESET state where we will re-neg the contract
        if (contract == null) {
            return ContractAgentStatus.RESET;
        }
        // if contract is active, check if ship has inventory, if yes, then we are traveling to deliver
        // if no, then we are traveling to market
        if (ship.getCargo().getUnits() > 0) {
            return ContractAgentStatus.TRAVELING_TO_CONTRACT;
        }
        return ContractAgentStatus.TRAVELING_TO_MARKET;
    }

    private ContractAgentStatus handleTravelToMarket() {
        // pull all markets
        return ContractAgentStatus.INIT;
    }

    private ContractAgentStatus handleTravelToContract() {
        return ContractAgentStatus.INIT;
    }

    private ContractAgentStatus handleReset() {
        return ContractAgentStatus.INIT;
    }

    @Override
    public Long getNextUpdateTick() {
        return 0L;
    }

    private enum ContractAgentStatus {
        // the initial status set when the agent is created, but no ticks have yet been processed
        INIT,
        // traveling to market to purchase resources
        TRAVELING_TO_MARKET,
        // traveling to contract location
        TRAVELING_TO_CONTRACT,
        // set when a contract was completed, this should trigger the next contract negotiation
        RESET,
    }
}
