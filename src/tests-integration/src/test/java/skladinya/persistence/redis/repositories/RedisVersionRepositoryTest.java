package skladinya.persistence.redis.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import skladinya.domain.repositories.VersionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataRedisTest
@Testcontainers
@ContextConfiguration(classes = TestApplication.class)
class RedisVersionRepositoryTest {

    static int REDIS_PORT = 6379;

    static int TTL = 5;

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:alpine").withExposedPorts(REDIS_PORT);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }

    @Autowired
    private SpringVersionRepository springVersionRepository;

    private VersionRepository versionRepository;

    @BeforeEach
    void setUp() {
        versionRepository = new RedisVersionRepository(springVersionRepository, TTL);
    }

    @Test
    void givenValidData_whenGetByUserId_thenReturnVersion() {
        var userId = UUID.randomUUID();
        var version = "67";
        versionRepository.save(userId, version);

        var result = versionRepository.getByUserId(userId);

        assertEquals(version, result);
    }

    @Test
    void givenNewUserId_whenGetByUserId_thenReturnEmpty() {
        var userId = UUID.randomUUID();

        var result = versionRepository.getByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenAfterTimeout_whenGetByUserId_thenReturnEmpty() throws InterruptedException {
        var userId = UUID.randomUUID();
        var version = "67";
        versionRepository.save(userId, version);
        Thread.sleep(TTL * 1100L);

        var result = versionRepository.getByUserId(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenAfterUpdate_whenGetByUserId_thenReturnUpdated() throws InterruptedException {
        var userId = UUID.randomUUID();
        var version = "45";
        versionRepository.save(userId, version);
        var newVersion = "67";
        versionRepository.save(userId, newVersion);

        var result = versionRepository.getByUserId(userId);

        assertEquals(newVersion, result);
    }

    @AfterEach
    void tearDown() {
        springVersionRepository.deleteAll();
    }
}
