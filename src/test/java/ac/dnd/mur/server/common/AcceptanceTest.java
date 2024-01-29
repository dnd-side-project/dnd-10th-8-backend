package ac.dnd.mur.server.common;

import ac.dnd.mur.server.common.config.BlackboxLogicControlConfig;
import ac.dnd.mur.server.common.containers.LocalStackTestContainersConfig;
import ac.dnd.mur.server.common.containers.MySqlTestContainersExtension;
import ac.dnd.mur.server.common.containers.RedisTestContainersExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@Tag("Acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackTestContainersConfig.class)
@ExtendWith({
        MySqlTestContainersExtension.class,
        RedisTestContainersExtension.class
})
@Import({BlackboxLogicControlConfig.class})
public abstract class AcceptanceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
