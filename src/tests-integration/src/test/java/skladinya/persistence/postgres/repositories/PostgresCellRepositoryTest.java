package skladinya.persistence.postgres.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.repositories.CellRepository;
import skladinya.domain.repositories.StorageRepository;
import skladinya.domain.repositories.UserRepository;
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
@ActiveProfiles("test-persistence-postgres")
class PostgresCellRepositoryTest {

    @Autowired
    private StorageRepository storageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private CellRepository cellRepo;

    private Storage storage;
    private User user;

    @BeforeEach
    void setUp() {
        storage = storageRepo.create(StorageBuilder.builder().build());

        user = userRepo.create(UserBuilder.builder().build());
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
    void getByName_shouldReturnCell_whenExists() {
        var cell = CellBuilder.builder().storageId(storage.storageId()).build();
        cellRepo.create(cell);

        var result = cellRepo.getByName(storage.storageId(), cell.name()).orElse(null);

        assertNotNull(result);
        assertEquals(cell.cellId(), result.cellId());
        assertEquals(cell.storageId(), result.storageId());
        assertEquals(cell.name(), result.name());
        assertEquals(cell.cellClass(), result.cellClass());
    }

    @Test
    void getByName_shouldReturnEmpty_whenNotExists() {
        var result = cellRepo.getByName(storage.storageId(), UUID.randomUUID().toString()).orElse(null);

        assertNull(result);
    }

    @Test
    void getAllBySearchOptions_shouldReturnCells_whenFilterByClass() {
        UUID storageId = storage.storageId();
        createCells(storageId);

        CellSearchOptions options = new CellSearchOptions(
                null,
                null,
                List.of("A"),
                10,
                0
        );

        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(2, result.size());
        assertEquals("A", result.getFirst().cellClass());
    }

    @Test
    void getAllBySearchOptions_shouldExcludeBusyCells_whenIntervalBetweenBookings() {
        UUID storageId = storage.storageId();
        var cells = createCells(storageId);
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(1)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 12, 0))
                .build()
        );
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(2)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 17, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 19, 0))
                .build()
        );

        CellSearchOptions options = new CellSearchOptions(
                LocalDateTime.of(2020, 10, 10, 14, 0),
                Duration.ofHours(1),
                null,
                10,
                0
        );
        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(3, result.size());
    }

    @Test
    void getAllBySearchOptions_shouldExcludeBusyCells_whenIntervalLeftCrossBookings() {
        UUID storageId = storage.storageId();
        var cells = createCells(storageId);
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(1)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 12, 0))
                .build()
        );
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(2)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 17, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 19, 0))
                .build()
        );

        CellSearchOptions options = new CellSearchOptions(
                LocalDateTime.of(2020, 10, 10, 11, 0),
                Duration.ofHours(1),
                null,
                10,
                0
        );
        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(2, result.size());
    }

    @Test
    void getAllBySearchOptions_shouldExcludeBusyCells_whenIntervalLeftAndRightCrossBookings() {
        UUID storageId = storage.storageId();
        var cells = createCells(storageId);
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(1)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 10, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 12, 0))
                .build()
        );
        bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .cells(List.of(cells.get(2)))
                .user(user)
                .startTime(LocalDateTime.of(2020, 10, 10, 17, 0))
                .endTime(LocalDateTime.of(2020, 10, 10, 19, 0))
                .build()
        );

        CellSearchOptions options = new CellSearchOptions(
                LocalDateTime.of(2020, 10, 10, 11, 0),
                Duration.ofHours(10),
                null,
                10,
                0
        );
        List<Cell> result = cellRepo.getAllBySearchOptions(storageId, options);

        assertEquals(1, result.size());
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
