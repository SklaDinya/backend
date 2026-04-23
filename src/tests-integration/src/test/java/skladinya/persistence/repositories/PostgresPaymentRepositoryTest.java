package skladinya.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.storage.Storage;
import skladinya.domain.models.user.User;
import skladinya.tests.helper.builder.BookingBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.factory.PaymentFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test-persistence-postgres")
class PostgresPaymentRepositoryTest {

    @Autowired
    private SpringPaymentRepository springPaymentRepo;

    @Autowired
    private SpringBookingRepository springBookingRepo;

    @Autowired
    private SpringStorageRepository springStorageRepo;

    @Autowired
    private SpringUserRepository springUserRepo;

    private PostgresPaymentRepository paymentRepo;

    private Booking booking;

    @BeforeEach
    void setUp() {
        saveBooking();
        paymentRepo = new PostgresPaymentRepository(springPaymentRepo);
    }

    void saveBooking() {
        PostgresStorageRepository storageRepo = new PostgresStorageRepository(springStorageRepo);
        Storage storage = storageRepo.create(StorageBuilder.builder().build());

        PostgresUserRepository userRepo = new PostgresUserRepository(springUserRepo);
        User user = userRepo.create(UserBuilder.builder().build());

        PostgresBookingRepository bookingRepo = new PostgresBookingRepository(springBookingRepo);
        booking = bookingRepo.create(BookingBuilder.builder()
                .storage(storage)
                .user(user)
                .build()
        );
    }

    @Test
    void create_shouldSavePaymentAndReturnDomain() {
        UUID bookingId = booking.bookingId();

        Payment payment = PaymentFactory.createNoOp(bookingId);

        Payment result = paymentRepo.create(payment);
        Payment actual = paymentRepo.getByBookingId(result.bookingId()).orElse(null);

        assertNotNull(actual);
        assertEquals(payment.paymentId(), actual.paymentId());
        assertEquals(payment.bookingId(), actual.bookingId());
        assertEquals(payment.paymentType(), actual.paymentType());
    }

    @Test
    void getByBookingId_shouldReturnPayment_whenExists() {
        UUID bookingId = booking.bookingId();

        Payment payment = PaymentFactory.createRandom(bookingId);
        paymentRepo.create(payment);

        Payment actual = paymentRepo.getByBookingId(bookingId).orElse(null);

        assertNotNull(actual);
        assertEquals(payment.bookingId(), actual.bookingId());
        assertEquals(payment.paymentType(), actual.paymentType());
    }
}
