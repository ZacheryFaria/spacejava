package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.System;

public class SystemConverter {

    public static System fromApiSystem(System system,
        xyz.faria.space.spaceapi.model.System apiSystem) {
        system.setConstellation(apiSystem.getConstellation());
        system.setSymbol(apiSystem.getSymbol());
        system.setSectorSymbol(apiSystem.getSectorSymbol());
        system.setX(apiSystem.getX());
        system.setY(apiSystem.getY());
        system.setType(apiSystem.getType());
        system.setName(apiSystem.getName());

        return system;
    }

    public static System fromApiSystem(xyz.faria.space.spaceapi.model.System apiSystem) {
        return fromApiSystem(new System(), apiSystem);
    }
}
