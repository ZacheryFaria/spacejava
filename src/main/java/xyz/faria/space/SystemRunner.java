package xyz.faria.space;

import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.faria.space.models.Agent;
import xyz.faria.space.repositories.AgentRepository;
import xyz.faria.space.repositories.ResetRepository;
import xyz.faria.space.services.ResetService;
import xyz.faria.space.services.SystemService;

@Component
@RequiredArgsConstructor
public class SystemRunner implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(SystemRunner.class.getName());

    private final SystemService systemService;
    private final ResetService resetService;
    private final ResetRepository resetRepository;
    private final AgentRepository agentRepository;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        var currentReset = resetService.getCurrentReset();

        Agent agent = null;

        while (!currentReset.isSystemsCollected()) {
            if (agent == null) {
                var ag = agentRepository.findAgentByReset(currentReset);
                if (ag.isPresent()) {
                    agent = ag.get();
                } else {
                    logger.info(
                        "No agent found for current reset. Sleeping for 5 seconds to try again.");
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
            logger.info(String.format("Collected system page %d. Sleeping for 1 second",
                currentReset.getSystemsPage()));
            Thread.sleep(1000);
        }
    }
}
