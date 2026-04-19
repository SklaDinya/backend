package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;
import skladinya.domain.repositories.BookingRepository;
import skladinya.tests.helper.builder.BookingBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class PostgresBookingRepositoryTest {

    @Autowired
    private SpringBookingRepository springBookingRepo;

    @Autowired
    private SpringUserRepository springUserRepo;

    @Autowired
    private SpringStorageRepository springStorageRepo;

    private BookingRepository bookingRepo;

    private User user;

    private Storage storage;

    @BeforeEach
    void setUp() {
        PostgresUserRepository userRepo = new PostgresUserRepository(springUserRepo);
        user = userRepo.create(UserBuilder.builder().build());

        PostgresStorageRepository storageRepo = new PostgresStorageRepository(springStorageRepo);
        storage = storageRepo.create(StorageBuilder.builder().build());

        bookingRepo = new PostgresBookingRepository(springBookingRepo);
    }

    @Test
    void create_shouldSaveBookingAndReturnDomain() {
        Booking booking = BookingBuilder.builder().build();

        Booking result = bookingRepo.create(booking);
        Booking actual = bookingRepo.getByBookingId(result.bookingId()).orElse(null);

        assertNotNull(actual);
        assertEquals(booking.bookingId(), actual.bookingId());
        assertEquals(booking.startTime(), actual.startTime());
        assertEquals(booking.status(), actual.status());
    }

    @Test
    void getByBookingId_shouldReturnBooking_whenExists() {
        Booking booking = BookingBuilder.builder().build();
        bookingRepo.create(booking);

        Booking actual = bookingRepo.getByBookingId(booking.bookingId()).orElse(null);

        assertNotNull(actual);
        assertEquals(booking.bookingId(), actual.bookingId());
    }

    @Test
    void getAllForUser_shouldReturnBookings_whenUserHasBookings() {
        UUID userId = user.userId();
        Booking booking1 = BookingBuilder.builder()
                .user(user)
                .storage(storage)
                .build();
        Booking booking2 = BookingBuilder.builder()
                .user(user)
                .storage(storage)
                .build();
        bookingRepo.create(booking1);
        bookingRepo.create(booking2);

        List<Booking> result = bookingRepo.getAllForUser(userId, 10, 0);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(b -> b.userId().equals(userId)));
    }

    @Test
    void getAllForOperator_shouldReturnBookings_whenFilterByStatus() {
        UUID storageId = storage.storageId();
        Booking booking = BookingBuilder.builder()
                .user(user)
                .storage(storage)
                .status(BookingStatus.Created)
                .build();
        bookingRepo.create(booking);
        BookingSearchOptions options = new BookingSearchOptions(
                null,
                null,
                List.of(BookingStatus.Created),
                10,
                0
        );

        List<Booking> result = bookingRepo.getAllForOperator(storageId, options);

        assertEquals(1, result.size());
        assertEquals(BookingStatus.Created, result.get(0).status());
    }

    @Test
    void getAllForOperator_shouldFilterByTimeRange() {
        UUID storageId = storage.storageId();
        LocalDateTime now = LocalDateTime.now();
        Booking booking = BookingBuilder.builder()
                .user(user)
                .storage(storage)
                .startTime(now)
                .endTime(now.plusHours(2))
                .build();
        bookingRepo.create(booking);

        BookingSearchOptions options = new BookingSearchOptions(
                now.minusHours(1),
                now.plusHours(1),
                null,
                10,
                0
        );
        List<Booking> result = bookingRepo.getAllForOperator(storageId, options);

        assertFalse(result.isEmpty());
    }

    @Test
    void updateStatus_shouldChangeStatus_whenBookingExists() {
        Booking booking = BookingBuilder.builder().build();
        bookingRepo.create(booking);

        Booking result = bookingRepo.updateStatus(
                booking.bookingId(),
                BookingStatus.Finished
        );

        assertEquals(BookingStatus.Finished, result.status());
    }

    @Test
    void delete_shouldRemoveBooking_whenExists() {
        Booking booking = BookingBuilder.builder().build();
        bookingRepo.create(booking);

        bookingRepo.delete(booking.bookingId());

        Optional<Booking> result = bookingRepo.getByBookingId(booking.bookingId());

        assertTrue(result.isEmpty());
    }
}
