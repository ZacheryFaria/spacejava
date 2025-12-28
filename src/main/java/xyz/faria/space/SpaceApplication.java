package xyz.faria.space;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@StyleSheet("styles.css")
@EnableAsync
public class SpaceApplication implements AppShellConfigurator {

    static void main(String[] args) {
        SpringApplication.run(SpaceApplication.class, args);
    }
}
