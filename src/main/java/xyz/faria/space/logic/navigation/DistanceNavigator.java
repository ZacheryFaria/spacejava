package xyz.faria.space.logic.navigation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import xyz.faria.space.models.Waypoint;

public class DistanceNavigator implements Navigator {

    private final List<Waypoint> waypoints;

    public DistanceNavigator(List<Waypoint> waypoint) {
        this.waypoints = waypoint;
    }

    private Waypoint getNextPoint(Waypoint start, Waypoint end, int maxDistance) {
        var inRangeWaypoints = waypoints.stream()
            .filter(waypoint -> Utils.distance(start, waypoint) <= maxDistance)
            .map(Waypoint::getSymbol)
            .filter(symbol -> !symbol.equals(start.getSymbol()))
            .toList();

        Set<String> inRangeWaypointsSet = new HashSet<>(inRangeWaypoints);


        Waypoint best = null;
        double bestScore = Double.MAX_VALUE;

        for (var waypoint : waypoints) {
            if (inRangeWaypointsSet.contains(waypoint.getSymbol())) {
                double score = Utils.distance(end, waypoint);
                if (score < bestScore) {
                    best = waypoint;
                    bestScore = score;
                }
            }
        }

        return best;
    }

    @Override
    public List<Waypoint> getRoute(Waypoint start, Waypoint end, int maxDistance) {
        List<Waypoint> route = new ArrayList<>();

        var prev = start;
        while (true) {
            var next = getNextPoint(prev, end, maxDistance);
            route.add(next);
            prev = next;

            if (next.getSymbol().equals(end.getSymbol())) {
                break;
            }
        }

        return route;
    }
}
