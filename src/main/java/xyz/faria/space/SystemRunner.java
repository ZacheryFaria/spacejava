package xyz.faria.space;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Reset;
import xyz.faria.space.models.System;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.repositories.AgentRepository;
import xyz.faria.space.repositories.ResetRepository;
import xyz.faria.space.repositories.SystemRepository;
import xyz.faria.space.services.ResetService;
import xyz.faria.space.services.SystemService;
import xyz.faria.space.spaceapi.client.ApiException;

@Component
@RequiredArgsConstructor
public class SystemRunner implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(SystemRunner.class.getName());

    private final SystemService systemService;
    private final SystemRepository systemRepository;
    private final ResetService resetService;
    private final ResetRepository resetRepository;
    private final AgentRepository agentRepository;

    private Reset currentReset;
    private Agent agent;

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {
        currentReset = resetService.getCurrentReset();

        loadAgent();

        while (!currentReset.isSystemsCollected()) {
            if (agent == null) {
                loadAgent();
                if (agent == null) {
                    logger.info("No agent found. Sleeping for 5 seconds to try again.");
                    Thread.sleep(5000);
                    continue;
                }
            }

            logger.info(String.format("Collecting systems for reset %s. Current page %d",
                currentReset.getResetDate(), currentReset.getSystemsPage()));

            var count = systemService.loadSystems(agent, currentReset.getSystemsPage(), 20);
            currentReset.setSystemsPage(currentReset.getSystemsPage() + 1);
            resetRepository.save(currentReset);
            if (count < 20) {
                currentReset.setSystemsCollected(true);
                resetRepository.save(currentReset);
                break;
            }
            logger.info(String.format("Collected system page %d.",
                currentReset.getSystemsPage()));
        }
        loadAgentSystem();
        loadWaypoints();
    }

    private void loadAgent() {
        if (agent == null) {
            var ag = agentRepository.findAgentByReset(currentReset);
            ag.ifPresent(value -> agent = value);
        }
    }

    @Transactional
    protected void loadAgentSystem() throws ApiException {
        var system = systemRepository.findBySymbolAndReset(this.agent.getHeadquartersSystem(),
            currentReset);

        if (system.isEmpty()) {
            return;
        }

        loadSystemWaypoints(system.get());

        for (var waypoint : system.get().getWaypoints()) {
            if (waypoint.hasMarketplace() && waypoint.getMarket() == null) {
                logger.info(String.format("Loading market for waypoint %s", waypoint.getSymbol()));
                systemService.loadMarketForWaypoint(agent, waypoint);
            }
        }
    }

    @Transactional
    protected void loadSystemWaypoints(System system) throws ApiException {
        logger.info(String.format("Collecting waypoints for system %s", system.getSymbol()));

        // check if all waypoints have been scanned
        if (system.getWaypoints().stream().allMatch(Waypoint::getHasBeenScanned)) {
            return;
        }

        var page = 1;

        while (true) {
            var count = systemService.loadSystemWaypoints(agent, system, page, 20);
            logger.info(
                String.format("Collected waypoint page %d for system %s. Received %d waypoints",
                    page,
                    system.getSymbol(),
                    count));
            if (count < 20) {
                break;
            }
            page++;
        }
    }

    @Transactional
    protected void loadWaypoints() throws ApiException {
        List<System> systems = systemRepository.findSystemsWithUnscannedWaypoints();

        for (var sys : systems) {
            loadSystemWaypoints(sys);
        }
    }
}
