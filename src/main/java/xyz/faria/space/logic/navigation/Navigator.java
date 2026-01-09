package xyz.faria.space.logic.navigation;

import java.util.List;
import java.util.Optional;
import xyz.faria.space.models.Waypoint;

public interface Navigator {

    List<Waypoint> getRoute(Waypoint start, Waypoint end, int maxDistance);

    Optional<Waypoint> findNextStop(Waypoint start, Waypoint end, int maxDistance);

    Optional<Waypoint> findNearestWaypoint(Waypoint start);
}
