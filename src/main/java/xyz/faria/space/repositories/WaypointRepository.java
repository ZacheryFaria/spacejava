package xyz.faria.space.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Waypoint;


public interface WaypointRepository extends CrudRepository<Waypoint, Long> {

}
