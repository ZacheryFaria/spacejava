package xyz.faria.space.logic.navigation;

import java.util.List;
import xyz.faria.space.models.Waypoint;

public interface Navigator {

    List<Waypoint> getRoute(Waypoint start, Waypoint end, int maxDistance);
}
