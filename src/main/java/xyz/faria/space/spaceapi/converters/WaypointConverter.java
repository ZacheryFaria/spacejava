package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;

public class WaypointConverter {

    public static Waypoint fromApiWaypoint(Waypoint waypoint,
        xyz.faria.space.spaceapi.model.Waypoint apiWaypoint) {
        waypoint.setFaction(apiWaypoint.getFaction());
        waypoint.setModifiers(apiWaypoint.getModifiers());
        waypoint.setIsUnderConstruction(apiWaypoint.getIsUnderConstruction());
        waypoint.setTraits(apiWaypoint.getTraits());
        waypoint.setSymbol(apiWaypoint.getSymbol());
        waypoint.setOrbitals(apiWaypoint.getOrbitals());
        waypoint.setOrbits(apiWaypoint.getOrbits());
        waypoint.setX(apiWaypoint.getX());
        waypoint.setY(apiWaypoint.getY());
        waypoint.setType(apiWaypoint.getType());
        waypoint.setHasBeenScanned(true);

        return waypoint;
    }

    public static Waypoint fromApiWaypoint(xyz.faria.space.spaceapi.model.Waypoint apiWaypoint) {
        return fromApiWaypoint(new Waypoint(), apiWaypoint);
    }

    public static Waypoint fromApiSystemWaypoint(Waypoint waypoint,
        xyz.faria.space.spaceapi.model.SystemWaypoint apiSystemWaypoint, System system) {
        waypoint.setSymbol(apiSystemWaypoint.getSymbol());
        waypoint.setOrbits(apiSystemWaypoint.getOrbits());
        waypoint.setOrbitals(apiSystemWaypoint.getOrbitals());
        waypoint.setX(apiSystemWaypoint.getX());
        waypoint.setY(apiSystemWaypoint.getY());
        waypoint.setType(apiSystemWaypoint.getType());
        waypoint.setIsUnderConstruction(false);
        waypoint.setHasBeenScanned(false);
        waypoint.setSystemSymbol(system.getSymbol());
        waypoint.setSystem(system);
        return waypoint;
    }

    public static Waypoint fromApiSystemWaypoint(
        xyz.faria.space.spaceapi.model.SystemWaypoint apiSystemWaypoint, System system) {
        return fromApiSystemWaypoint(new Waypoint(), apiSystemWaypoint, system);
    }
}
