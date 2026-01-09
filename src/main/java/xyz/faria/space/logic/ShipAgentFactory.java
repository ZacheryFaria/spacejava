package xyz.faria.space.logic;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ContractService;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.SystemService;
import xyz.faria.space.services.WaypointService;

@Service
@RequiredArgsConstructor
public class ShipAgentFactory {

    private final ShipService shipService;
    private final WaypointService waypointService;
    private final ContractService contractService;
    private final SystemService systemService;

    public Optional<ShipAgent> createShipEngine(Ship ship) {
        var apiClient = ship.getAgent().getAgentClient();
        if (ship.getSymbol().endsWith("-1")) {
            return Optional.of(
                new ContractShipAgent(apiClient, ship, shipService, null, waypointService,
                    systemService, contractService));
        }
        return Optional.empty();
    }
}
