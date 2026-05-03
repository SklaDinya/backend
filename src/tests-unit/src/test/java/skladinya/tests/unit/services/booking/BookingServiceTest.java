package skladinya.tests.unit.services.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.models.booking.BookingCreate;
import skladinya.domain.models.booking.BookingSearchOptions;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.price.Price;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.services.*;
import skladinya.services.booking.BookingServiceImpl;
import skladinya.tests.helper.builder.BookingBuilder;
import skladinya.tests.helper.builder.CellBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private StorageService storageService;
    @Mock
    private CellService cellService;
    @Mock
    private PriceService priceService;
    @Mock
    private PaymentService paymentService;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        bookingService = new BookingServiceImpl(
                bookingRepository,
                userService,
                storageService,
                cellService,
                priceService,
                paymentService,
                synchronizer
        );
    }

    @Test
    void create_shouldCreateBookingAndReturnReceipt() {
        var user = UserBuilder.builder().build();
        var storage = StorageBuilder.builder().build();
        var cells = List.of(CellBuilder.builder().storageId(storage.storageId()).build());
        var hours = 2;

        var form = new BookingCreate(
                storage.storageId(),
                cells.stream().map(Cell::cellId).toList(),
                LocalDateTime.now().plusHours(67),
                Duration.ofHours(hours));

        given(userService.getByUserId(user.userId())).willReturn(user);
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellService.getAllByCellIds(any(), any())).willReturn(cells);
        given(priceService.getAllByStorageId(any())).willReturn(
                List.of(new Price(UUID.randomUUID(), storage.storageId(), cells.getFirst().cellClass(), BigDecimal.TEN, LocalDateTime.now()))
        );
        given(bookingRepository.create(any())).willAnswer(i -> i.getArgument(0));

        var result = bookingService.create(user.userId(), form);

        assertNotNull(result);
        assertEquals(BigDecimal.TEN.multiply(BigDecimal.valueOf(hours)), result.booking().price());
    }

    @Test
    void getByBookingId_shouldReturnBooking_whenOwnerMatches() {
        var booking = BookingBuilder.builder().build();

        given(bookingRepository.getByBookingId(booking.bookingId()))
                .willReturn(Optional.of(booking));

        var result = bookingService.getByBookingId(booking.userId(), booking.bookingId());

        assertEquals(booking, result);
    }

    @Test
    void getAllForUser_shouldReturnList() {
        var booking = BookingBuilder.builder().build();

        given(bookingRepository.getAllForUser(any(), anyInt(), anyInt()))
                .willReturn(List.of(booking));

        var result = bookingService.getAllForUser(booking.userId(), 10, 0);

        assertEquals(1, result.size());
    }

    @Test
    void getAllForOperator_shouldReturnList() {
        var booking = BookingBuilder.builder().build();

        given(bookingRepository.getAllForOperator(any(), any()))
                .willReturn(List.of(booking));

        var result = bookingService.getAllForOperator(UUID.randomUUID(), new BookingSearchOptions(null, null, null, 10, 0));

        assertEquals(1, result.size());
    }

    @Test
    void payNoOp_shouldValidateAndUpdateStatus() {
        var booking = BookingBuilder.builder()
                .status(BookingStatus.Created)
                .build();

        var receipt = booking.bookingId().toString();

        given(paymentService.validateReceipt(receipt)).willReturn(booking);
        given(bookingRepository.updateStatus(any(), any())).willReturn(booking);

        var result = bookingService.payNoOp(booking.userId(), receipt);

        assertEquals(booking, result);
        then(paymentService).should().payNoOp(booking);
    }

    @Test
    void payRandom_shouldValidateAndUpdateStatus() {
        var booking = BookingBuilder.builder()
                .status(BookingStatus.Created)
                .build();

        var receipt = booking.bookingId().toString();

        given(paymentService.validateReceipt(receipt)).willReturn(booking);
        given(bookingRepository.updateStatus(any(), any())).willReturn(booking);

        var result = bookingService.payRandom(booking.userId(), receipt);

        assertEquals(booking, result);
        then(paymentService).should().payRandom(booking);
    }

    @Test
    void cancel_shouldUpdateStatusToCancelled() {
        var booking = BookingBuilder.builder()
                .status(BookingStatus.Created)
                .build();

        given(bookingRepository.getByBookingId(booking.bookingId()))
                .willReturn(Optional.of(booking));

        given(bookingRepository.updateStatus(any(), any()))
                .willReturn(booking);

        var result = bookingService.cancel(booking.userId(), booking.bookingId());

        assertEquals(booking, result);
    }
}
