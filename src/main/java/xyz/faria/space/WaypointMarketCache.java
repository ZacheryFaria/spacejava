package xyz.faria.space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.spaceapi.model.TradeSymbol;

public class WaypointMarketCache {

    private final Map<String, Map<TradeSymbol, List<Waypoint>>> cache = new HashMap<>();

    private Map<TradeSymbol, List<Waypoint>> getSymbolToMarketLookup(String system) {
        return cache.computeIfAbsent(system, k -> new HashMap<>());
    }

    public void put(String system, TradeSymbol symbol, List<Waypoint> markets) {
        getSymbolToMarketLookup(system).put(symbol, markets);
    }

    public Optional<List<Waypoint>> get(String system, TradeSymbol symbol) {
        var list = this.getSymbolToMarketLookup(system).get(symbol);

        if (list == null) {
            return Optional.empty();
        } else {
            return Optional.of(list);
        }
    }
}
