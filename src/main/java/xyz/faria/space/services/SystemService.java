package xyz.faria.space.services;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Market;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.repositories.MarketRepository;
import xyz.faria.space.repositories.SystemRepository;
import xyz.faria.space.repositories.WaypointRepository;
import xyz.faria.space.spaceapi.api.SystemsApi;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.MarketConverter;
import xyz.faria.space.spaceapi.converters.SystemConverter;
import xyz.faria.space.spaceapi.converters.WaypointConverter;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final SystemRepository systemRepository;
    private final WaypointRepository waypointRepository;
    private final ResetService resetService;
    private final MarketRepository marketRepository;

    public Optional<System> getSystemBySymbol(String symbol) {
        return systemRepository.findBySymbol(symbol);
    }

    /**
     *
     * @param page
     * @param pageSize
     * @return number of systems loaded
     * @throws ApiException
     */
    public Integer loadSystems(Agent agent, Integer page, Integer pageSize) throws ApiException {
        var apiClient = agent.getAgentClient();
        var currentReset = resetService.getCurrentReset();
        var client = new SystemsApi(apiClient);

        var response = client.getSystems(page, pageSize);

        List<System> systems = new ArrayList<>();
        List<Waypoint> waypoints = new ArrayList<>();

        for (var system : response.getData()) {
            var systemModel = SystemConverter.fromApiSystem(system);
            systemModel.setReset(currentReset);
            systems.add(systemModel);

            for (var waypoint : system.getWaypoints()) {
                var waypointModel = WaypointConverter.fromApiSystemWaypoint(waypoint, systemModel);
                waypoints.add(waypointModel);
            }
        }

        systemRepository.saveAll(systems);

        waypointRepository.saveAll(waypoints);

        return systems.size();
    }

    public @Nonnull System loadSystem(Agent agent, String symbol) throws ApiException {
        var apiClient = agent.getAgentClient();
        var currentReset = resetService.getCurrentReset();
        var client = new SystemsApi(apiClient);
        var response = client.getSystem(symbol);
        List<Waypoint> waypoints = new ArrayList<>();

        var systemModel = SystemConverter.fromApiSystem(response.getData());
        systemModel.setReset(currentReset);
        systemRepository.save(systemModel);
        for (var waypoint : response.getData().getWaypoints()) {
            var waypointModel = WaypointConverter.fromApiSystemWaypoint(waypoint, systemModel);
            waypoints.add(waypointModel);
        }
        waypointRepository.saveAll(waypoints);
        return systemModel;
    }

    public Integer loadSystemWaypoints(Agent agent, System system, Integer page, Integer pageSize)
        throws ApiException {
        var apiClient = agent.getAgentClient();
        var client = new SystemsApi(apiClient);

        var response = client.getSystemWaypoints(system.getSymbol(), page, pageSize, null, null);

        Map<String, Waypoint> waypointMap = new HashMap<>();

        List<String> waypointIds = new ArrayList<>();

        for (var wp : response.getData()) {
            waypointIds.add(wp.getSymbol());
        }

        var waypoints = waypointRepository.findWaypointsBySymbolIsInAndSystem(waypointIds, system);

        for (var waypoint : waypoints) {
            waypointMap.put(waypoint.getSymbol(), waypoint);
        }

        List<Waypoint> updatedWaypoints = new ArrayList<>();

        for (var wp : response.getData()) {
            var waypoint = waypointMap.get(wp.getSymbol());
            if (waypoint == null) {
                throw new IllegalArgumentException(
                    "Waypoint not found for symbol: " + wp.getSymbol());
            }
            var updatedWaypoint = WaypointConverter.fromApiWaypoint(waypoint, wp);
            updatedWaypoints.add(updatedWaypoint);
        }

        waypointRepository.saveAll(updatedWaypoints);

        return response.getData().size();
    }

    public void loadMarketForWaypoint(Agent agent, Waypoint waypoint) throws ApiException {
        var apiClient = agent.getAgentClient();
        var client = new SystemsApi(apiClient);

        var response = client.getMarket(waypoint.getSystemSymbol(), waypoint.getSymbol());

        var marketModel = waypoint.getMarket();
        if (marketModel == null) {
            marketModel = new Market();
        }
        var market = MarketConverter.fromApiMarket(marketModel, response.getData());
        market.setWaypoint(waypoint);
        marketRepository.save(market);
    }
}
