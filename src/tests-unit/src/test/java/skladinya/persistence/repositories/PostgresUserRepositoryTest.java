package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.user.User;
import skladinya.persistence.entities.UserEntity;
import skladinya.persistence.mappers.UserMapper;
import skladinya.persistence.repositories.PostgresUserRepository;
import skladinya.persistence.repositories.SpringUserRepository;
import skladinya.tests.helper.builder.UserBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class PostgresUserRepositoryTest {

    @Autowired
    private SpringUserRepository springUserRepo;

    private PostgresUserRepository userRepo;

    @BeforeEach
    void setUp() {
        userRepo = new PostgresUserRepository(springUserRepo);
    }

    @Test
    void create_shouldSaveUserAndReturnDomain() {
        User user = UserBuilder.builder().build();

        User result = userRepo.create(user);

        assertNotNull(result);
        assertEquals(user.userId(), result.userId());
        assertEquals(user.username(), result.username());
    }

    @Test
    void getByUserId() {
    }

    @Test
    void getByUsername() {
    }

    @Test
    void getByEmail() {
    }

    @Test
    void getAllBySearchOptions() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}