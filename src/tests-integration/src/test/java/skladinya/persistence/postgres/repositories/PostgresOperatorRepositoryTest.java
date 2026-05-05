package skladinya.persistence.postgres.repositories;

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
import skladinya.domain.models.user.UserRole;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.domain.repositories.StorageRepository;
import skladinya.domain.repositories.UserRepository;
import skladinya.tests.helper.builder.OperatorBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresOperatorRepositoryTest {

    @Autowired
    private StorageRepository storageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OperatorRepository operatorRepo;

    private User user;
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = storageRepo.create(StorageBuilder.builder().build());
        user = userRepo.create(UserBuilder.builder()
                .name("test-name")
                .username("test-username")
                .role(UserRole.StorageOperator)
                .build()
        );
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

        List<Operator> result = operatorRepo.getAllBySearchOptions(storage.storageId(), options);

        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .anyMatch(o -> o.userId().equals(operator.userId())));
    }
}