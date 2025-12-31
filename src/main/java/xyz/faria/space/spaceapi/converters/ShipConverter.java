package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.Ship;

public class ShipConverter {

    public static Ship fromApiShip(Ship ship, xyz.faria.space.spaceapi.model.Ship apiShip) {
        ship.setSymbol(apiShip.getSymbol());
        ship.setCrew(apiShip.getCrew());
        ship.setCooldown(apiShip.getCooldown());
        ship.setCargo(apiShip.getCargo());
        ship.setEngine(apiShip.getEngine());
        ship.setFrame(apiShip.getFrame());
        ship.setFuel(apiShip.getFuel());
        ship.setModules(apiShip.getModules());
        ship.setNav(apiShip.getNav());
        ship.setReactor(apiShip.getReactor());
        ship.setRegistration(apiShip.getRegistration());
        return ship;
    }

    public static Ship fromApiShip(xyz.faria.space.spaceapi.model.Ship ship) {
        return fromApiShip(new Ship(), ship);
    }
}
