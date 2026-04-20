package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;
import skladinya.tests.helper.builder.BookingBuilder;
import skladinya.tests.helper.builder.CellBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class PostgresCellRepositoryTest {

    @Autowired
    private SpringCellRepository springCellRepo;

    @Autowired
    private SpringStorageRepository springStorageRepo;

    @Autowired
    private SpringUserRepository springUserRepo;

    @Autowired
    private SpringBookingRepository springBookingRepo;

    private PostgresCellRepository cellRepo;

    private Storage storage;
    private User user;
    private PostgresBookingRepository bookingRepo;

    @BeforeEach
    void setUp() {
        PostgresStorageRepository storageRepo = new PostgresStorageRepository(springStorageRepo);
        storage = storageRepo.create(StorageBuilder.builder().build());

        PostgresUserRepository userRepo = new PostgresUserRepository(springUserRepo);
        user = userRepo.create(UserBuilder.builder().build());
        bookingRepo = new PostgresBookingRepository(springBookingRepo);

        cellRepo = new PostgresCellRepository(springCellRepo);
    }

    @Test
    void create_shouldSaveCellAndReturnDomain() {
        Cell cell = CellBuilder.builder()
                .storageId(storage.storageId())
                .build();
        Cell result = cellRepo.create(cell);
        List<Cell> all = cellRepo.getAllByCellIds(List.of(result.cellId()));

        Cell actual = all.stream().findFirst().orElse(null);

        assertNotNull(actual);
        assertEquals(cell.cellId(), actual.cellId());
        assertEquals(cell.name(), actual.name());
        assertEquals(cell.cellClass(), actual.cellClass());
    }

    @Test
    void getAllBySearchOptions_shouldReturnCells_whenFilterByClass() {
        UUID storageId = storage.storageId();
        createCells(storageId);

        CellSearchOptions options = new CellSearchOptions(
                null,
                null,
                List.of("A"),
                0,
                10
        );

        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(2, result.size());
        assertEquals("A", result.getFirst().cellClass());
    }

    @Test
    void getAllBySearchOptions_shouldExcludeBusyCells_whenInnerInterval() { // TODO тест не проходит
        UUID storageId = storage.storageId();
        var cells = createCells(storageId);
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(1)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 10, 0))
                .bookingTime(Duration.ofHours(2))
                .build()
        );
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(2)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 17, 0))
                .bookingTime(Duration.ofHours(2))
                .build()
        );

        CellSearchOptions options = new CellSearchOptions(
                LocalDateTime.of(2020, 10, 10, 14, 0),
                Duration.ofHours(1),
                List.of("A"),
                0,
                10
        );
        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(3, result.size());
    }

    private List<Cell> createCells(UUID storageId) {
        Cell cell1 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("A")
                .build();
        Cell cell2 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("B")
                .build();
        Cell cell3 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("A")
                .build();
        cellRepo.create(cell1);
        cellRepo.create(cell2);
        cellRepo.create(cell3);

        return List.of(cell1, cell2, cell3);
    }

    @Test
    void getAllCellClasses_shouldReturnDistinctClasses_forStorage() {
        UUID storageId = storage.storageId();
        Cell cell1 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("A")
                .build();
        Cell cell2 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("B")
                .build();
        Cell cell3 = CellBuilder.builder()
                .storageId(storageId)
                .cellClass("A")
                .build();
        cellRepo.create(cell1);
        cellRepo.create(cell2);
        cellRepo.create(cell3);

        List<String> result = cellRepo.getAllCellClasses(storageId);

        assertEquals(2, result.size());
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
    }

    @Test
    void getAllByCellIds_shouldReturnCells_whenIdsExist() {
        Cell cell1 = CellBuilder.builder()
                .storageId(storage.storageId())
                .build();
        Cell cell2 = CellBuilder.builder()
                .storageId(storage.storageId())
                .build();

        cellRepo.create(cell1);
        cellRepo.create(cell2);

        List<Cell> result = cellRepo.getAllByCellIds(
                List.of(cell1.cellId(), cell2.cellId())
        );

        assertEquals(2, result.size());
    }
}
