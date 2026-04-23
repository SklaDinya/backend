package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.price.Price;
import skladinya.domain.models.storage.Storage;
import skladinya.tests.helper.builder.PriceBuilder;
import skladinya.tests.helper.builder.StorageBuilder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresPriceRepositoryTest {

    @Autowired
    private SpringPriceRepository springPriceRepo;

    @Autowired
    private SpringStorageRepository springStorageRepo;

    private PostgresPriceRepository priceRepo;

    private Storage storage;

    @BeforeEach
    void setUp() {
        PostgresStorageRepository storageRepo = new PostgresStorageRepository(springStorageRepo);
        storage = storageRepo.create(StorageBuilder.builder().build());

        priceRepo = new PostgresPriceRepository(springPriceRepo);
    }

    @Test
    void create_shouldSavePriceAndReturnDomain() {
        Price price = PriceBuilder.builder()
                .storageId(storage.storageId())
                .build();

        Price result = priceRepo.create(price);
        Price actual = priceRepo.getAllByStorageId(result.storageId()).stream()
                .findFirst()
                .orElse(null);

        assertNotNull(actual);
        assertEquals(price.priceId(), actual.priceId());
        assertEquals(price.cellClass(), actual.cellClass());
        assertEquals(price.price(), actual.price());
    }

    @Test
    void getAllByStorageId_shouldReturnPrices_whenStorageHasPrices() {
        UUID storageId = storage.storageId();
        Price price1 = PriceBuilder.builder()
                .storageId(storageId)
                .cellClass("A")
                .build();
        Price price2 = PriceBuilder.builder()
                .storageId(storageId)
                .cellClass("B")
                .build();
        priceRepo.create(price1);
        priceRepo.create(price2);

        List<Price> result = priceRepo.getAllByStorageId(storageId);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(p -> p.storageId().equals(storageId)));
    }

}