package xyz.faria.space.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Ship;


public interface ShipRepository extends CrudRepository<Ship, Long> {

    public Optional<Ship> findBySymbol(String symbol);
}
