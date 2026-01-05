package xyz.faria.space.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Reset;
import xyz.faria.space.models.Ship;


public interface ShipRepository extends CrudRepository<Ship, Long> {

    Optional<Ship> findBySymbol(String symbol);

    List<Ship> findShipsByAgent(Agent agent);


    @Query("SELECT s FROM Ship s JOIN Agent a on s.agent = a WHERE a.reset = :reset")
    List<Ship> findShipsByResetDate(Reset reset);

}
