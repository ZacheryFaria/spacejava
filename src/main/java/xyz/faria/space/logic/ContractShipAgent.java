package xyz.faria.space.logic;

import io.sentry.Sentry;
import java.util.logging.Logger;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.logic.navigation.DistanceNavigator;
import xyz.faria.space.logic.navigation.Navigator;
import xyz.faria.space.models.Contract;
import xyz.faria.space.models.Ship;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.services.ContractService;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;
import xyz.faria.space.services.WaypointService;
import xyz.faria.space.spaceapi.client.ApiClient;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.model.TradeSymbol;

public class ContractShipAgent extends ShipAgent {

    private final static Logger logger = Logger.getLogger(ContractShipAgent.class.getName());

    private final ContractService contractService;
    private ContractAgentStatus status = ContractAgentStatus.INIT;

    private Contract activeContract = null;

    public ContractShipAgent(ApiClient apiClient, Ship ship, ShipService shipService,
        ShipBus shipBus, WaypointService waypointService,
        SystemService systemService, ContractService contractService) {
        super(apiClient, ship, shipService, shipBus, systemService, waypointService);

        this.contractService = contractService;
    }

    @Override
    public void updateTick() {
        try {
            logger.info(
                String.format("Updating contract agent for ship: %s\tstatus: %s ", ship.getSymbol(),
                    this.status.toString()));
            this.status = switch (this.status) {
                case INIT -> handleInit();
                case TRAVELING_TO_MARKET -> handleTravelToMarket();
                case PURCHASE_GOODS -> handlePurchaseGoods();
                case TRAVELING_TO_CONTRACT -> handleTravelToContract();
                case DELIVER_GOODS -> handleDeliverGoods();
                case RESET -> handleReset();
            };
        } catch (ApiException e) {
            Sentry.captureException(e);
            e.printStackTrace();
        }
    }

    private ContractAgentStatus handleDeliverGoods() throws ApiException {
        var contract = getContract();

        this.activeContract = contractService.deliverContract(contract, ship);

        if (this.activeContract.isCompleted()) {
            this.activeContract = contractService.fulfillContract(this.activeContract);
            assert this.activeContract.getFulfilled();
            this.activeContract = null;
            return ContractAgentStatus.RESET;
        }
        return ContractAgentStatus.TRAVELING_TO_CONTRACT;
    }

    private ContractAgentStatus handlePurchaseGoods() throws ApiException {
        var contract = getContract();
        var deliverItem = contract.getTerms().getDeliver().getFirst();
        var purchaseSymbol = TradeSymbol.valueOf(deliverItem.getTradeSymbol());
        var contractItemsRemaining = deliverItem.getUnitsRemaining();
        var shipCapacity = this.ship.getCargo().getRemainingCapacity();

        var itemsShipMayPurchase = Math.min(contractItemsRemaining, shipCapacity);
        var marketItem = systemService.loadMarketForWaypoint(
            this.ship.getAgent(), this.ship.getNav().getWaypointSymbol());

        var tradeGood = marketItem.getTradeGoodBySymbol(purchaseSymbol).orElseThrow();

        var itemsToPurchase = Math.min(itemsShipMayPurchase, tradeGood.getTradeVolume());

        this.ship = shipService.purchase(this.ship, purchaseSymbol, itemsToPurchase);
        return ContractAgentStatus.TRAVELING_TO_CONTRACT;
    }

    private Contract getContract() {
        if (this.activeContract == null) {
            this.activeContract = contractService.getActiveContract(ship);
        }
        return this.activeContract;
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

    private ContractAgentStatus handleTravelToMarket() throws ApiException {
        // pull all markets with the target contract
        var targetResource = getContract();
        var deliver = targetResource.getDeliverGood();
        var marketsForResource = waypointService.findWaypointsBySystemAndHasMarketWithTradeSymbol(
            ship.getNav().getSystemSymbol(), TradeSymbol.valueOf(deliver.getTradeSymbol()));

        var navigator = new DistanceNavigator(marketsForResource);
        var startWaypoint = waypointService.findWaypointByShipNav(ship);
        var bestMarket = navigator.findNearestWaypoint(startWaypoint).orElseThrow();

        var fuelMarkets = waypointService.findWaypointsBySystemAndHasMarketWithTradeSymbol(
            ship.getNav().getSystemSymbol(), TradeSymbol.FUEL);
        fuelMarkets.add(bestMarket);
        navigator = new DistanceNavigator(fuelMarkets);
        var arrived = navigateToDestination(navigator, bestMarket);

        if (arrived) {
            // this means the ship is going to end at the final market. so the next step will
            // involve purchasing the goods from the market
            return ContractAgentStatus.PURCHASE_GOODS;
        }
        return ContractAgentStatus.TRAVELING_TO_MARKET;
    }

    private ContractAgentStatus handleTravelToContract() throws ApiException {
        var contractWaypoint = waypointService.findWaypointByContractDestination(getContract());

        var fuelMarkets = waypointService.findWaypointsBySystemAndHasMarketWithTradeSymbol(
            ship.getNav().getSystemSymbol(), TradeSymbol.FUEL);
        fuelMarkets.add(contractWaypoint);
        var navigator = new DistanceNavigator(fuelMarkets);
        var arrived = navigateToDestination(navigator, contractWaypoint);

        if (arrived) {
            // this means the ship is going to end at the final market. so the next step will
            // involve purchasing the goods from the market
            return ContractAgentStatus.DELIVER_GOODS;
        }

        return ContractAgentStatus.TRAVELING_TO_CONTRACT;
    }

    private ContractAgentStatus handleReset() throws ApiException {
        // dock ship, negotiate contract
        this.ship = shipService.dock(ship);
        var contract = contractService.negotiateContract(ship);
        contract = contractService.acceptContract(contract);
        this.activeContract = contract;
        this.ship = shipService.orbit(ship);
        return ContractAgentStatus.TRAVELING_TO_CONTRACT;
    }

    /*
     * Returns true if the ship has arrived at the destination waypoint
     */
    private boolean navigateToDestination(Navigator navigator, Waypoint destination)
        throws ApiException {
        var startWaypoint = waypointService.findWaypointByShipNav(ship);

        var nextStop = navigator.findNextStop(startWaypoint, destination,
            ship.getFuel().getCapacity()).orElseThrow();

        this.ship = shipService.dock(ship);
        this.ship = shipService.refuel(ship);
        this.ship = shipService.orbit(ship);
        this.ship = shipService.navigate(ship, nextStop.getSymbol());

        return ship.getNav().getWaypointSymbol().equals(destination.getSymbol());
    }

    @Override
    public Long getNextUpdateTick() {
        return this.ship.getNav().getRoute().getArrival().toInstant().toEpochMilli();
    }

    private enum ContractAgentStatus {
        // the initial status set when the agent is created, but no ticks have yet been processed
        INIT,
        // traveling to market to purchase resources
        TRAVELING_TO_MARKET,
        // this means the ship is reaching the destination and should purchase the goods next
        PURCHASE_GOODS,
        // traveling to contract location
        TRAVELING_TO_CONTRACT,
        // this means the ship has reached the contract destination and should deliver the goods
        DELIVER_GOODS,
        // set when a contract was completed, this should trigger the next contract negotiation
        RESET,
    }
}
