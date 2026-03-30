package skladinya.persistence.repositories;

import skladinya.domain.models.payment.Payment;
import skladinya.domain.repositories.PaymentRepository;

import java.util.Optional;
import java.util.UUID;

public class PaymentRepositoryPostgres implements PaymentRepository {
    @Override
    public Payment create(Payment payment) {
        return null;
    }

    @Override
    public Optional<Payment> getByBookingId(UUID bookingId) {
        return Optional.empty();
    }
}
