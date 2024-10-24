package utils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractPostgresContainerTest {

    private static final String POSTGRES_IMAGE_NAME = "postgres:14.9";

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE_NAME)
            .withReuse(true);

    static {
        postgres.start();
    }
}
