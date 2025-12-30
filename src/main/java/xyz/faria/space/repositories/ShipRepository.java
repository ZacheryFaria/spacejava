package xyz.faria.space.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Ship;


public interface ShipRepository extends CrudRepository<Ship, Long> {
}
