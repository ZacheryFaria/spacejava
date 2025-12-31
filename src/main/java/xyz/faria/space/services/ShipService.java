package xyz.faria.space.services;

import jakarta.transaction.Transactional;
import java.util.logging.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.services.events.ShipUpdatedEvent;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.model.ShipNavStatus;

@Service
public class ShipService {

    private static final Logger logger = Logger.getLogger(ShipService.class.getName());

    private final ShipRepository shipRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ShipService(ShipRepository shipRepository, ApplicationEventPublisher eventPublisher) {
        this.shipRepository = shipRepository;
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
}
