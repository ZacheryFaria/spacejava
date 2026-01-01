package xyz.faria.space.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.System;


public interface SystemRepository extends CrudRepository<System, Long> {

    Optional<System> findBySymbol(String symbol);

    @Query("""
        WITH x AS (SELECT w.system.id as id FROM Waypoint w WHERE w.hasBeenScanned = false
        GROUP BY w.system.id HAVING count(w.id) > 0)
        SELECT s FROM System s WHERE s.id in (SELECT id FROM x)
        """)
    List<System> findSystemsWithUnscannedWaypoints();

}
