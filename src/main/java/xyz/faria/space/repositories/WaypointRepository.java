package xyz.faria.space.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.spaceapi.model.TradeSymbol;


public interface WaypointRepository extends CrudRepository<Waypoint, Long> {

    Integer countBySystemSymbolAndHasBeenScanned(String systemSymbol, boolean hasBeenScanned);

    List<Waypoint> findWaypointsBySymbolIsInAndSystem(List<String> symbols, System system);

    @Query("SELECT w FROM Waypoint w WHERE w.system.symbol = :systemSymbol AND w.market IS NOT NULL")
    List<Waypoint> findWaypointsBySystemAndHasMarket(String systemSymbol);

    @Query("""
        SELECT w
        FROM Waypoint w
                 JOIN Market m ON w.id = m.waypoint.id
                 FULL OUTER JOIN m.exports e
                 FULL OUTER JOIN m.tradeGoods te
        WHERE (te.symbol = :tradeSymbol or e.symbol = :tradeSymbol) and w.systemSymbol = :systemSymbol
        """)
    List<Waypoint> findWaypointsBySystemAndHasMarketWithTradeSymbol(String systemSymbol,
        TradeSymbol tradeSymbol);

    Optional<Waypoint> findWaypointBySymbol(String symbol);

    Optional<Waypoint> findWaypointsBySymbol(String symbol);
}
