package skladinya.tests.unit.services.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.domain.services.PaymentService;
import skladinya.services.payment.PaymentServiceImpl;
import skladinya.tests.helper.builder.BookingBuilder;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        paymentService = new PaymentServiceImpl(paymentRepository, bookingRepository, synchronizer);
    }

    @Test
    void givenBooking_whenCreateReceipt_thenReturnBookingIdAsString() {
        var booking = BookingBuilder.builder().build();

        var result = paymentService.createReceipt(booking);

        assertEquals(booking.bookingId().toString(), result);
    }

    @Test
    void givenValidReceipt_whenValidateReceipt_thenReturnBooking() {
        var booking = BookingBuilder.builder().build();
        var receipt = booking.bookingId().toString();

        given(bookingRepository.getByBookingId(booking.bookingId()))
                .willReturn(Optional.of(booking));

        var result = paymentService.validateReceipt(receipt);

        assertEquals(booking, result);
    }

    @Test
    void givenValidBooking_whenPayNoOp_thenCreatePaymentAndUpdateStatus() {
        var booking = BookingBuilder.builder()
                .status(BookingStatus.Created)
                .build();

        given(paymentRepository.create(any())).willAnswer(invocation -> invocation.getArgument(0));

        var result = paymentService.payNoOp(booking);

        assertNotNull(result);
        assertEquals(booking.bookingId(), result.bookingId());

        then(paymentRepository).should().create(any());
    }

}
