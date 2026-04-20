package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorRole;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;
import skladinya.tests.helper.builder.OperatorBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class PostgresOperatorRepositoryTest {

    @Autowired
    private SpringOperatorRepository springOperatorRepo;

    @Autowired
    private SpringStorageRepository springStorageRepo;

    @Autowired
    private SpringUserRepository springUserRepo;

    private PostgresOperatorRepository operatorRepo;

    private User user;
    private Storage storage;

    @BeforeEach
    void setUp() {
        PostgresStorageRepository storageRepo = new PostgresStorageRepository(springStorageRepo);
        storage = storageRepo.create(StorageBuilder.builder().build());

        PostgresUserRepository userRepo = new PostgresUserRepository(springUserRepo);
        user = userRepo.create(UserBuilder.builder()
                .name("test-name")
                .username("test-username")
                .build()
        );

        operatorRepo = new PostgresOperatorRepository(springOperatorRepo);
    }

    @Test
    void create_shouldSaveOperatorAndReturnDomain() {
        Operator operator = OperatorBuilder.builder().build();

        Operator result = operatorRepo.create(operator);
        Operator actual = operatorRepo.getByOperatorId(result.operatorId()).orElse(null);

        assertNotNull(actual);
        assertEquals(operator.operatorId(), actual.operatorId());
        assertEquals(operator.role(), actual.role());
        assertEquals(operator.userId(), actual.userId());
    }

    @Test
    void getByOperatorId_shouldReturnOperator_whenExists() {
        Operator operator = OperatorBuilder.builder().build();
        operatorRepo.create(operator);

        Operator actual = operatorRepo.getByOperatorId(operator.operatorId()).orElse(null);

        assertNotNull(actual);
        assertEquals(operator.operatorId(), actual.operatorId());
        assertEquals(operator.role(), actual.role());
    }

    @Test
    void getByUserId_shouldReturnOperator_whenUserHasOperator() {
        Operator operator = OperatorBuilder.builder()
                .user(user)
                .storageId(storage.storageId())
                .build();
        operatorRepo.create(operator);

        Operator actual = operatorRepo.getByUserId(operator.userId()).orElse(null);

        assertNotNull(actual);
        assertEquals(operator.userId(), actual.userId());
    }

    @Test
    void getAllBySearchOptions_shouldReturnOperators_whenFilterByUsername() {
        Operator operator = OperatorBuilder.builder()
                .user(user)
                .storageId(storage.storageId())
                .build();
        operatorRepo.create(operator);
        OperatorSearchOptions options = new OperatorSearchOptions(
                "test-username",
                "test-name",
                null,
                null,
                10,
                0
        );

        List<Operator> result = operatorRepo.getAllBySearchOptions(options);

        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .anyMatch(o -> o.userId().equals(operator.userId())));
    }

    @Test
    void update_shouldModifyOperator_whenExists() {
        Operator operator = OperatorBuilder.builder().build();
        operatorRepo.create(operator);
        Operator updated = OperatorBuilder.builder()
                .role(OperatorRole.OrdinaryOperator)
                .build();

        Operator result = operatorRepo.update(operator.operatorId(), updated);

        assertEquals(OperatorRole.OrdinaryOperator, result.role());
    }

    @Test
    void delete_shouldRemoveOperator_whenExists() {
        Operator operator = OperatorBuilder.builder().build();
        operatorRepo.create(operator);
        operatorRepo.delete(operator.operatorId());

        Optional<Operator> result = operatorRepo.getByOperatorId(operator.operatorId());

        assertTrue(result.isEmpty());
    }
}