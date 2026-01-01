package xyz.faria.space;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.events.ShipBusSubscriber;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.AgentService;
import xyz.faria.space.services.ShipService;

@Component
@RequiredArgsConstructor
public class GameRunner implements CommandLineRunner, ShipBusSubscriber {

    private final ShipService shipService;
    private final AgentService agentService;

    private Agent mainAgent;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        var disp = ShipBus.getInstance().subscribe(this);
        this.mainAgent = agentService.getMainAgent();
//        while (true) {
//            // Your continuous background logic here
//            System.out.println("Worker is running...");
//            Thread.sleep(5000); // Sleep for 5 seconds to prevent tight loop
//        }
    }

    @Override
    public boolean shouldReceiveUpdate(String shipSymbol) {
        return true;
    }

    @Override
    public void onShipUpdate(Ship ship) {
        System.out.println("Ship updated: " + ship.getSymbol());
    }
}
