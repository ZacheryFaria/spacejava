package xyz.faria.space.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.WaypointMarketCache;
import xyz.faria.space.models.Contract;
import xyz.faria.space.models.Ship;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.repositories.SystemRepository;
import xyz.faria.space.repositories.WaypointRepository;
import xyz.faria.space.spaceapi.model.TradeSymbol;

@Service
@RequiredArgsConstructor
public class WaypointService {

    private static final Logger logger = Logger.getLogger(WaypointService.class.getName());

    private final SystemRepository systemRepository;
    private final WaypointRepository waypointRepository;

    private final WaypointMarketCache waypointMarketCache = new WaypointMarketCache();
    private final ResetService resetService;

    public List<Waypoint> findWaypointsBySystemAndHasMarketWithTradeSymbol(String systemSymbol,
        TradeSymbol tradeSymbol) {

        if (this.waypointMarketCache.get(systemSymbol, tradeSymbol).isEmpty()) {
            var data = waypointRepository.findWaypointsBySystemAndHasMarketWithTradeSymbol(
                systemSymbol, tradeSymbol);
            this.waypointMarketCache.put(systemSymbol, tradeSymbol, data);
        }
        return new ArrayList<>(
            this.waypointMarketCache.get(systemSymbol, tradeSymbol).orElse(List.of()));
    }

    public @Nonnull Waypoint findWaypointByShipNav(Ship ship) {
        return waypointRepository.findWaypointBySymbol(ship.getNav().getWaypointSymbol())
            .orElseThrow();
    }

    public @Nonnull Waypoint findWaypointByContractDestination(Contract contract) {
        return waypointRepository.findWaypointBySymbol(
                contract.getTerms().getDeliver().getFirst().getDestinationSymbol())
            .orElseThrow();
    }
}
