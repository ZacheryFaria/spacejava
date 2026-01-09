package xyz.faria.space;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import xyz.faria.space.logic.navigation.DistanceNavigator;
import xyz.faria.space.models.Waypoint;

public class DistanceNavigatorTest {

    private final List<Waypoint> waypoints;

    private final Waypoint start;
    private final Waypoint end;

    public DistanceNavigatorTest() {
        this.waypoints = new ArrayList<>();

        Waypoint start = new Waypoint();
        start.setX(-63);
        start.setY(3);
        start.setSymbol("start");
        this.start = start;
        Waypoint mid = new Waypoint();
        mid.setX(26);
        mid.setY(3);
        mid.setSymbol("mid");
        Waypoint planet = new Waypoint();
        planet.setX(69);
        planet.setY(34);
        planet.setSymbol("planet");
        this.end = planet;
        Waypoint asteroid = new Waypoint();
        asteroid.setX(16);
        asteroid.setY(24);
        asteroid.setSymbol("asteroid");

        waypoints.add(start);
        waypoints.add(mid);
        waypoints.add(planet);
        waypoints.add(asteroid);
    }

    @Test
    void test_distance() {
        DistanceNavigator navigator = new DistanceNavigator(waypoints);

        var result = navigator.getRoute(start, end, 100);

        assertEquals(2, result.size());
        assertEquals("planet", result.get(1).getSymbol());
    }

    @Test
    void test_next_step() {
        DistanceNavigator navigator = new DistanceNavigator(waypoints);

        var nextStop = navigator.findNextStop(start, end, 100).orElseThrow();

        assertEquals("mid", nextStop.getSymbol());

        var finalStop = navigator.findNextStop(nextStop, end, 100).orElseThrow();

        assertEquals("planet", finalStop.getSymbol());
    }
}
