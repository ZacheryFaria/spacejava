package xyz.faria.space.services.events;

import org.springframework.context.ApplicationEvent;
import xyz.faria.space.models.Ship;

public class ShipUpdatedEvent extends ApplicationEvent {

    public ShipUpdatedEvent(Ship source) {
        super(source);
    }

    public Ship getShip() {
        return (Ship) getSource();
    }
}
