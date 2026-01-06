package xyz.faria.space.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;


public interface WaypointRepository extends CrudRepository<Waypoint, Long> {

    Integer countBySystemSymbolAndHasBeenScanned(String systemSymbol, boolean hasBeenScanned);

    List<Waypoint> findWaypointsBySymbolIsInAndSystem(List<String> symbols, System system);

    @Query("SELECT w FROM Waypoint w WHERE w.system.symbol = :systemSymbol AND w.market IS NOT NULL")
    List<Waypoint> findWaypointsBySystemAndHasMarket(String systemSymbol);
}
