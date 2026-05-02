package skladinya.services.payment;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.booking.Booking;
import skladinya.domain.models.booking.BookingStatus;
import skladinya.domain.models.payment.Payment;
import skladinya.domain.models.payment.PaymentType;
import skladinya.domain.models.payment.payloads.NoOpPaymentPayload;
import skladinya.domain.models.payment.payloads.RandomPaymentPayload;
import skladinya.domain.repositories.BookingRepository;
import skladinya.domain.repositories.PaymentRepository;
import skladinya.domain.services.PaymentService;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequestScope
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Synchronizer synchronizer;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            Synchronizer synchronizer
    ) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.synchronizer = synchronizer;
    }

    @Override
    public String createReceipt(Booking booking) {
        return booking.bookingId().toString();
    }

    @Override
    public Booking validateReceipt(String receipt) {
        return synchronizer.executeSingleFunction(() -> {
            UUID bookingId;

            try {
                bookingId = UUID.fromString(receipt);
            } catch (Exception ex) {
                throw SklaDinyaException.validationError("Invalid receipt");
            }

            return bookingRepository.getByBookingId(bookingId)
                    .orElseThrow(() -> SklaDinyaException.notFound("Booking not found"));
        });
    }

    @Override
    public Payment payNoOp(Booking booking) {
        return synchronizer.executeTransactionFunction(() -> {

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            var payment = new Payment(
                    UUID.randomUUID(),
                    booking.bookingId(),
                    PaymentType.NoOp,
                    new NoOpPaymentPayload()
            );

            return paymentRepository.create(payment);
        });
    }

    @Override
    public Payment payRandom(Booking booking) {
        return synchronizer.executeTransactionFunction(() -> {

            if (booking.status() != BookingStatus.Created) {
                throw SklaDinyaException.conflict("Invalid status");
            }

            if (ThreadLocalRandom.current().nextBoolean()) {
                throw SklaDinyaException.conflict("Random payment failed");
            }

            var payment = new Payment(
                    UUID.randomUUID(),
                    booking.bookingId(),
                    PaymentType.Random,
                    new RandomPaymentPayload()
            );

            return paymentRepository.create(payment);
        });
    }

    @Override
    public void cancel(Payment payment) {
        synchronizer.executeTransactionConsumer(() -> {

            if (payment == null) {
                throw SklaDinyaException.validationError("Payment is null and WTF");
            }

        });
    }
}
