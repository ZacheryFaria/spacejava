package xyz.faria.space;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import xyz.faria.space.logic.ShipAgent;
import xyz.faria.space.logic.ShipAgentFactory;
import xyz.faria.space.models.Agent;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.services.AgentService;
import xyz.faria.space.services.ShipService;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class GameRunner implements CommandLineRunner {

    private final ShipService shipService;
    private final ShipRepository shipRepository;
    private final AgentService agentService;

    private Agent mainAgent;

    private final Map<String, ShipAgent> agents = new HashMap<>();
    private final Map<String, Thread> agentThreads = new HashMap<>();

    @Override
    @Transactional
    public void run(String @NonNull ... args) throws Exception {
        this.mainAgent = agentService.getMainAgent();
        var ships = shipRepository.findAll();

        for (var ship : ships) {
            agents.put(ship.getSymbol(), ShipAgentFactory.createShipEngine(ship));
            agentThreads.put(ship.getSymbol(),
                new Thread(() -> agents.get(ship.getSymbol()).updateTick()));
        }

//        while (true) {
//            // Your continuous background logic here
//            System.out.println("Worker is running...");
//            Thread.sleep(5000); // Sleep for 5 seconds to prevent tight loop
//        }
    }


}
