package skladinya.domain.repositories;

import skladinya.domain.models.payment.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment create(Payment payment);

    Optional<Payment> getByBookingId(UUID bookingId);
}
