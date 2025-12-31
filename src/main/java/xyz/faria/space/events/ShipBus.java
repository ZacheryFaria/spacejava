package xyz.faria.space.events;

import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import xyz.faria.space.models.Ship;

public class ShipBus {

    private static final ShipBus INSTANCE = new ShipBus();

    private final Sinks.Many<Ship> bus;

    private ShipBus() {
        this.bus = Sinks.many().multicast().directBestEffort();
    }

    public static ShipBus getInstance() {
        return INSTANCE;
    }

    public void publish(Ship ship) {
        bus.tryEmitNext(ship).orThrow();
    }

    public Disposable subscribe(ShipBusSubscriber subscriber) {
        return bus.asFlux()
            .filter(ship -> subscriber.shouldReceiveUpdate(ship.getSymbol()))
            .subscribe(subscriber::onShipUpdate);
    }
}
