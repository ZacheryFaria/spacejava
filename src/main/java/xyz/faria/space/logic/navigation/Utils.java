package xyz.faria.space.logic.navigation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import xyz.faria.space.models.Waypoint;

public class Utils {

    public static double distance(@Nonnull Waypoint start, @Nonnull Waypoint end) {
        return distance(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public static double distance(int xa, int ya, int xb, int yb) {
        return Math.sqrt(Math.pow(xb - xa, 2) + Math.pow(yb - ya, 2));
    }

    public static Map<String, Double> getDistanceMatrix(Waypoint origin, List<Waypoint> waypoints) {
        return waypoints.stream()
            .collect(Collectors.toMap(
                Waypoint::getSymbol,
                waypoint -> distance(origin, waypoint)
            ));
    }
}
