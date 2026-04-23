package skladinya.persistence.postgres.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.user.User;
import skladinya.domain.models.user.UserSearchOptions;
import skladinya.domain.repositories.UserRepository;
import skladinya.tests.helper.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresUserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void create_shouldSaveUserAndReturnDomain() {
        User user = UserBuilder.builder().build();

        User result = userRepo.create(user);
        User actual = userRepo.getByUserId(result.userId()).orElse(null);

        assertNotNull(actual);
        assertEquals(user.userId(), actual.userId());
        assertEquals(user.username(), actual.username());
    }

    @Test
    void getByUserId_shouldReturnSavedUser_whenUserExists() {
        User user = UserBuilder.builder().build();
        userRepo.create(user);

        User actual = userRepo.getByUserId(user.userId()).orElse(null);

        assertNotNull(actual);
        assertEquals(user.userId(), actual.userId());
        assertEquals(user.username(), actual.username());
    }

    @Test
    void getByUsername_shouldReturnUser_whenUsernameExists() {
        User user = UserBuilder.builder().build();
        userRepo.create(user);

        User actual = userRepo.getByUsername(user.username()).orElse(null);

        assertNotNull(actual);
        assertEquals(user.username(), actual.username());
        assertEquals(user.userId(), actual.userId());
    }

    @Test
    void getByEmail_shouldReturnUser_whenEmailExists() {
        User user = UserBuilder.builder().build();
        userRepo.create(user);

        User actual = userRepo.getByEmail(user.email()).orElse(null);

        assertNotNull(actual);
        assertEquals(user.email(), actual.email());
        assertEquals(user.userId(), actual.userId());
    }

    @Test
    void getAllBySearchOptions_shouldReturnUsers_whenFilterByUsername() {
        User user = UserBuilder.builder()
                .username("test_user")
                .build();
        userRepo.create(user);
        UserSearchOptions options = new UserSearchOptions(
                "test",
                null,
                null,
                null,
                10,
                0
        );

        List<User> result = userRepo.getAllBySearchOptions(options);

        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .anyMatch(u -> u.username().equals("test_user")));
    }

    @Test
    void update_shouldModifyUserFields_whenUserExists() {
        User user = UserBuilder.builder().build();
        userRepo.create(user);
        User updated = UserBuilder.builder()
                .name(user.name())
                .username("updated_username")
                .email("updated@mail.com")
                .build();

        User result = userRepo.update(user.userId(), updated);

        assertEquals(user.userId(), result.userId());
        assertEquals(user.name(), result.name());
        assertEquals(updated.username(), result.username());
        assertEquals(updated.email(), result.email());
    }

    @Test
    void delete_shouldRemoveUser_whenUserExists() {
        User user = UserBuilder.builder().build();
        userRepo.create(user);

        userRepo.delete(user.userId());

        Optional<User> result = userRepo.getByUserId(user.userId());
        assertTrue(result.isEmpty());
    }
}
