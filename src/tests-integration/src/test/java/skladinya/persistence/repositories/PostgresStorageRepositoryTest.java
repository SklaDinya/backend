package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.storage.Storage;
import skladinya.tests.helper.builder.StorageBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresStorageRepositoryTest {

    @Autowired
    private SpringStorageRepository springStorageRepo;

    private PostgresStorageRepository storageRepo;

    @BeforeEach
    void setUp() {
        storageRepo = new PostgresStorageRepository(springStorageRepo);
    }

    @Test
    void create_shouldSaveStorageAndReturnDomain() {
        Storage storage = StorageBuilder.builder().build();

        Storage result = storageRepo.create(storage);
        Storage actual = storageRepo.getByStorageId(result.storageId()).orElse(null);

        assertNotNull(actual);
        assertEquals(storage.storageId(), actual.storageId());
        assertEquals(storage.name(), actual.name());
        assertEquals(storage.address(), actual.address());
    }

    @Test
    void getByStorageId_shouldReturnStorage_whenExists() {
        Storage storage = StorageBuilder.builder().build();
        storageRepo.create(storage);

        Storage actual = storageRepo.getByStorageId(storage.storageId()).orElse(null);

        assertNotNull(actual);
        assertEquals(storage.storageId(), actual.storageId());
        assertEquals(storage.name(), actual.name());
    }

    @Test
    void update_shouldModifyStorageFields_whenStorageExists() {
        Storage storage = StorageBuilder.builder().build();
        storageRepo.create(storage);

        Storage updated = StorageBuilder.builder()
                .name("updated_name")
                .address("updated_address")
                .build();
        Storage result = storageRepo.update(storage.storageId(), updated);

        assertEquals("updated_name", result.name());
        assertEquals("updated_address", result.address());
    }

    @Test
    void delete_shouldRemoveStorage_whenExists() {
        Storage storage = StorageBuilder.builder().build();
        storageRepo.create(storage);

        storageRepo.delete(storage.storageId());

        Optional<Storage> result = storageRepo.getByStorageId(storage.storageId());
        assertTrue(result.isEmpty());
    }
}