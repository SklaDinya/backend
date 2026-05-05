package skladinya.persistence.postgres.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.storage.StorageSearchOptions;
import skladinya.domain.repositories.StorageRepository;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.factory.StorageSearchFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresStorageRepositoryTest {

    @Autowired
    private StorageRepository storageRepo;

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
    void getAllBySearchOptions_shouldReturnAll_whenExists() {
        var storage1 = StorageBuilder.builder().build();
        var storage2 = StorageBuilder.builder().build();
        storageRepo.create(storage1);
        storageRepo.create(storage2);
        var options = StorageSearchFactory.create();

        var result = storageRepo.getAllBySearchOptions(options);

        assertEquals(2, result.size());
    }

    @Test
    void getAllBySearchOptions_shouldReturnEmpty_whenNotExists() {

        var options = StorageSearchFactory.create();

        var result = storageRepo.getAllBySearchOptions(options);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllBySearchOptions_shouldReturnStorages_whenFilterByName() {
        var storage1 = StorageBuilder.builder().name("GoodName1").build();
        var storage2 = StorageBuilder.builder().name("Go0dName2").build();
        var storage3 = StorageBuilder.builder().name("GoodName3").build();
        storageRepo.create(storage1);
        storageRepo.create(storage2);
        storageRepo.create(storage3);
        var options = new StorageSearchOptions(
                "GoodName",
                null,
                List.of(),
                50,
                0
        );

        var result = storageRepo.getAllBySearchOptions(options);

        assertEquals(2, result.size());
    }

    @Test
    void update_shouldModifyStorageFields_whenStorageExists() {
        Storage storage = StorageBuilder.builder().build();
        storageRepo.create(storage);

        Storage updated = StorageBuilder.builder()
                .build();
        Storage result = storageRepo.update(storage.storageId(), updated);

        assertEquals(updated.name(), result.name());
        assertEquals(updated.address(), result.address());
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