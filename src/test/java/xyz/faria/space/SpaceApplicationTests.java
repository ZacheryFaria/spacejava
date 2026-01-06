package xyz.faria.space;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
@ActiveProfiles("test")
class SpaceApplicationTests {

    @Test
    void contextLoads() {
    }

}
