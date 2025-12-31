package xyz.faria.space.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.repositories.SystemRepository;
import xyz.faria.space.repositories.WaypointRepository;
import xyz.faria.space.spaceapi.api.SystemsApi;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.SystemConverter;
import xyz.faria.space.spaceapi.converters.WaypointConverter;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final SystemRepository systemRepository;
    private final WaypointRepository waypointRepository;
    private final ResetService resetService;

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
    @Transactional
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

}
