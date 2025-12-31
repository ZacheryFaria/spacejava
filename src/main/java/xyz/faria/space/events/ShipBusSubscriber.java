package xyz.faria.space.events;

import xyz.faria.space.models.Ship;

public interface ShipBusSubscriber {

    boolean shouldReceiveUpdate(String shipSymbol);

    void onShipUpdate(Ship ship);

}
