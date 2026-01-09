package xyz.faria.space.events;

import com.vaadin.flow.function.SerializableConsumer;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;
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
        var result = bus.tryEmitNext(ship);
        if (result != EmitResult.FAIL_ZERO_SUBSCRIBER) {
            result.orThrow();
        }
    }

    public Disposable subscribe(ShipBusSubscriber subscriber) {
        return bus.asFlux()
            .filter(ship -> subscriber.shouldReceiveUpdate(ship.getSymbol()))
            .subscribe(subscriber::onShipUpdate);
    }

    public Disposable subscribe(SerializableConsumer<Ship> consumer) {
        return bus.asFlux()
            .subscribe(consumer);
    }
}
