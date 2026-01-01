package xyz.faria.space.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Ship;


public interface ShipRepository extends CrudRepository<Ship, Long> {

    Optional<Ship> findBySymbol(String symbol);

    List<Ship> findShipsByAgent(Agent agent);
}
