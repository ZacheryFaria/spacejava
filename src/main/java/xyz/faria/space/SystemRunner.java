package xyz.faria.space;

import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Reset;
import xyz.faria.space.models.System;
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
        loadWaypoints();
    }

    private void loadAgent() {
        if (agent == null) {
            var ag = agentRepository.findAgentByReset(currentReset);
            ag.ifPresent(value -> agent = value);
        }
    }

    private void loadWaypoints() throws ApiException, InterruptedException {
        List<System> systems = systemRepository.findSystemsWithUnscannedWaypoints();

        systems.sort((o1, o2) -> {
            if (o1.getSymbol().equals(agent.getHeadquartersSystem())) {
                return -1;
            }
            if (o2.getSymbol().equals(agent.getHeadquartersSystem())) {
                return 1;
            }
            return o1.getSymbol().compareTo(o2.getSymbol());
        });

        for (var sys : systems) {
            logger.info(String.format("Collecting waypoints for system %s", sys.getSymbol()));

            var page = 1;

            while (true) {
                var count = systemService.loadSystemWaypoints(agent, sys, page, 20);
                if (count < 20) {
                    break;
                }
                page++;
                logger.info(String.format("Collected waypoint page %d for system %s", page,
                    sys.getSymbol()));
            }
        }
    }
}
