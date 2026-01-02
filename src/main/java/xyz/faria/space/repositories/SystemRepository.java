package xyz.faria.space.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Reset;
import xyz.faria.space.models.System;

import java.util.List;
import java.util.Optional;


public interface SystemRepository extends CrudRepository<System, Long> {

    Optional<System> findBySymbol(String symbol);

    Optional<System> findBySymbolAndReset(String symbol, Reset reset);

    @Query("""
            WITH x AS (SELECT w.system.id as id FROM Waypoint w WHERE w.hasBeenScanned = false
            GROUP BY w.system.id HAVING count(w.id) > 0)
            SELECT s.symbol FROM System s WHERE s.id in (SELECT id FROM x)
            """)
    List<String> findSystemSymbolsWithUnscannedWaypoints();

}
