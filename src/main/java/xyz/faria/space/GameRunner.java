package xyz.faria.space;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameRunner implements CommandLineRunner {

    @Override
    public void run(String @NonNull ... args) throws Exception {
        while (true) {
            // Your continuous background logic here
            System.out.println("Worker is running...");
            Thread.sleep(5000); // Sleep for 5 seconds to prevent tight loop
        }
    }
}
