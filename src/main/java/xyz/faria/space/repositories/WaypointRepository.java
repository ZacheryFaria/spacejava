package xyz.faria.space.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;


public interface WaypointRepository extends CrudRepository<Waypoint, Long> {

    Integer countBySystemSymbolAndHasBeenScanned(String systemSymbol, boolean hasBeenScanned);

    List<Waypoint> findWaypointsBySymbolIsInAndSystem(List<String> symbols, System system);
}
