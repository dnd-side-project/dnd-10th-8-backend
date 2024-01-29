package ac.dnd.mur.server.common.containers;

import org.junit.jupiter.api.extension.Extension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SuppressWarnings("rawtypes")
@Testcontainers
public class RedisTestContainersExtension implements Extension {
    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer<?> container;

    static {
        container = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT);
        container.start();

        System.setProperty("spring.data.redis.host", container.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(container.getMappedPort(REDIS_PORT)));
    }
}
