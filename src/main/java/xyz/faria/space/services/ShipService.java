package xyz.faria.space.services;

import jakarta.transaction.Transactional;
import java.util.logging.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.AgentRepository;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.services.events.ShipUpdatedEvent;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.AgentConverter;
import xyz.faria.space.spaceapi.model.NavigateShipRequest;
import xyz.faria.space.spaceapi.model.PurchaseCargoRequest;
import xyz.faria.space.spaceapi.model.RefuelShipRequest;
import xyz.faria.space.spaceapi.model.ShipNavStatus;
import xyz.faria.space.spaceapi.model.TradeSymbol;

@Service
public class ShipService {

    private static final Logger logger = Logger.getLogger(ShipService.class.getName());

    private final ShipRepository shipRepository;
    private final AgentRepository agentRepository;

    private final ApplicationEventPublisher eventPublisher;

    public ShipService(ShipRepository shipRepository, AgentRepository agentRepository,
        ApplicationEventPublisher eventPublisher) {
        this.shipRepository = shipRepository;
        this.agentRepository = agentRepository;
        this.eventPublisher = eventPublisher;
    }

    public void saveAndPub(Ship ship) {
        logger.info("Updating ship " + ship.getSymbol());
        shipRepository.save(ship);
        logger.info("Ship updated, publishing event for ship " + ship.getSymbol());
        eventPublisher.publishEvent(new ShipUpdatedEvent(ship));

        ShipBus.getInstance().publish(ship);
    }

    @Transactional
    public Ship refuel(Ship ship) throws ApiException {
        var fleetApi = ship.getFleetApi();
        var data = fleetApi.refuelShip(ship.getSymbol(), new RefuelShipRequest()).getData();
        ship.setFuel(data.getFuel());
        saveAndPub(ship);
        var agent = agentRepository.findAgentById(ship.getAgent().getId());
        AgentConverter.fromApiAgent(agent, data.getAgent());
        agentRepository.save(agent);
        return ship;
    }

    @Transactional
    public Ship dock(Ship ship) throws ApiException {
        if (ship.getNav().getStatus() == ShipNavStatus.DOCKED) {
            return ship;
        }
        var fleetApi = ship.getFleetApi();
        var data = fleetApi.dockShip(ship.getSymbol()).getData();
        ship.setNav(data.getNav());
        saveAndPub(ship);
        return ship;
    }

    @Transactional
    public Ship orbit(Ship ship) throws ApiException {
        if (ship.getNav().getStatus() == ShipNavStatus.IN_ORBIT) {
            return ship;
        }
        var fleetApi = ship.getFleetApi();
        var data = fleetApi.orbitShip(ship.getSymbol()).getData();
        ship.setNav(data.getNav());
        saveAndPub(ship);
        return ship;
    }

    @Transactional
    public Ship navigate(Ship ship, String destination) throws ApiException {
        var fleetApi = ship.getFleetApi();
        var request = new NavigateShipRequest().waypointSymbol(destination);
        var data = fleetApi.navigateShip(ship.getSymbol(), request).getData();
        ship.setNav(data.getNav());
        ship.setFuel(data.getFuel());
        saveAndPub(ship);
        return ship;
    }

    @Transactional
    public Ship purchase(Ship ship, TradeSymbol symbol, int quantity) throws ApiException {
        var fleetApi = ship.getFleetApi();
        var request = new PurchaseCargoRequest().symbol(symbol).units(quantity);
        var data = fleetApi.purchaseCargo(ship.getSymbol(), request).getData();
        ship.setCargo(data.getCargo());
        saveAndPub(ship);
        return ship;
    }
}
