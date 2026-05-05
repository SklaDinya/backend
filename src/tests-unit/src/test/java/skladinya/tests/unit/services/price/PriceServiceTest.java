package skladinya.tests.unit.services.price;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.price.Price;
import skladinya.domain.models.price.PriceCreate;
import skladinya.domain.repositories.PriceRepository;
import skladinya.domain.services.PriceService;
import skladinya.domain.services.StorageService;
import skladinya.services.price.PriceServiceImpl;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private StorageService storageService;

    private PriceService priceService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        priceService = new PriceServiceImpl(priceRepository, storageService, synchronizer);
    }

    @Test
    void givenValidStorage_whenCreate_thenReturnPrice() {
        UUID storageId = UUID.randomUUID();
        var createForm = new PriceCreate("A", BigDecimal.TEN);
        var expected = new Price(
                UUID.randomUUID(),
                storageId,
                createForm.cellClass(),
                createForm.price(),
                LocalDateTime.now()
        );
        given(storageService.getByStorageId(storageId)).willReturn(null);
        given(priceRepository.create(any())).willReturn(expected);

        var result = priceService.create(storageId, createForm);

        assertEquals(expected, result);
    }

    @Test
    void givenInvalidStorage_whenCreate_thenThrowException() {
        UUID storageId = UUID.randomUUID();
        var createForm = new PriceCreate("A", BigDecimal.TEN);

        given(storageService.getByStorageId(storageId))
                .willThrow(SklaDinyaException.notFound("Storage not found"));

        assertThrows(SklaDinyaException.class,
                () -> priceService.create(storageId, createForm));
    }

    @Test
    void givenValidStorage_whenGetAllByStorageId_thenReturnPrices() {
        UUID storageId = UUID.randomUUID();
        var prices = List.of(
                new Price(UUID.randomUUID(), storageId, "A", BigDecimal.TEN, LocalDateTime.now()),
                new Price(UUID.randomUUID(), storageId, "B", BigDecimal.ONE, LocalDateTime.now())
        );
        given(storageService.getByStorageId(storageId)).willReturn(null);
        given(priceRepository.getAllByStorageId(storageId)).willReturn(prices);

        var result = priceService.getAllByStorageId(storageId);

        assertEquals(prices, result);
    }
}
